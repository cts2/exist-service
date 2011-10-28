package edu.mayo.cts2.framework.plugin.service.exist.profile.association;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.association.Association;
import edu.mayo.cts2.framework.model.updates.ChangeableResourceChoice;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistMaintenanceService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
import edu.mayo.cts2.framework.service.profile.association.AssociationMaintenanceService;
import edu.mayo.cts2.framework.service.profile.association.name.AssociationReadId;

@Component
public class ExistAssociationMaintenanceService 
	extends AbstractExistMaintenanceService<Association,AssociationReadId,edu.mayo.cts2.framework.model.service.association.AssociationMaintenanceService>
	implements AssociationMaintenanceService {

	@Resource
	private AssociationResourceInfo associationResourceInfo;

	@Override
	protected ResourceInfo<Association, AssociationReadId> getResourceInfo() {
		return this.associationResourceInfo;
	}

	@Override
	protected void addResourceToChangeableResourceChoice(
			ChangeableResourceChoice choice, Association resource) {
		choice.setAssociation(resource);
	}

	
}
