package edu.mayo.cts2.framework.plugin.service.exist.profile.association

import java.lang.Override
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import edu.mayo.cts2.framework.model.association.Association
import edu.mayo.cts2.framework.model.association.AssociationDirectoryEntry
import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.model.core.CodeSystemReference
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference
import edu.mayo.cts2.framework.model.core.PredicateReference
import edu.mayo.cts2.framework.model.core.StatementTarget
import edu.mayo.cts2.framework.model.core.URIAndEntityName
import edu.mayo.cts2.framework.model.directory.DirectoryResult
import edu.mayo.cts2.framework.model.service.exception.UnknownAssociation
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.plugin.service.exist.profile.BaseServiceTestBaseIT
import edu.mayo.cts2.framework.plugin.service.exist.profile.TestResourceSummaries
import edu.mayo.cts2.framework.service.profile.association.name.AssociationReadId
import edu.mayo.cts2.framework.model.service.exception.UnknownResourceReference
import edu.mayo.cts2.framework.model.command.ResolvedReadContext
import edu.mayo.cts2.framework.service.profile.association.AssociationQuery
import edu.mayo.cts2.framework.model.service.core.Query
import edu.mayo.cts2.framework.model.extension.LocalIdAssociation

class ExistAssociationServiceTestIT 
	extends BaseServiceTestBaseIT[LocalIdAssociation,AssociationDirectoryEntry]
			with TestResourceSummaries[LocalIdAssociation,AssociationDirectoryEntry] {

  @Autowired var readService: ExistAssociationReadService = null
  @Autowired var maintService: ExistAssociationMaintenanceService = null
  @Autowired var queryService: ExistAssociationQueryService = null
 
  override def getUri():String = {"someUri"}
   
    @Override
   def createResources(changeSetUri:String):Int = {
    val resources = List(createAssociation("http://Test1", "someuri", changeSetUri), createAssociation("http://Test2", "someuri", changeSetUri));
    resources.foreach(resource => maintService.createResource(resource))
    
    resources.size
   }
  
  def getExceptionClass(): Class[_ <: UnknownResourceReference] = {
    classOf[UnknownAssociation]
  }
    
   def getResourceSummaries():DirectoryResult[AssociationDirectoryEntry] = {
     queryService.getResourceSummaries(new TestQuery(),null,new Page());
  }
   
  def createAssociation(name: String,uri: String, changeSetUri:String):Association = {
    var entry = new Association()
    entry.setAssociationID(uri)
    entry.setSubject(new URIAndEntityName())
    entry.getSubject().setName("name")
    entry.getSubject().setNamespace("namespace")
    entry.getSubject().setUri("uri")

    entry.addTarget(new StatementTarget())
    entry.getTarget(0).setExternalIdentifier(name)
    entry.getTarget(0).setEntity(new URIAndEntityName())
    entry.getTarget(0).getEntity().setName("name")
    entry.getTarget(0).getEntity().setNamespace("namespace")
    entry.getTarget(0).getEntity().setUri("uri")

    entry.setPredicate(new PredicateReference())
    entry.getPredicate().setName("name")
    entry.getPredicate().setNamespace("namespace")
    entry.getPredicate().setUri("uri")

    entry.setAssertedBy(new CodeSystemVersionReference())
    entry.getAssertedBy().setVersion(new NameAndMeaningReference())
    entry.getAssertedBy().getVersion().setContent("csv")
    
    entry.getAssertedBy().setCodeSystem(new CodeSystemReference())
    entry.getAssertedBy().getCodeSystem().setContent("cs")
    
    entry.setChangeableElementGroup(buildChangeableElementGroup(changeSetUri))
    
    entry
  }
  
  def createResource(name: String, uri:String, changeSetUri:String):LocalIdAssociation = {
    var entry = createAssociation(name,uri,changeSetUri)
    
    maintService.createResource(entry)

  }

  def getResource(name: String): LocalIdAssociation = {
    var id = new AssociationReadId(name, ModelUtils.nameOrUriFromName("csv"))
  
    readService.read(id, null)
  }
  
      def getResourceByUri(uri:String):LocalIdAssociation = {
        var id = new AssociationReadId(uri)

        readService.read(id, null)

    }

}

class TestQuery extends AssociationQuery {

	def getQuery():Query = {null}

	def getFilterComponent() = {null}

	def getReadContext():ResolvedReadContext = {null}
	
	def getRestrictions() = { null }
	
}