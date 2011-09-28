package edu.mayo.cts2.framework.plugin.service.exist.profile.codesystemversion

import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

import edu.mayo.cts2.framework.plugin.service.exist.profile.codesystemversion.ExistCodeSystemVersionMaintenanceService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.codesystemversion.ExistCodeSystemVersionReadService;
import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry
import edu.mayo.cts2.framework.model.core.CodeSystemReference
import edu.mayo.cts2.framework.model.core.SourceAndNotation
import edu.mayo.cts2.framework.model.service.exception.UnknownCodeSystemVersion
import edu.mayo.cts2.framework.model.service.exception.UnknownResourceReference
import edu.mayo.cts2.framework.plugin.service.exist.profile.BaseServiceTestBaseIT
import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntrySummary

class ExistCodeSystemVersionServiceTestIT extends BaseServiceTestBaseIT[CodeSystemVersionCatalogEntry,CodeSystemVersionCatalogEntrySummary] {
  
    @Autowired var readService:ExistCodeSystemVersionReadService = null
    @Autowired var maintService:ExistCodeSystemVersionMaintenanceService = null
 
    def getExceptionClass():Class[_<:UnknownResourceReference] = {
       classOf[UnknownCodeSystemVersion]
    }
  
    def createResource(name:String) = {
      var entry = new CodeSystemVersionCatalogEntry()
      entry.setCodeSystemVersionName(name);
      entry.setAbout("about")
      entry.setDocumentURI("uri")
      entry.setSourceAndNotation(new SourceAndNotation());
	  entry.setVersionOf(new CodeSystemReference());
      
      maintService.createResource("", entry)
    }
      
    def getResource(name:String):CodeSystemVersionCatalogEntry = {
    	readService.read(name)
    }
  
}