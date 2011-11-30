package edu.mayo.cts2.framework.plugin.service.exist.integration;

import static org.junit.Assert.*

import org.junit.Test

import edu.mayo.cts2.framework.model.conceptdomain.ConceptDomainCatalogEntry
import edu.mayo.cts2.framework.model.conceptdomain.ConceptDomainCatalogEntryMsg


class ConceptDomainReadServiceTestIT extends BaseReadServiceTestITBase {
			
	@Override
	public Object getResourceClass() {
		ConceptDomainCatalogEntryMsg.class
	}

	@Override
	public Object getReadByNameUrl() {
		"conceptdomain/TESTDOMAIN"
	}

	@Override
	public Object getReadByUriUrl() {
		"conceptdomainbyuri?uri=http://about"
	}

	@Override
	public Object getCreateUrl() {
		"conceptdomain"
	}

	@Override
	public Object getResource() {
		new ConceptDomainCatalogEntry(conceptDomainName:"TESTDOMAIN", about:"http://about")
	}

	@Override
	public Object resourcesEqual(Object resource) {
		resource.getConceptDomainCatalogEntry().getConceptDomainName().equals("TESTDOMAIN")
	}

}
