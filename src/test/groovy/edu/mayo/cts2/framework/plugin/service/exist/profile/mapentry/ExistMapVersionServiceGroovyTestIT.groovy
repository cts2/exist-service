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
import edu.mayo.cts2.framework.model.core.URIAndEntityName
import edu.mayo.cts2.framework.model.core.types.ChangeType
import edu.mayo.cts2.framework.model.mapversion.*
import edu.mayo.cts2.framework.model.mapversion.types.MapProcessingRule
import edu.mayo.cts2.framework.plugin.service.exist.profile.BaseServiceDbCleaningBase
import edu.mayo.cts2.framework.service.command.restriction.MapEntryQueryServiceRestrictions

class ExistMapEntryServiceGroovyTestIT extends BaseServiceDbCleaningBase {

	@Autowired
	ExistMapEntryReadService existMapEntryReadService
	
	@Autowired
	ExistMapEntryQueryService existMapEntryQueryService
	
	@Autowired
	ExistMapEntryMaintenanceService existMapEntryMaintenanceService

	@Test void "Get Map Entries"(){
		
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

		def mapEntry = new MapEntry(mapFrom: new URIAndEntityName(name:"test", namespace:"ns"))
		mapEntry.setChangeableElementGroup(new ChangeableElementGroup(
			changeDescription: new ChangeDescription(
				changeType: ChangeType.CREATE, 
				changeDate: new Date(),
				containingChangeSet: changeSetUri)))
		mapEntry.assertedBy = new MapVersionReference(map: new MapReference(), mapVersion: new NameAndMeaningReference() )
		mapEntry.processingRule = MapProcessingRule.FIRST_MATCH
		
		existMapEntryMaintenanceService.createResource(mapEntry)
		
		changeSetService.commitChangeSet(changeSetUri)
		
		assertEquals 1, existMapEntryQueryService.getResourceSummaries(null, null, null, new Page()).entries.size()
	}

	@Test void "Get Map Entries with target code restriction NONE"(){
		
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()
		

		def mapEntry = new MapEntry(mapFrom: new URIAndEntityName(name:"test", namespace:"ns"))
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
				new MapTarget(entryOrder:1, mapTo: new URIAndEntityName(name:"asdf",namespace:"asdfa"))
			])
		]


		def restritions = new MapEntryQueryServiceRestrictions(targetentity:["NS:___INVALID___"])
		assertEquals 0, existMapEntryQueryService.getResourceSummaries(null, null, restritions,new Page()).entries.size()
	}

	@Test void "Get Map Entries with target code restriction ONE"(){
		
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()
		

		def mapEntry = new MapEntry(mapFrom: new URIAndEntityName(name:"test", namespace:"ns"))
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
				new MapTarget(entryOrder:1, mapTo: new URIAndEntityName(name:"targetName",namespace:"targetNs"))
			])
		]

		existMapEntryMaintenanceService.createResource(mapEntry)
		
		changeSetService.commitChangeSet(changeSetUri)

		def restritions = new MapEntryQueryServiceRestrictions(targetentity:["targetName"])
		assertEquals 1, existMapEntryQueryService.getResourceSummaries(null, null, restritions, new Page()).entries.size()
	}

	@Test void "Get Map Entries with target code restriction TWO"(){
		
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()
		

		def mapEntry = new MapEntry(mapFrom: new URIAndEntityName(name:"test", namespace:"ns"))
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
				new MapTarget(entryOrder:1, mapTo: new URIAndEntityName(name:"targetName1",namespace:"targetNs")),
				new MapTarget(entryOrder:2, mapTo: new URIAndEntityName(name:"targetName2",namespace:"targetNs"))
			])
		]

		existMapEntryMaintenanceService.createResource(mapEntry)
		
		changeSetService.commitChangeSet(changeSetUri)

		def restritions = new MapEntryQueryServiceRestrictions(targetentity:["targetName2"])
		assertEquals 1, existMapEntryQueryService.getResourceSummaries(null, null, restritions, new Page()).entries.size()
	}
}
