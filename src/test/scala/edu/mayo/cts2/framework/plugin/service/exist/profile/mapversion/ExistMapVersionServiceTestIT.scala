package edu.mayo.cts2.framework.plugin.service.exist.profile.mapversion

import org.springframework.beans.factory.annotation.Autowired

import edu.mayo.cts2.framework.model.core.MapReference
import edu.mayo.cts2.framework.model.core.SourceAndNotation
import edu.mayo.cts2.framework.model.mapversion.MapVersion
import edu.mayo.cts2.framework.model.mapversion.MapVersionDirectoryEntry
import edu.mayo.cts2.framework.model.service.exception.UnknownMapVersion
import edu.mayo.cts2.framework.model.service.exception.UnknownResourceReference
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.plugin.service.exist.profile.BaseServiceTestBaseIT

class ExistMapVersionServiceTestIT extends BaseServiceTestBaseIT[MapVersion,MapVersionDirectoryEntry] {
  
    @Autowired var readService:ExistMapVersionReadService = null
    @Autowired var maintService:ExistMapVersionMaintenanceService = null
 
    def getExceptionClass():Class[_<:UnknownResourceReference] = {
       classOf[UnknownMapVersion]
    }
  
    def createResource(name:String, uri:String, changeSetUri:String):MapVersion = {
      var entry = new MapVersion()
      entry.setMapVersionName(name);
      entry.setAbout(uri)
      entry.setSourceAndNotation(new SourceAndNotation());
      entry.setVersionOf(new MapReference())
      entry.getVersionOf().setContent("map")
      entry.setChangeableElementGroup(buildChangeableElementGroup(changeSetUri))

      maintService.createResource(entry)
    }
      
    def getResource(name:String):MapVersion = {
    	readService.read(ModelUtils.nameOrUriFromName(name), null)
    }
     
    def getResourceByUri(uri:String):MapVersion = {
    	readService.read(ModelUtils.nameOrUriFromUri(uri), null)
    }
}