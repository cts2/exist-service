package edu.mayo.cts2.framework.plugin.service.exist.valuesetResolution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.oxm.XmlMappingException;
import org.xmldb.api.base.XMLDBException;
import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.core.ComponentReference;
import edu.mayo.cts2.framework.model.core.DescriptionInCodeSystem;
import edu.mayo.cts2.framework.model.core.ScopedEntityName;
import edu.mayo.cts2.framework.model.core.SortCriteria;
import edu.mayo.cts2.framework.model.core.SortCriterion;
import edu.mayo.cts2.framework.model.core.types.SortDirection;
import edu.mayo.cts2.framework.model.entity.EntityDirectoryEntry;
import edu.mayo.cts2.framework.model.service.core.EntityNameOrURI;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.service.exception.UnknownCodeSystemVersion;
import edu.mayo.cts2.framework.model.valuesetdefinition.ResolvedValueSet;
import edu.mayo.cts2.framework.plugin.service.valueSetDefinitionResolutionServices.ctsUtility.queryBuilders.ResolvedValueSetResolutionEntityQueryBuilder;
import edu.mayo.cts2.framework.service.constant.ExternalCts2Constants;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ResolvedValueSetResolutionEntityQuery;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ResolvedValueSetResult;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.name.ValueSetDefinitionReadId;

public class ValueSetDefinitionResolutionTest extends ValueSetDefinitionResolutionTestBase
{	
	@BeforeClass
	public static void init()
	{
		JUnitAnnoyance.reset();
	}
	
	@Before
	public void dataInit() throws XmlMappingException, XMLDBException, IOException
	{
		setupExist();
	}
	
	@Test
	public void testCompleteCodeSystem()
	{
		ResolvedValueSet rvs = vsr.resolveDefinitionAsCompleteSet(new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet1-1.0"), null, null, null, null);
		validate(rvs, assemble("tag:informatics.mayo.edu,2013-08-03:UnbelievableTestData", new String[] {"A", "B", "C", "D"}), false);
		
		HashSet<String> resolvedUsing = new HashSet<String>();
		resolvedUsing.add(makeString("UnbelievableTestData", "tag:informatics.mayo.edu,2013-08-03:UnbelievableTestData", "UnbelievableTestData-1.0", 
				"tag:informatics.mayo.edu,2013-08-03:UnbelievableTestData-1.0"));
		
		validate(rvs.getResolutionInfo(), "ValueSet1", "tag:informatics.mayo.edu,2013-08-03:vs/ValueSet1", null, "tag:informatics.mayo.edu,2013-08-03:vs/ValueSet1-1.0",
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

		
		validate(rvs.getResolutionInfo(), "ValueSet8", "tag:informatics.mayo.edu,2013-08-03:vs/ValueSet8", null, "tag:informatics.mayo.edu,2013-08-03:vs/ValueSet8-1.0",
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
	
	@Test
	public void queryTestFiltering1()
	{
		ResolvedValueSetResolutionEntityQuery query = ResolvedValueSetResolutionEntityQueryBuilder.build();
		query.getFilterComponent().add(buildFilter(ExternalCts2Constants.MA_RESOURCE_NAME_NAME, "exactMatch", "3"));
		
		ResolvedValueSetResult<EntityDirectoryEntry> rvsr = vsr.resolveDefinitionAsEntityDirectory(
				new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet6-1.0"), 
				null, null, query, null, null, new Page());
		
		validate(rvsr.getEntries(), assemble("tag:informatics.mayo.edu,2013-08-03:BelievableTestData", new String[] {"3"}), false);
	}
	
	@Test
	public void queryTestFiltering2()
	{
		ResolvedValueSetResolutionEntityQuery query = ResolvedValueSetResolutionEntityQueryBuilder.build();
		query.getFilterComponent().add(buildFilter(ExternalCts2Constants.MA_ENTITY_DESCRIPTION_DESIGNATION_NAME, "contains", "number"));
		
		ResolvedValueSetResult<EntityDirectoryEntry> rvsr = vsr.resolveDefinitionAsEntityDirectory(
				new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet6-1.0"), 
				null, null, query, null, null, new Page());
		
		validate(rvsr.getEntries(), assemble("tag:informatics.mayo.edu,2013-08-03:BelievableTestData", new String[] {"12", "111"}), false);
	}
	
	@Test
	public void queryTestFiltering3()
	{
		ResolvedValueSetResolutionEntityQuery query = ResolvedValueSetResolutionEntityQueryBuilder.build();
		query.getFilterComponent().add(buildFilter(ExternalCts2Constants.MA_ENTITY_DESCRIPTION_DESIGNATION_NAME, "startsWith", "a"));
		
		ResolvedValueSetResult<EntityDirectoryEntry> rvsr = vsr.resolveDefinitionAsEntityDirectory(
				new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet6-1.0"), 
				null, null, query, null, null, new Page());
		
		validate(rvsr.getEntries(), assemble("tag:informatics.mayo.edu,2013-08-03:BelievableTestData", new String[] {"1", "2"}), false);
	}
	
	@Test
	public void queryTestEntityRestrictions()
	{
		ResolvedValueSetResolutionEntityQuery query = ResolvedValueSetResolutionEntityQueryBuilder.build();
		EntityNameOrURI en = new EntityNameOrURI();
		ScopedEntityName sen = new ScopedEntityName();
		sen.setName("2");
		sen.setNamespace("BelievableTestData");
		en.setEntityName(sen);
		query.getResolvedValueSetResolutionEntityRestrictions().getEntities().add(en);
		en = new EntityNameOrURI();
		en.setUri("tag:informatics.mayo.edu,2013-08-03:BelievableTestData/Concept#11");
		query.getResolvedValueSetResolutionEntityRestrictions().getEntities().add(en);
		
		ResolvedValueSetResult<EntityDirectoryEntry> rvsr = vsr.resolveDefinitionAsEntityDirectory(
				new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet6-1.0"), 
				null, null, query, null, null, new Page());
		
		validate(rvsr.getEntries(), assemble("tag:informatics.mayo.edu,2013-08-03:BelievableTestData", new String[] {"2", "11"}), false);
	}
	
	@Test
	public void queryTestEntityRestrictions2()
	{
		ResolvedValueSetResolutionEntityQuery query = ResolvedValueSetResolutionEntityQueryBuilder.build();

		NameOrURI nou = new NameOrURI();
		nou.setName("UnbelievableTestData-1.0");
		query.getResolvedValueSetResolutionEntityRestrictions().setCodeSystemVersion(nou);
		
		ResolvedValueSetResult<EntityDirectoryEntry> rvsr = vsr.resolveDefinitionAsEntityDirectory(
				new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet8-1.0"), 
				null, null, query, null, null, new Page());
		
		validate(rvsr.getEntries(), assemble("tag:informatics.mayo.edu,2013-08-03:UnbelievableTestData", new String[] {"A"}), false);
	}
	
	@Test
	public void queryTestEntityRestrictions3()
	{
		ResolvedValueSetResolutionEntityQuery query = ResolvedValueSetResolutionEntityQueryBuilder.build();

		NameOrURI nou = new NameOrURI();
		nou.setUri("tag:informatics.mayo.edu,2013-08-03:BelievableTestData-1.0");
		query.getResolvedValueSetResolutionEntityRestrictions().setCodeSystemVersion(nou);
		
		ResolvedValueSetResult<EntityDirectoryEntry> rvsr = vsr.resolveDefinitionAsEntityDirectory(
				new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet8-1.0"), 
				null, null, query, null, null, new Page());
		
		validate(rvsr.getEntries(), assemble("tag:informatics.mayo.edu,2013-08-03:BelievableTestData", new String[] {"1"}), false);
	}
}
