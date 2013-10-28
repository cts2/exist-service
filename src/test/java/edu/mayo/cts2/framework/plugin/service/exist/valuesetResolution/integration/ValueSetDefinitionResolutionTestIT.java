package edu.mayo.cts2.framework.plugin.service.exist.valuesetResolution.integration;

import java.io.IOException;
import javax.xml.transform.stream.StreamSource;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.oxm.XmlMappingException;
import org.xmldb.api.base.XMLDBException;
import edu.mayo.cts2.framework.core.client.Cts2RestClient;
import edu.mayo.cts2.framework.core.xml.DelegatingMarshaller;
import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.core.types.SetOperator;
import edu.mayo.cts2.framework.model.entity.EntityDirectoryEntry;
import edu.mayo.cts2.framework.model.service.core.Query6Choice;
import edu.mayo.cts2.framework.model.updates.ChangeSet;
import edu.mayo.cts2.framework.model.updates.ChangeableResource;
import edu.mayo.cts2.framework.plugin.service.exist.valuesetResolution.ValueSetDefinitionResolutionTest;
import edu.mayo.cts2.framework.plugin.service.exist.valuesetResolution.ValueSetDefinitionResolutionTestBase;
import edu.mayo.cts2.framework.plugin.service.valueSetDefinitionResolutionServices.ctsUtility.queryBuilders.ResolvedValueSetResolutionEntityQueryBuilder;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ResolvedValueSetResolutionEntityQuery;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ResolvedValueSetResult;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.name.ValueSetDefinitionReadId;

public class ValueSetDefinitionResolutionTestIT extends ValueSetDefinitionResolutionTestBase
{
	private final static String rootURL = "http://localhost:1984/webapp-rest/";
	
	// no idea why this needs to be static... something funny about how the spring junit test runner handles this.
	protected static boolean remoteDbInited = false;
	private static String changeSetURI;
	
	protected String changeSetAddOn;
	
	public ValueSetDefinitionResolutionTestIT()
	{
		dbInited = false;
	}
	
	// Sigh - BeforeClass requires static, which doesn't play nice with spring...
	@Before
	public void setupExistRemote() throws XMLDBException, XmlMappingException, IOException
	{
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
}
