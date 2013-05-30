package edu.mayo.cts2.framework.plugin.service.exist.profile.entitydescription;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.entity.EntityDescription;
import edu.mayo.cts2.framework.model.updates.ChangeableResource;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistDefaultMaintenanceService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.DefaultResourceInfo;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionMaintenanceService;
import edu.mayo.cts2.framework.service.profile.entitydescription.name.EntityDescriptionReadId;

@Component
public class ExistEntityDescriptionMaintenanceService
		extends
		AbstractExistDefaultMaintenanceService<EntityDescription,EntityDescriptionReadId,edu.mayo.cts2.framework.model.service.entitydescription.EntityDescriptionMaintenanceService>
		implements EntityDescriptionMaintenanceService {

	@Resource
	private EntityDescriptionResourceInfo entityDescriptionResourceInfo;


	@Override
	protected DefaultResourceInfo<EntityDescription, EntityDescriptionReadId> getResourceInfo() {
		return this.entityDescriptionResourceInfo;
	}


	@Override
	protected void addResourceToChangeableResource(
			ChangeableResource choice, EntityDescription resource) {
		choice.setEntityDescription(resource);
	}

	@Override
	protected EntityDescription getResourceFromChangeableResource(
			ChangeableResource choice) {
		return choice.getEntityDescription();
	}

}
