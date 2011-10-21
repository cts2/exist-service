package edu.mayo.cts2.framework.plugin.service.exist.profile.mapversion;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.filter.match.StateAdjustingModelAttributeReference.StateUpdater;
import edu.mayo.cts2.framework.model.core.FilterComponent;
import edu.mayo.cts2.framework.model.core.PredicateReference;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.mapversion.MapVersion;
import edu.mayo.cts2.framework.model.mapversion.MapVersionDirectoryEntry;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.plugin.service.exist.dao.MapVersionExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistQueryService;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder;
import edu.mayo.cts2.framework.service.command.Page;
import edu.mayo.cts2.framework.service.command.restriction.MapVersionQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.mapversion.MapVersionQueryService;

@Component
public class ExistMapVersionQueryService 
	extends AbstractExistQueryService
		<edu.mayo.cts2.framework.model.service.mapversion.MapVersionQueryService,MapVersionDirectoryState>
	implements MapVersionQueryService {

	@Resource
	private MapVersionExistDao mapVersionExistDao;


	@Override
	public PredicateReference getPropertyReference(String nameOrUri) {
		// TODO Auto-generated method stub
		return null;
	}

	private class MapVersionDirectoryBuilder extends
			XpathDirectoryBuilder<MapVersionDirectoryState,MapVersionDirectoryEntry> {

		public MapVersionDirectoryBuilder() {
			super(new MapVersionDirectoryState(),
					new Callback<MapVersionDirectoryState, MapVersionDirectoryEntry>() {

				@Override
				public DirectoryResult<MapVersionDirectoryEntry> execute(
						MapVersionDirectoryState state, 
						int start, 
						int maxResults) {
					return mapVersionExistDao.getResourceSummaries(
							createPath(state.getMap()), 
							state.getXpath(), 
							start,
							maxResults);
				}

				@Override
				public int executeCount(MapVersionDirectoryState state) {
					throw new UnsupportedOperationException();
				}
			},

			getAvailableMatchAlgorithmReferences(), 
			getAvailableModelAttributeReferences());
		}
	}

	@Override
	public DirectoryResult<MapVersionDirectoryEntry> getResourceSummaries(
			Query query, 
			FilterComponent filterComponent,
			MapVersionQueryServiceRestrictions restrictions, 
			Page page) {
		MapVersionDirectoryBuilder builder =
				new MapVersionDirectoryBuilder();

		return builder.addMaxToReturn(page.getEnd())
				.addStart(page.getStart()).restrict(filterComponent)
				.restrict(query).resolve();
	}

	@Override
	public DirectoryResult<MapVersion> getResourceList(Query query,
			FilterComponent filterComponent,
			MapVersionQueryServiceRestrictions restrictions, Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int count(
			Query query, 
			FilterComponent filterComponent,
			MapVersionQueryServiceRestrictions restrictions) {
		MapVersionDirectoryBuilder builder = 
				new MapVersionDirectoryBuilder();

		return builder.
				restrict(filterComponent).
				restrict(query).count();
	}

	@Override
	protected List<? extends PredicateReference> getAvailablePredicateReferences() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected StateUpdater<MapVersionDirectoryState> getResourceNameStateUpdater() {
		return null;
	}
}
