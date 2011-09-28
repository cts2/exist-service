package edu.mayo.cts2.framework.plugin.service.exist.profile.codesystemversion;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry;
import edu.mayo.cts2.framework.plugin.service.exist.dao.CodeSystemVersionExistDao;
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionReadService;

@Component
public class ExistCodeSystemVersionReadService implements CodeSystemVersionReadService {

	@Resource
	private CodeSystemVersionExistDao codeSystemVersionExistDao;

	@Override
	public CodeSystemVersionCatalogEntry read(String resourceName) {
		return this.codeSystemVersionExistDao.getResource("", resourceName);
	}

	@Override
	public boolean exists(String resourceName) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean existsCodeSystemVersionForCodeSystem(String codeSystemName,
			String tagName) {
		throw new UnsupportedOperationException();
	}

	@Override
	public CodeSystemVersionCatalogEntry getCodeSystemVersionForCodeSystem(
			String codeSystemName, String tagName) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean existsVersionId(String codeSystemName,
			String officialResourceVersionId) {
		throw new UnsupportedOperationException();
	}

}
