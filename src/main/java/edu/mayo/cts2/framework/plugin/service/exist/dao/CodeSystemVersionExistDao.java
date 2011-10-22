package edu.mayo.cts2.framework.plugin.service.exist.dao;

import org.springframework.stereotype.Component;
import org.xmldb.api.base.Resource;

import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry;
import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntrySummary;

@Component
public class CodeSystemVersionExistDao
		extends
		AbstractResourceExistDao<CodeSystemVersionCatalogEntrySummary, CodeSystemVersionCatalogEntry> {

	private static final String CODESYSTEMVERSIONS_PATH = "/codesystemversions";

	@Override
	public CodeSystemVersionCatalogEntry getResource(String path, String name) {
		// TODO Auto-generated method stub
		CodeSystemVersionCatalogEntry cs = super.getResource(path, name);
		
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
	
	public CodeSystemVersionCatalogEntry getCodeSystemVersionByOfficialResourceId(String codeSystemName, String officialResourceId) {
		return this.getResourceByXpath(
				"", 
				this.getResourceXpath() + "[core:officialResourceVersionId[text() &= (\""+officialResourceId+"\")] and " +
						"codesystemversion:versionOf[text() &= (\""+codeSystemName+"\")]]");
	}

	@Override
	protected String getName(CodeSystemVersionCatalogEntry entry) {
		return entry.getCodeSystemVersionName();
	}

	@Override
	protected String doGetResourceBasePath() {
		return CODESYSTEMVERSIONS_PATH;
	}

	@Override
	protected CodeSystemVersionCatalogEntrySummary createSummary() {
		return new CodeSystemVersionCatalogEntrySummary();
	}

	@Override
	protected CodeSystemVersionCatalogEntrySummary doTransform(
			CodeSystemVersionCatalogEntry resource,
			CodeSystemVersionCatalogEntrySummary summary, Resource eXistResource) {
		summary = this.baseTransform(summary, resource);
		summary.setDocumentURI(resource.getDocumentURI());
		summary.setCodeSystemVersionName(resource.getCodeSystemVersionName());

		summary.setHref(getUrlConstructor().createCodeSystemVersionUrl(
				resource.getVersionOf().getContent(),
				resource.getCodeSystemVersionName()));
		summary.setVersionOf(resource.getVersionOf());

		return summary;
	}

	@Override
	protected String getResourceXpath() {
		return "/codesystemversion:CodeSystemVersionCatalogEntry";
	}

	@Override
	protected String getUriXpath() {
		return "@documentURI";
	}
}
