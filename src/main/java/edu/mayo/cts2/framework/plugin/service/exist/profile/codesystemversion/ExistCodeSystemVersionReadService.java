package edu.mayo.cts2.framework.plugin.service.exist.profile.codesystemversion;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.VersionTagReference;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistDefaultReadService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.DefaultResourceInfo;
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionReadService;

@Component
public class ExistCodeSystemVersionReadService 
	extends AbstractExistDefaultReadService<
		CodeSystemVersionCatalogEntry,
		NameOrURI,
		edu.mayo.cts2.framework.model.service.codesystemversion.CodeSystemVersionReadService>
	implements CodeSystemVersionReadService {

	@Resource
	private CodeSystemVersionResourceInfo codeSystemVersionResourceInfo;
	
	@Override
	public CodeSystemVersionCatalogEntry read(NameOrURI identifier, ResolvedReadContext readContext){

		CodeSystemVersionCatalogEntry cs = super.read(identifier, readContext);
		
		if(cs == null){
			return null;
		}

		String codeSystemName = cs.getVersionOf().getContent();
		String codeSystemVersionName = cs.getCodeSystemVersionName();

		cs.setEntityDescriptions(this.getUrlConstructor()
				.createEntitiesOfCodeSystemVersionUrl(codeSystemName,
						codeSystemVersionName));

		cs.setAssociations(this.getUrlConstructor()
				.createAssociationsOfCodeSystemVersionUrl(codeSystemName,
						codeSystemVersionName));
		
		if(StringUtils.isBlank(cs.getVersionOf().getHref())){
			cs.getVersionOf().setHref(
					this.getUrlConstructor().createCodeSystemUrl(codeSystemName));
		}

		return cs;
	}

	@Override
	public boolean existsVersionId(
			NameOrURI codeSystem,
			String officialResourceVersionId) {
		throw new UnsupportedOperationException();
	}
	
	public CodeSystemVersionCatalogEntry readByTag(NameOrURI parentId, VersionTagReference tag, ResolvedReadContext readContext) {
		return null;
	}
		
	public boolean existsByTag(NameOrURI parentId, VersionTagReference tag, ResolvedReadContext readContext) {
		throw new UnsupportedOperationException();
	}

	@Override
	public CodeSystemVersionCatalogEntry getCodeSystemByVersionId(
			NameOrURI codeSystemName, 
			String officialResourceVersionId,
			ResolvedReadContext readContext) {
		
		String xpath = "[core:officialResourceVersionId = '" + officialResourceVersionId + "' and " +
				"codesystemversion:versionOf = '"+codeSystemName.getName()+"']";
		
		return this.readByXpath("", xpath, readContext);
	}

	@Override
	protected DefaultResourceInfo<CodeSystemVersionCatalogEntry, NameOrURI> getResourceInfo() {
		return this.codeSystemVersionResourceInfo;
	}
}
