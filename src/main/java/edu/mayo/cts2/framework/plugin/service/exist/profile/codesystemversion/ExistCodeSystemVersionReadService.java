package edu.mayo.cts2.framework.plugin.service.exist.profile.codesystemversion;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.service.core.ReadContext;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistReadService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
import edu.mayo.cts2.framework.service.command.ResolvedReadContext;
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionReadService;

@Component
public class ExistCodeSystemVersionReadService 
	extends AbstractExistReadService<
		CodeSystemVersionCatalogEntry,
		NameOrURI,
		edu.mayo.cts2.framework.model.service.codesystemversion.CodeSystemVersionReadService>
	implements CodeSystemVersionReadService {

	@Resource
	private CodeSystemVersionResourceInfo codeSystemVersionResourceInfo;

	@Override
	public boolean existsCodeSystemVersionForCodeSystem(
			NameOrURI codeSystem,
			String tagName) {
		throw new UnsupportedOperationException();
	}	
	
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

		return cs;
	}

	@Override
	public CodeSystemVersionCatalogEntry getCodeSystemVersionForCodeSystem(
			NameOrURI codeSystem, 
			String tagName) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean existsVersionId(
			NameOrURI codeSystem,
			String officialResourceVersionId) {
		throw new UnsupportedOperationException();
	}

	@Override
	public CodeSystemVersionCatalogEntry getCodeSystemVersionForCodeSystem(
			NameOrURI codeSystem, 
			String tagName, 
			ReadContext readContext) {
		throw new UnsupportedOperationException();
	}
	
	public CodeSystemVersionCatalogEntry getCodeSystemVersionByOfficialResourceId(
			String codeSystemName, 
			String officialResourceId) {
		org.xmldb.api.base.Resource resource =
				this.getExistResourceDao().getResourceByXpath(
						this.getResourceInfo().getResourceBasePath(),
						this.getResourceInfo().getResourceXpath() +
						"[core:officialResourceVersionId[text() &= (\""+officialResourceId+"\")] and " +
						"codesystemversion:versionOf[text() &= (\""+codeSystemName+"\")]]");
		
		return (CodeSystemVersionCatalogEntry) this.getResourceUnmarshaller().unmarshallResource(resource);
	}


	@Override
	public CodeSystemVersionCatalogEntry getCodeSystemByVersionId(
			NameOrURI codeSystem, 
			String officialResourceVersionId,
			ReadContext readContext) {
		
		return this.
				getCodeSystemVersionByOfficialResourceId(
						codeSystem.getName(),
						officialResourceVersionId);
	}

	@Override
	protected ResourceInfo<CodeSystemVersionCatalogEntry, NameOrURI> getResourceInfo() {
		return this.codeSystemVersionResourceInfo;
	}
}
