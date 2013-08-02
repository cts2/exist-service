package edu.mayo.cts2.framework.plugin.service.exist.profile.update
import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.model.command.ResolvedFilter
import edu.mayo.cts2.framework.model.core.ComponentReference
import edu.mayo.cts2.framework.plugin.service.exist.profile.BaseServiceDbCleaningBase
import edu.mayo.cts2.framework.service.meta.StandardMatchAlgorithmReference
import edu.mayo.cts2.framework.service.profile.update.ChangeSetQuery
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import static org.junit.Assert.assertEquals

class ExistChangeSetQueryServiceTestIT extends BaseServiceDbCleaningBase {

	@Autowired
	ExistChangeSetService service
	
	@Autowired
	ExistChangeSetQueryService query

	@Test void TestQuery(){
		
		changeSetService.createChangeSet().getChangeSetURI()
		
		def q = [
			getFilterComponent : { },
			getReadContext : { },
			getQuery : { },
			getRestrictions : { }
		] as ChangeSetQuery

		def result = query.getResourceSummaries(q, null, new Page())
		
		assertEquals 1, result.getEntries().size()
	}
	
	@Test void TestQueryFINALfilter(){
		
		def openUri = changeSetService.createChangeSet().getChangeSetURI()
		
		def closedUri = changeSetService.createChangeSet().getChangeSetURI()
		
		changeSetService.commitChangeSet(closedUri)
		
		def fc = new ResolvedFilter(
			matchAlgorithmReference:StandardMatchAlgorithmReference.EXACT_MATCH.getMatchAlgorithmReference(),
			matchValue:"FINAL",
			componentReference: new ComponentReference(attributeReference: "state"))
		
		def q = [
			getFilterComponent : { [fc] as Set },
			getReadContext : { },
			getQuery : { },
			getRestrictions : { }
		] as ChangeSetQuery

		def result = query.getResourceSummaries(q, null, new Page())
		
		assertEquals 1, result.getEntries().size()
		
		assertEquals closedUri, result.getEntries().get(0).getChangeSetURI()
	}
	
	@Test void TestQueryOPENfilter(){
		
		def openUri = changeSetService.createChangeSet().getChangeSetURI()
		
		def closedUri = changeSetService.createChangeSet().getChangeSetURI()
		
		changeSetService.commitChangeSet(closedUri)
		
		def fc = new ResolvedFilter(
			matchAlgorithmReference:StandardMatchAlgorithmReference.EXACT_MATCH.getMatchAlgorithmReference(),
			matchValue:"OPEN",
                componentReference: new ComponentReference(attributeReference: "state"))
		
		def q = [
			getFilterComponent : { [fc] as Set },
			getReadContext : { },
			getQuery : { },
			getRestrictions : { }
		] as ChangeSetQuery

		def result = query.getResourceSummaries(q, null, new Page())
		
		assertEquals 1, result.getEntries().size()
		
		assertEquals openUri, result.getEntries().get(0).getChangeSetURI()
	}
}
