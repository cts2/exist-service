package edu.mayo.cts2.framework.plugin.service.exist.dao;

import org.springframework.stereotype.Component;
import org.xmldb.api.base.Resource;

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry;
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntrySummary;
import edu.mayo.cts2.framework.model.service.exception.UnknownCodeSystem;
import edu.mayo.cts2.framework.model.service.exception.UnknownResourceReference;

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
	protected Class<? extends UnknownResourceReference> getUnknownResourceExceptionClass() {
		return UnknownCodeSystem.class;
	}
}
