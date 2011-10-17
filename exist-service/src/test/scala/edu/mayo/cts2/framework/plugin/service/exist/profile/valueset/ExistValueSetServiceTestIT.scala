package edu.mayo.cts2.framework.plugin.service.exist.profile.valueset

import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntry
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntrySummary
import edu.mayo.cts2.framework.plugin.service.exist.profile.BaseServiceTestBaseIT
import edu.mayo.cts2.framework.model.service.exception.UnknownValueSet
import edu.mayo.cts2.framework.model.service.exception.UnknownResourceReference
import edu.mayo.cts2.framework.service.name.Name

class ExistValueSetServiceTestIT extends BaseServiceTestBaseIT[ValueSetCatalogEntry,ValueSetCatalogEntrySummary] {

  @Autowired var maintService: ExistValueSetMaintenanceService = null
  @Autowired var readService: ExistValueSetReadService = null

  def getExceptionClass(): Class[_ <: UnknownResourceReference] = {
    classOf[UnknownValueSet]
  }

  def createResource(name: String, uri:String) = {
    var entry = new ValueSetCatalogEntry()
    entry.setAbout(uri)
    entry.setValueSetName(name)

    maintService.createResource("", entry)
  }

  def getResource(name: String): ValueSetCatalogEntry = {
    readService.read(new Name(name))
  }
  
    def getResourceByUri(uri:String):ValueSetCatalogEntry = {
    	readService.readByUri(uri)
    } 
}