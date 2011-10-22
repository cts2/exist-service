package edu.mayo.cts2.framework.plugin.service.exist.dao;

import org.springframework.stereotype.Component;
import org.xmldb.api.base.Resource;

import edu.mayo.cts2.framework.model.mapversion.MapEntry;
import edu.mayo.cts2.framework.model.mapversion.MapEntryDirectoryEntry;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;

@Component
public class MapEntryExistDao extends
		AbstractResourceExistDao<MapEntryDirectoryEntry, MapEntry> {

	private static final String MAPENTRIES_PATH = "/mapentries";

	@Override
	public MapEntryDirectoryEntry doTransform(
			MapEntry resource,
			MapEntryDirectoryEntry summary, 
			Resource eXistResource) {
		String mapEntryName = this.getMapEntryNameForSummary(resource);
		
		String mapName = resource.getAssertedBy().getMap().getContent();
		String mapVersionName = resource.getAssertedBy().getMapVersion().getContent();

		summary.setAssertedBy(resource.getAssertedBy());
		
		summary.setMapFrom(resource.getMapFrom());
		summary.setResourceName(mapEntryName);
		summary.setHref(this.getUrlConstructor().createMapEntryUrl(
				mapName, 
				mapVersionName, 
				mapEntryName));

		return summary;
	}
	
	private String getMapEntryNameForSummary(MapEntry mapEntry){
		return mapEntry.getMapFrom().getNamespace() + ":" + mapEntry.getMapFrom().getName();
	}

	@Override
	protected String getName(MapEntry entry) {
		return ExistServiceUtils.getExistEntityName(entry.getMapFrom());
	}

	@Override
	protected String doGetResourceBasePath() {
		return MAPENTRIES_PATH;
	}
	

	@Override
	protected MapEntryDirectoryEntry createSummary() {
		return new MapEntryDirectoryEntry();
	}

	@Override
	protected String getResourceXpath() {
		return "/mapversion:MapEntry";
	}

	@Override
	protected String getUriXpath() {
		return "mapversion:mapFrom/@uri";
	}
}
