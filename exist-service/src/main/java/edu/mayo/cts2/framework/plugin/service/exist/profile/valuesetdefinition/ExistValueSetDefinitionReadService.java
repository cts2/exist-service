package edu.mayo.cts2.framework.plugin.service.exist.profile.valuesetdefinition;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinition;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ValueSetDefinitionExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistReadService;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionReadService;

@Component
public class ExistValueSetDefinitionReadService 
	extends AbstractExistReadService<
		ValueSetDefinition,
		String,
		edu.mayo.cts2.framework.model.service.valuesetdefinition.ValueSetDefinitionReadService>
	implements ValueSetDefinitionReadService {

	@Resource
	private ValueSetDefinitionExistDao valueSetDefinitionExistDao;


	@Override
	protected ExistDao<?, ValueSetDefinition> getExistDao() {
		return this.valueSetDefinitionExistDao;
	}


	@Override
	protected boolean isReadByUri(String identifier) {
		return true;
	}


	@Override
	protected String createPath(String resourceIdentifier) {
		return "";
	}


	@Override
	protected String getResourceName(String resourceIdentifier) {
		throw new UnsupportedOperationException();
	}


	@Override
	protected String getResourceUri(String resourceIdentifier) {
		return resourceIdentifier;
	}
}
