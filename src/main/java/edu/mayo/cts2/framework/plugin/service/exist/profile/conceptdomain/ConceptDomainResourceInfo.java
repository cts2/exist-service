package edu.mayo.cts2.framework.plugin.service.exist.profile.conceptdomain;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.conceptdomain.ConceptDomainCatalogEntry;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractNameOrUriResourceInfo;

@Component
public class ConceptDomainResourceInfo extends AbstractNameOrUriResourceInfo<ConceptDomainCatalogEntry,NameOrURI> {

	private static final String CONCEPTDOMAINS_PATH = "/conceptdomains";
	
	@Override
	public String getResourceBasePath() {
		return CONCEPTDOMAINS_PATH;
	}

	@Override
	public String getResourceXpath() {
		return "/conceptdomain:ConceptDomainCatalogEntry";
	}
	
	@Override
	public String createPath(NameOrURI resourceIdentifier) {
		return "";
	}

	@Override
	public String createPathFromResource(ConceptDomainCatalogEntry resource) {
		return "";
	}

	@Override
	public String getExistResourceNameFromResource(ConceptDomainCatalogEntry resource) {
		return resource.getConceptDomainName();
	}

	@Override
	public String getUriXpath() {
		return "@about";
	}

	@Override
	public String getResourceNameXpath() {
		return "@conceptDomainName";
	}
}
