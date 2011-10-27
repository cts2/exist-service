package edu.mayo.cts2.framework.plugin.service.exist.profile.entitydescription;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.entity.EntityDescription;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistMaintenanceService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionMaintenanceService;
import edu.mayo.cts2.framework.service.profile.entitydescription.name.EntityDescriptionReadId;

@Component
public class ExistEntityDescriptionMaintenanceService
		extends
		AbstractExistMaintenanceService<EntityDescription,EntityDescriptionReadId,edu.mayo.cts2.framework.model.service.entitydescription.EntityDescriptionMaintenanceService>
		implements EntityDescriptionMaintenanceService {

	@Resource
	private EntityDescriptionResourceInfo entityDescriptionResourceInfo;


	@Override
	protected ResourceInfo<EntityDescription, EntityDescriptionReadId> getResourceInfo() {
		return this.entityDescriptionResourceInfo;
	}

}
