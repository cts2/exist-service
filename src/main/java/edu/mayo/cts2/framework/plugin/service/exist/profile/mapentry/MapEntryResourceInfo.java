package edu.mayo.cts2.framework.plugin.service.exist.profile.mapentry;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.mapversion.MapEntry;
import edu.mayo.cts2.framework.plugin.service.exist.profile.DefaultResourceInfo;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;
import edu.mayo.cts2.framework.service.profile.mapentry.name.MapEntryReadId;

@Component
public class MapEntryResourceInfo implements DefaultResourceInfo<MapEntry,MapEntryReadId> {

	private static final String MAPENTRIES_PATH = "/mapentries";

	@Override
	public String getResourceBasePath() {
		return MAPENTRIES_PATH;
	}

	@Override
	public String getResourceXpath() {
		return "/mapversion:MapEntry";
	}

	@Override
	public boolean isReadByUri(MapEntryReadId identifier) {
		return StringUtils.isNotBlank(identifier.getUri());
	}

	@Override
	public String createPath(MapEntryReadId id) {
		return ExistServiceUtils.createPath(id.getMapVersion().getName());
	}

	@Override
	public String createPathFromResource(MapEntry resource) {
		String path = resource.getAssertedBy().getMapVersion().getContent();
		
		return ExistServiceUtils.createPath(path);
	}

	@Override
	public String getExistResourceName(MapEntryReadId id) {
		return ExistServiceUtils.getExistEntityName(id.getEntityName());
	}

	@Override
	public String getResourceUri(MapEntryReadId id) {
		return id.getUri();
	}

	@Override
	public String getExistResourceNameFromResource(MapEntry entry) {
		return ExistServiceUtils.getExistEntityName(entry.getMapFrom());
	}
	
	@Override
	public String getUriXpath() {
		return "mapversion:mapFrom/@uri";
	}

	@Override
	public String getResourceNameXpath() {
		return "mapversion:mapFrom/core:name";
	}
}
