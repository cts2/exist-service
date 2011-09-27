package edu.mayo.cts2.sdk.plugin.service.exist.profile

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.runner.RunWith
import org.junit.Before
import org.junit.Test
import org.scalatest.junit.AssertionsForJUnit
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import edu.mayo.cts2.sdk.model.core.ScopedEntityName
import edu.mayo.cts2.sdk.model.entity.NamedEntityDescription
import edu.mayo.cts2.sdk.model.exception.Cts2RestException
import edu.mayo.cts2.sdk.model.service.exception.UnknownEntity
import edu.mayo.cts2.sdk.plugin.service.exist.dao.ExistManager
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import edu.mayo.cts2.sdk.model.service.exception.UnknownResourceReference
import edu.mayo.cts2.sdk.plugin.service.exist.dao.ExistManager
import edu.mayo.cts2.sdk.model.directory.DirectoryResult

@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(
  locations = 
Array("/exist-test-context.xml"))
abstract class BaseServiceTestBaseIT[T,S] extends BaseServiceTestBase {
 
  @Autowired var manager:ExistManager = null
  
  @Before def cleanExist() {
    	manager.getCollectionManagementService().removeCollection("/db");
  }
  
   @Test def testInsertAndRetrieve() {
    var name = "name"
    	 createResource(name)
    	
    	
    	assertNotNull(  getResource(name));
  }
  
   
   @Test def testInsertAndRetrieveNotFound() {
	   var name = "name"
    	createResource(name)
    	
    	try {
    	getResource("__INVALID_NAME__")
    	} catch {
    	  case e: Cts2RestException => checkCTS2RestException(e)
    	  return
    	}
    	
    	fail
   }
   
   def checkCTS2RestException(ex:Cts2RestException){
     var clazz = ex.getCts2Exception().getClass();
     
     assertEquals(clazz, getExceptionClass())
   }
   
   def getExceptionClass():Class[_<:UnknownResourceReference]
  
    def createResource(name:String)
      
    def getResource(name:String):T
}

  trait TestResourceSummaries[T,S] {
	    @Test def testInsertAndGetSummaries() {
	    	var resources = createResources();
	    	    	
	    	assertEquals(resources, getResourceSummaries().getEntries().size());
	    }
	    
	   def createResources():Int;
	    
	   def getResourceSummaries():DirectoryResult[S];
   }
  