package edu.mayo.cts2.framework.plugin.service.exist.profile.association;

import edu.mayo.cts2.framework.model.association.Association;
import edu.mayo.cts2.framework.model.extension.LocalIdAssociation;
import edu.mayo.cts2.framework.model.updates.ChangeableResource;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistLocalIdMaintenanceService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.LocalIdResourceInfo;
import edu.mayo.cts2.framework.service.profile.association.AssociationMaintenanceService;
import edu.mayo.cts2.framework.service.profile.association.name.AssociationReadId;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ExistAssociationMaintenanceService 
	extends AbstractExistLocalIdMaintenanceService<Association,LocalIdAssociation,AssociationReadId,edu.mayo.cts2.framework.model.service.association.AssociationMaintenanceService>
	implements AssociationMaintenanceService {

	@Resource
	private AssociationResourceInfo associationResourceInfo;

	@Override
	protected LocalIdResourceInfo<LocalIdAssociation, AssociationReadId> getResourceInfo() {
		return this.associationResourceInfo;
	}

    @Override
    protected LocalIdAssociation createLocalIdResource(String id, Association resource) {
        return new LocalIdAssociation(id, resource);
    }

    @Override
    protected void addResourceToChangeableResource(ChangeableResource choice, LocalIdAssociation resource) {
        choice.setAssociation(resource.getResource());
    }

	@Override
	protected Association getResourceFromChangeableResource(
			ChangeableResource choice) {
		return choice.getAssociation();
	}

}
