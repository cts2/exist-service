package edu.mayo.cts2.sdk.plugin.service.exist.profile.codesystem

import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

import edu.mayo.cts2.framework.plugin.service.exist.profile.codesystem.ExistCodeSystemMaintenanceService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.codesystem.ExistCodeSystemReadService;
import edu.mayo.cts2.sdk.model.codesystem.CodeSystemCatalogEntry
import edu.mayo.cts2.sdk.model.service.exception.UnknownCodeSystem
import edu.mayo.cts2.sdk.model.service.exception.UnknownResourceReference
import edu.mayo.cts2.sdk.plugin.service.exist.profile.BaseServiceTestBaseIT
import edu.mayo.cts2.sdk.model.codesystem.CodeSystemCatalogEntrySummary

class ExistCodeSystemServiceTestIT 
	extends BaseServiceTestBaseIT[CodeSystemCatalogEntry,CodeSystemCatalogEntrySummary] {
  
    @Autowired var readService:ExistCodeSystemReadService = null
    @Autowired var maintService:ExistCodeSystemMaintenanceService = null
 
    def getExceptionClass():Class[_<:UnknownResourceReference] = {
       classOf[UnknownCodeSystem]
    }
  
    def createResource(name:String) = {
      var entry = new CodeSystemCatalogEntry()
      entry.setCodeSystemName(name);
      entry.setAbout("about")
      
      maintService.createResource("", entry)
    }
      
    def getResource(name:String):CodeSystemCatalogEntry = {
    	readService.read(name)
    }
  
}