package edu.mayo.cts2.framework.plugin.service.exist.profile.conceptdomainbinding;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.extension.LocalIdConceptDomainBinding;
import edu.mayo.cts2.framework.plugin.service.exist.profile.LocalIdResourceInfo;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;
import edu.mayo.cts2.framework.service.profile.conceptdomainbinding.name.ConceptDomainBindingReadId;

@Component
public class ConceptDomainBindingResourceInfo implements LocalIdResourceInfo<LocalIdConceptDomainBinding,ConceptDomainBindingReadId> {

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
	public boolean isReadByUri(ConceptDomainBindingReadId identifier) {
		return !(identifier.getUri() == null);
	}

	@Override
	public String createPath(ConceptDomainBindingReadId id) {
		return ExistServiceUtils.createPath(id.getConceptDomain().getName());
	}

	@Override
	public String createPathFromResource(LocalIdConceptDomainBinding resource) {
		return ExistServiceUtils.createPath(
				resource.getResource().getBindingFor().getContent());
	}

	@Override
	public String getExistResourceName(ConceptDomainBindingReadId resourceIdentifier) {
		return resourceIdentifier.getName();
	}

	@Override
	public String getResourceUri(ConceptDomainBindingReadId id) {
		return id.getUri();
	}
	
	@Override
	public String getUriXpath() {
		return "conceptdomainbinding:bindingURI";
	}

	@Override
	public String getResourceNameXpath() {
		return "conceptdomainbinding:bindingURI";
	}

}
