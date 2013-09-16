package edu.mayo.cts2.framework.plugin.service.exist.profile.mapentry;

import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.core.SortCriteria;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.mapversion.MapEntry;
import edu.mayo.cts2.framework.model.mapversion.MapEntryDirectoryEntry;
import edu.mayo.cts2.framework.model.mapversion.MapEntryListEntry;
import edu.mayo.cts2.framework.model.service.core.EntityNameOrURI;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistQueryService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.PathInfo;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;
import edu.mayo.cts2.framework.plugin.service.exist.xpath.XpathStateBuildingRestriction;
import edu.mayo.cts2.framework.plugin.service.exist.xpath.XpathStateBuildingRestriction.AllOrAny;
import edu.mayo.cts2.framework.service.command.restriction.MapEntryQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.mapentry.MapEntryQuery;
import edu.mayo.cts2.framework.service.profile.mapentry.MapEntryQueryService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExistMapEntryQueryService
	extends AbstractExistQueryService
		<MapEntry,
		MapEntryDirectoryEntry,
		MapEntryQueryServiceRestrictions,
		MapEntryDirectoryState>
	implements MapEntryQueryService {
	
	@Resource
	private MapEntryResourceInfo mapEntryResourceInfo;
	
	private class MapEntryDirectoryBuilder extends
			XpathDirectoryBuilder<MapEntryDirectoryState,MapEntryDirectoryEntry> {

		public MapEntryDirectoryBuilder restrict(
				final MapEntryQueryServiceRestrictions restrictions) {

			if (restrictions != null) {
				if(restrictions.getMapVersion() != null){
					this.getRestrictions().add(new StateBuildingRestriction<MapEntryDirectoryState>() {

						@Override
						public MapEntryDirectoryState restrict(
								MapEntryDirectoryState state) {
							state.setMapVersion(restrictions.getMapVersion().getName());
							return state;
						}
					});
				}
				if(CollectionUtils.isNotEmpty(restrictions.getTargetEntities())) {
					
					List<String> names = new ArrayList<String>();
					
					//TODO This doesn't account for URIs or Namespaces
					for(EntityNameOrURI scopedName : restrictions.getTargetEntities()){
						names.add(scopedName.getEntityName().getName());
					}
					
					this.getRestrictions().add(
						 new XpathStateBuildingRestriction<MapEntryDirectoryState>(
								"mapversion:mapSet/mapversion:mapTarget/mapversion:mapTo/core:name", 
								".",
								AllOrAny.ANY,
								names));
				}
			}
			return this;
		}

		public MapEntryDirectoryBuilder(final String changeSetUri) {
			super(new MapEntryDirectoryState(),
					new Callback<MapEntryDirectoryState, MapEntryDirectoryEntry>() {

				@Override
				public DirectoryResult<MapEntryDirectoryEntry> execute(
						MapEntryDirectoryState state, int start, int maxResults) {
					
					return getResourceSummaries(
							getResourceInfo(),
							changeSetUri,
							ExistServiceUtils.createPath(state.getMapVersion()), 
							state.getXpath(), 
							start,
							maxResults);
				}

				@Override
				public int executeCount(MapEntryDirectoryState state) {
					throw new UnsupportedOperationException();
				}
			}, 
			getSupportedMatchAlgorithms(),
			getSupportedSearchReferences());
		}
	}

	@Override
	public DirectoryResult<MapEntryDirectoryEntry> getResourceSummaries(
			MapEntryQuery query, 
			SortCriteria sortCriteria,
			Page page) {
		
		MapEntryDirectoryBuilder builder = 
				new MapEntryDirectoryBuilder(
						this.getChangeSetUri(
								query.getReadContext()));

		return builder.
				restrict(query.getRestrictions()).
				restrict(query.getFilterComponent()).
				restrict(query.getQuery()).
				addMaxToReturn(page.getEnd()).
				addStart(page.getStart()).
				resolve();
	}

	@Override
	public DirectoryResult<MapEntryListEntry> getResourceList(
			MapEntryQuery query, 
			SortCriteria sortCriteria,
			Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public MapEntryDirectoryEntry doTransform(
			MapEntry resource,
			MapEntryDirectoryEntry summary, 
			org.xmldb.api.base.Resource eXistResource) {
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
	public int count(MapEntryQuery query) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected MapEntryDirectoryEntry createSummary() {
		return new MapEntryDirectoryEntry();
	}

	@Override
	protected PathInfo getResourceInfo() {
		return this.mapEntryResourceInfo;
	}
}
