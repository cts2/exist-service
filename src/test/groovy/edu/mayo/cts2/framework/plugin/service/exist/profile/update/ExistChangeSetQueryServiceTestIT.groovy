package edu.mayo.cts2.framework.plugin.service.exist.profile.update

import static org.junit.Assert.*

import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.model.command.ResolvedFilter
import edu.mayo.cts2.framework.model.core.ModelAttributeReference
import edu.mayo.cts2.framework.model.core.types.TargetReferenceType
import edu.mayo.cts2.framework.model.mapversion.*
import edu.mayo.cts2.framework.plugin.service.exist.profile.BaseServiceDbCleaningBase
import edu.mayo.cts2.framework.service.constant.ExternalCts2Constants
import edu.mayo.cts2.framework.service.meta.StandardMatchAlgorithmReference

class ExistChangeSetQueryServiceTestIT extends BaseServiceDbCleaningBase {

	@Autowired
	ExistChangeSetService service
	
	@Autowired
	ExistChangeSetQueryService query

	@Test void TestQuery(){
		
		changeSetService.createChangeSet().getChangeSetURI()

		def result = query.getResourceSummaries(null, null, null, new Page())
		
		assertEquals 1, result.getEntries().size()
	}
	
	@Test void TestQueryFINALfilter(){
		
		def openUri = changeSetService.createChangeSet().getChangeSetURI()
		
		def closedUri = changeSetService.createChangeSet().getChangeSetURI()
		
		changeSetService.commitChangeSet(closedUri)
		
		def fc = new ResolvedFilter(
			matchAlgorithmReference:StandardMatchAlgorithmReference.EXACT_MATCH.getMatchAlgorithmReference(),
			matchValue:"FINAL",
			modelAttributeReference: new ModelAttributeReference(content:"state"),
			referenceType:TargetReferenceType.ATTRIBUTE)

		def result = query.getResourceSummaries(null, [fc] as Set, null, new Page())
		
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
			modelAttributeReference: new ModelAttributeReference(content:"state"),
			referenceType:TargetReferenceType.ATTRIBUTE)

		def result = query.getResourceSummaries(null, [fc] as Set, null, new Page())
		
		assertEquals 1, result.getEntries().size()
		
		assertEquals openUri, result.getEntries().get(0).getChangeSetURI()
	}
}
