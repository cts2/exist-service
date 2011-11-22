package edu.mayo.cts2.framework.plugin.service.exist.profile.codesystem

import static org.junit.Assert.*

import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry
import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.model.command.ResolvedFilter
import edu.mayo.cts2.framework.model.command.ResolvedReadContext
import edu.mayo.cts2.framework.model.core.ChangeDescription
import edu.mayo.cts2.framework.model.core.ChangeableElementGroup
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference
import edu.mayo.cts2.framework.model.core.ModelAttributeReference
import edu.mayo.cts2.framework.model.core.types.ChangeCommitted
import edu.mayo.cts2.framework.model.core.types.ChangeType
import edu.mayo.cts2.framework.model.core.types.TargetReferenceType
import edu.mayo.cts2.framework.model.mapversion.*
import edu.mayo.cts2.framework.model.service.core.NameOrURI
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.plugin.service.exist.profile.BaseServiceDbCleaningBase
import edu.mayo.cts2.framework.service.constant.ExternalCts2Constants
import edu.mayo.cts2.framework.service.meta.StandardMatchAlgorithmReference

class ExistCodeSystemServiceGroovyTestIT extends BaseServiceDbCleaningBase {

	@Autowired
	ExistCodeSystemReadService read
	
	@Autowired
	ExistCodeSystemQueryService query
	
	@Autowired
	ExistCodeSystemMaintenanceService maint

	@Test void TestReadContextNoChangeSetURI(){
		
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()
		
		def cs = new CodeSystemCatalogEntry(about:"about",codeSystemName:"name")
		cs.setChangeableElementGroup(new ChangeableElementGroup(
			changeDescription: new ChangeDescription(
				changeType: ChangeType.CREATE,
				changeDate: new Date(),
				containingChangeSet: changeSetUri)))
		
		maint.createResource(cs)

		assertNull read.read(new NameOrURI(name:"name"), null)
	}

	@Test void TestReadContextWithURI(){
		
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()
		
		def cs = new CodeSystemCatalogEntry(about:"about",codeSystemName:"name")
		cs.setChangeableElementGroup(new ChangeableElementGroup(
			changeDescription: new ChangeDescription(
				changeType: ChangeType.CREATE,
				changeDate: new Date(),
				containingChangeSet: changeSetUri)))
		
		maint.createResource(cs)

		assertNotNull read.read(new NameOrURI(name:"name"), new ResolvedReadContext(changeSetContextUri:changeSetUri))
	}
	
	@Test void TestQueryWithURI(){
		
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()
		
		def cs = new CodeSystemCatalogEntry(about:"about",codeSystemName:"name")
		cs.setChangeableElementGroup(new ChangeableElementGroup(
			changeDescription: new ChangeDescription(
				changeType: ChangeType.CREATE,
				changeDate: new Date(),
				containingChangeSet: changeSetUri)))
		
		maint.createResource(cs)

		assertEquals 1, query.getResourceSummaries(
			null, null, null, new ResolvedReadContext(changeSetContextUri:changeSetUri), new Page()).entries.size()
	}
	
	@Test void TestQueryWithNoURI(){
		
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()
		
		def cs = new CodeSystemCatalogEntry(about:"about",codeSystemName:"name")
		cs.setChangeableElementGroup(new ChangeableElementGroup(
			changeDescription: new ChangeDescription(
				changeType: ChangeType.CREATE,
				changeDate: new Date(),
				containingChangeSet: changeSetUri)))
		
		maint.createResource(cs)

		assertEquals 0, query.getResourceSummaries(
			null, null, null, null, new Page()).entries.size()
	}
	
	@Test void TestQueryWithContains(){
		
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()
		
		def cs = new CodeSystemCatalogEntry(about:"about",codeSystemName:"name")
		cs.setChangeableElementGroup(new ChangeableElementGroup(
			changeDescription: new ChangeDescription(
				changeType: ChangeType.CREATE,
				changeDate: new Date(),
				containingChangeSet: changeSetUri)))
		
		maint.createResource(cs)
		changeSetService.commitChangeSet(changeSetUri)
		
		def filter = new ResolvedFilter(
			matchAlgorithmReference:new MatchAlgorithmReference(content:"contains"),
			matchValue:"am",
			modelAttributeReference:new ModelAttributeReference(content:"resourceName")
		)

		assertEquals 1, query.getResourceSummaries(
			null, [filter] as Set, null, null, new Page()).entries.size()
	}
	
	@Test void TestCommittedChangeStatus(){
		
		def changeSetUri1 = changeSetService.createChangeSet().getChangeSetURI()
		
		def cs1 = new CodeSystemCatalogEntry(about:"about",codeSystemName:"name")
		cs1.setChangeableElementGroup(new ChangeableElementGroup(
			changeDescription: new ChangeDescription(
				committed: ChangeCommitted.PENDING,
				changeType: ChangeType.CREATE,
				changeDate: new Date(),
				containingChangeSet: changeSetUri1)))
		
		maint.createResource(cs1)
		changeSetService.commitChangeSet(changeSetUri1)
		
		def csFromRead = read.read(ModelUtils.nameOrUriFromName("name"), null);

		assertEquals ChangeCommitted.COMMITTED, 
			csFromRead.getChangeableElementGroup().getChangeDescription().getCommitted()
	}
	
	@Test void TestQueryWithOpenAndCommittedChangeSetNoReadContext(){
		
		def changeSetUri1 = changeSetService.createChangeSet().getChangeSetURI()
		
		def cs1 = new CodeSystemCatalogEntry(about:"about",codeSystemName:"name")
		cs1.setChangeableElementGroup(new ChangeableElementGroup(
			changeDescription: new ChangeDescription(
				changeType: ChangeType.CREATE,
				changeDate: new Date(),
				containingChangeSet: changeSetUri1)))
		
		maint.createResource(cs1)
		changeSetService.commitChangeSet(changeSetUri1)
		
		def changeSetUri2 = changeSetService.createChangeSet().getChangeSetURI()
		
		def cs2 = new CodeSystemCatalogEntry(about:"about",codeSystemName:"name")
		cs2.setChangeableElementGroup(new ChangeableElementGroup(
			changeDescription: new ChangeDescription(
				changeType: ChangeType.CREATE,
				changeDate: new Date(),
				containingChangeSet: changeSetUri2)))
		
		maint.createResource(cs2)

		assertEquals 1, query.getResourceSummaries(
			null, null, null, null, new Page()).entries.size()
	}
	
	@Test void TestQueryWithOpenAndCommittedChangeSetWithReadContext(){
		
		def changeSetUri1 = changeSetService.createChangeSet().getChangeSetURI()
		
		def cs1 = new CodeSystemCatalogEntry(about:"about",codeSystemName:"name")
		cs1.setChangeableElementGroup(new ChangeableElementGroup(
			changeDescription: new ChangeDescription(
				changeType: ChangeType.CREATE,
				changeDate: new Date(),
				containingChangeSet: changeSetUri1)))
		
		maint.createResource(cs1)
		changeSetService.commitChangeSet(changeSetUri1)
		
		def changeSetUri2 = changeSetService.createChangeSet().getChangeSetURI()
		
		def cs2 = new CodeSystemCatalogEntry(about:"about2",codeSystemName:"name2")
		cs2.setChangeableElementGroup(new ChangeableElementGroup(
			changeDescription: new ChangeDescription(
				changeType: ChangeType.CREATE,
				changeDate: new Date(),
				containingChangeSet: changeSetUri2)))
		
		maint.createResource(cs2)

		assertEquals 2, query.getResourceSummaries(
			null, null, null, new ResolvedReadContext(changeSetContextUri:changeSetUri2), new Page()).entries.size()
	}
	
	@Test void TestQueryWithOpenAndCommittedChangeSetWithReadContextModify(){
		
		def changeSetUri1 = changeSetService.createChangeSet().getChangeSetURI()
		
		def cs1 = new CodeSystemCatalogEntry(about:"about",codeSystemName:"name")
		cs1.setChangeableElementGroup(new ChangeableElementGroup(
			changeDescription: new ChangeDescription(
				changeType: ChangeType.CREATE,
				changeDate: new Date(),
				containingChangeSet: changeSetUri1)))
		
		maint.createResource(cs1)
		changeSetService.commitChangeSet(changeSetUri1)
		
		def changeSetUri2 = changeSetService.createChangeSet().getChangeSetURI()
		
		def cs2 = new CodeSystemCatalogEntry(about:"about",codeSystemName:"name", formalName:"new formal name")
		cs2.setChangeableElementGroup(new ChangeableElementGroup(
			changeDescription: new ChangeDescription(
				changeType: ChangeType.UPDATE,
				changeDate: new Date(),
				containingChangeSet: changeSetUri2)))
		
		maint.createResource(cs2)

		def summaries = query.getResourceSummaries(
			null, null, null, new ResolvedReadContext(changeSetContextUri:changeSetUri2), new Page())
		
		assertEquals 1, summaries.entries.size()
		
		assertEquals "new formal name", summaries.entries.get(0).formalName
	}
	
	@Test void TestQueryWithOpenAndCommittedChangeSetWithReadContextModifyWithQuery(){
		
		def changeSetUri1 = changeSetService.createChangeSet().getChangeSetURI()
		
		def cs1 = new CodeSystemCatalogEntry(about:"about",codeSystemName:"name")
		cs1.setChangeableElementGroup(new ChangeableElementGroup(
			changeDescription: new ChangeDescription(
				changeType: ChangeType.CREATE,
				changeDate: new Date(),
				containingChangeSet: changeSetUri1)))
		
		maint.createResource(cs1)
		changeSetService.commitChangeSet(changeSetUri1)
		
		def changeSetUri2 = changeSetService.createChangeSet().getChangeSetURI()
		
		def cs2 = new CodeSystemCatalogEntry(about:"about",codeSystemName:"name", formalName:"new formal name")
		cs2.setChangeableElementGroup(new ChangeableElementGroup(
			changeDescription: new ChangeDescription(
				changeType: ChangeType.UPDATE,
				changeDate: new Date(),
				containingChangeSet: changeSetUri2)))
		
		maint.createResource(cs2)
		
		def fc = new ResolvedFilter(
			matchAlgorithmReference:StandardMatchAlgorithmReference.CONTAINS.getMatchAlgorithmReference(),
			matchValue:"name",
			modelAttributeReference: new ModelAttributeReference(content:ExternalCts2Constants.MA_RESOURCE_NAME_NAME),
			referenceType:TargetReferenceType.ATTRIBUTE)

		def summaries = query.getResourceSummaries(
			null, [fc] as Set, null, new ResolvedReadContext(changeSetContextUri:changeSetUri2), new Page())
		
		assertEquals 1, summaries.entries.size()
		
		assertEquals "new formal name", summaries.entries.get(0).formalName
	}
	
	@Test void TestQueryWithOpenAndCommittedChangeSetWithReadContextModifyWithBadQuery(){
		
		def changeSetUri1 = changeSetService.createChangeSet().getChangeSetURI()
		
		def cs1 = new CodeSystemCatalogEntry(about:"about",codeSystemName:"name")
		cs1.setChangeableElementGroup(new ChangeableElementGroup(
			changeDescription: new ChangeDescription(
				changeType: ChangeType.CREATE,
				changeDate: new Date(),
				containingChangeSet: changeSetUri1)))
		
		maint.createResource(cs1)
		changeSetService.commitChangeSet(changeSetUri1)
		
		def changeSetUri2 = changeSetService.createChangeSet().getChangeSetURI()
		
		def cs2 = new CodeSystemCatalogEntry(about:"about",codeSystemName:"name", formalName:"new formal name")
		cs2.setChangeableElementGroup(new ChangeableElementGroup(
			changeDescription: new ChangeDescription(
				changeType: ChangeType.UPDATE,
				changeDate: new Date(),
				containingChangeSet: changeSetUri2)))
		
		maint.createResource(cs2)
		
		def fc = new ResolvedFilter(
			matchAlgorithmReference:StandardMatchAlgorithmReference.CONTAINS.getMatchAlgorithmReference(),
			matchValue:"__INVALID__",
			modelAttributeReference: new ModelAttributeReference(content:ExternalCts2Constants.MA_RESOURCE_NAME_NAME),
			referenceType:TargetReferenceType.ATTRIBUTE)

		def summaries = query.getResourceSummaries(
			null, [fc] as Set, null, new ResolvedReadContext(changeSetContextUri:changeSetUri2), new Page())
		
		assertEquals 0, summaries.entries.size()
	}
	
}
