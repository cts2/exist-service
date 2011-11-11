package edu.mayo.cts2.framework.plugin.service.exist.profile.valueset;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntry;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractNameOrUriResourceInfo;

@Component
public class ValueSetResourceInfo extends AbstractNameOrUriResourceInfo<ValueSetCatalogEntry,NameOrURI> {

	private static final String VALUESETS_PATH = "/valuesets";

	@Override
	public String getResourceBasePath() {
		return VALUESETS_PATH;
	}

	@Override
	public String getResourceXpath() {
		return "/valueset:ValueSetCatalogEntry";
	}
	
	@Override
	public String createPath(NameOrURI resourceIdentifier) {
		return "";
	}

	@Override
	public String createPathFromResource(ValueSetCatalogEntry resource) {
		return "";
	}

	@Override
	public String getExistResourceNameFromResource(ValueSetCatalogEntry resource) {
		return resource.getValueSetName();
	}

	@Override
	public String getUriXpath() {
		return "@about";
	}

	@Override
	public String getResourceNameXpath() {
		return "@valueSetName";
	}

}
