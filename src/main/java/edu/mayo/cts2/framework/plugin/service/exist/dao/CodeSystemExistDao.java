package edu.mayo.cts2.framework.plugin.service.exist.dao;

import org.springframework.stereotype.Component;
import org.xmldb.api.base.Resource;

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry;
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntrySummary;

@Component
public class CodeSystemExistDao extends AbstractResourceExistDao<CodeSystemCatalogEntrySummary, CodeSystemCatalogEntry> {

	private static final String CODESYSTEMS_PATH = "/codesystems";
	
	@Override
	protected String getName(CodeSystemCatalogEntry entry) {
		return entry.getCodeSystemName();
	}

	@Override
	protected CodeSystemCatalogEntrySummary createSummary() {
		return new CodeSystemCatalogEntrySummary();
	}

	@Override
	protected CodeSystemCatalogEntrySummary doTransform(
			CodeSystemCatalogEntry resource,
			CodeSystemCatalogEntrySummary summary, Resource eXistResource) {
		summary = this.baseTransform(summary, resource);
		
		summary.setCodeSystemName(resource.getCodeSystemName());
		summary.setHref(getUrlConstructor().createCodeSystemUrl(resource.getCodeSystemName()));
		summary.setVersions(getUrlConstructor().createVersionsOfCodeSystemUrl(resource.getCodeSystemName()));
		
		return summary;
	}

	@Override
	protected String doGetResourceBasePath() {
		return CODESYSTEMS_PATH;
	}

	@Override
	protected String getResourceXpath() {
		return "/codesystem:CodeSystemCatalogEntry";
	}



	@Override
	protected String getUriXpath() {
		return "@about";
	}
}
