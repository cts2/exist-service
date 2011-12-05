package edu.mayo.cts2.framework.plugin.service.exist.profile.valuesetdefinition;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.core.ChangeableElementGroup;
import edu.mayo.cts2.framework.model.extension.LocalIdValueSetDefinition;
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinition;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistLocalIdReadService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionReadService;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.name.ValueSetDefinitionReadId;

@Component
public class ExistValueSetDefinitionReadService 
	extends AbstractExistLocalIdReadService<
		ValueSetDefinition,
		LocalIdValueSetDefinition,
		ValueSetDefinitionReadId,
		edu.mayo.cts2.framework.model.service.valuesetdefinition.ValueSetDefinitionReadService>
	implements ValueSetDefinitionReadService {

	@Resource
	private ValueSetDefinitionResourceInfo valueSetDefinitionResourceInfo;

	@Override
	protected ResourceInfo<ValueSetDefinitionReadId> getResourceInfo() {
		return this.valueSetDefinitionResourceInfo;
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
