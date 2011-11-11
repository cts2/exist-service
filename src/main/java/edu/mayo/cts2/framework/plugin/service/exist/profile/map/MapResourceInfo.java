package edu.mayo.cts2.framework.plugin.service.exist.profile.map;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.map.MapCatalogEntry;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractNameOrUriResourceInfo;

@Component
public class MapResourceInfo extends AbstractNameOrUriResourceInfo<MapCatalogEntry,NameOrURI> {

	private static final String MAPS_PATH = "/maps";

	@Override
	public String getResourceBasePath() {
		return MAPS_PATH;
	}

	@Override
	public String getResourceXpath() {
		return "/map:MapCatalogEntry";
	}
	
	@Override
	public String createPath(NameOrURI resourceIdentifier) {
		return "";
	}

	@Override
	public String createPathFromResource(MapCatalogEntry resource) {
		return "";
	}

	@Override
	public String getExistResourceNameFromResource(MapCatalogEntry resource) {
		return resource.getMapName();
	}

	@Override
	public String getUriXpath() {
		return "@about";
	}

	@Override
	public String getResourceNameXpath() {
		return "@mapName";
	}
}
