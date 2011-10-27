package edu.mayo.cts2.framework.plugin.service.exist.profile.conceptdomainbinding;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.conceptdomainbinding.ConceptDomainBinding;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistMaintenanceService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
import edu.mayo.cts2.framework.service.profile.conceptdomainbinding.ConceptDomainBindingMaintenanceService;

@Component
public class ExistConceptDomainBindingMaintenanceService 
	extends AbstractExistMaintenanceService<ConceptDomainBinding,String,edu.mayo.cts2.framework.model.service.conceptdomainbinding.ConceptDomainBindingMaintenanceService> 
	implements ConceptDomainBindingMaintenanceService {

	@Resource
	private ConceptDomainBindingResourceInfo conceptDomainBindingResourceInfo;

	@Override
	protected ResourceInfo<ConceptDomainBinding, String> getResourceInfo() {
		return this.conceptDomainBindingResourceInfo;
	}

}
