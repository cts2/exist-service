package edu.mayo.cts2.framework.plugin.service.exist.profile.concept

import org.springframework.beans.factory.annotation.Autowired

import edu.mayo.cts2.framework.model.conceptdomain.ConceptDomainCatalogEntry
import edu.mayo.cts2.framework.model.conceptdomain.ConceptDomainCatalogEntrySummary
import edu.mayo.cts2.framework.model.service.exception.UnknownAssociation
import edu.mayo.cts2.framework.model.service.exception.UnknownResourceReference
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.plugin.service.exist.profile.conceptdomain.ExistConceptDomainMaintenanceService
import edu.mayo.cts2.framework.plugin.service.exist.profile.conceptdomain.ExistConceptDomainQueryService
import edu.mayo.cts2.framework.plugin.service.exist.profile.conceptdomain.ExistConceptDomainReadService
import edu.mayo.cts2.framework.plugin.service.exist.profile.BaseServiceTestBaseIT

class ConceptDomainServiceTestIT 
	extends BaseServiceTestBaseIT[ConceptDomainCatalogEntry,ConceptDomainCatalogEntrySummary] {

  @Autowired var readService: ExistConceptDomainReadService = null
  @Autowired var maintService: ExistConceptDomainMaintenanceService = null
  @Autowired var queryService: ExistConceptDomainQueryService = null
 
  def getExceptionClass(): Class[_ <: UnknownResourceReference] = {
    classOf[UnknownAssociation]
  }

  
  
  def createResource(name: String, uri:String, changeSetUri:String):ConceptDomainCatalogEntry = {
    var entry = new ConceptDomainCatalogEntry()
    entry.setConceptDomainName(name)
    entry.setAbout(uri)
    entry.setChangeableElementGroup(buildChangeableElementGroup(changeSetUri))
    
    maintService.createResource(entry)
  }

  def getResource(name: String): ConceptDomainCatalogEntry = {
    var id = ModelUtils.nameOrUriFromName(name)
  
    readService.read(id, null)
  }
  
      def getResourceByUri(uri:String):ConceptDomainCatalogEntry = {
           var id = ModelUtils.nameOrUriFromUri(uri)
    	
           readService.read(id, null)
    }

}