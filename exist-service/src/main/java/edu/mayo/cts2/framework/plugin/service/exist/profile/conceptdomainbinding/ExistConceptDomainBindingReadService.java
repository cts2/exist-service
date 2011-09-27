package edu.mayo.cts2.framework.plugin.service.exist.profile.conceptdomainbinding;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.plugin.service.exist.dao.ConceptDomainBindingExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistService;
import edu.mayo.cts2.sdk.model.conceptdomainbinding.ConceptDomainBinding;
import edu.mayo.cts2.sdk.service.profile.conceptdomainbinding.ConceptDomainBindingReadService;
import edu.mayo.cts2.sdk.service.profile.conceptdomainbinding.id.ConceptDomainBindingId;

@Component
public class ExistConceptDomainBindingReadService 
	extends AbstractExistService<edu.mayo.cts2.sdk.model.service.conceptdomainbinding.ConceptDomainBindingReadService> 
	implements ConceptDomainBindingReadService {

	@Resource
	private ConceptDomainBindingExistDao conceptDomainBindingExistDao;

	@Override
	public ConceptDomainBinding read(ConceptDomainBindingId identifier) {
		return this.conceptDomainBindingExistDao.getResource(
				this.createPath(identifier.getConceptDomain()), 
				identifier.getConceptDomainBinding());
	}

	@Override
	public boolean exists(ConceptDomainBindingId identifier) {
		throw new UnsupportedOperationException();
	}
}
