package edu.mayo.cts2.framework.plugin.service.exist.profile.codesystem;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.plugin.service.exist.dao.CodeSystemExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistService;
import edu.mayo.cts2.sdk.model.codesystem.CodeSystemCatalogEntry;
import edu.mayo.cts2.sdk.service.profile.codesystem.CodeSystemReadService;

@Component
public class ExistCodeSystemReadService 
	extends AbstractExistService<edu.mayo.cts2.sdk.model.service.codesystem.CodeSystemReadService>
	implements CodeSystemReadService {

	@Resource
	private CodeSystemExistDao codeSystemExistDao;

	@Override
	public CodeSystemCatalogEntry read(String resourceName) {
		return this.codeSystemExistDao.getResource("", resourceName);
	}

	@Override
	public boolean exists(String resourceName) {
		throw new UnsupportedOperationException();
	}
}
