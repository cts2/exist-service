package edu.mayo.cts2.framework.plugin.service.exist.profile.codesystemversion;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.service.core.ReadContext;
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
	public boolean existsCodeSystemVersionForCodeSystem(
			NameOrURI codeSystem,
			String tagName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CodeSystemVersionCatalogEntry getCodeSystemVersionForCodeSystem(
			NameOrURI codeSystem, 
			String tagName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean existsVersionId(
			NameOrURI codeSystem,
			String officialResourceVersionId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CodeSystemVersionCatalogEntry getCodeSystemVersionForCodeSystem(
			NameOrURI codeSystem, 
			String tagName, 
			ReadContext readContext) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CodeSystemVersionCatalogEntry getCodeSystemByVersionId(
			NameOrURI codeSystem, 
			String officialResourceVersionId,
			ReadContext readContext) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ExistDao<?, CodeSystemVersionCatalogEntry> getExistDao() {
		return this.codeSystemVersionExistDao;
	}
}
