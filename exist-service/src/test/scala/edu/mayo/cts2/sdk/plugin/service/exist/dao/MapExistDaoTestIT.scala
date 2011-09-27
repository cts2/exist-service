package edu.mayo.cts2.sdk.plugin.service.exist.dao

import org.junit.Assert.assertEquals

import edu.mayo.cts2.framework.plugin.service.exist.dao.MapExistDao;
import edu.mayo.cts2.sdk.model.map.MapCatalogEntry
import org.springframework.beans.factory.annotation.Autowired
import org.scalatest.junit.AssertionsForJUnit
import org.springframework.test.context.ContextConfiguration
import org.junit.Test
import org.junit.Assert._
import org.junit.runner.RunWith
import org.junit.Before
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import edu.mayo.cts2.sdk.model.map.MapCatalogEntrySummary
import edu.mayo.cts2.sdk.model.directory.DirectoryResult

@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(
  locations = 
Array("/exist-test-context.xml"))
class MapExistDaoTestIT extends AssertionsForJUnit {
 
 
  @Autowired var dao:MapExistDao = null
  
  @Before def cleanExist() {
    dao.getExistManager().getCollectionManagementService().removeCollection("/db");
  }
   
  @Test def testInsertMap() {
    var entry = new MapCatalogEntry()
    entry.setMapName("testName")
    entry.setAbout("about")
    
    insertMap(entry)
    
    var foundMap = dao.getResource("", "testName")
    
    println(foundMap.getMapName())
  }
  
   @Test def testGetSummaries() {
    var entry = new MapCatalogEntry()
    entry.setMapName("testName")
    entry.setAbout("about")
    insertMap(entry)
    
    var summaries = dao.getResourceSummaries("", "", 0, 1);
    
    assertEquals(1, summaries.getEntries().size)
  }
  
   def getMapCatalogs(map:MapCatalogEntrySummary):DirectoryResult[MapCatalogEntrySummary] =  {
     return dao.getResourceSummaries("", "", 0, 1)
   }

   def insertMap(map:MapCatalogEntry) =  dao.storeResource("", map)

}