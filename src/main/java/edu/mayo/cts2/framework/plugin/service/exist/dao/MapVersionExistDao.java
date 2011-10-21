package edu.mayo.cts2.framework.plugin.service.exist.dao;

import org.springframework.stereotype.Component;
import org.xmldb.api.base.Resource;

import edu.mayo.cts2.framework.model.mapversion.MapVersion;
import edu.mayo.cts2.framework.model.mapversion.MapVersionDirectoryEntry;
import edu.mayo.cts2.framework.model.service.exception.UnknownMapVersion;
import edu.mayo.cts2.framework.model.service.exception.UnknownResourceReference;

@Component
public class MapVersionExistDao extends
		AbstractResourceExistDao<MapVersionDirectoryEntry, MapVersion> {

	private static final String MAPVERSIONS_PATH = "/mapversions";

	@Override
	public MapVersionDirectoryEntry doTransform(MapVersion resource,
			MapVersionDirectoryEntry summary, Resource eXistResource) {
		summary = this.baseTransform(summary, resource);
		summary.setMapVersionName(resource.getMapVersionName());
		summary.setHref(getUrlConstructor().createMapUrl(resource.getMapVersionName()));

		return summary;
	}

	@Override
	protected String getName(MapVersion entry) {
		return entry.getMapVersionName();
	}

	@Override
	protected String doGetResourceBasePath() {
		return MAPVERSIONS_PATH;
	}

	@Override
	protected MapVersionDirectoryEntry createSummary() {
		return new MapVersionDirectoryEntry();
	}

	@Override
	protected Class<? extends UnknownResourceReference> getUnknownResourceExceptionClass() {
		return UnknownMapVersion.class;
	}

	@Override
	protected String getResourceXpath() {
		return "/mapversion:MapVersion";
	}

	@Override
	protected String getUriXpath() {
		return "@documentURI";
	}
	
}
