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
import org.junit.Test
import org.junit.Assert._
import edu.mayo.cts2.framework.model.extension.LocalIdValueSetDefinition
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.name.ValueSetDefinitionReadId
import edu.mayo.cts2.framework.model.command.ResolvedReadContext
import edu.mayo.cts2.framework.plugin.service.exist.profile.TestResourceSummaries
import edu.mayo.cts2.framework.model.association.AssociationDirectoryEntry
import edu.mayo.cts2.framework.model.directory.DirectoryResult
import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.service.profile.mapentry.MapEntryQuery
import edu.mayo.cts2.framework.model.service.core.Query
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionQuery
import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry
import edu.mayo.cts2.framework.model.core.VersionTagReference

class ExistValueSetDefinitionServiceTestIT 
	extends BaseServiceTestBaseIT[LocalIdValueSetDefinition,ValueSetDefinitionDirectoryEntry]
			with TestResourceSummaries[LocalIdValueSetDefinition,ValueSetDefinitionDirectoryEntry] {

  @Autowired var readService: ExistValueSetDefinitionReadService = null
  @Autowired var maintService: ExistValueSetDefinitionMaintenanceService = null
  @Autowired var queryService: ExistValueSetDefinitionQueryService = null

  def getExceptionClass(): Class[_ <: UnknownResourceReference] = {
    classOf[UnknownValueSetDefinition]
  }
  
  def getResourceSummaries():DirectoryResult[ValueSetDefinitionDirectoryEntry] = {
     queryService.getResourceSummaries(new TestQuery(),null,new Page());
  }
      
  def createResources(changeSetUri:String):Int = {
    val resources = List(
        buildValueSetDefinition("http://Test1",changeSetUri), 
        buildValueSetDefinition("http://Test2",changeSetUri));
    
    resources.foreach(resource => maintService.createResource(resource))
    
    resources.size
   }
      
  def createResource(name: String, uri:String, changeSetUri:String):LocalIdValueSetDefinition = {
	var entry = buildValueSetDefinition(uri,changeSetUri)
    
     entry.setChangeableElementGroup(buildChangeableElementGroup(changeSetUri))
     entry.setFormalName(name)
     maintService.createResource(entry)
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

  def getResource(name: String): LocalIdValueSetDefinition = {
    var id = new ValueSetDefinitionReadId(name, ModelUtils.nameOrUriFromName("vs"));
    
    readService.read(id,new ResolvedReadContext())
  }

    def getResourceByUri(uri:String):LocalIdValueSetDefinition = {
       var id = new ValueSetDefinitionReadId(uri);
       
    	readService.read(id,new ResolvedReadContext())
    } 
    
    @Test def testInsertAndRetrieveCurrent() {
      var changeSetId = changeSetService.createChangeSet().getChangeSetURI();
     
      val vs1 = buildValueSetDefinition("uri1", changeSetId)
      vs1.addVersionTag(new VersionTagReference("NOT_CURRENT"))
     
      val vs2 = buildValueSetDefinition("uri2", changeSetId)
      vs2.addVersionTag(new VersionTagReference("CURRENT"))
    	
	  maintService.createResource(vs1)
	  maintService.createResource(vs2)

	 changeSetService.commitChangeSet(changeSetId)
	 
	 val tag = new VersionTagReference("CURRENT");
     val parent = ModelUtils.nameOrUriFromName("vs")
     
     val vs = readService.readByTag(parent,tag,null)
	 assertNotNull( vs )
	 assertEquals( "uri2", vs.getResource().getDocumentURI() )
  }
}

class TestQuery extends ValueSetDefinitionQuery {

	def getQuery():Query = {null}

	def getFilterComponent() = {null}

	def getReadContext():ResolvedReadContext = {null}

	def getRestrictions() = {null}
	
}