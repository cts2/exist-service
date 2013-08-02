package edu.mayo.cts2.framework.plugin.service.exist.profile.codesystemversion;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractNameOrUriResourceInfo;

@Component
public class CodeSystemVersionResourceInfo extends AbstractNameOrUriResourceInfo<CodeSystemVersionCatalogEntry,NameOrURI> {

	private static final String CODESYSTEMVERSIONS_PATH = "/codesystemversions";

	@Override
	public String getResourceBasePath() {
		return CODESYSTEMVERSIONS_PATH;
	}

	@Override
	public String getResourceXpath() {
		return "/codesystemversion:CodeSystemVersionCatalogEntry";
	}
	
	@Override
	public String createPath(NameOrURI resourceIdentifier) {
		return "";
	}

	@Override
	public String createPathFromResource(CodeSystemVersionCatalogEntry resource) {
		return "";
	}
	
	@Override
	public String getExistResourceNameFromResource(CodeSystemVersionCatalogEntry resource) {
		return resource.getCodeSystemVersionName();
	}
	
	@Override
	public String getUriXpath() {
		return "@about";
	}

	@Override
	public String getResourceNameXpath() {
		return "@codeSystemVersionName";
	}

}
