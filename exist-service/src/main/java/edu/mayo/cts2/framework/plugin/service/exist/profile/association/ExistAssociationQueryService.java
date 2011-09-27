package edu.mayo.cts2.framework.plugin.service.exist.profile.association;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.plugin.service.exist.dao.AssociationExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistQueryService;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder.XpathState;
import edu.mayo.cts2.sdk.filter.match.StateAdjustingModelAttributeReference.StateUpdater;
import edu.mayo.cts2.sdk.model.association.Association;
import edu.mayo.cts2.sdk.model.association.AssociationDirectoryEntry;
import edu.mayo.cts2.sdk.model.core.FilterComponent;
import edu.mayo.cts2.sdk.model.core.PredicateReference;
import edu.mayo.cts2.sdk.model.directory.DirectoryResult;
import edu.mayo.cts2.sdk.model.entity.EntityDirectoryEntry;
import edu.mayo.cts2.sdk.model.service.core.Query;
import edu.mayo.cts2.sdk.service.command.Page;
import edu.mayo.cts2.sdk.service.command.restriction.AssociationQueryServiceRestrictions;
import edu.mayo.cts2.sdk.service.profile.association.AssociationQueryService;
import edu.mayo.cts2.sdk.service.profile.entitydescription.id.EntityDescriptionId;

@Component
public class ExistAssociationQueryService 	
	extends AbstractExistQueryService
		<edu.mayo.cts2.sdk.model.service.association.AssociationQueryService,XpathState>
	implements AssociationQueryService {

	@Resource
	private AssociationExistDao associationExistDao;

	@Override
	public PredicateReference getPropertyReference(String nameOrUri) {
		throw new UnsupportedOperationException();
	}

	
	private class AssociationDirectoryBuilder extends XpathDirectoryBuilder<XpathState,AssociationDirectoryEntry> {

		public AssociationDirectoryBuilder() {
			super(new XpathState(), new Callback<XpathState, AssociationDirectoryEntry>() {

				@Override
				public DirectoryResult<AssociationDirectoryEntry> execute(
						XpathState state, 
						int start, 
						int maxResults) {
					return associationExistDao.getResourceSummaries(
							"",
							state.getXpath(), 
							start, 
							maxResults);
				}

				@Override
				public int executeCount(XpathState state) {
					throw new UnsupportedOperationException();
				}},
				
				getAvailableMatchAlgorithmReferences(),
				getAvailableModelAttributeReferences());
		}
	}

	@Override
	public DirectoryResult<AssociationDirectoryEntry> getResourceSummaries(
			Query query, 
			FilterComponent filterComponent, 
			AssociationQueryServiceRestrictions restrictions,
			Page page) {
		AssociationDirectoryBuilder builder = new AssociationDirectoryBuilder();
		
		return builder.restrict(filterComponent).
				addStart(page.getStart())
				.addMaxToReturn(page.getMaxtoreturn()).
				resolve();
	}

	@Override
	public DirectoryResult<Association> getResourceList(
			Query query,
			FilterComponent filterComponent,
			AssociationQueryServiceRestrictions restrictions, 
			Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int count(
			Query query, 
			FilterComponent filterComponent,
			AssociationQueryServiceRestrictions restrictions) {
		AssociationDirectoryBuilder builder = new AssociationDirectoryBuilder();
		
		return builder.restrict(filterComponent).
				restrict(query).
				count();
	}

	@Override
	public DirectoryResult<EntityDirectoryEntry> getChildrenAssociationsOfEntity(
			Query query, 
			FilterComponent filterComponent, 
			Page page,
			EntityDescriptionId id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DirectoryResult<EntityDirectoryEntry> getParentAssociationsOfEntity(
			Query query, 
			FilterComponent filterComponent, 
			Page page,
			EntityDescriptionId id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DirectoryResult<AssociationDirectoryEntry> getSourceOfAssociationsOfEntity(
			Query query, 
			FilterComponent filterComponent, 
			Page page,
			EntityDescriptionId id) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected StateUpdater<XpathState> getResourceNameStateUpdater() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<? extends PredicateReference> getAvailablePredicateReferences() {
		// TODO Auto-generated method stub
		return null;
	}
}
