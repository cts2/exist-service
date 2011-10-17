package edu.mayo.cts2.framework.plugin.service.exist.profile.valuesetdefinition;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinition;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ValueSetDefinitionExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistReadService;
import edu.mayo.cts2.framework.service.name.Name;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionReadService;

@Component
public class ExistValueSetDefinitionReadService 
	extends AbstractExistReadService<
		ValueSetDefinition,
		Name,
		edu.mayo.cts2.framework.model.service.valuesetdefinition.ValueSetDefinitionReadService>
	implements ValueSetDefinitionReadService {

	@Resource
	private ValueSetDefinitionExistDao valueSetDefinitionExistDao;

	@Override
	public ValueSetDefinition read(Name identifier) {
		return this.valueSetDefinitionExistDao.getResource("", identifier.getResourceId());
	}

	@Override
	public boolean exists(Name identifier) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected ExistDao<?, ValueSetDefinition> getExistDao() {
		return this.valueSetDefinitionExistDao;
	}
}
