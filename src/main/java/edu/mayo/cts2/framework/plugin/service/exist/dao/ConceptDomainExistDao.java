package edu.mayo.cts2.framework.plugin.service.exist.dao;

import org.springframework.stereotype.Component;
import org.xmldb.api.base.Resource;

import edu.mayo.cts2.framework.model.conceptdomain.ConceptDomainCatalogEntry;
import edu.mayo.cts2.framework.model.conceptdomain.ConceptDomainCatalogEntrySummary;

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
	protected String getResourceXpath() {
		return "/conceptdomain:ConceptDomainCatalogEntry";
	}

	@Override
	protected String getUriXpath() {
		return "@about";
	}
}
