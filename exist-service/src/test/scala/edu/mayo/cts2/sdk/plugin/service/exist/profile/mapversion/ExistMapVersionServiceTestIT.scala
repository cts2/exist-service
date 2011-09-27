package edu.mayo.cts2.sdk.plugin.service.exist.profile.mapversion

import org.springframework.beans.factory.annotation.Autowired

import edu.mayo.cts2.framework.plugin.service.exist.profile.mapversion.ExistMapVersionMaintenanceService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.mapversion.ExistMapVersionReadService;
import edu.mayo.cts2.sdk.model.service.exception.UnknownMapVersion
import edu.mayo.cts2.sdk.model.service.exception.UnknownResourceReference
import edu.mayo.cts2.sdk.model.mapversion.MapVersion
import edu.mayo.cts2.sdk.model.core.SourceAndNotation
import edu.mayo.cts2.sdk.model.core.MapReference
import org.junit.Test
import edu.mayo.cts2.sdk.model.mapversion.MapVersionDirectoryEntry
import edu.mayo.cts2.sdk.plugin.service.exist.profile.BaseServiceTestBaseIT

class ExistMapVersionServiceTestIT extends BaseServiceTestBaseIT[MapVersion,MapVersionDirectoryEntry] {
  
    @Autowired var readService:ExistMapVersionReadService = null
    @Autowired var maintService:ExistMapVersionMaintenanceService = null
 
    def getExceptionClass():Class[_<:UnknownResourceReference] = {
       classOf[UnknownMapVersion]
    }
  
    def createResource(name:String) = {
      var entry = new MapVersion()
      entry.setMapVersionName(name);
      entry.setAbout("about")
      entry.setDocumentURI("uri")
      entry.setSourceAndNotation(new SourceAndNotation());
      entry.setVersionOf(new MapReference())
      entry.getVersionOf().setContent("map")

      maintService.createResource("", entry)
    }
      
    def getResource(name:String):MapVersion = {
    	readService.read(name)
    }

}