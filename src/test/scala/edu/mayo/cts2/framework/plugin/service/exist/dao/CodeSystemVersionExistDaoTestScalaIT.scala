package edu.mayo.cts2.framework.plugin.service.exist.dao

import org.junit.Assert.assertEquals
import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry
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
import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntrySummary
import edu.mayo.cts2.framework.model.core.SourceAndNotation
import edu.mayo.cts2.framework.model.core.CodeSystemReference

@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(
  locations = 
Array("/exist-test-context.xml"))
class CodeSystemVersionExistDaoTestScalaIT extends AssertionsForJUnit {
 
  @Autowired var dao:ExistDaoImpl = null
  
  @Before def cleanExist() {
		dao.removeCollection(dao.getExistManager().getCollectionRoot());
  }
   
  @Test def testCodeSystemVersion() {
    var entry = new CodeSystemVersionCatalogEntry()
    entry.setCodeSystemVersionName("testName")
    entry.setAbout("about")
    entry.setDocumentURI("uri")
    entry.setSourceAndNotation(new SourceAndNotation());
	entry.setVersionOf(new CodeSystemReference());
    
    insert("cs", entry)
    
    var found = dao.getResource("cs", "testName")
    
    assertNotNull(found)
  }
  

   def insert(path:String, csv:CodeSystemVersionCatalogEntry) =  dao.storeResource(
       path, csv.getCodeSystemVersionName(), csv)

}