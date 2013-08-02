package edu.mayo.cts2.framework.plugin.service.exist.profile.mapversion;

import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.core.EntityReferenceList;
import edu.mayo.cts2.framework.model.core.SortCriteria;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.entity.EntityDescription;
import edu.mayo.cts2.framework.model.entity.EntityDirectoryEntry;
import edu.mayo.cts2.framework.model.mapversion.MapVersion;
import edu.mayo.cts2.framework.model.mapversion.MapVersionDirectoryEntry;
import edu.mayo.cts2.framework.model.mapversion.MapVersionListEntry;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.service.mapversion.types.MapRole;
import edu.mayo.cts2.framework.model.service.mapversion.types.MapStatus;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistQueryService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.DefaultResourceInfo;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;
import edu.mayo.cts2.framework.service.command.restriction.MapVersionQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionQuery;
import edu.mayo.cts2.framework.service.profile.mapversion.MapVersionQuery;
import edu.mayo.cts2.framework.service.profile.mapversion.MapVersionQueryService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ExistMapVersionQueryService 
	extends AbstractExistQueryService
		<MapVersion,
		MapVersionDirectoryEntry,
		MapVersionQueryServiceRestrictions,
		MapVersionDirectoryState>
	implements MapVersionQueryService {

	@Resource
	private MapVersionResourceInfo mapVersionResourceInfo;
	
	@Override
	public MapVersionDirectoryEntry doTransform(MapVersion resource,
			MapVersionDirectoryEntry summary, org.xmldb.api.base.Resource eXistResource) {
		summary = this.baseTransformResourceVersion(summary, resource);
		
		summary.setMapVersionName(resource.getMapVersionName());
		summary.setVersionOf(resource.getVersionOf());
		summary.setVersionTag(resource.getVersionTag());
		
		summary.setHref(getUrlConstructor().createMapVersionUrl(
				resource.getVersionOf().getContent(),
				resource.getMapVersionName()));

		return summary;
	}

	@Override
	protected MapVersionDirectoryEntry createSummary() {
		return new MapVersionDirectoryEntry();
	}

	private class MapVersionDirectoryBuilder extends
			XpathDirectoryBuilder<MapVersionDirectoryState,MapVersionDirectoryEntry> {

		public MapVersionDirectoryBuilder(final String changeSetUri) {
			super(new MapVersionDirectoryState(),
					new Callback<MapVersionDirectoryState, MapVersionDirectoryEntry>() {

				@Override
				public DirectoryResult<MapVersionDirectoryEntry> execute(
						MapVersionDirectoryState state, 
						int start, 
						int maxResults) {
					return getResourceSummaries(
							getResourceInfo(),
							changeSetUri,
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

			getSupportedMatchAlgorithms(),
			getSupportedSearchReferences());
		}
	}

	@Override
	public DirectoryResult<MapVersionDirectoryEntry> getResourceSummaries(
			MapVersionQuery query, 
			SortCriteria sort,
			Page page) {
		MapVersionDirectoryBuilder builder =
				new MapVersionDirectoryBuilder(
						this.getChangeSetUri(query.getReadContext()));

		return builder.addMaxToReturn(page.getEnd()).
				addStart(page.getStart()).
				restrict(query.getFilterComponent()).
				restrict(query.getQuery()).
				resolve();
	}

	@Override
	public DirectoryResult<MapVersionListEntry> getResourceList(
			MapVersionQuery query, 
			SortCriteria sort,
			Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int count(
			MapVersionQuery query) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected DefaultResourceInfo<MapVersion, ?> getResourceInfo() {
		return this.mapVersionResourceInfo;
	}

	@Override
	public DirectoryResult<EntityDirectoryEntry> mapVersionEntities(
			NameOrURI mapVersion, MapRole mapRole, MapStatus mapStatus,
			EntityDescriptionQuery query, SortCriteria sort, Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DirectoryResult<EntityDescription> mapVersionEntityList(
			NameOrURI mapVersion, MapRole mapRole, MapStatus mapStatus,
			EntityDescriptionQuery query, SortCriteria sort, Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public EntityReferenceList mapVersionEntityReferences(NameOrURI mapVersion,
			MapRole mapRole, MapStatus mapStatus, EntityDescriptionQuery query,
			SortCriteria sort, Page page) {
		throw new UnsupportedOperationException();
	}



}
