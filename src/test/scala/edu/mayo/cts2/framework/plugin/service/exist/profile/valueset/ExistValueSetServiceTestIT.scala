package edu.mayo.cts2.framework.plugin.service.exist.profile.valueset

import org.springframework.beans.factory.annotation.Autowired

import edu.mayo.cts2.framework.model.service.exception.UnknownResourceReference
import edu.mayo.cts2.framework.model.service.exception.UnknownValueSet
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntry
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntrySummary
import edu.mayo.cts2.framework.plugin.service.exist.profile.BaseServiceTestBaseIT

class ExistValueSetServiceTestIT extends BaseServiceTestBaseIT[ValueSetCatalogEntry,ValueSetCatalogEntrySummary] {

  @Autowired var maintService: ExistValueSetMaintenanceService = null
  @Autowired var readService: ExistValueSetReadService = null

  def getExceptionClass(): Class[_ <: UnknownResourceReference] = {
    classOf[UnknownValueSet]
  }

  def createResource(name: String, uri:String, changeSetUri:String):ValueSetCatalogEntry = {
    var entry = new ValueSetCatalogEntry()
    entry.setAbout(uri)
    entry.setValueSetName(name)
    entry.setChangeableElementGroup(buildChangeableElementGroup(changeSetUri))

    maintService.createResource(entry)
  }

  def getResource(name: String): ValueSetCatalogEntry = {
    readService.read(ModelUtils.nameOrUriFromName(name), null)
  }
  
    def getResourceByUri(uri:String):ValueSetCatalogEntry = {
    	readService.read(ModelUtils.nameOrUriFromUri(uri), null)
    } 
}