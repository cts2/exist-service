package edu.mayo.cts2.framework.plugin.service.exist.profile.association;

import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.filter.match.StateAdjustingModelAttributeReference.StateUpdater;
import edu.mayo.cts2.framework.model.association.Association;
import edu.mayo.cts2.framework.model.association.AssociationDirectoryEntry;
import edu.mayo.cts2.framework.model.association.AssociationGraph;
import edu.mayo.cts2.framework.model.association.types.GraphDirection;
import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.PredicateReference;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.entity.EntityDirectoryEntry;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistQueryService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder.XpathState;
import edu.mayo.cts2.framework.service.command.restriction.AssociationQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.association.AssociationQueryService;
import edu.mayo.cts2.framework.service.profile.association.name.AssociationReadId;
import edu.mayo.cts2.framework.service.profile.entitydescription.name.EntityDescriptionReadId;

@Component
public class ExistAssociationQueryService 	
	extends AbstractExistQueryService
		<Association,
		AssociationDirectoryEntry,
		AssociationQueryServiceRestrictions,
		edu.mayo.cts2.framework.model.service.association.AssociationQueryService,XpathState>
	implements AssociationQueryService {
	
	@Resource
	private AssociationResourceInfo associationResourceInfo;
	
	private class AssociationDirectoryBuilder extends XpathDirectoryBuilder<XpathState,AssociationDirectoryEntry> {

		public AssociationDirectoryBuilder() {
			super(new XpathState(), new Callback<XpathState, AssociationDirectoryEntry>() {

				@Override
				public DirectoryResult<AssociationDirectoryEntry> execute(
						XpathState state, 
						int start, 
						int maxResults) {
					return getResourceSummaries(
							"",
							state.getXpath(), 
							start, 
							maxResults);
				}

				@Override
				public int executeCount(XpathState state) {
					throw new UnsupportedOperationException();
				}},
				
				getSupportedMatchAlgorithms(),
				getSupportedModelAttributes());
		}
	}

	@Override
	public DirectoryResult<AssociationDirectoryEntry> getResourceSummaries(
			Query query, 
			Set<ResolvedFilter> filterComponent, 
			AssociationQueryServiceRestrictions restrictions,
			ResolvedReadContext readContext,
			Page page) {
		AssociationDirectoryBuilder builder = new AssociationDirectoryBuilder();
		
		return builder.restrict(filterComponent).
				addStart(page.getStart())
				.addMaxToReturn(page.getMaxToReturn()).
				resolve();
	}

	@Override
	public DirectoryResult<Association> getResourceList(
			Query query,
			Set<ResolvedFilter> filterComponent,
			AssociationQueryServiceRestrictions restrictions, 
			ResolvedReadContext readContext,
			Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int count(
			Query query, 
			Set<ResolvedFilter> filterComponent,
			AssociationQueryServiceRestrictions restrictions) {
		AssociationDirectoryBuilder builder = new AssociationDirectoryBuilder();
		
		return builder.restrict(filterComponent).
				restrict(query).
				count();
	}

	@Override
	protected ResourceInfo<Association, AssociationReadId> getResourceInfo() {
		return this.associationResourceInfo;
	}

	@Override
	protected StateUpdater<XpathState> getResourceNameStateUpdater() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected AssociationDirectoryEntry createSummary() {
		return new AssociationDirectoryEntry();
	}

	@Override
	protected AssociationDirectoryEntry doTransform(
			Association resource,
			AssociationDirectoryEntry summary,
			org.xmldb.api.base.Resource eXistResource) {
		
		return summary;
	}

	@Override
	public Set<? extends PredicateReference> getSupportedProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AssociationGraph getAssociationGraph(EntityDescriptionReadId focus,
			GraphDirection direction, long depth) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DirectoryResult<EntityDirectoryEntry> getChildrenAssociationsOfEntity(
			Query query, Set<ResolvedFilter> filterComponent,
			EntityDescriptionReadId entity,
			AssociationQueryServiceRestrictions restrictions,
			ResolvedReadContext readContext, Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DirectoryResult<EntityDirectoryEntry> getSourceEntities(Query query,
			Set<ResolvedFilter> filterComponent,
			AssociationQueryServiceRestrictions restrictions,
			ResolvedReadContext readContext, Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DirectoryResult<EntityDirectoryEntry> getTargetEntities(Query query,
			Set<ResolvedFilter> filterComponent,
			AssociationQueryServiceRestrictions restrictions,
			ResolvedReadContext readContext, Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DirectoryResult<EntityDirectoryEntry> getAllSourceAndTargetEntities(
			Query query, Set<ResolvedFilter> filterComponent,
			AssociationQueryServiceRestrictions restrictions,
			ResolvedReadContext readContext, Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DirectoryResult<EntityDirectoryEntry> getPredicates(Query query,
			Set<ResolvedFilter> filterComponent,
			AssociationQueryServiceRestrictions restrictions,
			ResolvedReadContext readContext, Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DirectoryResult<AssociationDirectoryEntry> getSourceOfAssociationsOfEntity(
			Query query, Set<ResolvedFilter> filterComponent, Page page,
			EntityDescriptionReadId entity, ResolvedReadContext readContext) {
		throw new UnsupportedOperationException();
	}

}
