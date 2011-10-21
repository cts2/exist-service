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

class ExistAssociationServiceTestIT extends BaseServiceTestBaseIT[Association,AssociationDirectoryEntry] {

  @Autowired var readService: ExistAssociationReadService = null
  @Autowired var maintService: ExistAssociationMaintenanceService = null
  @Autowired var queryService: ExistAssociationQueryService = null
 
  def getExceptionClass(): Class[_ <: UnknownResourceReference] = {
    classOf[UnknownAssociation]
  }

   def createResources():List[Association] = {
      List(createAssociation("Test"), createAssociation("Test2"))
   }
    
   def getResourceSummaries():DirectoryResult[AssociationDirectoryEntry] = {
     queryService.getResourceSummaries(null,null,null,new Page());
  }
   
  def createAssociation(name: String):Association = {
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
    
    entry
  }
  
  def createResource(name: String, uri:String) = {
    var entry1 = createAssociation(uri)
    var entry2 = createAssociation(name)
    
    maintService.createResource("", entry1)
    maintService.createResource("", entry2)
  }

  def getResource(uri: String): Association = {
    var id = new AssociationReadId(uri, "codesystemversion")
  
    readService.read(id, null)
  }
  
      def getResourceByUri(uri:String):Association = {
         var id = new AssociationReadId(uri, "codesystemversion")
    	readService.read(id, null)
    }

}