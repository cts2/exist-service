package edu.mayo.cts2.framework.plugin.service.exist.valuesetResolution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.transform.stream.StreamSource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.XmlMappingException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xmldb.api.base.XMLDBException;
import edu.mayo.cts2.framework.core.xml.DelegatingMarshaller;
import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference;
import edu.mayo.cts2.framework.model.core.ComponentReference;
import edu.mayo.cts2.framework.model.core.DescriptionInCodeSystem;
import edu.mayo.cts2.framework.model.core.SortCriteria;
import edu.mayo.cts2.framework.model.core.SortCriterion;
import edu.mayo.cts2.framework.model.core.types.SortDirection;
import edu.mayo.cts2.framework.model.entity.EntityDirectoryEntry;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.service.exception.UnknownCodeSystemVersion;
import edu.mayo.cts2.framework.model.updates.ChangeSet;
import edu.mayo.cts2.framework.model.valuesetdefinition.ResolvedValueSet;
import edu.mayo.cts2.framework.model.valuesetdefinition.ResolvedValueSetHeader;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistManager;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistResourceDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.update.ExistChangeSetService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.valuesetdefinition.ExistValueSetDefinitionResolutionService;
import edu.mayo.cts2.framework.service.constant.ExternalCts2Constants;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ResolvedValueSetResult;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.name.ValueSetDefinitionReadId;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = "classpath:/exist-test-context.xml")
public class ValueSetDefinitionResolutionTest
{
	@Autowired
	public ExistChangeSetService changeSetService;

	@Autowired 
	public ExistManager existManager;

	@Autowired 
	public ExistResourceDao dao;
	
	@Autowired
	public ExistValueSetDefinitionResolutionService vsr;
	
	//no idea why this needs to be static... something funny about how the spring juni test runner handles this.
	private static boolean dbInited = false;

	//Sigh - BeforeClass requires static, which doesn't play nice with spring...
	@Before
	public void setupExist() throws XMLDBException, XmlMappingException, IOException
	{
		if (!dbInited)
		{
			dao.removeCollection(existManager.getCollectionRoot());
	
			DelegatingMarshaller marshaller = new DelegatingMarshaller();
	
			ChangeSet changeSet = (ChangeSet) marshaller.unmarshal(new StreamSource(ValueSetDefinitionResolutionTest.class.getResourceAsStream("/valuesetResolutionData/CodeSystems.xml")));
			changeSetService.importChangeSet(changeSet);
			changeSetService.commitChangeSet(changeSet.getChangeSetURI());
	
			changeSet = (ChangeSet) marshaller.unmarshal(new StreamSource(ValueSetDefinitionResolutionTest.class.getResourceAsStream("/valuesetResolutionData/Entities.xml")));
			
			changeSetService.importChangeSet(changeSet);
			changeSetService.commitChangeSet(changeSet.getChangeSetURI());
	
			changeSet = (ChangeSet) marshaller.unmarshal(new StreamSource(ValueSetDefinitionResolutionTest.class.getResourceAsStream("/valuesetResolutionData/ValueSets.xml")));
			
			changeSetService.importChangeSet(changeSet);
			changeSetService.commitChangeSet(changeSet.getChangeSetURI());
			dbInited = true;
		}
	}
	
	@Test
	public void testCompleteCodeSystem()
	{
		ResolvedValueSet rvs = vsr.resolveDefinitionAsCompleteSet(new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet1-1.0"), null, null, null, null);
		validate(rvs, assemble("tag:informatics.mayo.edu,2013-08-03:UnbelievableTestData", new String[] {"A", "B", "C", "D"}), false);
		
		HashSet<String> resolvedUsing = new HashSet<String>();
		resolvedUsing.add(makeString("UnbelievableTestData", "tag:informatics.mayo.edu,2013-08-03:UnbelievableTestData", "UnbelievableTestData-1.0", 
				"tag:informatics.mayo.edu,2013-08-03:UnbelievableTestData-1.0"));
		
		validate(rvs.getResolutionInfo(), "ValueSet1", "tag:informatics.mayo.edu,2013-08-03:vs/ValueSet1", "1", "tag:informatics.mayo.edu,2013-08-03:vs/ValueSet1-1.0",
				resolvedUsing, new ArrayList<String>());
	}
	
	@Test
	public void testTransitiveClosure()
	{
		ResolvedValueSet rvs = vsr.resolveDefinitionAsCompleteSet(new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet2-1.0"), null, null, null, null);
		validate(rvs, assemble("tag:informatics.mayo.edu,2013-08-03:UnbelievableTestData", new String[] {"A", "B", "C", "D"}), false);
	}
	
	@Test
	public void testTransitiveClosure2()
	{
		ResolvedValueSet rvs = vsr.resolveDefinitionAsCompleteSet(new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet5-1.0"), null, null, null, null);
		validate(rvs, assemble("tag:informatics.mayo.edu,2013-08-03:BelievableTestData", new String[] {"1", "2", "3", "4"}), false);
	}
	
	@Test
	public void testTransitiveClosure3()
	{
		ResolvedValueSet rvs = vsr.resolveDefinitionAsCompleteSet(new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet5-2.0"), null, null, null, null);
		validate(rvs, assemble("tag:informatics.mayo.edu,2013-08-03:BelievableTestData", new String[] {"1", "2", "3"}), false);
	}
	
	@Test
	public void testCycleDetect()
	{
		ResolvedValueSet rvs = vsr.resolveDefinitionAsCompleteSet(new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet5-3.0"), null, null, null, null);
		validate(rvs, assemble("tag:informatics.mayo.edu,2013-08-03:BelievableTestData", new String[] {"6", "5"}), false);
	}
	
	@Test
	public void testCycleDetect2()
	{
		ResolvedValueSet rvs = vsr.resolveDefinitionAsCompleteSet(new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet5-7.0"), null, null, null, null);
		validate(rvs, assemble("tag:informatics.mayo.edu,2013-08-03:BelievableTestData", new String[] {}), false);
	}
	
	@Test
	public void testLeafOnly()
	{
		ResolvedValueSet rvs = vsr.resolveDefinitionAsCompleteSet(new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet5-4.0"), null, null, null, null);
		validate(rvs, assemble("tag:informatics.mayo.edu,2013-08-03:BelievableTestData", new String[] {"1"}), false);
	}
	
	@Test
	public void testLeafOnlyDirect()
	{
		ResolvedValueSet rvs = vsr.resolveDefinitionAsCompleteSet(new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet5-5.0"), null, null, null, null);
		validate(rvs, assemble("tag:informatics.mayo.edu,2013-08-03:BelievableTestData", new String[] {"3"}), false);
	}
	
	@Test
	public void testLeafOnlyAll()
	{
		ResolvedValueSet rvs = vsr.resolveDefinitionAsCompleteSet(new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet5-6.0"), null, null, null, null);
		validate(rvs, assemble("tag:informatics.mayo.edu,2013-08-03:BelievableTestData", new String[] {"3"}), false);
	}
	
	
	@Test
	public void testEntityList()
	{
		ResolvedValueSet rvs = vsr.resolveDefinitionAsCompleteSet(new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet3-1.0"), null, null, null, null);
		validate(rvs, assemble("tag:informatics.mayo.edu,2013-08-03:UnbelievableTestData", new String[] {"A", "C"}), false);
	}
	
	@Test
	public void testSubtractAndCompleteValueSet()
	{
		ResolvedValueSet rvs = vsr.resolveDefinitionAsCompleteSet(new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet4-1.0"), null, null, null, null);
		validate(rvs, assemble("tag:informatics.mayo.edu,2013-08-03:UnbelievableTestData", new String[] {"A", "B", "D"}), false);
	}
	
	@Test
	public void testSort1()
	{
		SortCriteria sc = new SortCriteria();
		SortCriterion sc1 = new SortCriterion();
		sc1.setEntryOrder(1l);
		sc1.setSortDirection(SortDirection.ASCENDING);
		ComponentReference cr = new ComponentReference();
		cr.setAttributeReference(ExternalCts2Constants.MA_ENTITY_DESCRIPTION_DESIGNATION_NAME);
		sc1.setSortElement(cr);
		sc.addEntry(sc1);
		SortCriterion sc2 = new SortCriterion();
		sc2.setEntryOrder(2l);
		sc2.setSortDirection(SortDirection.DESCENDING);
		cr = new ComponentReference();
		cr.setAttributeReference(ExternalCts2Constants.MA_RESOURCE_NAME_NAME);
		sc2.setSortElement(cr);
		sc.addEntry(sc2);
		ResolvedValueSet rvs = vsr.resolveDefinitionAsCompleteSet(new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet6-1.0"), null, null, sc, null);
		validate(rvs, assemble("tag:informatics.mayo.edu,2013-08-03:BelievableTestData", new String[] {"2", "1", "3", "4", "5", "6", "11", "12", "111"}), true);
	}
	
	@Test
	public void testSort2()
	{
		SortCriteria sc = new SortCriteria();
		SortCriterion sc2 = new SortCriterion();
		sc2.setEntryOrder(2l);
		sc2.setSortDirection(SortDirection.ASCENDING);
		ComponentReference cr = new ComponentReference();
		cr.setAttributeReference(ExternalCts2Constants.MA_RESOURCE_NAME_NAME);
		sc2.setSortElement(cr);
		sc.addEntry(sc2);
		
		//purposefully loading them backwards of the entry order to catch out trusting implementations....
		SortCriterion sc1 = new SortCriterion();
		sc1.setEntryOrder(1l);
		sc1.setSortDirection(SortDirection.DESCENDING);
		cr = new ComponentReference();
		cr.setAttributeReference(ExternalCts2Constants.MA_ENTITY_DESCRIPTION_DESIGNATION_NAME);
		sc1.setSortElement(cr);
		sc.addEntry(sc1);
		
		ResolvedValueSet rvs = vsr.resolveDefinitionAsCompleteSet(new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet6-1.0"), null, null, sc, null);
		validate(rvs, assemble("tag:informatics.mayo.edu,2013-08-03:BelievableTestData", new String[] {"111", "12", "11", "6", "5", "4", "3", "1","2"}), true);
	}
	
	@Test
	public void testSort3()
	{
		SortCriteria sc = new SortCriteria();
		SortCriterion sc1 = new SortCriterion();
		sc1.setEntryOrder(1l);
		sc1.setSortDirection(SortDirection.DESCENDING);
		ComponentReference cr = new ComponentReference();
		cr.setAttributeReference(ExternalCts2Constants.MA_RESOURCE_NAME_NAME);
		sc1.setSortElement(cr);
		sc.addEntry(sc1);
		ResolvedValueSet rvs = vsr.resolveDefinitionAsCompleteSet(new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet6-1.0"), null, null, sc, null);
		validate(rvs, assemble("tag:informatics.mayo.edu,2013-08-03:BelievableTestData", new String[] {"111", "12", "11", "6", "5", "4", "3", "2", "1"}), true);
	}
	
	@Test
	public void testSort4()
	{
		SortCriteria sc = new SortCriteria();
		SortCriterion sc1 = new SortCriterion();
		sc1.setEntryOrder(1l);
		sc1.setSortDirection(SortDirection.ASCENDING);
		ComponentReference cr = new ComponentReference();
		cr.setAttributeReference(ExternalCts2Constants.MA_RESOURCE_NAME_NAME);
		sc1.setSortElement(cr);
		sc.addEntry(sc1);
		ResolvedValueSet rvs = vsr.resolveDefinitionAsCompleteSet(new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet6-1.0"), null, null, sc, null);
		validate(rvs, assemble("tag:informatics.mayo.edu,2013-08-03:BelievableTestData", new String[] {"1", "2", "3", "4", "5", "6", "11", "12", "111"}), true);
	}
	
	@Test
	public void testPropertyQueryRef1()
	{
		ResolvedValueSet rvs = vsr.resolveDefinitionAsCompleteSet(new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet7-1.0"), null, null, null, null);
		validate(rvs, assemble("tag:informatics.mayo.edu,2013-08-03:UnbelievableTestData", new String[] {"B"}), false);
	}
	
	@Test
	public void testPropertyQueryRef2()
	{
		ResolvedValueSet rvs = vsr.resolveDefinitionAsCompleteSet(new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet7-2.0"), null, null, null, null);
		validate(rvs, assemble("tag:informatics.mayo.edu,2013-08-03:UnbelievableTestData", new String[] {"A", "B"}), false);
	}
	
	@Test
	public void testHeaderAcrossTwo()
	{
		ResolvedValueSet rvs = vsr.resolveDefinitionAsCompleteSet(new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet8-1.0"), null, null, null, null);
		validate(rvs, new String[] {"tag:informatics.mayo.edu,2013-08-03:UnbelievableTestData/Concept#A", 
				"tag:informatics.mayo.edu,2013-08-03:BelievableTestData/Concept#1"}, false);
		
		HashSet<String> resolvedUsing = new HashSet<String>();
		resolvedUsing.add(makeString("UnbelievableTestData", "tag:informatics.mayo.edu,2013-08-03:UnbelievableTestData", "UnbelievableTestData-1.0", 
				"tag:informatics.mayo.edu,2013-08-03:UnbelievableTestData-1.0"));
		resolvedUsing.add(makeString("BelievableTestData", "tag:informatics.mayo.edu,2013-08-03:BelievableTestData", "BelievableTestData-1.0", 
				"tag:informatics.mayo.edu,2013-08-03:BelievableTestData-1.0"));

		
		validate(rvs.getResolutionInfo(), "ValueSet8", "tag:informatics.mayo.edu,2013-08-03:vs/ValueSet8", "1", "tag:informatics.mayo.edu,2013-08-03:vs/ValueSet8-1.0",
				resolvedUsing, new ArrayList<String>());
	}
	
	@Test
	public void testVersionResolutionBadVer1()
	{
		Set<NameOrURI> shouldResolveUsing = new HashSet<NameOrURI>();
		NameOrURI nou = new NameOrURI();
		nou.setUri("tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData-5.0");
		shouldResolveUsing.add(nou);
		
		try
		{
			vsr.resolveDefinitionAsCompleteSet(new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet9-1.0"), 
				shouldResolveUsing, null, null, null);
			fail("didn't validate requested code system version list");
		}
		catch (UnknownCodeSystemVersion e)
		{
			//expected
		}
	}
	
	@Test
	public void testVersionResolutionBadVer2()
	{
		Set<NameOrURI> shouldResolveUsing = new HashSet<NameOrURI>();
		NameOrURI nou = new NameOrURI();
		nou.setUri("tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData-1.0");
		shouldResolveUsing.add(nou);
		nou = new NameOrURI();
		nou.setUri("tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData-2.0");
		shouldResolveUsing.add(nou);
		
		try
		{
			vsr.resolveDefinitionAsCompleteSet(new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet9-1.0"), 
				shouldResolveUsing, null, null, null);
			fail("didn't validate 1 version per code system");
		}
		catch (Exception e)
		{
			//expected
		}
	}
	
	@Test
	public void testVersionResolution1()
	{
		Set<NameOrURI> shouldResolveUsing = new HashSet<NameOrURI>();
		NameOrURI nou = new NameOrURI();
		nou.setUri("tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData-1.0");
		shouldResolveUsing.add(nou);
		
		ResolvedValueSet rvs = vsr.resolveDefinitionAsCompleteSet(new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet9-1.0"), 
				shouldResolveUsing, null, null, null);
		validate(rvs, assemble("tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData", new String[] {"157", "42"}), false);
		
		HashSet<String> resolvedUsing = new HashSet<String>();
		resolvedUsing.add(makeString("MultiVersionTestData", "tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData", "MultiVersionTestData-1.0", 
				"tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData-1.0"));
		
		validate(rvs.getResolutionInfo(), "ValueSet9", "tag:informatics.mayo.edu,2013-08-03:vs/ValueSet9", null, "tag:informatics.mayo.edu,2013-08-03:vs/ValueSet9-1.0",
				resolvedUsing, new ArrayList<String>());
	}
	
	@Test
	public void testVersionResolution2()
	{
		Set<NameOrURI> shouldResolveUsing = new HashSet<NameOrURI>();
		NameOrURI nou = new NameOrURI();
		nou.setUri("tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData-2.0");
		shouldResolveUsing.add(nou);
		
		ResolvedValueSet rvs = vsr.resolveDefinitionAsCompleteSet(new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet9-1.0"), 
				shouldResolveUsing, null, null, null);
		validate(rvs, assemble("tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData", new String[] {"257"}), false);
		
		HashSet<String> resolvedUsing = new HashSet<String>();
		resolvedUsing.add(makeString("MultiVersionTestData", "tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData", "MultiVersionTestData-2.0", 
				"tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData-2.0"));
		
		validate(rvs.getResolutionInfo(), "ValueSet9", "tag:informatics.mayo.edu,2013-08-03:vs/ValueSet9", null, "tag:informatics.mayo.edu,2013-08-03:vs/ValueSet9-1.0",
				resolvedUsing, new ArrayList<String>());
	}
	
	@Test
	public void testVersionResolution3()
	{
		ResolvedValueSet rvs = vsr.resolveDefinitionAsCompleteSet(new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet9-2.0"), 
				null, null, null, null);
		validate(rvs, assemble("tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData", new String[] {"157", "42"}), false);
		
		HashSet<String> resolvedUsing = new HashSet<String>();
		resolvedUsing.add(makeString("MultiVersionTestData", "tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData", "MultiVersionTestData-1.0", 
				"tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData-1.0"));
		
		validate(rvs.getResolutionInfo(), "ValueSet9", "tag:informatics.mayo.edu,2013-08-03:vs/ValueSet9", null, "tag:informatics.mayo.edu,2013-08-03:vs/ValueSet9-2.0",
				resolvedUsing, new ArrayList<String>());
	}
	
	@Test
	public void testVersionResolution4()
	{
		//this should be ignored
		Set<NameOrURI> shouldResolveUsing = new HashSet<NameOrURI>();
		NameOrURI nou = new NameOrURI();
		nou.setUri("tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData-2.0");
		shouldResolveUsing.add(nou);
		
		ResolvedValueSet rvs = vsr.resolveDefinitionAsCompleteSet(new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet9-2.0"), 
				shouldResolveUsing, null, null, null);
		validate(rvs, assemble("tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData", new String[] {"157", "42"}), false);
		
		HashSet<String> resolvedUsing = new HashSet<String>();
		resolvedUsing.add(makeString("MultiVersionTestData", "tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData", "MultiVersionTestData-1.0", 
				"tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData-1.0"));
		
		validate(rvs.getResolutionInfo(), "ValueSet9", "tag:informatics.mayo.edu,2013-08-03:vs/ValueSet9", null, "tag:informatics.mayo.edu,2013-08-03:vs/ValueSet9-2.0",
				resolvedUsing, new ArrayList<String>());
	}
	
	@Test
	public void testVersionResolution5()
	{
		//this should be ignored
		Set<NameOrURI> shouldResolveUsing = new HashSet<NameOrURI>();
		NameOrURI nou = new NameOrURI();
		nou.setUri("tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData-1.0");
		shouldResolveUsing.add(nou);
		
		ResolvedValueSet rvs = vsr.resolveDefinitionAsCompleteSet(new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet9-3.0"), 
				shouldResolveUsing, null, null, null);
		validate(rvs, assemble("tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData", new String[] {"257"}), false);
		
		HashSet<String> resolvedUsing = new HashSet<String>();
		resolvedUsing.add(makeString("MultiVersionTestData", "tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData", "MultiVersionTestData-2.0", 
				"tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData-2.0"));
		
		validate(rvs.getResolutionInfo(), "ValueSet9", "tag:informatics.mayo.edu,2013-08-03:vs/ValueSet9", null, "tag:informatics.mayo.edu,2013-08-03:vs/ValueSet9-3.0",
				resolvedUsing, new ArrayList<String>());
	}
	
	@Test
	public void testVersionResolution6()
	{
		Set<NameOrURI> shouldResolveUsing = new HashSet<NameOrURI>();
		NameOrURI nou = new NameOrURI();
		nou.setUri("tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData-1.0");
		shouldResolveUsing.add(nou);
		
		ResolvedValueSet rvs = vsr.resolveDefinitionAsCompleteSet(new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet9-4.0"), 
				shouldResolveUsing, null, null, null);
		validate(rvs, assemble("tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData", new String[] {"157"}), false);
		
		assertEquals("something in version 1",rvs.getEntry(0).getDesignation());
		
		HashSet<String> resolvedUsing = new HashSet<String>();
		resolvedUsing.add(makeString("MultiVersionTestData", "tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData", "MultiVersionTestData-1.0", 
				"tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData-1.0"));
		
		validate(rvs.getResolutionInfo(), "ValueSet9", "tag:informatics.mayo.edu,2013-08-03:vs/ValueSet9", null, "tag:informatics.mayo.edu,2013-08-03:vs/ValueSet9-4.0",
				resolvedUsing, new ArrayList<String>());
	}
	
	@Test
	public void testVersionResolution7()
	{
		Set<NameOrURI> shouldResolveUsing = new HashSet<NameOrURI>();
		NameOrURI nou = new NameOrURI();
		nou.setUri("tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData-3.0");
		shouldResolveUsing.add(nou);
		
		ResolvedValueSet rvs = vsr.resolveDefinitionAsCompleteSet(new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet9-4.0"), 
				shouldResolveUsing, null, null, null);
		validate(rvs, assemble("tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData", new String[] {"157"}), false);
		
		assertEquals("same concept redefined with a different desc in ver 3",rvs.getEntry(0).getDesignation());
		
		HashSet<String> resolvedUsing = new HashSet<String>();
		resolvedUsing.add(makeString("MultiVersionTestData", "tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData", "MultiVersionTestData-3.0", 
				"tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData-3.0"));
		
		validate(rvs.getResolutionInfo(), "ValueSet9", "tag:informatics.mayo.edu,2013-08-03:vs/ValueSet9", null, "tag:informatics.mayo.edu,2013-08-03:vs/ValueSet9-4.0",
				resolvedUsing, new ArrayList<String>());
	}
	
	@Test
	public void testVersionResolution8()
	{
		Set<NameOrURI> shouldResolveUsing = new HashSet<NameOrURI>();
		NameOrURI nou = new NameOrURI();
		nou.setUri("tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData-1.0");
		shouldResolveUsing.add(nou);
		
		Page p = new Page();
		p.setMaxToReturn(50);
		p.setPage(0);
		
		ResolvedValueSetResult<EntityDirectoryEntry> rvsr = vsr.resolveDefinitionAsEntityDirectory(
				new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet9-5.0"), 
				shouldResolveUsing, null, null, null, null, p);
		
		validate(rvsr.getEntries(), assemble("tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData", new String[] {"157", "42"}), false);
		
		DescriptionInCodeSystem[] d = (rvsr.getEntries().get(0).getResourceName().equals("157") ? 
				rvsr.getEntries().get(0).getKnownEntityDescription() : rvsr.getEntries().get(1).getKnownEntityDescription()); 
		
		assertEquals("something in version 1",d[0].getDesignation());
		assertEquals(1, d.length);
		
		HashSet<String> resolvedUsing = new HashSet<String>();
		resolvedUsing.add(makeString("MultiVersionTestData", "tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData", "MultiVersionTestData-1.0", 
				"tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData-1.0"));
		
		validate(rvsr.getResolvedValueSetHeader(), "ValueSet9", "tag:informatics.mayo.edu,2013-08-03:vs/ValueSet9", null, "tag:informatics.mayo.edu,2013-08-03:vs/ValueSet9-5.0",
				resolvedUsing, new ArrayList<String>());
	}
	
	@Test
	public void testVersionResolution9()
	{
		Set<NameOrURI> shouldResolveUsing = new HashSet<NameOrURI>();
		NameOrURI nou = new NameOrURI();
		nou.setUri("tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData-3.0");
		shouldResolveUsing.add(nou);
		
		Page p = new Page();
		p.setMaxToReturn(50);
		p.setPage(0);
		
		ResolvedValueSetResult<EntityDirectoryEntry> rvsr = vsr.resolveDefinitionAsEntityDirectory(
				new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet9-5.0"), 
				shouldResolveUsing, null, null, null, null, p);
		validate(rvsr.getEntries(), assemble("tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData", new String[] {"157", "42"}), false);
		
		DescriptionInCodeSystem[] d = (rvsr.getEntries().get(0).getResourceName().equals("157") ? 
				rvsr.getEntries().get(0).getKnownEntityDescription() : rvsr.getEntries().get(1).getKnownEntityDescription()); 
		
		assertEquals("same concept redefined with a different desc in ver 3",d[0].getDesignation());
		assertEquals(1, d.length);
		
		HashSet<String> resolvedUsing = new HashSet<String>();
		resolvedUsing.add(makeString("MultiVersionTestData", "tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData", "MultiVersionTestData-3.0", 
				"tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData-3.0"));
		
		validate(rvsr.getResolvedValueSetHeader(), "ValueSet9", "tag:informatics.mayo.edu,2013-08-03:vs/ValueSet9", null, "tag:informatics.mayo.edu,2013-08-03:vs/ValueSet9-5.0",
				resolvedUsing, new ArrayList<String>());
	}
	
	//same test as 8, but this time, using an association to find entities
	@Test
	public void testVersionResolution8_1()
	{
		Set<NameOrURI> shouldResolveUsing = new HashSet<NameOrURI>();
		NameOrURI nou = new NameOrURI();
		nou.setUri("tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData-1.0");
		shouldResolveUsing.add(nou);
		
		Page p = new Page();
		p.setMaxToReturn(50);
		p.setPage(0);
		
		ResolvedValueSetResult<EntityDirectoryEntry> rvsr = vsr.resolveDefinitionAsEntityDirectory(
				new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet9-6.0"), 
				shouldResolveUsing, null, null, null, null, p);
		
		validate(rvsr.getEntries(), assemble("tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData", new String[] {"157"}), false);
		
		DescriptionInCodeSystem[] d = (rvsr.getEntries().get(0).getResourceName().equals("157") ? 
				rvsr.getEntries().get(0).getKnownEntityDescription() : rvsr.getEntries().get(1).getKnownEntityDescription()); 
		
		assertEquals("something in version 1",d[0].getDesignation());
		assertEquals(1, d.length);
		
		HashSet<String> resolvedUsing = new HashSet<String>();
		resolvedUsing.add(makeString("MultiVersionTestData", "tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData", "MultiVersionTestData-1.0", 
				"tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData-1.0"));
		
		validate(rvsr.getResolvedValueSetHeader(), "ValueSet9", "tag:informatics.mayo.edu,2013-08-03:vs/ValueSet9", null, "tag:informatics.mayo.edu,2013-08-03:vs/ValueSet9-6.0",
				resolvedUsing, new ArrayList<String>());
	}
	
	//same test as 9, but this time, using an association to find entities
	@Test
	public void testVersionResolution9_1()
	{
		Set<NameOrURI> shouldResolveUsing = new HashSet<NameOrURI>();
		NameOrURI nou = new NameOrURI();
		nou.setUri("tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData-3.0");
		shouldResolveUsing.add(nou);
		
		Page p = new Page();
		p.setMaxToReturn(50);
		p.setPage(0);
		
		ResolvedValueSetResult<EntityDirectoryEntry> rvsr = vsr.resolveDefinitionAsEntityDirectory(
				new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet9-6.0"), 
				shouldResolveUsing, null, null, null, null, p);
		validate(rvsr.getEntries(), assemble("tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData", new String[] {"157"}), false);
		
		DescriptionInCodeSystem[] d = (rvsr.getEntries().get(0).getResourceName().equals("157") ? 
				rvsr.getEntries().get(0).getKnownEntityDescription() : rvsr.getEntries().get(1).getKnownEntityDescription()); 
		
		assertEquals("same concept redefined with a different desc in ver 3",d[0].getDesignation());
		assertEquals(1, d.length);
		
		HashSet<String> resolvedUsing = new HashSet<String>();
		resolvedUsing.add(makeString("MultiVersionTestData", "tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData", "MultiVersionTestData-3.0", 
				"tag:informatics.mayo.edu,2013-08-03:MultiVersionTestData-3.0"));
		
		validate(rvsr.getResolvedValueSetHeader(), "ValueSet9", "tag:informatics.mayo.edu,2013-08-03:vs/ValueSet9", null, "tag:informatics.mayo.edu,2013-08-03:vs/ValueSet9-6.0",
				resolvedUsing, new ArrayList<String>());
	}
	
	private String[] assemble(String codeSystemVersionURI, String[] codes)
	{
		String[] result = new String[codes.length];
		for (int i = 0; i < codes.length; i++)
		{
			result[i] = codeSystemVersionURI + "/Concept#" + codes[i];
		}
		return result;
	}
	
	private void validate(ResolvedValueSet rvs, String[] codes, boolean orderMatters)
	{
		assertEquals("Resolved ValueSet does not contain the expected number of results.", codes.length, rvs.getEntryCount());
		
		if (orderMatters)
		{
			for (int i = 0; i < codes.length; i++)
			{
				assertEquals("Incorrect code", codes[i], rvs.getEntry(i).getUri());
			}
		}
		else
		{
			HashSet<String> temp = new HashSet<String>(Arrays.asList(codes));
			
			for (int i = 0; i < codes.length; i++)
			{
				boolean removed = temp.remove(rvs.getEntry(i).getUri());
				assertTrue("Incorrect code, found " + rvs.getEntry(i).getUri() + " which was not expected", removed);
			}
			assertEquals(temp.size(), 0);
		}
	}
	
	private void validate(List<EntityDirectoryEntry> entries, String[] codes, boolean orderMatters)
	{
		assertEquals("Resolved ValueSet does not contain the expected number of results.", codes.length, entries.size());
		
		if (orderMatters)
		{
			for (int i = 0; i < codes.length; i++)
			{
				assertEquals("Incorrect code", codes[i], entries.get(i).getAbout());
			}
		}
		else
		{
			HashSet<String> temp = new HashSet<String>(Arrays.asList(codes));
			
			for (int i = 0; i < codes.length; i++)
			{
				boolean removed = temp.remove(entries.get(i).getAbout());
				assertTrue("Incorrect code, found " + entries.get(i).getAbout() + " which was not expected", removed);
			}
			assertEquals(temp.size(), 0);
		}
	}
	
	private String makeString(String codeSystemName, String codeSystemURI, String codeSystemVersionName, String codeSystemVersionURI)
	{
		return codeSystemName + ":" + codeSystemURI + ":" + codeSystemVersionName + ":" + codeSystemVersionURI;
	}
	
	private String makeString(CodeSystemVersionReference csvr)
	{
		return makeString(csvr.getCodeSystem().getContent(),  csvr.getCodeSystem().getUri(), csvr.getVersion().getContent(), csvr.getVersion().getUri());
	}
	
	private void validate(ResolvedValueSetHeader rvsh, String valueSetName, String valueSetURI, String valueSetDefinitionName, String valueSetDefinitionURI, 
			HashSet<String> resolvedUsing, List<String> containsNestedValueSetURI)
	{
		assertEquals(rvsh.getResolutionOf().getValueSet().getUri(), valueSetURI);
		assertEquals(rvsh.getResolutionOf().getValueSet().getContent(), valueSetName);
		assertEquals(rvsh.getResolutionOf().getValueSetDefinition().getUri(), valueSetDefinitionURI);
		if (valueSetDefinitionName != null)
		{
			//Due to multi-threading during load, we can't always predict what the valueSetDefinitionName will be - if null is passed in
			//it is a case where we won't be able to predict.
			assertEquals(rvsh.getResolutionOf().getValueSetDefinition().getContent(), valueSetDefinitionName);
		}
		
		assertEquals(rvsh.getResolvedUsingCodeSystemCount(), resolvedUsing.size());
		for (CodeSystemVersionReference csvr : rvsh.getResolvedUsingCodeSystemAsReference())
		{
			assertTrue(resolvedUsing.contains(makeString(csvr)));
		}
		
		assertEquals(rvsh.getIncludesResolvedValueSetCount(), containsNestedValueSetURI.size());
		for (ResolvedValueSetHeader nestedRvsh : rvsh.getIncludesResolvedValueSetAsReference())
		{
			boolean found = false;
			for (String shouldContainNested : containsNestedValueSetURI)
			{
				if (nestedRvsh.getResolutionOf().getValueSetDefinition().getUri().equals(shouldContainNested))
				{
					found = true;
					break;
				}
			}
			if (!found)
			{
				fail("nested IncludedResolvedValueSetAsReference list is missing an item it should have");
			}
		}	
	}
}
