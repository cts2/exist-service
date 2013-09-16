package edu.mayo.cts2.framework.plugin.service.exist.profile.valueset
import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.model.command.ResolvedFilter
import edu.mayo.cts2.framework.model.core.*
import edu.mayo.cts2.framework.model.core.types.ChangeType
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntry
import edu.mayo.cts2.framework.plugin.service.exist.profile.BaseServiceDbCleaningBase
import edu.mayo.cts2.framework.service.profile.valueset.ValueSetQuery
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

class ExistValueSetQueryServiceTest extends BaseServiceDbCleaningBase {

	@Autowired
	ExistValueSetMaintenanceService maint
	
	@Autowired
	ExistValueSetQueryService query
	
	@Test void TestQuery(){
		
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()
		
		def vs = new ValueSetCatalogEntry(about:"about",valueSetName:"name")
		vs.setChangeableElementGroup(new ChangeableElementGroup(
			changeDescription: new ChangeDescription(
				changeType: ChangeType.CREATE,
				changeDate: new Date(),
				containingChangeSet: changeSetUri)))
		
		maint.createResource(vs)
		changeSetService.commitChangeSet(changeSetUri)

		def summaries = query.getResourceSummaries({} as ValueSetQuery,null,new Page())
		assertNotNull summaries
		assertEquals 1, summaries.entries.size()
	}
	
	@Test void TestQueryWithFilter(){
		
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()
		
		def vs = new ValueSetCatalogEntry(about:"about",valueSetName:"name")
		vs.setChangeableElementGroup(new ChangeableElementGroup(
			changeDescription: new ChangeDescription(
				changeType: ChangeType.CREATE,
				changeDate: new Date(),
				containingChangeSet: changeSetUri)))
		
		maint.createResource(vs)
		changeSetService.commitChangeSet(changeSetUri)

		def summaries = query.getResourceSummaries(
			[
				getRestrictions:{null},
				getReadContext:{null},
				getFilterComponent:{
					[new ResolvedFilter(
						matchValue:"m",
						matchAlgorithmReference: new MatchAlgorithmReference("contains"),
						componentReference: new ComponentReference(attributeReference: "resourceName")
						)] as Set
				}
				
			] as ValueSetQuery,null,new Page())
		assertNotNull summaries
		assertEquals 1, summaries.entries.size()
	}
	
	@Test void TestQueryWithStartsWithFilter(){
		
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()
		
		def vs = new ValueSetCatalogEntry(about:"about",valueSetName:"fred")
		vs.setChangeableElementGroup(new ChangeableElementGroup(
			changeDescription: new ChangeDescription(
				changeType: ChangeType.CREATE,
				changeDate: new Date(),
				containingChangeSet: changeSetUri)))
		
		maint.createResource(vs)
		changeSetService.commitChangeSet(changeSetUri)

		def summaries = query.getResourceSummaries(
			[
				getRestrictions:{null},
				getReadContext:{null},
				getFilterComponent:{
					[new ResolvedFilter(
						matchValue:"fre",
						matchAlgorithmReference: new MatchAlgorithmReference("startsWith"),
						componentReference: new ComponentReference(attributeReference: "resourceName")
						)] as Set
				}
				
			] as ValueSetQuery,null,new Page())
		assertNotNull summaries
		assertEquals 1, summaries.entries.size()
	}
	
	@Test void TestQueryWithStartsWithFilterShouldFail(){
		
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()
		
		def vs = new ValueSetCatalogEntry(about:"about",valueSetName:"fred")
		vs.setChangeableElementGroup(new ChangeableElementGroup(
			changeDescription: new ChangeDescription(
				changeType: ChangeType.CREATE,
				changeDate: new Date(),
				containingChangeSet: changeSetUri)))
		
		maint.createResource(vs)
		changeSetService.commitChangeSet(changeSetUri)

		def summaries = query.getResourceSummaries(
			[
				getRestrictions:{null},
				getReadContext:{null},
				getFilterComponent:{
					[new ResolvedFilter(
						matchValue:"red",
						matchAlgorithmReference: new MatchAlgorithmReference("startsWith"),
						componentReference: new ComponentReference(attributeReference: "resourceName")
						)] as Set
				}
				
			] as ValueSetQuery,null,new Page())
		assertNotNull summaries
		assertEquals 0, summaries.entries.size()
	}
	
	@Test void TestQueryWithLuceneFilter(){
		
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()
		
		def vs = new ValueSetCatalogEntry(about:"about",valueSetName:"a really long name with a lot of words")
		vs.setChangeableElementGroup(new ChangeableElementGroup(
			changeDescription: new ChangeDescription(
				changeType: ChangeType.CREATE,
				changeDate: new Date(),
				containingChangeSet: changeSetUri)))
		
		maint.createResource(vs)
		changeSetService.commitChangeSet(changeSetUri)

		def summaries = query.getResourceSummaries(
			[
				getRestrictions:{null},
				getReadContext:{null},
				getFilterComponent:{
					[new ResolvedFilter(
						matchValue:"long words",
						matchAlgorithmReference: new MatchAlgorithmReference("lucene"),
						componentReference: new ComponentReference(attributeReference: "resourceName")
						)] as Set
				}
				
			] as ValueSetQuery,null,new Page())
		assertNotNull summaries
		assertEquals 1, summaries.entries.size()
	}
	
	@Test void TestQueryWithLuceneFilterNoResult(){
		
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()
		
		def vs = new ValueSetCatalogEntry(about:"about",valueSetName:"a really long name with a lot of words")
		vs.setChangeableElementGroup(new ChangeableElementGroup(
			changeDescription: new ChangeDescription(
				changeType: ChangeType.CREATE,
				changeDate: new Date(),
				containingChangeSet: changeSetUri)))
		
		maint.createResource(vs)
		changeSetService.commitChangeSet(changeSetUri)

		def summaries = query.getResourceSummaries(
			[
				getRestrictions:{null},
				getReadContext:{null},
				getFilterComponent:{
					[new ResolvedFilter(
						matchValue:"fred",
						matchAlgorithmReference: new MatchAlgorithmReference("lucene"),
						componentReference: new ComponentReference(attributeReference: "resourceName")
						)] as Set
				}
				
			] as ValueSetQuery,null,new Page())
		assertNotNull summaries
		assertEquals 0, summaries.entries.size()
	}
	
	@Test void TestQueryWithFilterResourceSynopsis(){
		
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()
		
		def vs = new ValueSetCatalogEntry(about:"about",valueSetName:"name")
		vs.resourceSynopsis = new EntryDescription()
		vs.resourceSynopsis.setValue(ModelUtils.toTsAnyType("Some des"))
		vs.setChangeableElementGroup(new ChangeableElementGroup(
			changeDescription: new ChangeDescription(
				changeType: ChangeType.CREATE,
				changeDate: new Date(),
				containingChangeSet: changeSetUri)))
		
		maint.createResource(vs)
		changeSetService.commitChangeSet(changeSetUri)

		def summaries = query.getResourceSummaries(
			[
				getRestrictions:{null},
				getReadContext:{null},
				getFilterComponent:{
					[new ResolvedFilter(
						matchValue:"des",
						matchAlgorithmReference: new MatchAlgorithmReference("contains"),
						componentReference: new ComponentReference(attributeReference: "resourceSynopsis")
						)] as Set
				}
				
			] as ValueSetQuery,null,new Page())
		assertNotNull summaries
		assertEquals 1, summaries.entries.size()
	}
	
	@Test void TestQueryWithLuceneFilterResourceSynopsis(){
		
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()
		
		def vs = new ValueSetCatalogEntry(about:"about",valueSetName:"name")
		vs.resourceSynopsis = new EntryDescription()
		vs.resourceSynopsis.setValue(ModelUtils.toTsAnyType("another description with a bunch of words.... "))
		vs.setChangeableElementGroup(new ChangeableElementGroup(
			changeDescription: new ChangeDescription(
				changeType: ChangeType.CREATE,
				changeDate: new Date(),
				containingChangeSet: changeSetUri)))
		
		maint.createResource(vs)
		changeSetService.commitChangeSet(changeSetUri)

		def summaries = query.getResourceSummaries(
			[
				getRestrictions:{null},
				getReadContext:{null},
				getFilterComponent:{
					[new ResolvedFilter(
						matchValue:"des* words",
						matchAlgorithmReference: new MatchAlgorithmReference("lucene"),
						componentReference: new ComponentReference(attributeReference: "resourceSynopsis")
						)] as Set
				}
				
			] as ValueSetQuery,null,new Page())
		assertNotNull summaries
		assertEquals 1, summaries.entries.size()
	}
	
	@Test void TestQueryWithBadFilterResourceSynopsis(){
		
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()
		
		def vs = new ValueSetCatalogEntry(about:"about",valueSetName:"name")
		vs.resourceSynopsis = new EntryDescription()
		vs.resourceSynopsis.setValue(ModelUtils.toTsAnyType("Some des"))
		vs.setChangeableElementGroup(new ChangeableElementGroup(
			changeDescription: new ChangeDescription(
				changeType: ChangeType.CREATE,
				changeDate: new Date(),
				containingChangeSet: changeSetUri)))
		
		maint.createResource(vs)
		changeSetService.commitChangeSet(changeSetUri)

		def summaries = query.getResourceSummaries(
			[
				getRestrictions:{null},
				getReadContext:{null},
				getFilterComponent:{
					[new ResolvedFilter(
						matchValue:"__INVALID__",
						matchAlgorithmReference: new MatchAlgorithmReference("contains"),
						componentReference: new ComponentReference(attributeReference: "resourceSynopsis")
						)] as Set
				}
				
			] as ValueSetQuery,null,new Page())
		assertNotNull summaries
		assertEquals 0, summaries.entries.size()
	}
	
	@Test void TestQueryWithBadFilter(){
		
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()
		
		def vs = new ValueSetCatalogEntry(about:"about",valueSetName:"name")
		vs.setChangeableElementGroup(new ChangeableElementGroup(
			changeDescription: new ChangeDescription(
				changeType: ChangeType.CREATE,
				changeDate: new Date(),
				containingChangeSet: changeSetUri)))
		
		maint.createResource(vs)
		changeSetService.commitChangeSet(changeSetUri)

		def summaries = query.getResourceSummaries(
			[
				getRestrictions:{null},
				getReadContext:{null},
				getFilterComponent:{
					[new ResolvedFilter(
						matchValue:"__INVALID__",
						matchAlgorithmReference: new MatchAlgorithmReference("contains"),
						componentReference: new ComponentReference(attributeReference: "resourceSynopsis")
						)] as Set
				}
				
			] as ValueSetQuery,null,new Page())
		assertNotNull summaries
		assertEquals 0, summaries.entries.size()
	}
	
}
