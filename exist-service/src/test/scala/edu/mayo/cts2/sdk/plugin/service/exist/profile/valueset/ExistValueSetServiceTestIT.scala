package edu.mayo.cts2.sdk.plugin.service.exist.profile.valueset

import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

import edu.mayo.cts2.framework.plugin.service.exist.profile.valueset.ExistValueSetMaintenanceService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.valueset.ExistValueSetReadService;
import edu.mayo.cts2.sdk.model.valueset.ValueSetCatalogEntry
import edu.mayo.cts2.sdk.model.valueset.ValueSetCatalogEntrySummary
import edu.mayo.cts2.sdk.plugin.service.exist.profile.BaseServiceTestBaseIT
import edu.mayo.cts2.sdk.model.service.exception.UnknownValueSet
import edu.mayo.cts2.sdk.model.service.exception.UnknownResourceReference

class ExistValueSetServiceTestIT extends BaseServiceTestBaseIT[ValueSetCatalogEntry,ValueSetCatalogEntrySummary] {

  @Autowired var maintService: ExistValueSetMaintenanceService = null
  @Autowired var readService: ExistValueSetReadService = null

  def getExceptionClass(): Class[_ <: UnknownResourceReference] = {
    classOf[UnknownValueSet]
  }

  def createResource(name: String) = {
    var entry = new ValueSetCatalogEntry()
    entry.setAbout("about")
    entry.setValueSetName(name)

    maintService.createResource("", entry)
  }

  def getResource(name: String): ValueSetCatalogEntry = {
    readService.read(name)
  }

}