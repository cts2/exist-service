package edu.mayo.cts2.framework.plugin.service.exist.profile.association;

import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import edu.mayo.cts2.framework.model.association.Association;
import edu.mayo.cts2.framework.model.association.AssociationDirectoryEntry;
import edu.mayo.cts2.framework.model.association.GraphNode;
import edu.mayo.cts2.framework.model.association.types.GraphDirection;
import edu.mayo.cts2.framework.model.association.types.GraphFocus;
import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference;
import edu.mayo.cts2.framework.model.core.PredicateReference;
import edu.mayo.cts2.framework.model.core.SortCriteria;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.entity.EntityDirectoryEntry;
import edu.mayo.cts2.framework.model.entity.EntityList;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistQueryService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.PathInfo;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder.XpathState;
import edu.mayo.cts2.framework.service.command.restriction.AssociationQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.command.restriction.EntityDescriptionQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.association.AssociationQuery;
import edu.mayo.cts2.framework.service.profile.association.AssociationQueryService;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionQuery;
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

		public AssociationDirectoryBuilder(final String changeSetUri) {
			super(new XpathState(), new Callback<XpathState, AssociationDirectoryEntry>() {

				@Override
				public DirectoryResult<AssociationDirectoryEntry> execute(
						XpathState state, 
						int start, 
						int maxResults) {
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
				}},
				
				getSupportedMatchAlgorithms(),
				getSupportedModelAttributes());
		}
	}

	@Override
	public DirectoryResult<AssociationDirectoryEntry> getResourceSummaries(
			AssociationQuery query,
			SortCriteria sortCriteria,
			Page page) {
		AssociationDirectoryBuilder builder = new AssociationDirectoryBuilder(
				this.getChangeSetUri(query.getReadContext()));
		
		//TODO
		AssociationQueryServiceRestrictions restrictions = query.getRestrictions();
		
		return builder.restrict(
				query.getFilterComponent()).
				addStart(page.getStart()).
				addMaxToReturn(page.getMaxToReturn()).
				resolve();
	}

	@Override
	protected PathInfo getResourceInfo() {
		return this.associationResourceInfo;
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
		
		CodeSystemVersionReference assertedIn = getAssertedIn(resource);
		
		Assert.notNull(assertedIn.getCodeSystem(), "Association MUST have CodeSystem reference for 'assertedIn'.");
		Assert.notNull(assertedIn.getVersion(), "Association MUST have CodeSystemVersion reference for 'assertedIn'.");
		
		summary.setAssertedBy(resource.getAssertedBy());
		summary.setHref(
				this.getUrlConstructor().createAssociationOfCodeSystemVersionUrl(
						assertedIn.getCodeSystem().getContent(),
						assertedIn.getVersion().getContent(),
						resource.getLocalID()));
		summary.setResourceName(resource.getLocalID());
		summary.setSubject(resource.getSubject());
		summary.setPredicate(resource.getPredicate());
		summary.setTarget(resource.getTarget(0));
		
		return summary;
	}
	
	private static CodeSystemVersionReference getAssertedIn(Association resource){
		if(resource.getAssertedIn() != null){
			return resource.getAssertedIn();
		} else {
			return resource.getAssertedBy();
		}
	}

	@Override
	public Set<? extends PredicateReference> getSupportedProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DirectoryResult<GraphNode> getAssociationGraph(
			GraphFocus focusType,
			EntityDescriptionReadId focusEntity, 
			GraphDirection direction,
			long depth) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DirectoryResult<Association> getResourceList(AssociationQuery query,
			SortCriteria sortCriteria, Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int count(AssociationQuery query) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DirectoryResult<EntityDirectoryEntry> getChildrenAssociationsOfEntity(
			EntityDescriptionReadId entity, EntityDescriptionQuery query,
			ResolvedReadContext readContext, Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DirectoryResult<EntityDirectoryEntry> getChildrenAssociationsOfEntityList(
			EntityDescriptionReadId entity, EntityDescriptionQuery query,
			ResolvedReadContext readContext, Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DirectoryResult<EntityDirectoryEntry> getSourceEntities(
			AssociationQueryServiceRestrictions associationRestrictions,
			EntityDescriptionQueryServiceRestrictions entityRestrictions,
			ResolvedReadContext readContext, Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DirectoryResult<EntityList> getSourceEntitiesList(
			AssociationQueryServiceRestrictions associationRestrictions,
			EntityDescriptionQueryServiceRestrictions entityRestrictions,
			ResolvedReadContext readContext, Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DirectoryResult<EntityDirectoryEntry> getTargetEntities(
			AssociationQuery associationQuery,
			EntityDescriptionQuery entityDescriptionQuery,
			ResolvedReadContext readContext, Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DirectoryResult<EntityList> getTargetEntitiesList(
			AssociationQuery associationQuery,
			EntityDescriptionQuery entityDescriptionQuery,
			ResolvedReadContext readContext, Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DirectoryResult<EntityDirectoryEntry> getAllSourceAndTargetEntities(
			AssociationQuery associationQuery,
			EntityDescriptionQuery entityDescriptionQuery,
			ResolvedReadContext readContext, Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DirectoryResult<EntityList> getAllSourceAndTargetEntitiesList(
			AssociationQuery associationQuery,
			EntityDescriptionQuery entityDescriptionQuery,
			ResolvedReadContext readContext, Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DirectoryResult<EntityDirectoryEntry> getPredicates(
			AssociationQuery associationQuery,
			EntityDescriptionQuery entityDescriptionQuery,
			ResolvedReadContext readContext, Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DirectoryResult<EntityList> getPredicatesList(
			AssociationQuery associationQuery,
			EntityDescriptionQuery entityDescriptionQuery,
			ResolvedReadContext readContext, Page page) {
		// TODO Auto-generated method stub
		return null;
	}

}
