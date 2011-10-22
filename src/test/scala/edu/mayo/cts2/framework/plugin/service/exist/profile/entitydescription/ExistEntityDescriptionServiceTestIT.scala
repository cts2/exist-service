package edu.mayo.cts2.framework.plugin.service.exist.profile.entitydescription

import org.junit.Assert._
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.junit.Before
import org.junit.Test
import org.scalatest.junit.AssertionsForJUnit
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.ContextConfiguration

import edu.mayo.cts2.framework.model.core.CodeSystemReference
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference
import edu.mayo.cts2.framework.model.core.ScopedEntityName
import edu.mayo.cts2.framework.model.core.URIAndEntityName
import edu.mayo.cts2.framework.model.entity.EntityDescription
import edu.mayo.cts2.framework.model.entity.NamedEntityDescription
import edu.mayo.cts2.framework.model.exception.Cts2RestException
import edu.mayo.cts2.framework.model.service.exception.UnknownEntity
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistManager
import edu.mayo.cts2.framework.service.profile.entitydescription.name.EntityDescriptionReadId


@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(
  locations = 
Array("/exist-test-context.xml"))
class ExistEntityDescriptionServiceTestIT extends AssertionsForJUnit {
 
 
  @Autowired var readService:ExistEntityDescriptionReadService = null
  @Autowired var maintService:ExistEntityDescriptionMaintenanceService = null
  @Autowired var manager:ExistManager = null
  
  @Before def cleanExist() {
    	manager.getCollectionManagementService().removeCollection("/db");
  }
  
  @Test def testInsertAndRetrieve() {
	  	var ed = new EntityDescription();
	  	ed.setNamedEntity(createEntity("name", "namespace"))
	  	ed.getNamedEntity().setDescribingCodeSystemVersion(new CodeSystemVersionReference())
    	ed.getNamedEntity().getDescribingCodeSystemVersion().setVersion(new NameAndMeaningReference())
    	ed.getNamedEntity().getDescribingCodeSystemVersion().getVersion().setContent("csversion")
    	ed.getNamedEntity().getDescribingCodeSystemVersion().setCodeSystem(new CodeSystemReference())
    	ed.getNamedEntity().getDescribingCodeSystemVersion().getCodeSystem().setContent("cs")


	  	maintService.createResource("", ed)
    	
    	var name = new ScopedEntityName()
    	name.setName("name")
    	name.setNamespace("namespace")
    	
    	var id = new EntityDescriptionReadId(name, ModelUtils.nameOrUriFromName("csversion"))
    	
    	
    	assertNotNull(readService.read(id, null))
  }
  
    @Test def testInsertAndRetrieveDefaultNamespace() {
    	var ed = new EntityDescription();
	  	ed.setNamedEntity(createEntity("name", "cs"))
	    ed.getNamedEntity().setDescribingCodeSystemVersion(new CodeSystemVersionReference())
    	ed.getNamedEntity().getDescribingCodeSystemVersion().setVersion(new NameAndMeaningReference())
    	ed.getNamedEntity().getDescribingCodeSystemVersion().getVersion().setContent("csversion")
    	ed.getNamedEntity().getDescribingCodeSystemVersion().setCodeSystem(new CodeSystemReference())
    	ed.getNamedEntity().getDescribingCodeSystemVersion().getCodeSystem().setContent("cs")

    	maintService.createResource("", ed)
    	
    	var name = new ScopedEntityName()
    	name.setName("name")
    	name.setNamespace("cs")
    	
    	var id = new EntityDescriptionReadId(name, ModelUtils.nameOrUriFromName("csversion"))
    
    	
    	assertNotNull(readService.read(id, null))
  }
    
   @Test def testInsertAndRetrieveNotFound() {
    	var ed = new EntityDescription();
	  	ed.setNamedEntity(createEntity("name", "namespace"))
	    ed.getNamedEntity().setDescribingCodeSystemVersion(new CodeSystemVersionReference())
    	ed.getNamedEntity().getDescribingCodeSystemVersion().setVersion(new NameAndMeaningReference())
    	ed.getNamedEntity().getDescribingCodeSystemVersion().getVersion().setContent("csversion")
    	ed.getNamedEntity().getDescribingCodeSystemVersion().setCodeSystem(new CodeSystemReference())
    	ed.getNamedEntity().getDescribingCodeSystemVersion().getCodeSystem().setContent("cs")

    	maintService.createResource("", ed)
    	
    	var name = new ScopedEntityName()
    	name.setName("INVALID_NAME")
    	name.setNamespace("cs")
    	
    	var id = new EntityDescriptionReadId(name, ModelUtils.nameOrUriFromName("csversion"))
    	

    	assertNull(readService.read(id, null))
    	
   }
   
   def checkCTS2RestException(ex:Cts2RestException){
     var clazz = ex.getCts2Exception().getClass();
     
     assertEquals(clazz, new UnknownEntity().getClass)
   }
  
  def createEntity(name:String, ns:String):NamedEntityDescription =  {
     var entity = new NamedEntityDescription()
     entity.setEntityID(new ScopedEntityName())
     entity.getEntityID().setName(name)
     entity.getEntityID().setNamespace(ns)
     entity.setAbout("about")
     entity.setDescribingCodeSystemVersion(new CodeSystemVersionReference())
     entity.getDescribingCodeSystemVersion().setCodeSystem(new CodeSystemReference())
     entity.getDescribingCodeSystemVersion().setVersion(new NameAndMeaningReference())

	 entity.addEntityType(new URIAndEntityName())
	 entity.getEntityType(0).setName("name")
	 entity.getEntityType(0).setNamespace("ns")
     
     entity
  } 
}