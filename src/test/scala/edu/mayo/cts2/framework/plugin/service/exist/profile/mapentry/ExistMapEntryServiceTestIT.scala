package edu.mayo.cts2.framework.plugin.service.exist.profile.mapentry;

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.model.command.ResolvedFilter
import edu.mayo.cts2.framework.model.core.types.TargetReferenceType
import edu.mayo.cts2.framework.model.core.FilterComponent
import edu.mayo.cts2.framework.model.core.MapReference
import edu.mayo.cts2.framework.model.core.MapVersionReference
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference
import edu.mayo.cts2.framework.model.core.ScopedEntityName
import edu.mayo.cts2.framework.model.core.URIAndEntityName
import edu.mayo.cts2.framework.model.directory.DirectoryResult
import edu.mayo.cts2.framework.model.mapversion.types.MapProcessingRule
import edu.mayo.cts2.framework.model.mapversion.MapEntry
import edu.mayo.cts2.framework.model.mapversion.MapEntryDirectoryEntry
import edu.mayo.cts2.framework.model.mapversion.MapSet
import edu.mayo.cts2.framework.model.mapversion.MapTarget
import edu.mayo.cts2.framework.model.service.exception.UnknownResourceReference
import edu.mayo.cts2.framework.plugin.service.exist.profile.BaseServiceTestBaseIT
import edu.mayo.cts2.framework.plugin.service.exist.profile.TestResourceSummaries
import edu.mayo.cts2.framework.service.command.restriction.MapEntryQueryServiceRestrictions
import edu.mayo.cts2.framework.service.profile.mapentry.name.MapEntryReadId
import edu.mayo.cts2.framework.model.core.ModelAttributeReference
import java.util.HashSet

class ExistMapEntryServiceTestIT
	extends BaseServiceTestBaseIT[MapEntry,MapEntryDirectoryEntry] 
			with TestResourceSummaries[MapEntry,MapEntryDirectoryEntry] {

  @Autowired var readService: ExistMapEntryReadService = null
  @Autowired var maintService: ExistMapEntryMaintenanceService = null
  @Autowired var queryService: ExistMapEntryQueryService = null

  def getExceptionClass(): Class[_ <: UnknownResourceReference] = {
    classOf[UnknownResourceReference]
  }

  def createResource(name: String, uri:String, changeSetUri:String) = {
    var entry = createMapEntry(name, uri, changeSetUri)

    maintService.createResource(entry)
  }

  def getResource(name: String): MapEntry = {
    var sn = new ScopedEntityName()
    sn.setName(name)
    sn.setNamespace("namespace")
    
    readService.read(getMapEntryId("mapversion", sn), null)
  }
  
  def createMapEntry(name:String, uri:String, changeSetUri:String): MapEntry = {
    var entry = new MapEntry()
    entry.setMapFrom(new URIAndEntityName())
    entry.setAssertedBy(new MapVersionReference())
    entry.getAssertedBy().setMap(new MapReference())
    entry.getAssertedBy().getMap().setContent("map")
    entry.getAssertedBy().setMapVersion(new NameAndMeaningReference())
    entry.getAssertedBy().getMapVersion().setContent("mapversion")
    entry.setProcessingRule(MapProcessingRule.FIRST_MATCH)
    entry.getMapFrom().setName(name)
    entry.getMapFrom().setNamespace("namespace")
    entry.getMapFrom().setUri(uri)
    entry.setChangeableElementGroup(buildChangeableElementGroup(changeSetUri))
    
    entry
  }
  
  @Override
   def createResources(changeSetUri:String):Int = {
    val resources = List(createMapEntry("Test", "someuri", changeSetUri), createMapEntry("Test2", "someuri", changeSetUri));
    resources.foreach(resource => maintService.createResource(resource))
    
    resources.size
   }
    
   def getResourceSummaries():DirectoryResult[MapEntryDirectoryEntry] = {
     queryService.getResourceSummaries(null,null,null,null,new Page())
   }
   
   @Test def testGetSummariesWithFilter() {
     var changeSetUri = changeSetService.createChangeSet().getChangeSetURI();
          
     val entry1 = createMapEntry("Test1", "someuri", changeSetUri)
     entry1.addMapSet(new MapSet())
     entry1.getMapSet(0).setEntryOrder(1);
     entry1.getMapSet(0).setProcessingRule(MapProcessingRule.FIRST_MATCH)
     entry1.getMapSet(0).addMapTarget(new MapTarget())
     entry1.getMapSet(0).getMapTarget(0).setEntryOrder(1);
     entry1.getMapSet(0).getMapTarget(0).setMapTo(new URIAndEntityName())
     entry1.getMapSet(0).getMapTarget(0).getMapTo().setName("name1")
     entry1.getMapSet(0).getMapTarget(0).getMapTo().setNamespace("ns")
    
     val entry2 = createMapEntry("Test2", "someuri", changeSetUri)

     entry2.addMapSet(new MapSet())
     entry2.getMapSet(0).setEntryOrder(1);
     entry2.getMapSet(0).setProcessingRule(MapProcessingRule.FIRST_MATCH)
     entry2.getMapSet(0).addMapTarget(new MapTarget())
     entry2.getMapSet(0).getMapTarget(0).setEntryOrder(1);
     entry2.getMapSet(0).getMapTarget(0).setMapTo(new URIAndEntityName())
     entry2.getMapSet(0).getMapTarget(0).getMapTo().setName("name2")
     entry2.getMapSet(0).getMapTarget(0).getMapTo().setNamespace("ns")
     
     maintService.createResource(entry1);
     maintService.createResource(entry2);
     
     changeSetService.commitChangeSet(changeSetUri)
     
     val mapRestrictions = new MapEntryQueryServiceRestrictions()
     mapRestrictions.setMapversion("mapversion")
     mapRestrictions.getTargetentity().add("name2")
     
     val entries = queryService.getResourceSummaries(null,null,mapRestrictions,null,new Page());
     
     assertEquals(1, entries.getEntries().size())
   }
   
    @Test def testGetSummariesWithBadFilter() {
     var changeSetUri = changeSetService.createChangeSet().getChangeSetURI();
       
     val entry1 = createMapEntry("Test1", "someuri", changeSetUri)
     entry1.addMapSet(new MapSet())
     entry1.getMapSet(0).setEntryOrder(1);
     entry1.getMapSet(0).setProcessingRule(MapProcessingRule.FIRST_MATCH)
     entry1.getMapSet(0).addMapTarget(new MapTarget())
     entry1.getMapSet(0).getMapTarget(0).setEntryOrder(1);
     entry1.getMapSet(0).getMapTarget(0).setMapTo(new URIAndEntityName())
     entry1.getMapSet(0).getMapTarget(0).getMapTo().setName("name1")
     entry1.getMapSet(0).getMapTarget(0).getMapTo().setNamespace("ns")
     entry1.setChangeableElementGroup(buildChangeableElementGroup(changeSetUri))
    
     val entry2 = createMapEntry("Test2", "someuri", changeSetUri)
     entry2.addMapSet(new MapSet())
     entry2.getMapSet(0).setEntryOrder(1);
     entry2.getMapSet(0).setProcessingRule(MapProcessingRule.FIRST_MATCH)
     entry2.getMapSet(0).addMapTarget(new MapTarget())
     entry2.getMapSet(0).getMapTarget(0).setEntryOrder(1);
     entry2.getMapSet(0).getMapTarget(0).setMapTo(new URIAndEntityName())
     entry2.getMapSet(0).getMapTarget(0).getMapTo().setName("name2")
     entry2.getMapSet(0).getMapTarget(0).getMapTo().setNamespace("ns")
     entry2.setChangeableElementGroup(buildChangeableElementGroup(changeSetUri))
     
     maintService.createResource(entry1);
     maintService.createResource(entry2);
     
     changeSetService.commitChangeSet(changeSetUri)
     
     val mapRestrictions = new MapEntryQueryServiceRestrictions()
     mapRestrictions.setMapversion("mapversion")
     mapRestrictions.getTargetentity().add("ns:INVALID")
     
     val entries = queryService.getResourceSummaries(null,null,mapRestrictions,null,new Page());
     
     assertEquals(0, entries.getEntries().size())
   }
    
    @Test def testGetSummariesWithMapFromFilter() {
     var changeSetUri = changeSetService.createChangeSet().getChangeSetURI();
      
     val entry1 = createMapEntry("Test1", "someuri", changeSetUri)
     entry1.addMapSet(new MapSet())
     entry1.getMapSet(0).setEntryOrder(1);
     entry1.getMapSet(0).setProcessingRule(MapProcessingRule.FIRST_MATCH)
     entry1.getMapSet(0).addMapTarget(new MapTarget())
     entry1.getMapSet(0).getMapTarget(0).setEntryOrder(1);
     entry1.getMapSet(0).getMapTarget(0).setMapTo(new URIAndEntityName())
     entry1.getMapSet(0).getMapTarget(0).getMapTo().setName("name1")
     entry1.getMapSet(0).getMapTarget(0).getMapTo().setNamespace("ns")
     entry1.setChangeableElementGroup(buildChangeableElementGroup(changeSetUri))
    
     val entry2 = createMapEntry("Test2", "someuri", changeSetUri)
     entry2.addMapSet(new MapSet())
     entry2.getMapSet(0).setEntryOrder(1);
     entry2.getMapSet(0).setProcessingRule(MapProcessingRule.FIRST_MATCH)
     entry2.getMapSet(0).addMapTarget(new MapTarget())
     entry2.getMapSet(0).getMapTarget(0).setEntryOrder(1);
     entry2.getMapSet(0).getMapTarget(0).setMapTo(new URIAndEntityName())
     entry2.getMapSet(0).getMapTarget(0).getMapTo().setName("name2")
     entry2.getMapSet(0).getMapTarget(0).getMapTo().setNamespace("ns")
     entry2.setChangeableElementGroup(buildChangeableElementGroup(changeSetUri))
     
     maintService.createResource(entry1);
     maintService.createResource(entry2);
     
     changeSetService.commitChangeSet(changeSetUri)
     
     val filterComponent = new ResolvedFilter()
     filterComponent.setMatchAlgorithmReference(new MatchAlgorithmReference())
     filterComponent.getMatchAlgorithmReference().setContent("exactMatch")
     filterComponent.setReferenceType(TargetReferenceType.ATTRIBUTE)
     filterComponent.setModelAttributeReference(new ModelAttributeReference())
     filterComponent.getModelAttributeReference().setContent("resourceName")
     filterComponent.setMatchValue("Test2")
     
     var set = new HashSet[ResolvedFilter]()
     set.add(filterComponent)
     val entries = queryService.getResourceSummaries(null,set,null,null,new Page());
     
     assertEquals(1, entries.getEntries().size())
   }
    
     @Test def testGetSummariesWithMapFromFilterContains() {
     var changeSetUri = changeSetService.createChangeSet().getChangeSetURI();
       
     val entry1 = createMapEntry("Test1", "someuri", changeSetUri)
     entry1.addMapSet(new MapSet())
     entry1.getMapSet(0).setEntryOrder(1);
     entry1.getMapSet(0).setProcessingRule(MapProcessingRule.FIRST_MATCH)
     entry1.getMapSet(0).addMapTarget(new MapTarget())
     entry1.getMapSet(0).getMapTarget(0).setEntryOrder(1);
     entry1.getMapSet(0).getMapTarget(0).setMapTo(new URIAndEntityName())
     entry1.getMapSet(0).getMapTarget(0).getMapTo().setName("name1")
     entry1.getMapSet(0).getMapTarget(0).getMapTo().setNamespace("ns")
     entry1.setChangeableElementGroup(buildChangeableElementGroup(changeSetUri))
    
     val entry2 = createMapEntry("Test2", "someuri", changeSetUri)
     entry2.addMapSet(new MapSet())
     entry2.getMapSet(0).setEntryOrder(1);
     entry2.getMapSet(0).setProcessingRule(MapProcessingRule.FIRST_MATCH)
     entry2.getMapSet(0).addMapTarget(new MapTarget())
     entry2.getMapSet(0).getMapTarget(0).setEntryOrder(1);
     entry2.getMapSet(0).getMapTarget(0).setMapTo(new URIAndEntityName())
     entry2.getMapSet(0).getMapTarget(0).getMapTo().setName("name2")
     entry2.getMapSet(0).getMapTarget(0).getMapTo().setNamespace("ns")
     entry2.setChangeableElementGroup(buildChangeableElementGroup(changeSetUri))
     
     maintService.createResource(entry1);
     maintService.createResource(entry2);
     
     changeSetService.commitChangeSet(changeSetUri)

     val filterComponent = new ResolvedFilter()
     filterComponent.setMatchAlgorithmReference(new MatchAlgorithmReference())
     filterComponent.getMatchAlgorithmReference().setContent("contains")
     filterComponent.setReferenceType(TargetReferenceType.ATTRIBUTE)
     filterComponent.setModelAttributeReference(new ModelAttributeReference())
     filterComponent.getModelAttributeReference().setContent("resourceName")
     filterComponent.setMatchValue("st2")
     
     var set = new HashSet[ResolvedFilter]()
     set.add(filterComponent)
     
     val entries = queryService.getResourceSummaries(null,set,null,null,new Page());
     
     assertEquals(1, entries.getEntries().size())
   }
     
   def getMapEntryId(mapVersion:String, name:ScopedEntityName):MapEntryReadId = {
     val id = new MapEntryReadId(name, mapVersion)
     
     id
   }
   
       def getResourceByUri(uri:String):MapEntry = {
    	val id = new MapEntryReadId(uri, "mapversion")
     
    	readService.read(id, null)
    }
}