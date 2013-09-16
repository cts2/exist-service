package edu.mayo.cts2.framework.plugin.service.exist.profile

import java.util.Date
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.runner.RunWith
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.ContextConfiguration
import edu.mayo.cts2.framework.model.core.types.ChangeType
import edu.mayo.cts2.framework.model.core.ChangeDescription
import edu.mayo.cts2.framework.model.core.ChangeableElementGroup
import edu.mayo.cts2.framework.model.directory.DirectoryResult
import edu.mayo.cts2.framework.model.service.exception.UnknownResourceReference
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistManager
import edu.mayo.cts2.framework.service.profile.update.ChangeSetService
import edu.mayo.cts2.framework.model.service.exception.CTS2Exception
import edu.mayo.cts2.framework.model.service.exception.ChangeSetIsNotOpen
import edu.mayo.cts2.framework.model.extension.LocalIdResource
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistDaoImpl

@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(
  locations = 
Array("/exist-test-context.xml"))
abstract class BaseServiceTestBaseIT[T,S] extends BaseServiceTestBase {
 
  @Autowired var manager:ExistManager = null
  
  @Autowired var changeSetService:ChangeSetService = null
  
  @Autowired var dao:ExistDaoImpl = null
  
  @Before def cleanExist() {
		dao.removeCollection(manager.getCollectionRoot());
  }
  
  def buildChangeableElementGroup(uri:String):ChangeableElementGroup = {
	  var g = new ChangeableElementGroup()
      
	  g.setChangeDescription(new ChangeDescription())
	  g.getChangeDescription().setChangeDate(new Date())
	  g.getChangeDescription().setChangeType(ChangeType.CREATE)
	  g.getChangeDescription().setContainingChangeSet(uri)
	  
	  g
 } 
  
  def getName():String = {"name"}
  def getUri():String = {"someUri"}
  
  
   @Test def testInsertAndRetrieve() {
    var name = getName()
       var uri = getUri()
         var changeSetId = changeSetService.createChangeSet().getChangeSetURI();
    
    	var resource = createResource(name, uri, changeSetId);
    	
    	if (resource.isInstanceOf[LocalIdResource[T]])
    	{
    		def temp = resource.asInstanceOf[LocalIdResource[T]]
    		name = temp.getLocalID();
    	}
    	
    	changeSetService.commitChangeSet(changeSetId)
    	
    	assertNotNull(  getResource(name));
    
  }
   
   @Test(expected = classOf[ChangeSetIsNotOpen])
   def testInsertWithClosedChangeSet() {
        var name = getName()
       var uri = getUri()
         var changeSetId = changeSetService.createChangeSet().getChangeSetURI();

    	 changeSetService.commitChangeSet(changeSetId)
    	 
    	 createResource(name, uri, changeSetId)

  }
   
    @Test def testInsertAndRetrieveWithChangeSetCommit() {
        var name = getName()
       var uri = getUri()
         var changeSetId = changeSetService.createChangeSet().getChangeSetURI();
           
    	var resource = createResource(name, uri, changeSetId);
    	if (resource.isInstanceOf[LocalIdResource[T]])
    	{
    		def temp = resource.asInstanceOf[LocalIdResource[T]]
    		name = temp.getLocalID();
    	}
    	
    	 changeSetService.commitChangeSet(changeSetId)
    	
    	assertNotNull(  getResource(name));
    
    	var changeSet = changeSetService.readChangeSet(changeSetId);
    	
    	assertEquals(changeSet.getMemberCount(), 1);
  }
    
    @Test def testInsertAndRetrieveWithNoChangeSetCommit() {
       var name = getName()
       var uri = getUri()
         var changeSetId = changeSetService.createChangeSet().getChangeSetURI();
           
    	var resource = createResource(name, uri, changeSetId);
    	if (resource.isInstanceOf[LocalIdResource[T]])
    	{
    		def temp = resource.asInstanceOf[LocalIdResource[T]]
    		name = temp.getLocalID();
    	}
    	
    	assertNull(  getResource(name));
    
    	var changeSet = changeSetService.readChangeSet(changeSetId);
    	
    	assertEquals(changeSet.getMemberCount(), 1);
  }
    
        @Test def testInsertAndRetrieveWithChangeSetRollback() {
      var name = getName()
       var uri = getUri()
         var changeSetId = changeSetService.createChangeSet().getChangeSetURI();
           
    	var resource = createResource(name, uri, changeSetId);
    	if (resource.isInstanceOf[LocalIdResource[T]])
    	{
    		def temp = resource.asInstanceOf[LocalIdResource[T]]
    		name = temp.getLocalID();
    	}
    	
    	changeSetService.rollbackChangeSet(changeSetId);
    	
    	assertNull(  getResource(name));
    
    	var changeSet = changeSetService.readChangeSet(changeSetId);
    	
    	assertNull(changeSet);
  }
   
   
   @Test def testInsertAndRetrieveNotFound() {
	      var name = getName()
       var uri = getUri()
	     var changeSetId = changeSetService.createChangeSet().getChangeSetURI();
	        
    	createResource(name, uri, changeSetId)
    	
    	changeSetService.commitChangeSet(changeSetId)
    	
    	var resource = getResource("__INVALID_NAME__")
    	
    	assertNull(resource)
    	
   }
   
       @Test def testInsertAndRetrieveByUri() {
	    	    var name = getName()
       var uri = getUri()
	    	var changeSetId = changeSetService.createChangeSet().getChangeSetURI();
    	 
	    	createResource(name, uri, changeSetId)
    	
	    	changeSetService.commitChangeSet(changeSetId)
    	
	    	assertNotNull(  getResourceByUri(uri));
	    }
       
   
   def checkCTS2RestException(ex:CTS2Exception){
     var clazz = ex.getClass();
     
     assertEquals(clazz, getExceptionClass())
   }
   
   def getExceptionClass():Class[_<:UnknownResourceReference]
  
    def createResource(name:String, uri:String, changeSetId:String):T
      
    def getResource(name:String):T
    
    def getResourceByUri(uri:String):T
}

  trait TestResourceSummaries[T,S] {
    
    @Autowired var changeSetServiceInTrait:ChangeSetService = null
     
	    @Test def testInsertAndGetSummaries() {
	        var changeSetUri = changeSetServiceInTrait.createChangeSet().getChangeSetURI();

	    	var resources = createResources(changeSetUri);
	    	
	    	changeSetServiceInTrait.commitChangeSet(changeSetUri)
	    	    	
	    	assertEquals(resources, getResourceSummaries().getEntries().size());
	    }
	    
	   def createResources(changeSetUri:String):Int;
	    
	   def getResourceSummaries():DirectoryResult[S];
   }
  