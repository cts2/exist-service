package edu.mayo.cts2.framework.plugin.service.exist.profile.mapentry;

import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.PredicateReference;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.mapversion.MapEntry;
import edu.mayo.cts2.framework.model.mapversion.MapEntryDirectoryEntry;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistQueryService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;
import edu.mayo.cts2.framework.plugin.service.exist.xpath.XpathStateBuildingRestriction;
import edu.mayo.cts2.framework.plugin.service.exist.xpath.XpathStateBuildingRestriction.AllOrAny;
import edu.mayo.cts2.framework.service.command.restriction.MapEntryQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.mapentry.MapEntryQueryService;

@Component
public class ExistMapEntryQueryService
	extends AbstractExistQueryService
		<MapEntry,
		MapEntryDirectoryEntry,
		MapEntryQueryServiceRestrictions,
		edu.mayo.cts2.framework.model.service.mapentry.MapEntryQueryService,MapEntryDirectoryState>
	implements MapEntryQueryService {
	
	@Resource
	private MapEntryResourceInfo mapEntryResourceInfo;
	
	private class MapEntryDirectoryBuilder extends
			XpathDirectoryBuilder<MapEntryDirectoryState,MapEntryDirectoryEntry> {

		public MapEntryDirectoryBuilder restrict(
				final MapEntryQueryServiceRestrictions restrictions) {

			if (restrictions != null) {
				if(StringUtils.isNotBlank(restrictions.getMapversion())){
					this.getRestrictions().add(new StateBuildingRestriction<MapEntryDirectoryState>() {

						@Override
						public MapEntryDirectoryState restrict(
								MapEntryDirectoryState state) {
							state.setMapVersion(restrictions.getMapversion());
							return state;
						}
					});
				}
				if(CollectionUtils.isNotEmpty(restrictions.getTargetentity())) {
					
					this.getRestrictions().add(
						 new XpathStateBuildingRestriction<MapEntryDirectoryState>(
								"mapversion:mapSet/mapversion:mapTarget/mapversion:mapTo/core:name", 
								"text()",
								AllOrAny.ANY,
								restrictions.getTargetentity()));
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
			getSupportedModelAttributes());
		}
	}

	@Override
	public DirectoryResult<MapEntryDirectoryEntry> getResourceSummaries(
			Query query, 
			Set<ResolvedFilter> filterComponent,
			MapEntryQueryServiceRestrictions restrictions, 
			ResolvedReadContext readContext,
			Page page) {
		MapEntryDirectoryBuilder builder = 
				new MapEntryDirectoryBuilder(this.getChangeSetUri(readContext));

		return builder.
				restrict(restrictions).
				restrict(filterComponent).
				restrict(query).
				addMaxToReturn(page.getEnd()).
				addStart(page.getStart()).
				resolve();
	}

	@Override
	public DirectoryResult<MapEntry> getResourceList(
			Query query,
			Set<ResolvedFilter> filterComponent,
			MapEntryQueryServiceRestrictions restrictions, 
			ResolvedReadContext readContext,
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
	public int count(Query query, Set<ResolvedFilter> filterComponent,
			MapEntryQueryServiceRestrictions restrictions) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected MapEntryDirectoryEntry createSummary() {
		return new MapEntryDirectoryEntry();
	}

	@Override
	protected ResourceInfo<MapEntry, ?> getResourceInfo() {
		return this.mapEntryResourceInfo;
	}

	@Override
	public Set<? extends PredicateReference> getSupportedProperties() {
		// TODO Auto-generated method stub
		return null;
	}
}
