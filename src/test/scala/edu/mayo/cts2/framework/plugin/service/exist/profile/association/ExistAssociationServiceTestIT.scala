package edu.mayo.cts2.framework.plugin.service.exist.profile.association

import org.springframework.beans.factory.annotation.Autowired

import edu.mayo.cts2.framework.model.association.Association
import edu.mayo.cts2.framework.model.association.AssociationDirectoryEntry
import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference
import edu.mayo.cts2.framework.model.core.PredicateReference
import edu.mayo.cts2.framework.model.core.StatementTarget
import edu.mayo.cts2.framework.model.core.URIAndEntityName
import edu.mayo.cts2.framework.model.directory.DirectoryResult
import edu.mayo.cts2.framework.model.service.exception.UnknownAssociation
import edu.mayo.cts2.framework.model.service.exception.UnknownResourceReference
import edu.mayo.cts2.framework.plugin.service.exist.profile.BaseServiceTestBaseIT
import edu.mayo.cts2.framework.service.profile.association.name.AssociationReadId

class ExistAssociationServiceTestIT extends BaseServiceTestBaseIT[Association,AssociationDirectoryEntry] {

  @Autowired var readService: ExistAssociationReadService = null
  @Autowired var maintService: ExistAssociationMaintenanceService = null
  @Autowired var queryService: ExistAssociationQueryService = null
 
  override def getName():String = {"someUri"}
    
  override def getUri():String = {"someUri"}
   
  def getExceptionClass(): Class[_ <: UnknownResourceReference] = {
    classOf[UnknownAssociation]
  }
    
   def getResourceSummaries():DirectoryResult[AssociationDirectoryEntry] = {
     queryService.getResourceSummaries(null,null,null,new Page());
  }
   
  def createAssociation(name: String,changeSetUri:String):Association = {
    var entry = new Association()
    entry.setAssociationID(name)
    entry.setSubject(new URIAndEntityName())
    entry.getSubject().setName("name")
    entry.getSubject().setNamespace("namespace")

    entry.addTarget(new StatementTarget())
    entry.getTarget(0).setEntity(new URIAndEntityName())
    entry.getTarget(0).getEntity().setName("name")
    entry.getTarget(0).getEntity().setNamespace("namespace")

    entry.setPredicate(new PredicateReference())
    entry.getPredicate().setName("name")
    entry.getPredicate().setNamespace("namespace")

    entry.setAssertedBy(new CodeSystemVersionReference())
    entry.getAssertedBy().setVersion(new NameAndMeaningReference())
    entry.getAssertedBy().getVersion().setContent("csv")
    entry.setChangeableElementGroup(buildChangeableElementGroup(changeSetUri))
    
    entry
  }
  
  def createResource(name: String, uri:String, changeSetUri:String) = {
    var entry = createAssociation(uri,changeSetUri)
    
    maintService.createResource(entry)

  }

  def getResource(uri: String): Association = {
    var id = new AssociationReadId(uri, "csv")
  
    readService.read(id, null)
  }
  
      def getResourceByUri(uri:String):Association = {
         var id = new AssociationReadId(uri, "csv")
    	readService.read(id, null)
    }

}