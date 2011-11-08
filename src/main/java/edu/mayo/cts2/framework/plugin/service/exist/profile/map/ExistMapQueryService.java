package edu.mayo.cts2.framework.plugin.service.exist.profile.map;

import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.filter.match.StateAdjustingModelAttributeReference.StateUpdater;
import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference;
import edu.mayo.cts2.framework.model.core.PredicateReference;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.map.MapCatalogEntry;
import edu.mayo.cts2.framework.model.map.MapCatalogEntrySummary;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistQueryService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder.XpathState;
import edu.mayo.cts2.framework.service.command.restriction.MapQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.map.MapQueryService;

@Component
public class ExistMapQueryService 
	extends AbstractExistQueryService
	<MapCatalogEntry,
	MapCatalogEntrySummary,
	MapQueryServiceRestrictions,
	edu.mayo.cts2.framework.model.service.map.MapCatalogQueryService,XpathState> 
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
	private class CodeSystemNameStateUpdater implements StateUpdater<XpathState> {

		@Override
		public XpathState updateState(XpathState currentState, MatchAlgorithmReference matchAlgorithm, String queryString) {
			currentState.setXpath("codesystem:CodeSystemCatalogEntry/@codeSystemName = '"
						+ queryString + "'");

			return currentState;
		}
	}

	private class MapDirectoryBuilder extends
			XpathDirectoryBuilder<XpathState,MapCatalogEntrySummary> {

		public MapDirectoryBuilder() {
			super(new XpathState(), new Callback<XpathState, MapCatalogEntrySummary>() {

				@Override
				public DirectoryResult<MapCatalogEntrySummary> execute(
						XpathState state, int start, int maxResults) {
					
					return getResourceSummaries(
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
			getSupportedModelAttributes());
		}
	}

	@Override
	public DirectoryResult<MapCatalogEntrySummary> getResourceSummaries(
			Query query, 
			Set<ResolvedFilter> filterComponent,
			MapQueryServiceRestrictions restrictions, 
			ResolvedReadContext readContext,
			Page page) {
		MapDirectoryBuilder builder = new MapDirectoryBuilder();

		return builder.restrict(filterComponent).addStart(page.getStart())
				.addMaxToReturn(page.getMaxToReturn()).resolve();
	}

	@Override
	public DirectoryResult<MapCatalogEntry> getResourceList(
			Query query,
			Set<ResolvedFilter> filterComponent,
			MapQueryServiceRestrictions restrictions, 
			ResolvedReadContext readContext,
			Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int count(Query query, Set<ResolvedFilter> filterComponent,
			MapQueryServiceRestrictions restrictions) {
		MapDirectoryBuilder builder = new MapDirectoryBuilder();

		return builder.restrict(filterComponent).count();
	}

	@Override
	protected StateUpdater<XpathState> getResourceNameStateUpdater() {
		return new CodeSystemNameStateUpdater();
	}

	@Override
	protected ResourceInfo<MapCatalogEntry, ?> getResourceInfo() {
		return this.mapResourceInfo;
	}

	@Override
	public Set<? extends PredicateReference> getSupportedProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PredicateReference getPropertyReference(String nameOrUri) {
		// TODO Auto-generated method stub
		return null;
	}

}
