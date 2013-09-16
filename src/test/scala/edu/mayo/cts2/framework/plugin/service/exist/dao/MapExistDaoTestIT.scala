package edu.mayo.cts2.framework.plugin.service.exist.dao

import org.junit.Assert.assertEquals
import edu.mayo.cts2.framework.model.map.MapCatalogEntry
import org.springframework.beans.factory.annotation.Autowired
import org.scalatest.junit.AssertionsForJUnit
import org.springframework.test.context.ContextConfiguration
import org.junit.Test
import org.junit.Assert._
import org.junit.runner.RunWith
import org.junit.Before
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import edu.mayo.cts2.framework.model.map.MapCatalogEntrySummary
import edu.mayo.cts2.framework.model.directory.DirectoryResult

@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(
  locations = 
Array("/exist-test-context.xml"))
class MapExistDaoTestIT extends AssertionsForJUnit {
 
 
  @Autowired var dao:ExistDaoImpl = null
  
  @Before def cleanExist() {
		dao.removeCollection(dao.getExistManager().getCollectionRoot());
  }
   
  @Test def testInsertMap() {
    var entry = new MapCatalogEntry()
    entry.setMapName("testName")
    entry.setAbout("about")
    
    insertMap(entry)
    
    var foundMap = dao.getResource("", "testName")
    
    assertNotNull(foundMap)
  }
 

   def insertMap(map:MapCatalogEntry) =  dao.storeResource("", map.getMapName(), map)

}