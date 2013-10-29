package edu.mayo.cts2.framework.plugin.service.exist.valuesetResolution.integration;

import java.io.IOException;
import javax.xml.transform.stream.StreamSource;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.oxm.XmlMappingException;
import org.xmldb.api.base.XMLDBException;
import edu.mayo.cts2.framework.core.client.Cts2RestClient;
import edu.mayo.cts2.framework.core.xml.DelegatingMarshaller;
import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.core.types.SetOperator;
import edu.mayo.cts2.framework.model.entity.EntityDirectoryEntry;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.service.core.NameOrURIList;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.model.service.core.Query6Choice;
import edu.mayo.cts2.framework.model.service.core.Query6Choice2;
import edu.mayo.cts2.framework.model.updates.ChangeSet;
import edu.mayo.cts2.framework.model.updates.ChangeableResource;
import edu.mayo.cts2.framework.plugin.service.exist.valuesetResolution.JUnitAnnoyance;
import edu.mayo.cts2.framework.plugin.service.exist.valuesetResolution.ValueSetDefinitionResolutionTest;
import edu.mayo.cts2.framework.plugin.service.exist.valuesetResolution.ValueSetDefinitionResolutionTestBase;
import edu.mayo.cts2.framework.plugin.service.valueSetDefinitionResolutionServices.ctsUtility.queryBuilders.ResolvedValueSetResolutionEntityQueryBuilder;
import edu.mayo.cts2.framework.service.constant.ExternalCts2Constants;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ResolvedValueSetResolutionEntityQuery;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ResolvedValueSetResult;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.name.ValueSetDefinitionReadId;

/**
 * These tests have to be run as a (IT) integration test - because currently the valueSetDefinitionResolution service can only
 * resolve a query URI by using the REST interface - so we need to have a REST server up and running (with the content) to resolve
 * the QUERY URIs.
 * 
 * This leads to a funny situation where we have two Exist servers running at the same time - we have a local server handling the first
 * part of the resolve (just like it does for all of the tests in ValueSetDefinitionResolutionTest) but when it encounters the URI in the 
 * query, it will resolve it against the IT server which has its own (distinct) existDB.
 * 
 * So prior to running these tests, we load all of the content twice - once in the local server (see superclass), and once in the IT server.
 * @author darmbrust
 *
 */
public class ValueSetDefinitionResolutionTestIT extends ValueSetDefinitionResolutionTestBase
{
	private final static String rootURL = "http://localhost:1984/webapp-rest/";
	
	// no idea why this needs to be static... something funny about how the spring junit test runner handles this.
	protected static boolean remoteDbInited = false;
	private static String changeSetURI;
	
	//same here... spring (or someone) is nulling out variables between tests for some reason ( but not consistently??)
	protected static String changeSetAddOn;
	
	@BeforeClass
	public static void init()
	{
		JUnitAnnoyance.reset();
	}
	
	// Sigh - BeforeClass requires static, which doesn't play nice with spring...
	@Before
	public void setupExistRemote() throws XMLDBException, XmlMappingException, IOException
	{
		setupExist();
		if (!remoteDbInited)
		{
			Cts2RestClient restClient = Cts2RestClient.instance();

			DelegatingMarshaller marshaller = new DelegatingMarshaller();
			
			ChangeSet mergedChangeSet = (ChangeSet) marshaller.unmarshal(new StreamSource(ValueSetDefinitionResolutionTest.class
					.getResourceAsStream("/valuesetResolutionData/CodeSystems.xml")));
			
			ChangeSet changeSet = (ChangeSet) marshaller.unmarshal(new StreamSource(ValueSetDefinitionResolutionTest.class
					.getResourceAsStream("/valuesetResolutionData/Entities.xml")));
			
			for (ChangeableResource cr : changeSet.getMemberAsReference())
			{
				cr.getChangeableElementGroup().getChangeDescription().setContainingChangeSet(mergedChangeSet.getChangeSetURI());
				mergedChangeSet.getMemberAsReference().add(cr);
			}

			changeSet = (ChangeSet) marshaller.unmarshal(new StreamSource(ValueSetDefinitionResolutionTest.class
					.getResourceAsStream("/valuesetResolutionData/ValueSets.xml")));
			
			for (ChangeableResource cr : changeSet.getMemberAsReference())
			{
				cr.getChangeableElementGroup().getChangeDescription().setContainingChangeSet(mergedChangeSet.getChangeSetURI());
				mergedChangeSet.getMemberAsReference().add(cr);
			}

			restClient.putCts2Resource(rootURL + "changeset/" + mergedChangeSet.getChangeSetURI() + "?commit=false", mergedChangeSet);
			changeSetURI = mergedChangeSet.getChangeSetURI();
			
			changeSetAddOn = "changesetcontext="+changeSetURI;
			remoteDbInited = true;
		}
	}
	
	@AfterClass
	public static void cleanup()
	{
		Cts2RestClient.instance().deleteCts2Resource(rootURL + "changeset/" + changeSetURI);
	}
	
	@Test
	public void queryTestSubQueries()
	{
		ResolvedValueSetResolutionEntityQuery query = ResolvedValueSetResolutionEntityQueryBuilder.build();

		Query6Choice subQuery = new Query6Choice();
		subQuery.setDirectoryUri1(rootURL + "valueset/ValueSet3/definition/1/entities?" + changeSetAddOn );
		
		query.getQuery().setQuery6Choice(subQuery);
		query.getQuery().setSetOperation(SetOperator.UNION);
		
		ResolvedValueSetResult<EntityDirectoryEntry> rvsr = vsr.resolveDefinitionAsEntityDirectory(
				new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet8-1.0"), 
				null, null, query, null, null, new Page());
		
		validate(rvsr.getEntries(), new String[] {"tag:informatics.mayo.edu,2013-08-03:UnbelievableTestData/Concept#A", 
			"tag:informatics.mayo.edu,2013-08-03:UnbelievableTestData/Concept#C",
			"tag:informatics.mayo.edu,2013-08-03:BelievableTestData/Concept#1"}, false);
	}
	
	@Test
	public void queryTestSubQueries2()
	{
		Query nestedQuery = new Query();
		
		Query6Choice nestedQueryLeft = new Query6Choice();
		nestedQueryLeft.setDirectoryUri1(rootURL + "codesystem/UnbelievableTestData/version/UnbelievableTestData-1.0/entities?" + changeSetAddOn);
		nestedQuery.setQuery6Choice(nestedQueryLeft);
		
		Query6Choice2 nestedQueryRight = new Query6Choice2();
		nestedQueryRight.setDirectoryUri2(rootURL + "valueset/ValueSet4/definition/1/entities?" + changeSetAddOn);
		nestedQuery.setQuery6Choice2(nestedQueryRight);

		nestedQuery.setSetOperation(SetOperator.SUBTRACT);
			
		
		ResolvedValueSetResolutionEntityQuery query = ResolvedValueSetResolutionEntityQueryBuilder.build();
		Query6Choice topQueryRight = new Query6Choice();
		topQueryRight.setQuery1(nestedQuery);
		query.getQuery().setQuery6Choice(topQueryRight);
		query.getQuery().setSetOperation(SetOperator.INTERSECT);
		
		ResolvedValueSetResult<EntityDirectoryEntry> rvsr = vsr.resolveDefinitionAsEntityDirectory(
				new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet1-1.0"), 
				null, null, query, null, null, new Page());
		
		validate(rvsr.getEntries(), assemble("tag:informatics.mayo.edu,2013-08-03:UnbelievableTestData", new String[] {"C"}), false);
	}
	
	@Test
	public void queryTestSubQueries3()
	{
		Query nestedQuery = new Query();
		
		Query6Choice nestedQueryLeft = new Query6Choice();
		nestedQueryLeft.setDirectoryUri1(rootURL + "codesystem/UnbelievableTestData/version/UnbelievableTestData-1.0/entities?" + changeSetAddOn);
		nestedQuery.setQuery6Choice(nestedQueryLeft);
		
		Query6Choice2 nestedQueryRight = new Query6Choice2();
		nestedQueryRight.setDirectoryUri2(rootURL + "codesystem/UnbelievableTestData/version/BelievableTestData-1.0/entities?" + changeSetAddOn);
		nestedQuery.setQuery6Choice2(nestedQueryRight);
		
		nestedQuery.setSetOperation(SetOperator.UNION);
		
		NameOrURIList filters = new NameOrURIList();
		NameOrURI nou = new NameOrURI();
		nou.setName(ExternalCts2Constants.MA_ENTITY_DESCRIPTION_DESIGNATION_NAME);
		filters.addEntry(nou);
		nestedQuery.setFilterComponent(filters);
		NameOrURI matchAlgorithm = new NameOrURI();
		matchAlgorithm.setName("contains");
		nestedQuery.setMatchAlgorithm(matchAlgorithm);
		nestedQuery.setMatchValue("filter");
		
		ResolvedValueSetResolutionEntityQuery query = ResolvedValueSetResolutionEntityQueryBuilder.build();
		Query6Choice topQueryRight = new Query6Choice();
		topQueryRight.setQuery1(nestedQuery);
		query.getQuery().setQuery6Choice(topQueryRight);
		query.getQuery().setSetOperation(SetOperator.UNION);
		
		ResolvedValueSetResult<EntityDirectoryEntry> rvsr = vsr.resolveDefinitionAsEntityDirectory(
				new ValueSetDefinitionReadId("tag:informatics.mayo.edu,2013-08-03:vs/ValueSet3-1.0"), 
				null, null, query, null, null, new Page());
		
		validate(rvsr.getEntries(), new String[] {"tag:informatics.mayo.edu,2013-08-03:UnbelievableTestData/Concept#A", 
			"tag:informatics.mayo.edu,2013-08-03:UnbelievableTestData/Concept#B", 
			"tag:informatics.mayo.edu,2013-08-03:UnbelievableTestData/Concept#C",
			"tag:informatics.mayo.edu,2013-08-03:BelievableTestData/Concept#6"}, false);
	}
}
