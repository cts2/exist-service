package edu.mayo.cts2.framework.plugin.service.exist.profile.conceptdomainbinding;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.conceptdomainbinding.ConceptDomainBinding;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistReadService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
import edu.mayo.cts2.framework.service.profile.conceptdomainbinding.ConceptDomainBindingReadService;

@Component
public class ExistConceptDomainBindingReadService 
	extends AbstractExistReadService<
		ConceptDomainBinding,
		String,
		edu.mayo.cts2.framework.model.service.conceptdomainbinding.ConceptDomainBindingReadService> 
	implements ConceptDomainBindingReadService {

	@Resource
	private ConceptDomainBindingResourceInfo conceptDomainBindingResourceInfo;


	@Override
	protected ResourceInfo<ConceptDomainBinding, String> getResourceInfo() {
		return this.conceptDomainBindingResourceInfo;
	}

}
