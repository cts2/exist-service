package edu.mayo.cts2.framework.plugin.service.exist.profile.mapentry

import static org.junit.Assert.*

import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.model.core.ChangeDescription
import edu.mayo.cts2.framework.model.core.ChangeableElementGroup
import edu.mayo.cts2.framework.model.core.MapReference
import edu.mayo.cts2.framework.model.core.MapVersionReference
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference
import edu.mayo.cts2.framework.model.core.ScopedEntityName
import edu.mayo.cts2.framework.model.core.URIAndEntityName
import edu.mayo.cts2.framework.model.core.types.ChangeType
import edu.mayo.cts2.framework.model.mapversion.*
import edu.mayo.cts2.framework.model.mapversion.types.MapProcessingRule
import edu.mayo.cts2.framework.model.service.core.EntityNameOrURI
import edu.mayo.cts2.framework.plugin.service.exist.profile.BaseServiceDbCleaningBase
import edu.mayo.cts2.framework.service.command.restriction.MapEntryQueryServiceRestrictions
import edu.mayo.cts2.framework.service.profile.mapentry.MapEntryQuery

class ExistMapEntryServiceGroovyTestIT extends BaseServiceDbCleaningBase {

	@Autowired
	ExistMapEntryReadService existMapEntryReadService
	
	@Autowired
	ExistMapEntryQueryService existMapEntryQueryService
	
	@Autowired
	ExistMapEntryMaintenanceService existMapEntryMaintenanceService

	@Test void GetMapEntries(){
		
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

		def mapEntry = new MapEntry(mapFrom: new URIAndEntityName(name:"test", namespace:"ns", uri:"uri"))
		mapEntry.setChangeableElementGroup(new ChangeableElementGroup(
			changeDescription: new ChangeDescription(
				changeType: ChangeType.CREATE, 
				changeDate: new Date(),
				containingChangeSet: changeSetUri)))
		mapEntry.assertedBy = new MapVersionReference(map: new MapReference(), mapVersion: new NameAndMeaningReference() )
		mapEntry.processingRule = MapProcessingRule.FIRST_MATCH
		
		existMapEntryMaintenanceService.createResource(mapEntry)
		
		changeSetService.commitChangeSet(changeSetUri)
		
		def q = [
			getFilterComponent : { },
			getReadContext : { },
			getQuery : { },
			getRestrictions : { }
		] as MapEntryQuery
		
		assertEquals 1, existMapEntryQueryService.getResourceSummaries(q, null, new Page()).entries.size()
	}

	@Test void GetMapEntriesWithTargetCodeRestrictionNONE(){
		
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()
		

		def mapEntry = new MapEntry(mapFrom: new URIAndEntityName(name:"test", namespace:"ns", uri:"uri"))
		mapEntry.setChangeableElementGroup(new ChangeableElementGroup(
			changeDescription: new ChangeDescription(
				changeType: ChangeType.CREATE, 
				changeDate: new Date(),
				containingChangeSet: changeSetUri)))
		mapEntry.assertedBy = new MapVersionReference(map: new MapReference(), mapVersion: new NameAndMeaningReference() )
		mapEntry.processingRule = MapProcessingRule.FIRST_MATCH
		
		existMapEntryMaintenanceService.createResource(mapEntry)
		
		changeSetService.commitChangeSet(changeSetUri)

		mapEntry.mapSet = [
			new MapSet(entryOrder:1, processingRule:MapProcessingRule.FIRST_MATCH,
			mapTarget:[
				new MapTarget(entryOrder:1, mapTo: new URIAndEntityName(name:"asdf",namespace:"asdfa", uri:"uri"))
			])
		]


		def restritions = new MapEntryQueryServiceRestrictions(
			targetEntities:[
			new EntityNameOrURI(entityName: new ScopedEntityName(name:"NS:___INVALID___"))] as Set )

		def q = [
			getFilterComponent : { },
			getReadContext : { },
			getQuery : { },
			getRestrictions : { restritions }
		] as MapEntryQuery
	
		assertEquals 0, existMapEntryQueryService.getResourceSummaries(q, null, new Page()).entries.size()
	}

	@Test void GetMapEntriesWithTargetCodeRestrictionONE(){
		
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()
		

		def mapEntry = new MapEntry(mapFrom: new URIAndEntityName(name:"test", namespace:"ns", uri:"uri"))
		mapEntry.setChangeableElementGroup(new ChangeableElementGroup(
			changeDescription: new ChangeDescription(
				changeType: ChangeType.CREATE, 
				changeDate: new Date(),
				containingChangeSet: changeSetUri)))
		mapEntry.assertedBy = new MapVersionReference(map: new MapReference(), mapVersion: new NameAndMeaningReference() )
		mapEntry.processingRule = MapProcessingRule.FIRST_MATCH

		mapEntry.mapSet = [
			new MapSet(entryOrder:1, processingRule:MapProcessingRule.FIRST_MATCH,
			mapTarget:[
				new MapTarget(entryOrder:1, mapTo: new URIAndEntityName(name:"targetName",namespace:"targetNs", uri:"uri"))
			])
		]

		existMapEntryMaintenanceService.createResource(mapEntry)
		
		changeSetService.commitChangeSet(changeSetUri)

		def restritions = new MapEntryQueryServiceRestrictions(targetEntities:[
			new EntityNameOrURI(entityName: new ScopedEntityName(name:"targetName"))] as Set)
		
		def q = [			
			getFilterComponent : { },
			getReadContext : { },
			getQuery : { },
			getRestrictions : { restritions }
		] as MapEntryQuery
	
		assertEquals 1, existMapEntryQueryService.getResourceSummaries(q, null, new Page()).entries.size()
	}

	@Test void GetMapEntriesWithTargetCodeRestrictionTWO(){
		
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()
		

		def mapEntry = new MapEntry(mapFrom: new URIAndEntityName(name:"test", namespace:"ns", uri:"uri"))
		mapEntry.setChangeableElementGroup(new ChangeableElementGroup(
			changeDescription: new ChangeDescription(
				changeType: ChangeType.CREATE, 
				changeDate: new Date(),
				containingChangeSet: changeSetUri)))
		mapEntry.assertedBy = new MapVersionReference(map: new MapReference(), mapVersion: new NameAndMeaningReference() )
		mapEntry.processingRule = MapProcessingRule.FIRST_MATCH

		mapEntry.mapSet = [
			new MapSet(entryOrder:1, processingRule:MapProcessingRule.FIRST_MATCH,
			mapTarget:[
				new MapTarget(entryOrder:1, mapTo: new URIAndEntityName(name:"targetName1",namespace:"targetNs", uri:"uri")),
				new MapTarget(entryOrder:2, mapTo: new URIAndEntityName(name:"targetName2",namespace:"targetNs", uri:"uri"))
			])
		]

		existMapEntryMaintenanceService.createResource(mapEntry)
		
		changeSetService.commitChangeSet(changeSetUri)

		def restritions = new MapEntryQueryServiceRestrictions(targetEntities:[
			new EntityNameOrURI(entityName: new ScopedEntityName(name:"targetName2"))] as Set)
		
		def q = [
			getFilterComponent : { },
			getReadContext : { },
			getQuery : { },
			getRestrictions : { restritions }
		] as MapEntryQuery
	
		assertEquals 1, existMapEntryQueryService.getResourceSummaries(q, null, new Page()).entries.size()
	}
}
