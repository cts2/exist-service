package edu.mayo.cts2.framework.plugin.service.exist.dao;

import org.springframework.stereotype.Component;
import org.xmldb.api.base.Resource;

import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry;
import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntrySummary;
import edu.mayo.cts2.framework.model.service.exception.UnknownCodeSystemVersion;
import edu.mayo.cts2.framework.model.service.exception.UnknownResourceReference;

@Component
public class CodeSystemVersionExistDao
		extends
		AbstractResourceExistDao<CodeSystemVersionCatalogEntrySummary, CodeSystemVersionCatalogEntry> {

	private static final String CODESYSTEMVERSIONS_PATH = "/codesystemversions";

	@Override
	public CodeSystemVersionCatalogEntry getResource(String path, String name) {
		// TODO Auto-generated method stub
		CodeSystemVersionCatalogEntry cs = super.getResource(path, name);

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
	protected Class<? extends UnknownResourceReference> getUnknownResourceExceptionClass() {
		return UnknownCodeSystemVersion.class;
	}

	@Override
	protected String getResourceXpath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getUriXpath() {
		// TODO Auto-generated method stub
		return null;
	}
}
