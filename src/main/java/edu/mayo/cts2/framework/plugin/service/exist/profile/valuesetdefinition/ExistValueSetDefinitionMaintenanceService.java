package edu.mayo.cts2.framework.plugin.service.exist.profile.valuesetdefinition;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.extension.LocalIdValueSetDefinition;
import edu.mayo.cts2.framework.model.updates.ChangeableResource;
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinition;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistLocalIdMaintenanceService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.LocalIdResourceInfo;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionMaintenanceService;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.name.ValueSetDefinitionReadId;

@Component
public class ExistValueSetDefinitionMaintenanceService 
	extends AbstractExistLocalIdMaintenanceService<
	ValueSetDefinition,
	LocalIdValueSetDefinition,
	ValueSetDefinitionReadId,
	edu.mayo.cts2.framework.model.service.valuesetdefinition.ValueSetDefinitionMaintenanceService>
	implements ValueSetDefinitionMaintenanceService {

	@Resource
	private ValueSetDefinitionResourceInfo valueSetDefinitionResourceInfo;

	@Override
	protected LocalIdResourceInfo<LocalIdValueSetDefinition,ValueSetDefinitionReadId> getResourceInfo() {
		return this.valueSetDefinitionResourceInfo;
	}

	@Override
	protected LocalIdValueSetDefinition createLocalIdResource(String id,
			ValueSetDefinition resource) {
		return new LocalIdValueSetDefinition(id, resource);
	}

	@Override
	protected void addResourceToChangeableResource(
			ChangeableResource choice, LocalIdValueSetDefinition resource) {
		choice.setValueSetDefinition(resource.getResource());
	}

	@Override
	protected String getPathFromResource(LocalIdValueSetDefinition resource) {
		return ExistServiceUtils.createPath(
				resource.getResource().getDefinedValueSet().getContent());
	}

	@Override
	protected ValueSetDefinition getResourceFromChangeableResource(
			ChangeableResource choice) {
		return choice.getValueSetDefinition();
	}

}
