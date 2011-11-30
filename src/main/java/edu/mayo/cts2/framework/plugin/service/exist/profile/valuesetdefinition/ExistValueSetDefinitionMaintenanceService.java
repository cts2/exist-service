package edu.mayo.cts2.framework.plugin.service.exist.profile.valuesetdefinition;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.core.ChangeableElementGroup;
import edu.mayo.cts2.framework.model.extension.LocalIdValueSetDefinition;
import edu.mayo.cts2.framework.model.updates.ChangeableResourceChoice;
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinition;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistLocalIdMaintenanceService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
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
	protected ResourceInfo<LocalIdValueSetDefinition, ValueSetDefinitionReadId> getResourceInfo() {
		return this.valueSetDefinitionResourceInfo;
	}

	@Override
	protected void addResourceToChangeableResourceChoice(
			ChangeableResourceChoice choice, LocalIdValueSetDefinition resource) {
		choice.setValueSetDefinition(resource.getResource());
	}

	@Override
	protected ChangeableElementGroup getChangeableElementGroup(
			LocalIdValueSetDefinition resource) {
		return resource.getResource().getChangeableElementGroup();
	}

	@Override
	protected LocalIdValueSetDefinition createLocalIdResource(String id,
			ValueSetDefinition resource) {
		return new LocalIdValueSetDefinition(id, resource);
	}

}
