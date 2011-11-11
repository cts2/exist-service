package edu.mayo.cts2.framework.plugin.service.exist.profile.codesystem;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractNameOrUriResourceInfo;

@Component
public class CodeSystemResourceInfo extends AbstractNameOrUriResourceInfo<CodeSystemCatalogEntry,NameOrURI> {

	private static final String CODESYSTEMS_PATH = "/codesystems";

	@Override
	public String getResourceBasePath() {
		return CODESYSTEMS_PATH;
	}

	@Override
	public String getResourceXpath() {
		return "/codesystem:CodeSystemCatalogEntry";
	}
	
	@Override
	public String createPath(NameOrURI resourceIdentifier) {
		return "";
	}

	@Override
	public String createPathFromResource(CodeSystemCatalogEntry resource) {
		return "";
	}

	@Override
	public String getExistResourceNameFromResource(CodeSystemCatalogEntry resource) {
		return resource.getCodeSystemName();
	}

	@Override
	public String getUriXpath() {
		return "@about";
	}

	@Override
	public String getResourceNameXpath() {
		return "@codeSystemName";
	}

}
