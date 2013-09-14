package edu.mayo.cts2.framework.plugin.service.exist.profile.statement

import org.springframework.beans.factory.annotation.Autowired
import edu.mayo.cts2.framework.model.core.types.SetOperator
import edu.mayo.cts2.framework.model.core.CodeSystemReference
import edu.mayo.cts2.framework.model.core.SourceAndNotation
import edu.mayo.cts2.framework.model.core.ValueSetReference
import edu.mayo.cts2.framework.model.service.exception.UnknownResourceReference
import edu.mayo.cts2.framework.model.service.exception.UnknownValueSetDefinition
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.model.valuesetdefinition.CompleteCodeSystemReference
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinition
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinitionDirectoryEntry
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinitionEntry
import edu.mayo.cts2.framework.plugin.service.exist.profile.BaseServiceTestBaseIT
import junit.framework.Test
import edu.mayo.cts2.framework.model.extension.LocalIdValueSetDefinition
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.name.ValueSetDefinitionReadId
import edu.mayo.cts2.framework.model.command.ResolvedReadContext
import edu.mayo.cts2.framework.plugin.service.exist.profile.TestResourceSummaries
import edu.mayo.cts2.framework.model.association.AssociationDirectoryEntry
import edu.mayo.cts2.framework.model.directory.DirectoryResult
import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.model.service.exception.UnknownStatement
import edu.mayo.cts2.framework.model.extension.LocalIdStatement
import edu.mayo.cts2.framework.model.statement.StatementDirectoryEntry
import edu.mayo.cts2.framework.model.statement.Statement
import edu.mayo.cts2.framework.model.core.URIAndEntityName
import edu.mayo.cts2.framework.model.statement.StatementSubject
import edu.mayo.cts2.framework.model.core.StatementTarget
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference
import edu.mayo.cts2.framework.service.profile.statement.name.StatementReadId
import edu.mayo.cts2.framework.model.service.core.Query
import edu.mayo.cts2.framework.model.service.core.NameOrURI
import edu.mayo.cts2.framework.service.profile.ResourceQuery

class ExistStatementServiceTestIT 
	extends BaseServiceTestBaseIT[LocalIdStatement,StatementDirectoryEntry]
			with TestResourceSummaries[LocalIdStatement,StatementDirectoryEntry] {

  @Autowired var readService: ExistStatementReadService = null
  @Autowired var maintService: ExistStatementMaintenanceService = null
  @Autowired var queryService: ExistStatementQueryService = null

  def getExceptionClass(): Class[_ <: UnknownResourceReference] = {
    classOf[UnknownStatement]
  }
  
  override def getName():String = {"someName"}
    
   override def getUri():String = {"someUri"}

  def getResourceSummaries():DirectoryResult[StatementDirectoryEntry] = {
     queryService.getResourceSummaries(new TestQuery(),null,new Page());
  }
      
  def createResources(changeSetUri:String):Int = {
    val resources = List(
        buildEntry("http://Test1",changeSetUri), 
        buildEntry("http://Test2",changeSetUri));
    
    resources.foreach(resource => 
      maintService.createResource(resource))
    
    resources.size
   }
      
  def createResource(name: String, uri:String, changeSetUri:String):LocalIdStatement = {
	var entry = buildEntry(uri,changeSetUri)
    
     entry.setChangeableElementGroup(buildChangeableElementGroup(changeSetUri))
     
     maintService.createResource(entry)
  }
  
  def buildEntry(uri:String,changeSetUri:String):Statement = {
    
    var entry = new Statement()
    entry.setStatementURI(uri)
	entry.setSubject(new StatementSubject())
	entry.setPredicate(new URIAndEntityName())
	entry.getPredicate().setName("name")
	entry.getPredicate().setNamespace("ns")
	entry.getPredicate().setUri("uri")
	
	var urientityname = new URIAndEntityName()
	urientityname.setName("name")
	urientityname.setNamespace("ns")
	urientityname.setUri("uri")
	
	var entityTarget = new StatementTarget()
    entityTarget.setEntity(urientityname);
    
	entry.addTarget(entityTarget)

	entry.setAssertedBy(new CodeSystemVersionReference())
	entry.getAssertedBy().setCodeSystem(new CodeSystemReference())
	entry.getAssertedBy().getCodeSystem().setContent("statementtestcs")
	entry.getAssertedBy().setVersion(new NameAndMeaningReference())
	entry.getAssertedBy().getVersion().setContent("statementtestcsversion")

    entry.setChangeableElementGroup(buildChangeableElementGroup(changeSetUri))
    
    entry
  }

  def getResource(name: String): LocalIdStatement = {
    var nameOrURI = new NameOrURI();
    nameOrURI.setName("statementtestcsversion");
    var id = new StatementReadId(name, nameOrURI);
    
    readService.read(id,new ResolvedReadContext())
  }

    def getResourceByUri(uri:String):LocalIdStatement = {
       var id = new StatementReadId(uri);
       
    	readService.read(id,new ResolvedReadContext())
    } 
}

class TestQuery extends ResourceQuery {

	def getQuery():Query = {null}

	def getFilterComponent() = {null}

	def getReadContext():ResolvedReadContext = {null}
	
}