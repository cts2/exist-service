package edu.mayo.cts2.framework.plugin.service.exist.profile.codesystem

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry
import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.model.command.ResolvedFilter
import edu.mayo.cts2.framework.model.command.ResolvedReadContext
import edu.mayo.cts2.framework.model.core.*
import edu.mayo.cts2.framework.model.core.types.ChangeCommitted
import edu.mayo.cts2.framework.model.core.types.ChangeType
import edu.mayo.cts2.framework.model.service.core.NameOrURI
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.plugin.service.exist.profile.BaseServiceDbCleaningBase
import edu.mayo.cts2.framework.service.meta.StandardMatchAlgorithmReference
import edu.mayo.cts2.framework.service.meta.StandardModelAttributeReference
import edu.mayo.cts2.framework.service.profile.ResourceQuery
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import static org.junit.Assert.*

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
	
	@Test void TestReadContextWithAlternateID(){
		
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()
		
		def cs = new CodeSystemCatalogEntry(
			about:"about",
			codeSystemName:"name", 
			alternateID:["http://something/different"])
		cs.setChangeableElementGroup(new ChangeableElementGroup(
			changeDescription: new ChangeDescription(
				changeType: ChangeType.CREATE,
				changeDate: new Date(),
				containingChangeSet: changeSetUri)))
		
		maint.createResource(cs)

		assertNotNull read.read(new NameOrURI(uri:"http://something/different"), new ResolvedReadContext(changeSetContextUri:changeSetUri))
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
		
		def q = [
			getFilterComponent : { },
			getReadContext : { new ResolvedReadContext(changeSetContextUri:changeSetUri) },
			getQuery : { }
		] as ResourceQuery

		assertEquals 1, query.getResourceSummaries(q, null, new Page()).entries.size()
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
		
		
		def q = [
			getFilterComponent : { },
			getReadContext : { },
			getQuery : { }
		] as ResourceQuery

		assertEquals 0, query.getResourceSummaries(
			q, null, new Page()).entries.size()
	}

    @Test void TestQueryWithContainsCurrentVersion(){

        def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

        def cs = new CodeSystemCatalogEntry(about:"about",codeSystemName:"name")
        cs.setChangeableElementGroup(new ChangeableElementGroup(
                changeDescription: new ChangeDescription(
                        changeType: ChangeType.CREATE,
                        changeDate: new Date(),
                        containingChangeSet: changeSetUri)))
        cs.currentVersion = new CodeSystemVersionReference(
                version:new NameAndMeaningReference(content:"test", uri:"test"))

        maint.createResource(cs)
        changeSetService.commitChangeSet(changeSetUri)

        def filter = new ResolvedFilter(
                matchAlgorithmReference:new MatchAlgorithmReference(content:"contains"),
                matchValue:"am",
                componentReference:new ComponentReference(attributeReference: "resourceName")
        )

        def q = [
                getFilterComponent : { [filter] as Set },
                getReadContext : { },
                getQuery : { }
        ] as ResourceQuery

        assertEquals 1, query.getResourceSummaries(q, null, new Page()).entries.size()
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
			componentReference:new ComponentReference(attributeReference: "resourceName")
		)
		
		def q = [
			getFilterComponent : { [filter] as Set },
			getReadContext : { },
			getQuery : { }
		] as ResourceQuery

		assertEquals 1, query.getResourceSummaries(q, null, new Page()).entries.size()
	}

    @Test void TestQueryWithContainsResourceSynopsis(){

        def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

        def cs = new CodeSystemCatalogEntry(
                about:"about",
                codeSystemName:"name",
                resourceSynopsis:new EntryDescription(value: ModelUtils.toTsAnyType("my thing")))
        cs.setChangeableElementGroup(new ChangeableElementGroup(
                changeDescription: new ChangeDescription(
                        changeType: ChangeType.CREATE,
                        changeDate: new Date(),
                        containingChangeSet: changeSetUri)))

        maint.createResource(cs)
        changeSetService.commitChangeSet(changeSetUri)

        def filter = new ResolvedFilter(
                matchAlgorithmReference:new MatchAlgorithmReference(content:"contains"),
                matchValue:"thing",
                componentReference:new ComponentReference(attributeReference: "resourceSynopsis")
        )

        def q = [
                getFilterComponent : { [filter] as Set },
                getReadContext : { },
                getQuery : { }
        ] as ResourceQuery

        assertEquals 1, query.getResourceSummaries(q, null, new Page()).entries.size()
    }

    @Test void TestQueryWithContainsResourceSynopsisBad(){

        def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

        def cs = new CodeSystemCatalogEntry(
                about:"about",
                codeSystemName:"name",
                resourceSynopsis:new EntryDescription(value: ModelUtils.toTsAnyType("my thing")))
        cs.setChangeableElementGroup(new ChangeableElementGroup(
                changeDescription: new ChangeDescription(
                        changeType: ChangeType.CREATE,
                        changeDate: new Date(),
                        containingChangeSet: changeSetUri)))

        maint.createResource(cs)
        changeSetService.commitChangeSet(changeSetUri)

        def filter = new ResolvedFilter(
                matchAlgorithmReference:new MatchAlgorithmReference(content:"contains"),
                matchValue:"asdfasdfasdf",
                componentReference:new ComponentReference(attributeReference: "resourceSynopsis")
        )

        def q = [
                getFilterComponent : { [filter] as Set },
                getReadContext : { },
                getQuery : { }
        ] as ResourceQuery

        assertEquals 0, query.getResourceSummaries(q, null, new Page()).entries.size()
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
	
	@Test void TestCommittedChangeStatusInChangeSet(){
		
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
	
		def changeSet = changeSetService.readChangeSet(changeSetUri1)
			
		assertEquals ChangeCommitted.COMMITTED,
			changeSet.getMember(0).getChangeableElementGroup().getChangeDescription().getCommitted();
	}
	
	@Test void TestCreateAndUpdateBothInChangeSet(){
		
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()
		
		def cs1 = new CodeSystemCatalogEntry(about:"about",codeSystemName:"name")
		cs1.setChangeableElementGroup(new ChangeableElementGroup(
			changeDescription: new ChangeDescription(
				committed: ChangeCommitted.PENDING,
				changeType: ChangeType.CREATE,
				changeDate: new Date(),
				containingChangeSet: changeSetUri)))
		
		maint.createResource(cs1)
		
		cs1.setFormalName("test formalName")
		cs1.getChangeableElementGroup().getChangeDescription().setChangeType(ChangeType.UPDATE)
		
		maint.updateResource(cs1)
	
		def changeSet = changeSetService.readChangeSet(changeSetUri)
			
		assertEquals 2,
			changeSet.getMemberCount()
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
		
		def q = [
			getFilterComponent : { },
			getReadContext : { },
			getQuery : { }
		] as ResourceQuery

		assertEquals 1, query.getResourceSummaries(
			q, null, new Page()).entries.size()
	}
	
	@Test void TestQueryWithOpenAndEmputCommittedChangeSetWithReadContext(){
		
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
		
		def q = [
			getFilterComponent : { },
			getReadContext : { new ResolvedReadContext(changeSetContextUri:changeSetUri2) },
			getQuery : { }
		] as ResourceQuery

		assertEquals 1, query.getResourceSummaries(q, null, new Page()).entries.size()
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
		
		def q = [
			getFilterComponent : { },
			getReadContext : { new ResolvedReadContext(changeSetContextUri:changeSetUri2) },
			getQuery : { }
		] as ResourceQuery


		assertEquals 2, query.getResourceSummaries(q, null, new Page()).entries.size()
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

		def q = [
			getFilterComponent : { },
			getReadContext : { new ResolvedReadContext(changeSetContextUri:changeSetUri2) },
			getQuery : { }
		] as ResourceQuery
	
		def summaries = query.getResourceSummaries(q, null, new Page())
		
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
			componentReference: StandardModelAttributeReference.RESOURCE_NAME.componentReference)
		
		def q = [
			getFilterComponent : { [fc] as Set },
			getReadContext : { new ResolvedReadContext(changeSetContextUri:changeSetUri2) },
			getQuery : { }
		] as ResourceQuery

		def summaries = query.getResourceSummaries(q, null, new Page())
		
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
			componentReference: StandardModelAttributeReference.RESOURCE_NAME.componentReference)
		
		def q = [
			getFilterComponent : { [fc] as Set },
			getReadContext : { new ResolvedReadContext(changeSetContextUri:changeSetUri2) },
			getQuery : { }
		] as ResourceQuery

		def summaries = query.getResourceSummaries(q, null, new Page())
		
		assertEquals 0, summaries.entries.size()
	}
	
	@Test void TestQueryWithOpenAndCommittedChangeSetWithReadContextDelete(){
		
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
		
		maint.deleteResource(ModelUtils.nameOrUriFromName("name"), changeSetUri2);
		
		def q = [
			getFilterComponent : { },
			getReadContext : {  },
			getQuery : { }
		] as ResourceQuery
		
		assertEquals 1, query.getResourceSummaries(
			q, null, new Page()).entries.size()
			
		q = [
				getFilterComponent : { },
				getReadContext : { new ResolvedReadContext(changeSetContextUri:changeSetUri2) },
				getQuery : { }
			] as ResourceQuery

		assertEquals 0, query.getResourceSummaries(q, null, new Page()).entries.size()
	}
	
	@Test void TestQueryCommittedChangeSetWithReadContextDelete(){
		
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
		
		maint.deleteResource(ModelUtils.nameOrUriFromName("name"), changeSetUri2);
		changeSetService.commitChangeSet(changeSetUri2)
		
		def q = [
			getFilterComponent : { },
			getReadContext : { },
			getQuery : { }
		] as ResourceQuery
		
		assertEquals 0, query.getResourceSummaries(
			q, null, new Page()).entries.size()
	}
}
