package edu.mayo.cts2.framework.plugin.service.exist.integration;

import static org.junit.Assert.*
import edu.mayo.cts2.framework.model.conceptdomainbinding.ConceptDomainBinding
import edu.mayo.cts2.framework.model.conceptdomainbinding.ConceptDomainBindingDirectory
import edu.mayo.cts2.framework.model.core.ConceptDomainReference
import edu.mayo.cts2.framework.model.core.ValueSetReference

class ConceptDomainBindingQueryServiceTestIT extends BaseQueryServiceTestITBase {

	@Override
	getResourceClass() {
		ConceptDomainBindingDirectory.class
	}

	@Override
	getQueryUrl() {
		"conceptdomainbindings"
	}
	
	@Override
	getCreateUrl(){
		"conceptdomainbinding"
	}

	@Override
	List getResources() {
		[createEntry("http://testAbout1"),
			createEntry("http://testAbout2"),
			createEntry("http://testAbout3")]
	}

	def createEntry(uri) {
			def entry = new ConceptDomainBinding(bindingURI:uri)
		entry.setBindingFor(new ConceptDomainReference(content:"testcd"))
		entry.setBoundValueSet(new ValueSetReference(content:"testvs"))
		
		entry
	}
	
}
