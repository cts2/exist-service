package edu.mayo.cts2.framework.plugin.service.exist.profile.valuesetdefinition;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.plugin.service.exist.dao.ValueSetDefinitionExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistService;
import edu.mayo.cts2.sdk.model.valuesetdefinition.ValueSetDefinition;
import edu.mayo.cts2.sdk.service.profile.valuesetdefinition.ValueSetDefinitionMaintenanceService;

@Component
public class ExistValueSetDefinitionMaintenanceService 
	extends AbstractExistService<edu.mayo.cts2.sdk.model.service.valuesetdefinition.ValueSetDefinitionMaintenanceService>
	implements ValueSetDefinitionMaintenanceService {

	@Resource
	private ValueSetDefinitionExistDao valueSetDefinitionExistDao;

	@Override
	public void createResource(
			String changeSetUri,
			ValueSetDefinition resource) {
		this.valueSetDefinitionExistDao.storeResource("", resource);
	}

	@Override
	public void updateResource(String changeSetUri,
			ValueSetDefinition resource) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteResource(String changeSetUri, String resourceName) {
		throw new UnsupportedOperationException();
	}
}
