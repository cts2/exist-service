package edu.mayo.cts2.framework.plugin.service.exist.profile.conceptdomainbinding;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.conceptdomainbinding.ConceptDomainBinding;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ConceptDomainBindingExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistReadService;
import edu.mayo.cts2.framework.service.name.Name;
import edu.mayo.cts2.framework.service.profile.conceptdomainbinding.ConceptDomainBindingReadService;

@Component
public class ExistConceptDomainBindingReadService 
	extends AbstractExistReadService<
		ConceptDomainBinding,
		Name,
		edu.mayo.cts2.framework.model.service.conceptdomainbinding.ConceptDomainBindingReadService> 
	implements ConceptDomainBindingReadService {

	@Resource
	private ConceptDomainBindingExistDao conceptDomainBindingExistDao;

	@Override
	public ConceptDomainBinding read(Name identifier) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean exists(Name identifier) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected ExistDao<?, ConceptDomainBinding> getExistDao() {
		return this.conceptDomainBindingExistDao;
	}
}
