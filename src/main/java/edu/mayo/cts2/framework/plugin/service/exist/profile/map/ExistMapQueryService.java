package edu.mayo.cts2.framework.plugin.service.exist.profile.map;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.filter.match.StateAdjustingModelAttributeReference.StateUpdater;
import edu.mayo.cts2.framework.model.core.FilterComponent;
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference;
import edu.mayo.cts2.framework.model.core.PredicateReference;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.map.MapCatalogEntry;
import edu.mayo.cts2.framework.model.map.MapCatalogEntrySummary;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.plugin.service.exist.dao.MapExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistQueryService;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder.XpathState;
import edu.mayo.cts2.framework.service.command.Page;
import edu.mayo.cts2.framework.service.command.restriction.MapQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.map.MapQueryService;

@Component
public class ExistMapQueryService 
	extends AbstractExistQueryService
	<edu.mayo.cts2.framework.model.service.map.MapCatalogQueryService,XpathState> 
	implements MapQueryService {

	@Resource
	private MapExistDao mapExistDao;

	@Override
	public PredicateReference getPropertyReference(String nameOrUri) {
		// TODO Auto-generated method stub
		return null;
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
					
					return mapExistDao.getResourceSummaries(
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

			getAvailableMatchAlgorithmReferences(), 
			getAvailableModelAttributeReferences());
		}
	}

	@Override
	public DirectoryResult<MapCatalogEntrySummary> getResourceSummaries(
			Query query, FilterComponent filterComponent,
			MapQueryServiceRestrictions restrictions, Page page) {
		MapDirectoryBuilder builder = new MapDirectoryBuilder();

		return builder.restrict(filterComponent).addStart(page.getStart())
				.addMaxToReturn(page.getMaxtoreturn()).resolve();
	}

	@Override
	public DirectoryResult<MapCatalogEntry> getResourceList(Query query,
			FilterComponent filterComponent,
			MapQueryServiceRestrictions restrictions, Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int count(Query query, FilterComponent filterComponent,
			MapQueryServiceRestrictions restrictions) {
		MapDirectoryBuilder builder = new MapDirectoryBuilder();

		return builder.restrict(filterComponent).count();
	}

	@Override
	protected List<? extends PredicateReference> getAvailablePredicateReferences() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected StateUpdater<XpathState> getResourceNameStateUpdater() {
		return new CodeSystemNameStateUpdater();
	}

}
