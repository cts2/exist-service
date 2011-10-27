package edu.mayo.cts2.framework.plugin.service.exist.profile.conceptdomainbinding;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.conceptdomainbinding.ConceptDomainBinding;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;

@Component
public class ConceptDomainBindingResourceInfo implements ResourceInfo<ConceptDomainBinding,String> {

	private static final String CONCEPTDOMAINBINDINGS_PATH = "/conceptdomainbindings";
	
	@Override
	public String getResourceBasePath() {
		return CONCEPTDOMAINBINDINGS_PATH;
	}

	@Override
	public String getResourceXpath() {
		return "/conceptdomainbinding:ConceptDomainBinding";
	}
	
	@Override
	public boolean isReadByUri(String identifier) {
		return true;
	}

	@Override
	public String createPath(String resourceIdentifier) {
		return "";
	}

	@Override
	public String createPathFromResource(ConceptDomainBinding resource) {
		return "";
	}

	@Override
	public String getExistResourceName(String resourceIdentifier) {
		throw new UnsupportedOperationException("Cannot reference ConceptDomainBinding by name.");

	}

	@Override
	public String getResourceUri(String id) {
		return id;
	}

	@Override
	public String getExistResourceNameFromResource(ConceptDomainBinding resource) {
		throw new UnsupportedOperationException("Cannot reference ConceptDomainBinding by name.");
	}
	
	@Override
	public String getUriXpath() {
		return "@bindingURI";
	}

}
