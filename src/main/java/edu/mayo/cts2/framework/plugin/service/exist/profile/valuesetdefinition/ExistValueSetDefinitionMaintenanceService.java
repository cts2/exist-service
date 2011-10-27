package edu.mayo.cts2.framework.plugin.service.exist.profile.valuesetdefinition;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinition;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistMaintenanceService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionMaintenanceService;

@Component
public class ExistValueSetDefinitionMaintenanceService 
	extends AbstractExistMaintenanceService<ValueSetDefinition,String,edu.mayo.cts2.framework.model.service.valuesetdefinition.ValueSetDefinitionMaintenanceService>
	implements ValueSetDefinitionMaintenanceService {

	@Resource
	private ValueSetDefinitionResourceInfo valueSetDefinitionResourceInfo;

	@Override
	protected ResourceInfo<ValueSetDefinition, String> getResourceInfo() {
		return this.valueSetDefinitionResourceInfo;
	}

}
