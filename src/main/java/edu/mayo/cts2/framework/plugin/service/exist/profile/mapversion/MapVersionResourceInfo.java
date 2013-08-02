package edu.mayo.cts2.framework.plugin.service.exist.profile.mapversion;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.mapversion.MapVersion;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractNameOrUriResourceInfo;

@Component
public class MapVersionResourceInfo extends AbstractNameOrUriResourceInfo<MapVersion,NameOrURI> {

	private static final String MAPVERSIONS_PATH = "/mapversions";

	@Override
	public String getResourceBasePath() {
		return MAPVERSIONS_PATH;
	}

	@Override
	public String getResourceXpath() {
		return "/mapversion:MapVersion";
	}
	
	@Override
	public String createPath(NameOrURI resourceIdentifier) {
		return "";
	}

	@Override
	public String createPathFromResource(MapVersion resource) {
		return "";
	}

	@Override
	public String getExistResourceNameFromResource(MapVersion resource) {
		return resource.getMapVersionName();
	}

	@Override
	public String getUriXpath() {
		return "@about";
	}

	@Override
	public String getResourceNameXpath() {
		return "@mapVersionName";
	}
	
}
