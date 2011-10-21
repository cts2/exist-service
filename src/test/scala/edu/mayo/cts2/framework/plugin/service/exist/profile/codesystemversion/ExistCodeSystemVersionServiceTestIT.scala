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

class ExistCodeSystemVersionServiceTestIT extends BaseServiceTestBaseIT[CodeSystemVersionCatalogEntry,CodeSystemVersionCatalogEntrySummary] {
  
    @Autowired var readService:ExistCodeSystemVersionReadService = null
    @Autowired var maintService:ExistCodeSystemVersionMaintenanceService = null
 
    def getExceptionClass():Class[_<:UnknownResourceReference] = {
       classOf[UnknownCodeSystemVersion]
    }
  
    def createResource(name:String, uri:String) = {
      var entry = new CodeSystemVersionCatalogEntry()
      entry.setCodeSystemVersionName(name);
      entry.setAbout("about")
      entry.setDocumentURI(uri)
      entry.setSourceAndNotation(new SourceAndNotation());
	  entry.setVersionOf(new CodeSystemReference());
      
      maintService.createResource("", entry)
    }
      
    def getResource(name:String):CodeSystemVersionCatalogEntry = {
    	readService.read(ModelUtils.nameOrUriFromName(name), null)
    }
    
        def getResourceByUri(uri:String):CodeSystemVersionCatalogEntry = {
    	readService.read(ModelUtils.nameOrUriFromUri(uri), null)
    }
  
}