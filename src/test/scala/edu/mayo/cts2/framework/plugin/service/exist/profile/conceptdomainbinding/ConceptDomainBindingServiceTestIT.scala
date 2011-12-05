package edu.mayo.cts2.framework.plugin.service.exist.profile.conceptdomainbinding

import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.model.conceptdomainbinding.ConceptDomainBinding
import edu.mayo.cts2.framework.model.conceptdomainbinding.ConceptDomainBindingDirectoryEntry
import edu.mayo.cts2.framework.model.directory.DirectoryResult
import edu.mayo.cts2.framework.model.extension.LocalIdConceptDomainBinding
import edu.mayo.cts2.framework.model.service.exception.UnknownConceptDomainBinding
import edu.mayo.cts2.framework.plugin.service.exist.profile.BaseServiceTestBaseIT
import edu.mayo.cts2.framework.plugin.service.exist.profile.TestResourceSummaries
import edu.mayo.cts2.framework.service.profile.conceptdomainbinding.name.ConceptDomainBindingReadId
import edu.mayo.cts2.framework.model.service.exception.UnknownResourceReference
import edu.mayo.cts2.framework.model.core.ConceptDomainReference
import edu.mayo.cts2.framework.model.core.ValueSetReference

class ConceptDomainBindingServiceTestIT 
	extends BaseServiceTestBaseIT[LocalIdConceptDomainBinding,ConceptDomainBindingDirectoryEntry] 
			with TestResourceSummaries[LocalIdConceptDomainBinding,ConceptDomainBindingDirectoryEntry] {

  @Autowired var readService: ExistConceptDomainBindingReadService = null
  @Autowired var maintService: ExistConceptDomainBindingMaintenanceService = null
  @Autowired var queryService: ExistConceptDomainBindingQueryService = null
 
  override def getName():String = {"http://def/uri/1"}
    
   override def getUri():String = {"http://def/uri/1"}
   
  def getExceptionClass(): Class[_ <: UnknownResourceReference] = {
    classOf[UnknownConceptDomainBinding]
  }
  
  def getResourceSummaries():DirectoryResult[ConceptDomainBindingDirectoryEntry] = {
     queryService.getResourceSummaries(null,null,null,null,new Page());
  }

  def createResources(changeSetUri:String):Int = {
    val resources = List(
        "http://Test1", 
        "http://Test2");
    
    resources.foreach(uri => createResource(null,uri,changeSetUri))
    
    resources.size
   }
    
  def createResource(name: String, uri:String, changeSetUri:String) = {
    var entry = new ConceptDomainBinding()
    entry.setBindingURI(uri)
    entry.setChangeableElementGroup(buildChangeableElementGroup(changeSetUri))
    entry.setBindingFor(new ConceptDomainReference())
    entry.getBindingFor().setContent("cd")
    
    entry.setBoundValueSet(new ValueSetReference())
    entry.getBoundValueSet().setContent("vs")
    
    maintService.createResource(entry)
  }

  def getResource(uri: String): LocalIdConceptDomainBinding = {
    var id = new ConceptDomainBindingReadId(uri);
  
    readService.read(id, null)
  }
  
      def getResourceByUri(uri:String):LocalIdConceptDomainBinding = {
           var id = new ConceptDomainBindingReadId(uri);
    	
           readService.read(id, null)
    }

}