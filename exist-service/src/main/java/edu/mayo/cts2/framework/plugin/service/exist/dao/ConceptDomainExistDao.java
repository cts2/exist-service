package edu.mayo.cts2.framework.plugin.service.exist.dao;

import org.springframework.stereotype.Component;
import org.xmldb.api.base.Resource;

import edu.mayo.cts2.sdk.model.conceptdomain.ConceptDomainCatalogEntry;
import edu.mayo.cts2.sdk.model.conceptdomain.ConceptDomainCatalogEntrySummary;
import edu.mayo.cts2.sdk.model.service.exception.UnknownConceptDomain;
import edu.mayo.cts2.sdk.model.service.exception.UnknownResourceReference;

@Component
public class ConceptDomainExistDao extends AbstractResourceExistDao<ConceptDomainCatalogEntrySummary, ConceptDomainCatalogEntry> {

	private static final String CONCEPTDOMAINS_PATH = "/conceptdomains";
	
	@Override
	protected String getName(ConceptDomainCatalogEntry entry) {
		return entry.getConceptDomainName();
	}

	@Override
	protected ConceptDomainCatalogEntrySummary createSummary() {
		return new ConceptDomainCatalogEntrySummary();
	}

	@Override
	protected ConceptDomainCatalogEntrySummary doTransform(
			ConceptDomainCatalogEntry resource,
			ConceptDomainCatalogEntrySummary summary, Resource eXistResource) {
		summary = this.baseTransform(summary, resource);
		
		summary.setConceptDomainName(resource.getConceptDomainName());
			
		return summary;
	}

	@Override
	protected String doGetResourceBasePath() {
		return CONCEPTDOMAINS_PATH;
	}
	
	@Override
	protected Class<? extends UnknownResourceReference> getUnknownResourceExceptionClass() {
		return UnknownConceptDomain.class;
	}
}
