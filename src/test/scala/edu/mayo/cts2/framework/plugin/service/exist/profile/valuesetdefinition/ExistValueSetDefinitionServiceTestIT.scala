package edu.mayo.cts2.framework.plugin.service.exist.profile.valuesetdefinition

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

class ExistValueSetDefinitionServiceTestIT 
	extends BaseServiceTestBaseIT[LocalIdValueSetDefinition,ValueSetDefinitionDirectoryEntry]
			with TestResourceSummaries[LocalIdValueSetDefinition,ValueSetDefinitionDirectoryEntry] {

  @Autowired var readService: ExistValueSetDefinitionReadService = null
  @Autowired var maintService: ExistValueSetDefinitionMaintenanceService = null
  @Autowired var queryService: ExistValueSetDefinitionQueryService = null

  def getExceptionClass(): Class[_ <: UnknownResourceReference] = {
    classOf[UnknownValueSetDefinition]
  }
  
  override def getName():String = {"someUri"}
    
   override def getUri():String = {"someUri"}

  def getResourceSummaries():DirectoryResult[ValueSetDefinitionDirectoryEntry] = {
     queryService.getResourceSummaries(null,null,null,null,new Page());
  }
      
  def createResources(changeSetUri:String):Int = {
    val resources = List(
        buildValueSetDefinition("http://Test1",changeSetUri), 
        buildValueSetDefinition("http://Test2",changeSetUri));
    
    resources.foreach(resource => maintService.createResource(new LocalIdValueSetDefinition(resource)))
    
    resources.size
   }
      
  def createResource(name: String, uri:String, changeSetUri:String) = {
	var entry = buildValueSetDefinition(uri,changeSetUri)
    
     entry.setChangeableElementGroup(buildChangeableElementGroup(changeSetUri))
     
     maintService.createResource(new LocalIdValueSetDefinition(entry))
  }
  
  def buildValueSetDefinition(uri:String,changeSetUri:String):ValueSetDefinition = {
    
    var entry = new ValueSetDefinition()
    entry.setDocumentURI(uri)
    entry.setAbout(uri)
    entry.setSourceAndNotation(new SourceAndNotation())
    entry.setDefinedValueSet(new ValueSetReference())
    entry.getDefinedValueSet().setContent("vs")
    entry.addEntry(new ValueSetDefinitionEntry())
    entry.getEntry(0).setCompleteCodeSystem(new CompleteCodeSystemReference())
    entry.getEntry(0).getCompleteCodeSystem().setCodeSystem(new CodeSystemReference())
    entry.getEntry(0).setOperator(SetOperator.UNION)
    entry.getEntry(0).setEntryOrder(1l);
    
    entry.setChangeableElementGroup(buildChangeableElementGroup(changeSetUri))
    
    entry
  }

  def getResource(uri: String): LocalIdValueSetDefinition = {
    var id = new ValueSetDefinitionReadId(uri);
    
    readService.read(id,new ResolvedReadContext())
  }

    def getResourceByUri(uri:String):LocalIdValueSetDefinition = {
       var id = new ValueSetDefinitionReadId(uri);
       
    	readService.read(id,new ResolvedReadContext())
    } 
}