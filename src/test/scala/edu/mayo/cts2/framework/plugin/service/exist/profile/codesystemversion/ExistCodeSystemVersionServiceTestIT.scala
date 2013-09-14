package edu.mayo.cts2.framework.plugin.service.exist.profile.codesystemversion

import org.springframework.beans.factory.annotation.Autowired
import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry
import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntrySummary
import edu.mayo.cts2.framework.model.core.CodeSystemReference
import edu.mayo.cts2.framework.model.core.SourceAndNotation
import edu.mayo.cts2.framework.model.service.exception.UnknownCodeSystemVersion
import edu.mayo.cts2.framework.model.service.exception.UnknownResourceReference
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.plugin.service.exist.profile.BaseServiceTestBaseIT
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.runner.RunWith
import org.junit.Before
import org.junit.Test

class ExistCodeSystemVersionServiceTestIT extends BaseServiceTestBaseIT[CodeSystemVersionCatalogEntry,CodeSystemVersionCatalogEntrySummary] {
  
    @Autowired var readService:ExistCodeSystemVersionReadService = null
    @Autowired var maintService:ExistCodeSystemVersionMaintenanceService = null
 
    def getExceptionClass():Class[_<:UnknownResourceReference] = {
       classOf[UnknownCodeSystemVersion]
    }
  
    def createResource(name:String, uri:String, changeSetUri:String):CodeSystemVersionCatalogEntry = {
      var entry = new CodeSystemVersionCatalogEntry()
      entry.setCodeSystemVersionName(name);
      entry.setAbout(uri)
      entry.setSourceAndNotation(new SourceAndNotation());
	  entry.setVersionOf(new CodeSystemReference());
	  entry.setChangeableElementGroup(buildChangeableElementGroup(changeSetUri))
      
      maintService.createResource(entry)
    }
      
    def getResource(name:String):CodeSystemVersionCatalogEntry = {
    	readService.read(ModelUtils.nameOrUriFromName(name), null)
    }
    
        def getResourceByUri(uri:String):CodeSystemVersionCatalogEntry = {
    	readService.read(ModelUtils.nameOrUriFromUri(uri), null)
    }
        
          
   @Test def testInsertAndRetrieveByOfficialVersionIdWithVersionId() {
     var changeSetId = changeSetService.createChangeSet().getChangeSetURI();
     
      var entry = new CodeSystemVersionCatalogEntry()
      entry.setCodeSystemVersionName("Name_5.0_owl");
      entry.setAbout("http://about")
      entry.setDocumentURI("http://docuri")
      entry.setSourceAndNotation(new SourceAndNotation());
	  entry.setVersionOf(new CodeSystemReference());
	  entry.getVersionOf().setContent("csname")
	  entry.setOfficialResourceVersionId("5.0")
	  entry.setChangeableElementGroup(buildChangeableElementGroup(changeSetId))
    	
	  maintService.createResource(entry)

	 changeSetService.commitChangeSet(changeSetId)
	 assertNotNull( readService.getCodeSystemByVersionId(
			 ModelUtils.nameOrUriFromName("csname"), "5.0", null) )

 
  }
   
  @Test def testInsertAndRetrieveByOfficialVersionIdWithFullName() {
     var changeSetId = changeSetService.createChangeSet().getChangeSetURI();
     
     
      var entry = new CodeSystemVersionCatalogEntry()
      entry.setCodeSystemVersionName("Name_5.0_owl");
      entry.setAbout("http://about")
      entry.setDocumentURI("http://docuri")
      entry.setSourceAndNotation(new SourceAndNotation());
	  entry.setVersionOf(new CodeSystemReference());
	  entry.getVersionOf().setContent("csname")
	  entry.setOfficialResourceVersionId("5.0")
	  entry.setChangeableElementGroup(buildChangeableElementGroup(changeSetId))
    	
	  maintService.createResource(entry)
	  
	  changeSetService.commitChangeSet(changeSetId)

 	  assertNotNull( readService.read(ModelUtils.nameOrUriFromName("Name_5.0_owl"), null) )
  }
  
}