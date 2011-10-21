package edu.mayo.cts2.framework.plugin.service.exist.profile.conceptdomainbinding;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.conceptdomainbinding.ConceptDomainBinding;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ConceptDomainBindingExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistService;
import edu.mayo.cts2.framework.service.profile.conceptdomainbinding.ConceptDomainBindingMaintenanceService;

@Component
public class ExistConceptDomainBindingMaintenanceService 
	extends AbstractExistService<edu.mayo.cts2.framework.model.service.conceptdomainbinding.ConceptDomainBindingMaintenanceService> 
	implements ConceptDomainBindingMaintenanceService {

	@Resource
	private ConceptDomainBindingExistDao conceptDomainBindingExistDao;

	@Override
	public void createResource(String changeSetUri,
			ConceptDomainBinding resource) {
		this.conceptDomainBindingExistDao.storeResource(
				this.createPath(resource.getBindingFor().getContent()), 
				resource);
	}

	@Override
	public void updateResource(String changeSetUri,
			ConceptDomainBinding resource) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteResource(String changeSetUri, String resourceName) {
		throw new UnsupportedOperationException();
	}
}
