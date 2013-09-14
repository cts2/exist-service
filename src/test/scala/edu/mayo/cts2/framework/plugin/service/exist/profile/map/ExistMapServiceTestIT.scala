package edu.mayo.cts2.framework.plugin.service.exist.profile.map

import org.springframework.beans.factory.annotation.Autowired

import edu.mayo.cts2.framework.model.map.MapCatalogEntry
import edu.mayo.cts2.framework.model.map.MapCatalogEntrySummary
import edu.mayo.cts2.framework.model.service.exception.UnknownMap
import edu.mayo.cts2.framework.model.service.exception.UnknownResourceReference
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.plugin.service.exist.profile.BaseServiceTestBaseIT

class ExistMapServiceTestIT extends BaseServiceTestBaseIT[MapCatalogEntry,MapCatalogEntrySummary] {
  
    @Autowired var readService:ExistMapReadService = null
    @Autowired var maintService:ExistMapMaintenanceService = null
 
    def getExceptionClass():Class[_<:UnknownResourceReference] = {
       classOf[UnknownMap]
    }
  
    def createResource(name:String, uri:String, changeSetUri:String):MapCatalogEntry = {
      var entry = new MapCatalogEntry()
      entry.setMapName(name)
      entry.setAbout(uri)
      entry.setChangeableElementGroup(buildChangeableElementGroup(changeSetUri))
      
      maintService.createResource(entry);
    }
      
    def getResource(name:String):MapCatalogEntry = {
    	readService.read(ModelUtils.nameOrUriFromName(name), null)
    }
  
    def getResourceByUri(uri:String):MapCatalogEntry = {
    	readService.read(ModelUtils.nameOrUriFromUri(uri), null)
    }
}