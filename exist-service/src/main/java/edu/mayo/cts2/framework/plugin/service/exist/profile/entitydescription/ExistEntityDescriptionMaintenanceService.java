package edu.mayo.cts2.framework.plugin.service.exist.profile.entitydescription;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.entity.EntityDescription;
import edu.mayo.cts2.framework.model.entity.EntityDescriptionBase;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.plugin.service.exist.dao.EntityDescriptionExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistService;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionMaintenanceService;

@Component
public class ExistEntityDescriptionMaintenanceService
		extends
		AbstractExistService<edu.mayo.cts2.framework.model.service.entitydescription.EntityDescriptionMaintenanceService>
		implements EntityDescriptionMaintenanceService {

	@Resource
	private EntityDescriptionExistDao entityDescriptionExistDao;

	@Override
	public void createResource(String changeSetUri, EntityDescription resource) {
		EntityDescriptionBase base = ModelUtils.getEntity(resource);

		this.entityDescriptionExistDao.storeResource(
				this.createPath(base.
							getDescribingCodeSystemVersion().
							getVersion().
							getContent()), 
							resource);
	}

	@Override
	public void updateResource(String changeSetUri, EntityDescription resource) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteResource(String changeSetUri, String resourceName) {
		// TODO Auto-generated method stub

	}
}
