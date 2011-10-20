package edu.mayo.cts2.framework.plugin.service.exist.dao;

import org.springframework.stereotype.Component;
import org.xmldb.api.base.Resource;

import edu.mayo.cts2.framework.model.conceptdomainbinding.ConceptDomainBinding;
import edu.mayo.cts2.framework.model.conceptdomainbinding.ConceptDomainBindingDirectoryEntry;
import edu.mayo.cts2.framework.model.service.exception.UnknownConceptDomainBinding;
import edu.mayo.cts2.framework.model.service.exception.UnknownResourceReference;

@Component
public class ConceptDomainBindingExistDao extends AbstractResourceExistDao<ConceptDomainBindingDirectoryEntry, ConceptDomainBinding> {

	private static final String CONCEPTDOMAINBINDINGS_PATH = "/conceptdomainbindings";
	
	@Override
	protected String getName(ConceptDomainBinding entry) {
		return null;
	}

	@Override
	protected String doGetResourceBasePath() {
		return CONCEPTDOMAINBINDINGS_PATH;
	}

	@Override
	protected ConceptDomainBindingDirectoryEntry createSummary() {
		return new ConceptDomainBindingDirectoryEntry();
	}

	@Override
	protected ConceptDomainBindingDirectoryEntry doTransform(
			ConceptDomainBinding resource,
			ConceptDomainBindingDirectoryEntry summary, Resource eXistResource) {

		return summary;
	}

	@Override
	protected Class<? extends UnknownResourceReference> getUnknownResourceExceptionClass() {
		return UnknownConceptDomainBinding.class;
	}

	@Override
	protected String getResourceXpath() {
		return "/conceptdomainbinding:ConceptDomainBinding";
	}

	@Override
	protected String getUriXpath() {
		return "@bindingURI";
	}
}
