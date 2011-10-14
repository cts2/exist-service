package edu.mayo.cts2.framework.plugin.service.exist.profile.codesystem;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry;
import edu.mayo.cts2.framework.plugin.service.exist.dao.CodeSystemExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistReadService;
import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemReadService;

@Component
public class ExistCodeSystemReadService 
	extends AbstractExistReadService<CodeSystemCatalogEntry,edu.mayo.cts2.framework.model.service.codesystem.CodeSystemReadService>
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

	@Override
	public CodeSystemCatalogEntry readByUri(String uri) {
		return this.codeSystemExistDao.getResourceByUri(uri);
	}

	@Override
	protected ExistDao<?, CodeSystemCatalogEntry> getExistDao() {
		return this.codeSystemExistDao;
	}
}
