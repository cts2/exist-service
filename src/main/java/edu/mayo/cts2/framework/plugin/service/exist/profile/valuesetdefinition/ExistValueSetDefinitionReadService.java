package edu.mayo.cts2.framework.plugin.service.exist.profile.valuesetdefinition;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinition;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistReadService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionReadService;

@Component
public class ExistValueSetDefinitionReadService 
	extends AbstractExistReadService<
		ValueSetDefinition,
		String,
		edu.mayo.cts2.framework.model.service.valuesetdefinition.ValueSetDefinitionReadService>
	implements ValueSetDefinitionReadService {

	@Resource
	private ValueSetDefinitionResourceInfo valueSetDefinitionResourceInfo;

	@Override
	protected ResourceInfo<ValueSetDefinition, String> getResourceInfo() {
		return this.valueSetDefinitionResourceInfo;
	}

}
