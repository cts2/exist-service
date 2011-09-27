package edu.mayo.cts2.sdk.plugin.service.exist.dao

import org.junit.Assert.assertEquals

import edu.mayo.cts2.framework.plugin.service.exist.dao.CodeSystemVersionExistDao;
import edu.mayo.cts2.sdk.model.codesystemversion.CodeSystemVersionCatalogEntry
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
import edu.mayo.cts2.sdk.model.codesystemversion.CodeSystemVersionCatalogEntrySummary
import edu.mayo.cts2.sdk.model.core.SourceAndNotation
import edu.mayo.cts2.sdk.model.core.CodeSystemReference

@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(
  locations = 
Array("/exist-test-context.xml"))
class CodeSystemVersionExistDaoTestScalaIT extends AssertionsForJUnit {
 
  @Autowired var dao:CodeSystemVersionExistDao = null
  
  @Before def cleanExist() {
    dao.getExistManager().getCollectionManagementService().removeCollection("/db");
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
    
    println(found.getCodeSystemVersionName())
  }
  
    @Test def testGetSummariesOfOneCodeSystem() {
    var entry1 = new CodeSystemVersionCatalogEntry()
    entry1.setCodeSystemVersionName("testName1")
    entry1.setAbout("about")
    entry1.setDocumentURI("uri1")
    entry1.setSourceAndNotation(new SourceAndNotation());
	entry1.setVersionOf(new CodeSystemReference());
   
    var entry2 = new CodeSystemVersionCatalogEntry()
    entry2.setCodeSystemVersionName("testName2")
    entry2.setAbout("about")
    entry2.setDocumentURI("uri2")
    entry2.setSourceAndNotation(new SourceAndNotation());
	entry2.setVersionOf(new CodeSystemReference());
	
    insert("cs", entry1)
    insert("cs", entry2)
    
    var summaries = dao.getResourceSummaries("", "", 0, 100);
    
    assertEquals(2, summaries.getEntries().size)
  }
  
   @Test def testGetSummariesOfTwoCodeSystems() {
    var entry1 = new CodeSystemVersionCatalogEntry()
    entry1.setCodeSystemVersionName("testName1")
    entry1.setAbout("about")
    entry1.setDocumentURI("uri1")
    entry1.setSourceAndNotation(new SourceAndNotation());
	entry1.setVersionOf(new CodeSystemReference());
   
    var entry2 = new CodeSystemVersionCatalogEntry()
    entry2.setCodeSystemVersionName("testName2")
    entry2.setAbout("about")
    entry2.setDocumentURI("uri2")
    entry2.setSourceAndNotation(new SourceAndNotation());
	entry2.setVersionOf(new CodeSystemReference());
    
    insert("cs1", entry1)
    insert("cs2", entry2)
    
    var summaries = dao.getResourceSummaries("", "", 0, 100);
    
    assertEquals(2, summaries.getEntries().size)
  }
   
    @Test def testGetSummariesOfCodeSystem() {
    var entry1 = new CodeSystemVersionCatalogEntry()
    entry1.setCodeSystemVersionName("testName1")
    entry1.setAbout("about")
    entry1.setDocumentURI("uri1")
    entry1.setSourceAndNotation(new SourceAndNotation());
	entry1.setVersionOf(new CodeSystemReference());
   
    var entry2 = new CodeSystemVersionCatalogEntry()
    entry2.setCodeSystemVersionName("testName2")
    entry2.setAbout("about")
    entry2.setDocumentURI("uri2")
    entry2.setSourceAndNotation(new SourceAndNotation());
	entry2.setVersionOf(new CodeSystemReference());
	
    insert("cs1", entry1)
    insert("cs2", entry2)
    
    var summaries1 = dao.getResourceSummaries("cs1", "", 0, 100);
    assertEquals(1, summaries1.getEntries().size)
    assertEquals("testName1", summaries1.getEntries().get(0).getCodeSystemVersionName())
    
    var summaries2 = dao.getResourceSummaries("cs2", "", 0, 100);
    assertEquals(1, summaries2.getEntries().size)
    assertEquals("testName2", summaries2.getEntries().get(0).getCodeSystemVersionName())
  }
  
   def getSummaries(map:CodeSystemVersionCatalogEntrySummary):DirectoryResult[CodeSystemVersionCatalogEntrySummary] =  {
     return dao.getResourceSummaries("", "", 0, 1)
   }

   def insert(path:String, csv:CodeSystemVersionCatalogEntry) =  dao.storeResource(path, csv)

}