package edu.mayo.cts2.framework.plugin.service.exist.profile.conceptdomain;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.conceptdomain.ConceptDomainCatalogEntry;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ConceptDomainExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistService;
import edu.mayo.cts2.framework.service.profile.conceptdomain.ConceptDomainMaintenanceService;

@Component
public class ExistConceptDomainMaintenanceService 
	extends AbstractExistService<edu.mayo.cts2.framework.model.service.conceptdomain.ConceptDomainMaintenanceService>
	implements ConceptDomainMaintenanceService {

	@Resource
	private ConceptDomainExistDao conceptDomainExistDao;

	@Override
	public void createResource(
			String changeSetUri,
			ConceptDomainCatalogEntry resource) {
		this.conceptDomainExistDao.storeResource("", resource);
	}

	@Override
	public void updateResource(
			String changeSetUri,
			ConceptDomainCatalogEntry resource) {
		this.conceptDomainExistDao.storeResource("", resource);
	}

	@Override
	public void deleteResource(String changeSetUri, String resourceName) {
		throw new UnsupportedOperationException();
	}
}
