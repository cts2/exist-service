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

class ExistValueSetDefinitionServiceTestIT extends BaseServiceTestBaseIT[ValueSetDefinition,ValueSetDefinitionDirectoryEntry] {

  @Autowired var readService: ExistValueSetDefinitionReadService = null
  @Autowired var maintService: ExistValueSetDefinitionMaintenanceService = null

  def getExceptionClass(): Class[_ <: UnknownResourceReference] = {
    classOf[UnknownValueSetDefinition]
  }

  def createResource(name: String, uri:String) = {
     var entry1 = buildValueSetDefinition(name)
     var entry2 = buildValueSetDefinition(uri)
    

     maintService.createResource("", entry1)
     maintService.createResource("", entry2)
  }
  
  def buildValueSetDefinition(uri:String):ValueSetDefinition = {
    
    var entry = new ValueSetDefinition()
    entry.setDocumentURI(uri)
    entry.setAbout(uri)
    entry.setSourceAndNotation(new SourceAndNotation())
    entry.setDefinedValueSet(new ValueSetReference())
    entry.addEntry(new ValueSetDefinitionEntry())
    entry.getEntry(0).setCompleteCodeSystem(new CompleteCodeSystemReference())
    entry.getEntry(0).getCompleteCodeSystem().setCodeSystem(new CodeSystemReference())
    entry.getEntry(0).setOperator(SetOperator.UNION)
    entry.getEntry(0).setEntryOrder(1l);
    
    entry
  }

  def getResource(uri: String): ValueSetDefinition = {
    readService.read(uri,null)
  }

    def getResourceByUri(uri:String):ValueSetDefinition = {
    	readService.read(uri, null)
    } 
}