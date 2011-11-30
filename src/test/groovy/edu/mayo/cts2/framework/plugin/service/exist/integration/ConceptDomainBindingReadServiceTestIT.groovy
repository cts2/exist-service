package edu.mayo.cts2.framework.plugin.service.exist.integration;

import static org.junit.Assert.*

import org.apache.commons.lang.StringUtils

import edu.mayo.cts2.framework.model.conceptdomainbinding.ConceptDomainBinding
import edu.mayo.cts2.framework.model.conceptdomainbinding.ConceptDomainBindingMsg
import edu.mayo.cts2.framework.model.core.ConceptDomainReference
import edu.mayo.cts2.framework.model.core.ValueSetReference


class ConceptDomainBindingReadServiceTestIT extends BaseReadServiceTestITBase {

	def bindingURI = "http://some/binding/100"

	@Override
	public Object getResourceClass() {
		ConceptDomainBindingMsg.class
	}

	@Override
	public Object getReadByNameUrl() {
		StringUtils.substringBefore(currentResourceUrl.toString(), "?")
	}

	@Override
	public Object getReadByUriUrl() {
		"conceptdomainbindingbyuri?uri="+bindingURI
	}

	@Override
	public Object getCreateUrl() {
		"conceptdomainbinding"
	}

	@Override
	public Object getResource() {
		def entry = new ConceptDomainBinding(bindingURI:bindingURI)
		entry.setBindingFor(new ConceptDomainReference(content:"testcd"))
		entry.setBoundValueSet(new ValueSetReference(content:"testvs"))
		
		entry
	}

	@Override
	resourcesEqual(msg) {
		msg.getConceptDomainBinding().getBindingURI().equals(bindingURI)
	}
	
	
}
