package edu.mayo.cts2.framework.plugin.service.exist.profile.association

import org.springframework.beans.factory.annotation.Autowired
import edu.mayo.cts2.framework.model.association.Association
import edu.mayo.cts2.framework.model.association.AssociationDirectoryEntry
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference
import edu.mayo.cts2.framework.model.core.PredicateReference
import edu.mayo.cts2.framework.model.core.StatementTarget
import edu.mayo.cts2.framework.model.core.URIAndEntityName
import edu.mayo.cts2.framework.model.directory.DirectoryResult
import edu.mayo.cts2.framework.model.service.exception.UnknownAssociation
import edu.mayo.cts2.framework.model.service.exception.UnknownResourceReference
import edu.mayo.cts2.framework.plugin.service.exist.profile.BaseServiceTestBaseIT
import edu.mayo.cts2.framework.service.command.Page
import edu.mayo.cts2.framework.service.profile.association.name.AssociationReadId
import edu.mayo.cts2.framework.model.conceptdomain.ConceptDomainCatalogEntrySummary
import edu.mayo.cts2.framework.model.conceptdomain.ConceptDomainCatalogEntry
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.plugin.service.exist.profile.conceptdomain.ExistConceptDomainReadService
import edu.mayo.cts2.framework.plugin.service.exist.profile.conceptdomain.ExistConceptDomainMaintenanceService
import edu.mayo.cts2.framework.plugin.service.exist.profile.conceptdomain.ExistConceptDomainQueryService

class ConceptDomainServiceTestIT extends BaseServiceTestBaseIT[ConceptDomainCatalogEntry,ConceptDomainCatalogEntrySummary] {

  @Autowired var readService: ExistConceptDomainReadService = null
  @Autowired var maintService: ExistConceptDomainMaintenanceService = null
  @Autowired var queryService: ExistConceptDomainQueryService = null
 
  def getExceptionClass(): Class[_ <: UnknownResourceReference] = {
    classOf[UnknownAssociation]
  }

  
  
  def createResource(name: String, uri:String) = {
    var entry = new ConceptDomainCatalogEntry()
    entry.setConceptDomainName(name)
    entry.setAbout(uri)
    
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