package edu.mayo.cts2.framework.plugin.service.exist.profile.codesystem;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.service.core.ReadContext;
import edu.mayo.cts2.framework.plugin.service.exist.dao.CodeSystemExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistNameOrUriReadService;
import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemReadService;

@Component
public class ExistCodeSystemReadService 
	extends AbstractExistNameOrUriReadService<
		CodeSystemCatalogEntry,
		NameOrURI,
		edu.mayo.cts2.framework.model.service.codesystem.CodeSystemReadService>
	implements CodeSystemReadService {

	@Resource
	private CodeSystemExistDao codeSystemExistDao;

	@Override
	protected String createPath(NameOrURI resourceIdentifier) {
		return "";
	}

	@Override
	protected ExistDao<?, CodeSystemCatalogEntry> getExistDao() {
		return this.codeSystemExistDao;
	}

	@Override
	public CodeSystemCatalogEntry getCodeSystemVersionForCodeSystem(
			String codeSystemName, String tagName, ReadContext readContext) {
		throw new UnsupportedOperationException();
	}
}
