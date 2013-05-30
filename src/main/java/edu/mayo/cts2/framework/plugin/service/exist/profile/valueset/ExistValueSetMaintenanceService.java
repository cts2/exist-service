package edu.mayo.cts2.framework.plugin.service.exist.profile.valueset;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.updates.ChangeableResource;
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntry;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistDefaultMaintenanceService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.DefaultResourceInfo;
import edu.mayo.cts2.framework.service.profile.valueset.ValueSetMaintenanceService;

@Component
public class ExistValueSetMaintenanceService 
	extends AbstractExistDefaultMaintenanceService<ValueSetCatalogEntry,NameOrURI,edu.mayo.cts2.framework.model.service.valueset.ValueSetMaintenanceService>
	implements ValueSetMaintenanceService {

	@Resource
	private ValueSetResourceInfo valueSetResourceInfo;

	@Override
	protected DefaultResourceInfo<ValueSetCatalogEntry, NameOrURI> getResourceInfo() {
		return this.valueSetResourceInfo;
	}

	@Override
	protected void addResourceToChangeableResource(
			ChangeableResource choice, ValueSetCatalogEntry resource) {
		choice.setValueSet(resource);
	}

	@Override
	protected ValueSetCatalogEntry getResourceFromChangeableResource(
			ChangeableResource choice) {
		return choice.getValueSet();
	}

}
