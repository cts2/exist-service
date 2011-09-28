package edu.mayo.cts2.framework.plugin.service.exist.dao;

import org.springframework.stereotype.Component;
import org.xmldb.api.base.Resource;

import edu.mayo.cts2.framework.model.map.MapCatalogEntry;
import edu.mayo.cts2.framework.model.map.MapCatalogEntrySummary;
import edu.mayo.cts2.framework.model.service.exception.UnknownMap;
import edu.mayo.cts2.framework.model.service.exception.UnknownResourceReference;

@Component
public class MapExistDao extends
		AbstractResourceExistDao<MapCatalogEntrySummary, MapCatalogEntry> {

	private static final String MAPS_PATH = "/maps";

	@Override
	public MapCatalogEntrySummary doTransform(MapCatalogEntry resource,
			MapCatalogEntrySummary summary, Resource eXistResource) {
		summary = this.baseTransform(summary, resource);
		summary.setMapName(resource.getMapName());
		summary.setHref(getUrlConstructor().createMapUrl(resource.getMapName()));

		return summary;
	}

	@Override
	protected String getName(MapCatalogEntry entry) {
		return entry.getMapName();
	}

	@Override
	protected String doGetResourceBasePath() {
		return MAPS_PATH;
	}

	@Override
	protected MapCatalogEntrySummary createSummary() {
		return new MapCatalogEntrySummary();
	}

	@Override
	protected Class<? extends UnknownResourceReference> getUnknownResourceExceptionClass() {
		return UnknownMap.class;
	}
}
