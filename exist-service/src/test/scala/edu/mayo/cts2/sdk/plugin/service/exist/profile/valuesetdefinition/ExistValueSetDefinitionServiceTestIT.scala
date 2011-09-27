package edu.mayo.cts2.sdk.plugin.service.exist.profile.valuesetdefinition

import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

import edu.mayo.cts2.framework.plugin.service.exist.profile.valuesetdefinition.ExistValueSetDefinitionMaintenanceService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.valuesetdefinition.ExistValueSetDefinitionReadService;
import edu.mayo.cts2.sdk.model.core.types.SetOperator
import edu.mayo.cts2.sdk.model.core.CodeSystemReference
import edu.mayo.cts2.sdk.model.core.SourceAndNotation
import edu.mayo.cts2.sdk.model.core.ValueSetReference
import edu.mayo.cts2.sdk.model.valuesetdefinition.CompleteCodeSystemReference
import edu.mayo.cts2.sdk.model.valuesetdefinition.ValueSetDefinition
import edu.mayo.cts2.sdk.model.valuesetdefinition.ValueSetDefinitionDirectoryEntry
import edu.mayo.cts2.sdk.model.valuesetdefinition.ValueSetDefinitionEntry
import edu.mayo.cts2.sdk.model.service.exception.UnknownValueSetDefinition
import edu.mayo.cts2.sdk.model.service.exception.UnknownResourceReference
import edu.mayo.cts2.sdk.plugin.service.exist.profile.BaseServiceTestBaseIT

class ExistValueSetDefinitionServiceTestIT extends BaseServiceTestBaseIT[ValueSetDefinition,ValueSetDefinitionDirectoryEntry] {

  @Autowired var readService: ExistValueSetDefinitionReadService = null
  @Autowired var maintService: ExistValueSetDefinitionMaintenanceService = null

  def getExceptionClass(): Class[_ <: UnknownResourceReference] = {
    classOf[UnknownValueSetDefinition]
  }

  def createResource(name: String) = {
    var entry = new ValueSetDefinition()
    entry.setDocumentURI(name)
    entry.setAbout("about")
    entry.setSourceAndNotation(new SourceAndNotation())
    entry.setDefinedValueSet(new ValueSetReference())
    entry.addEntry(new ValueSetDefinitionEntry())
    entry.getEntry(0).setCompleteCodeSystem(new CompleteCodeSystemReference())
    entry.getEntry(0).getCompleteCodeSystem().setCodeSystem(new CodeSystemReference())
    entry.getEntry(0).setOperator(SetOperator.UNION)
    entry.getEntry(0).setEntryOrder(1l);

    maintService.createResource("", entry)
  }

  def getResource(name: String): ValueSetDefinition = {
    readService.read(name)
  }

}