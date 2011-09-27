package edu.mayo.cts2.framework.plugin.service.exist.profile.entitydescription;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.plugin.service.exist.dao.EntityDescriptionExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistQueryService;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder;
import edu.mayo.cts2.framework.plugin.service.exist.xpath.XpathStateBuildingRestriction;
import edu.mayo.cts2.framework.plugin.service.exist.xpath.XpathStateUpdater;
import edu.mayo.cts2.framework.plugin.service.exist.xpath.XpathStateBuildingRestriction.AllOrAny;
import edu.mayo.cts2.sdk.filter.match.StateAdjustingModelAttributeReference;
import edu.mayo.cts2.sdk.filter.match.StateAdjustingModelAttributeReference.StateUpdater;
import edu.mayo.cts2.sdk.model.core.FilterComponent;
import edu.mayo.cts2.sdk.model.core.PredicateReference;
import edu.mayo.cts2.sdk.model.directory.DirectoryResult;
import edu.mayo.cts2.sdk.model.entity.EntityDescription;
import edu.mayo.cts2.sdk.model.entity.EntityDirectoryEntry;
import edu.mayo.cts2.sdk.model.service.core.Query;
import edu.mayo.cts2.sdk.service.command.Page;
import edu.mayo.cts2.sdk.service.command.restriction.EntityDescriptionQueryServiceRestrictions;
import edu.mayo.cts2.sdk.service.meta.StandardModelAttributeReference;
import edu.mayo.cts2.sdk.service.profile.entitydescription.EntityDescriptionQueryService;

@Component
public class ExistEntityDescriptionQueryService 
	extends AbstractExistQueryService
		<edu.mayo.cts2.sdk.model.service.entitydescription.EntityDescriptionQueryService,EntityDescriptionDirectoryState>    
	implements EntityDescriptionQueryService {

	@Resource
	private EntityDescriptionExistDao entityDescriptionExistDao;

	@Override
	public PredicateReference getPropertyReference(String nameOrUri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DirectoryResult<EntityDirectoryEntry> getResourceSummaries(
			Query query,
			FilterComponent filterComponent,
			EntityDescriptionQueryServiceRestrictions restrictions,
			Page page) {
		EntityDescriptionDirectoryBuilder builder = new EntityDescriptionDirectoryBuilder();
		
		return builder.
				restrict(restrictions).
				restrict(filterComponent).
				restrict(query).
				addMaxToReturn(page.getEnd()).
				addStart(page.getStart()).
				resolve();
	}

	@Override
	public DirectoryResult<EntityDescription> getResourceList(
			Query query,
			FilterComponent filterComponent, 
			EntityDescriptionQueryServiceRestrictions restrictions,
			Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int count(
			Query query, 
			FilterComponent filterComponent,
			EntityDescriptionQueryServiceRestrictions restrictions) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected List<? extends PredicateReference> getAvailablePredicateReferences() {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected List<StateAdjustingModelAttributeReference<EntityDescriptionDirectoryState>> getAvailableModelAttributeReferences() {
		List<StateAdjustingModelAttributeReference<EntityDescriptionDirectoryState>> list = super.getAvailableModelAttributeReferences();
		
		StateAdjustingModelAttributeReference<EntityDescriptionDirectoryState> resourceSynopsis = 
				StateAdjustingModelAttributeReference.toModelAttributeReference(
						StandardModelAttributeReference.RESOURCE_SYNOPSIS.getModelAttributeReference(),
						getResourceSynopsisStateUpdater());
			
		list.add(resourceSynopsis);
		
		return list;
	}
	
	private StateUpdater<EntityDescriptionDirectoryState> getResourceSynopsisStateUpdater() {
		return new XpathStateUpdater<EntityDescriptionDirectoryState>("/entity:EntityDescription", ".//entity:designation/core:value", "text()");
	}

	private class EntityDescriptionDirectoryBuilder extends XpathDirectoryBuilder<EntityDescriptionDirectoryState,EntityDirectoryEntry> {

		public EntityDescriptionDirectoryBuilder() {
			super(new EntityDescriptionDirectoryState(), 
					new Callback<EntityDescriptionDirectoryState, EntityDirectoryEntry>() {

				@Override
				public DirectoryResult<EntityDirectoryEntry> execute(
						EntityDescriptionDirectoryState state, 
						int start, 
						int maxResults) {
					return entityDescriptionExistDao.getResourceSummaries(
							createPath(state.getCodeSystemVersion()),
							state.getXpath(), 
							start, 
							maxResults);
				}

				@Override
				public int executeCount(EntityDescriptionDirectoryState state) {
					throw new UnsupportedOperationException();
				}},
				
				getAvailableMatchAlgorithmReferences(),
				getAvailableModelAttributeReferences());
		}
		
		public EntityDescriptionDirectoryBuilder restrict(final EntityDescriptionQueryServiceRestrictions restriction){
			getRestrictions().add(
					new StateBuildingRestriction<EntityDescriptionDirectoryState>() {

						@Override
						public EntityDescriptionDirectoryState restrict(EntityDescriptionDirectoryState currentState) {
							if(restriction != null &&
									StringUtils.isNotBlank(restriction.getCodesystemversion())){
								currentState.setCodeSystemVersion(restriction.getCodesystemversion());
							}
							
							return currentState;
						}
					});
			if(restriction != null &&
					CollectionUtils.isNotEmpty(restriction.getEntity())){
		
				getRestrictions().add(
						new XpathStateBuildingRestriction<EntityDescriptionDirectoryState>(
								"/entity:EntityDescription", 
								".//entity:entityID/core:name", 
								"text()", 
								AllOrAny.ANY,
								restriction.getEntity()));
			}
			
			return this;
		}
	}

	@Override
	protected StateUpdater<EntityDescriptionDirectoryState> getResourceNameStateUpdater() {
		return new XpathStateUpdater<EntityDescriptionDirectoryState>("/entity:EntityDescription", ".//entity:entityID/core:name", "text()");
	}
}
