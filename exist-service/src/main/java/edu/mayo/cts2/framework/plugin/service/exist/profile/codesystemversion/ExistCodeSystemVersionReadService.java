package edu.mayo.cts2.framework.plugin.service.exist.profile.codesystemversion;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.plugin.service.exist.dao.CodeSystemVersionExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistNameOrUriReadService;
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionReadService;

@Component
public class ExistCodeSystemVersionReadService 
	extends AbstractExistNameOrUriReadService<
		CodeSystemVersionCatalogEntry,
		NameOrURI,
		edu.mayo.cts2.framework.model.service.codesystemversion.CodeSystemVersionReadService>
	implements CodeSystemVersionReadService {

	@Resource
	private CodeSystemVersionExistDao codeSystemVersionExistDao;


	@Override
	protected String createPath(NameOrURI resourceIdentifier) {
		return "";
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

	@Override
	protected ExistDao<?, CodeSystemVersionCatalogEntry> getExistDao() {
		return this.codeSystemVersionExistDao;
	}
}
