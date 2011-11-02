package edu.mayo.cts2.framework.plugin.service.exist.profile.mapversion;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.filter.match.StateAdjustingModelAttributeReference.StateUpdater;
import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.core.PredicateReference;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.mapversion.MapVersion;
import edu.mayo.cts2.framework.model.mapversion.MapVersionDirectoryEntry;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistQueryService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;
import edu.mayo.cts2.framework.service.command.restriction.MapVersionQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.mapversion.MapVersionQueryService;

@Component
public class ExistMapVersionQueryService 
	extends AbstractExistQueryService
		<MapVersion,
		MapVersionDirectoryEntry,
		edu.mayo.cts2.framework.model.service.mapversion.MapVersionQueryService,MapVersionDirectoryState>
	implements MapVersionQueryService {

	@Resource
	private MapVersionResourceInfo mapVersionResourceInfo;
	
	@Override
	public MapVersionDirectoryEntry doTransform(MapVersion resource,
			MapVersionDirectoryEntry summary, org.xmldb.api.base.Resource eXistResource) {
		summary = this.baseTransform(summary, resource);
		summary.setMapVersionName(resource.getMapVersionName());
		summary.setHref(getUrlConstructor().createMapUrl(resource.getMapVersionName()));

		return summary;
	}


	@Override
	protected MapVersionDirectoryEntry createSummary() {
		return new MapVersionDirectoryEntry();
	}
	
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
					return getResourceSummaries(
							ExistServiceUtils.createPath(state.getMap()), 
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
			Set<ResolvedFilter> filterComponent,
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
			Set<ResolvedFilter> filterComponent,
			MapVersionQueryServiceRestrictions restrictions, Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int count(
			Query query, 
			Set<ResolvedFilter> filterComponent,
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


	@Override
	protected ResourceInfo<MapVersion, ?> getResourceInfo() {
		return this.mapVersionResourceInfo;
	}

}
