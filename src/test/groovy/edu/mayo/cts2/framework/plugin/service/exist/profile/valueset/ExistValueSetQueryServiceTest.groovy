package edu.mayo.cts2.framework.plugin.service.exist.profile.valueset;

import static org.junit.Assert.*

import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.model.command.ResolvedFilter
import edu.mayo.cts2.framework.model.core.ChangeDescription
import edu.mayo.cts2.framework.model.core.ChangeableElementGroup
import edu.mayo.cts2.framework.model.core.EntryDescription
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference
import edu.mayo.cts2.framework.model.core.PropertyReference
import edu.mayo.cts2.framework.model.core.URIAndEntityName
import edu.mayo.cts2.framework.model.core.types.ChangeType
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntry
import edu.mayo.cts2.framework.plugin.service.exist.profile.BaseServiceDbCleaningBase
import edu.mayo.cts2.framework.service.profile.valueset.ValueSetQuery

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
						propertyReference: new PropertyReference(referenceTarget: new URIAndEntityName(name:"resourceName"))
						)] as Set
				}
				
			] as ValueSetQuery,null,new Page())
		assertNotNull summaries
		assertEquals 1, summaries.entries.size()
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
						propertyReference: new PropertyReference(referenceTarget: new URIAndEntityName(name:"resourceSynopsis"))
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
						propertyReference: new PropertyReference(referenceTarget: new URIAndEntityName(name:"resourceSynopsis"))
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
						propertyReference: new PropertyReference(referenceTarget: new URIAndEntityName(name:"resourceName"))
						)] as Set
				}
				
			] as ValueSetQuery,null,new Page())
		assertNotNull summaries
		assertEquals 0, summaries.entries.size()
	}
	
}
