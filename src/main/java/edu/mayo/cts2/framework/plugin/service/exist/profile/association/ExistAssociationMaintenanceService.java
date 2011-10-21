package edu.mayo.cts2.framework.plugin.service.exist.profile.association;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.association.Association;
import edu.mayo.cts2.framework.plugin.service.exist.dao.AssociationExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistService;
import edu.mayo.cts2.framework.service.profile.association.AssociationMaintenanceService;

@Component
public class ExistAssociationMaintenanceService 
	extends AbstractExistService<edu.mayo.cts2.framework.model.service.association.AssociationMaintenanceService>
	implements AssociationMaintenanceService {

	@Resource
	private AssociationExistDao associationExistDao;

	@Override
	public void createResource(String changeSetUri,
			Association resource) {
		this.associationExistDao.storeResource(
				this.createPath(
						resource.getAssertedBy().getVersion().getContent()),
						resource);
	}

	@Override
	public void updateResource(String changeSetUri,
			Association resource) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteResource(String changeSetUri, String resourceName) {
		throw new UnsupportedOperationException();
	}
}
