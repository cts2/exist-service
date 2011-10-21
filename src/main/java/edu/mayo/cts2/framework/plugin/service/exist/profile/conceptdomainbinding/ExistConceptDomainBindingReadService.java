package edu.mayo.cts2.framework.plugin.service.exist.profile.conceptdomainbinding;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.conceptdomainbinding.ConceptDomainBinding;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ConceptDomainBindingExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistReadService;
import edu.mayo.cts2.framework.service.profile.conceptdomainbinding.ConceptDomainBindingReadService;

@Component
public class ExistConceptDomainBindingReadService 
	extends AbstractExistReadService<
		ConceptDomainBinding,
		String,
		edu.mayo.cts2.framework.model.service.conceptdomainbinding.ConceptDomainBindingReadService> 
	implements ConceptDomainBindingReadService {

	@Resource
	private ConceptDomainBindingExistDao conceptDomainBindingExistDao;

	
	@Override
	protected ExistDao<?, ConceptDomainBinding> getExistDao() {
		return this.conceptDomainBindingExistDao;
	}


	@Override
	protected boolean isReadByUri(String identifier) {
		return true;
	}


	@Override
	protected String createPath(String resourceIdentifier) {
		return "";
	}


	@Override
	protected String getResourceName(String resourceIdentifier) {
		throw new UnsupportedOperationException();
	}


	@Override
	protected String getResourceUri(String resourceIdentifier) {
		return resourceIdentifier;
	}
}
