package edu.mayo.cts2.framework.plugin.service.exist.profile.map;

import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.core.SortCriteria;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.map.MapCatalogEntry;
import edu.mayo.cts2.framework.model.map.MapCatalogEntryListEntry;
import edu.mayo.cts2.framework.model.map.MapCatalogEntrySummary;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistQueryService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.PathInfo;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder.XpathState;
import edu.mayo.cts2.framework.service.command.restriction.MapQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.map.MapQuery;
import edu.mayo.cts2.framework.service.profile.map.MapQueryService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ExistMapQueryService 
	extends AbstractExistQueryService
	<MapCatalogEntry,
	MapCatalogEntrySummary,
	MapQueryServiceRestrictions,
	XpathState> 
	implements MapQueryService {

	@Resource
	private MapResourceInfo mapResourceInfo;

	@Override
	public MapCatalogEntrySummary doTransform(MapCatalogEntry resource,
			MapCatalogEntrySummary summary, org.xmldb.api.base.Resource eXistResource) {
		summary = this.baseTransform(summary, resource);
		summary.setMapName(resource.getMapName());
		summary.setHref(getUrlConstructor().createMapUrl(resource.getMapName()));

		return summary;
	}
	
	@Override
	protected MapCatalogEntrySummary createSummary() {
		return new MapCatalogEntrySummary();
	}

	private class MapDirectoryBuilder extends
			XpathDirectoryBuilder<XpathState,MapCatalogEntrySummary> {

		public MapDirectoryBuilder(final String changeSetUri) {
			super(new XpathState(), new Callback<XpathState, MapCatalogEntrySummary>() {

				@Override
				public DirectoryResult<MapCatalogEntrySummary> execute(
						XpathState state, int start, int maxResults) {
					
					return getResourceSummaries(
							getResourceInfo(),
							changeSetUri,
							"", 
							state.getXpath(), 
							start,
							maxResults);
				}

				@Override
				public int executeCount(XpathState state) {
					throw new UnsupportedOperationException();
				}
			},

			getSupportedMatchAlgorithms(),
			getSupportedSearchReferences());
		}
	}

	@Override
	public DirectoryResult<MapCatalogEntrySummary> getResourceSummaries(
			MapQuery query,
			SortCriteria sortCriteria,
			Page page) {
		MapDirectoryBuilder builder = new MapDirectoryBuilder(
				this.getChangeSetUri(query.getReadContext()));

		return builder.restrict(query.getFilterComponent()).
				addStart(page.getStart()).
				addMaxToReturn(page.getMaxToReturn()).
				resolve();
	}

	@Override
	public DirectoryResult<MapCatalogEntryListEntry> getResourceList(
			MapQuery query,
			SortCriteria sortCriteria,
			Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int count(
			MapQuery query){
		throw new UnsupportedOperationException();
	}

	@Override
	protected PathInfo getResourceInfo() {
		return this.mapResourceInfo;
	}
}
