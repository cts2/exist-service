package edu.mayo.cts2.framework.plugin.service.exist.valuesetResolution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
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
import edu.mayo.cts2.framework.model.core.ComponentReference;
import edu.mayo.cts2.framework.model.core.SortCriteria;
import edu.mayo.cts2.framework.model.core.SortCriterion;
import edu.mayo.cts2.framework.model.core.types.SortDirection;
import edu.mayo.cts2.framework.model.updates.ChangeSet;
import edu.mayo.cts2.framework.model.valuesetdefinition.ResolvedValueSet;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistManager;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistResourceDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.update.ExistChangeSetService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.valuesetdefinition.ExistValueSetDefinitionResolutionService;
import edu.mayo.cts2.framework.service.constant.ExternalCts2Constants;
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

}
