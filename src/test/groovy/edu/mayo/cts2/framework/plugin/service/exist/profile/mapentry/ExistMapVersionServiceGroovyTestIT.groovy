package edu.mayo.cts2.framework.plugin.service.exist.profile.mapentry

import static org.junit.Assert.*

import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import edu.mayo.cts2.framework.model.core.MapReference
import edu.mayo.cts2.framework.model.core.MapVersionReference
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference
import edu.mayo.cts2.framework.model.core.URIAndEntityName
import edu.mayo.cts2.framework.model.mapversion.*
import edu.mayo.cts2.framework.model.mapversion.types.MapProcessingRule
import edu.mayo.cts2.framework.plugin.service.exist.profile.BaseServiceTestGroovy
import edu.mayo.cts2.framework.service.command.Page
import edu.mayo.cts2.framework.service.command.restriction.MapEntryQueryServiceRestrictions

class ExistMapEntryServiceGroovyTestIT extends BaseServiceTestGroovy {

	@Autowired
	ExistMapEntryReadService existMapEntryReadService
	
	@Autowired
	ExistMapEntryQueryService existMapEntryQueryService
	
	@Autowired
	ExistMapEntryMaintenanceService existMapEntryMaintenanceService

	@Test void "Get Map Entries"(){

		def mapEntry = new MapEntry(mapFrom: new URIAndEntityName(name:"test", namespace:"ns"))
		mapEntry.assertedBy = new MapVersionReference(map: new MapReference(), mapVersion: new NameAndMeaningReference() )
		mapEntry.processingRule = MapProcessingRule.FIRST_MATCH
		existMapEntryMaintenanceService.createResource(mapEntry)
		assertEquals 1, existMapEntryQueryService.getResourceSummaries(null, null, null, new Page()).entries.size()
	}

	@Test void "Get Map Entries with target code restriction NONE"(){

		def mapEntry = new MapEntry(mapFrom: new URIAndEntityName(name:"test", namespace:"ns"))
		mapEntry.assertedBy = new MapVersionReference(map: new MapReference(), mapVersion: new NameAndMeaningReference() )
		mapEntry.processingRule = MapProcessingRule.FIRST_MATCH
		existMapEntryMaintenanceService.createResource(mapEntry)

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

		def mapEntry = new MapEntry(mapFrom: new URIAndEntityName(name:"test", namespace:"ns"))
		mapEntry.assertedBy = new MapVersionReference(map: new MapReference(), mapVersion: new NameAndMeaningReference() )
		mapEntry.processingRule = MapProcessingRule.FIRST_MATCH

		mapEntry.mapSet = [
			new MapSet(entryOrder:1, processingRule:MapProcessingRule.FIRST_MATCH,
			mapTarget:[
				new MapTarget(entryOrder:1, mapTo: new URIAndEntityName(name:"targetName",namespace:"targetNs"))
			])
		]

		existMapEntryMaintenanceService.createResource(mapEntry)

		def restritions = new MapEntryQueryServiceRestrictions(targetentity:["targetName"])
		assertEquals 1, existMapEntryQueryService.getResourceSummaries(null, null, restritions, new Page()).entries.size()
	}

	@Test void "Get Map Entries with target code restriction TWO"(){

		def mapEntry = new MapEntry(mapFrom: new URIAndEntityName(name:"test", namespace:"ns"))
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

		def restritions = new MapEntryQueryServiceRestrictions(targetentity:["targetName2"])
		assertEquals 1, existMapEntryQueryService.getResourceSummaries(null, null, restritions, new Page()).entries.size()
	}
}
