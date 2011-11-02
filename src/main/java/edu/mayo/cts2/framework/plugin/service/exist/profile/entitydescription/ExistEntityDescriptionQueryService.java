package edu.mayo.cts2.framework.plugin.service.exist.profile.entitydescription;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.google.common.collect.Iterables;

import edu.mayo.cts2.framework.filter.match.StateAdjustingModelAttributeReference;
import edu.mayo.cts2.framework.filter.match.StateAdjustingModelAttributeReference.StateUpdater;
import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.core.DescriptionInCodeSystem;
import edu.mayo.cts2.framework.model.core.PredicateReference;
import edu.mayo.cts2.framework.model.core.ScopedEntityName;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.entity.Designation;
import edu.mayo.cts2.framework.model.entity.EntityDescription;
import edu.mayo.cts2.framework.model.entity.EntityDescriptionBase;
import edu.mayo.cts2.framework.model.entity.EntityDirectoryEntry;
import edu.mayo.cts2.framework.model.entity.NamedEntityDescription;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistQueryService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;
import edu.mayo.cts2.framework.plugin.service.exist.xpath.XpathStateBuildingRestriction;
import edu.mayo.cts2.framework.plugin.service.exist.xpath.XpathStateBuildingRestriction.AllOrAny;
import edu.mayo.cts2.framework.plugin.service.exist.xpath.XpathStateUpdater;
import edu.mayo.cts2.framework.service.command.restriction.EntityDescriptionQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.meta.StandardModelAttributeReference;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionQueryService;

@Component
public class ExistEntityDescriptionQueryService 
	extends AbstractExistQueryService
		<EntityDescription,
		EntityDirectoryEntry,
		edu.mayo.cts2.framework.model.service.entitydescription.EntityDescriptionQueryService,
		EntityDescriptionDirectoryState>    
	implements EntityDescriptionQueryService {

	@Resource
	private EntityDescriptionResourceInfo entityDescriptionResourceInfo;
	
	@Override
	public PredicateReference getPropertyReference(String nameOrUri) {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected ScopedEntityName getScopedEntityName(EntityDescription entry) {
		NamedEntityDescription entity = (NamedEntityDescription) ModelUtils.getEntity(entry);
		
		return entity.getEntityID();
	}

	@Override
	public EntityDirectoryEntry doTransform(
			EntityDescription resource,
			EntityDirectoryEntry summary, org.xmldb.api.base.Resource eXistResource) {
	
		ScopedEntityName scopedName = getScopedEntityName(resource);
		
		EntityDescriptionBase base = ModelUtils.getEntity(resource);
		
		String codeSystemName = 
				base.getDescribingCodeSystemVersion().getCodeSystem().getContent();
		String codeSystemVersionName = 
				base.getDescribingCodeSystemVersion().getVersion().getContent();

		summary.setName(scopedName);
		summary.setAbout(base.getAbout());
		
		if(StringUtils.isNotBlank(codeSystemName) && 
				StringUtils.isNotBlank(codeSystemVersionName)){
			summary.setHref(getUrlConstructor().createEntityUrl(
					codeSystemName, 
					codeSystemVersionName, 
					ExistServiceUtils.getExternalEntityName(scopedName, codeSystemName)));
		}
		
		summary.setKnownEntityDescription(
				getDescriptionsInCodeSystemVersion(
					 codeSystemName,
					 codeSystemVersionName,
						base));
		
		
		return summary;
	}
	
	@Override
	public DirectoryResult<EntityDirectoryEntry> getResourceSummaries(
			Query query,
			Set<ResolvedFilter> filterComponent,
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
			Set<ResolvedFilter> filterComponent, 
			EntityDescriptionQueryServiceRestrictions restrictions,
			Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int count(
			Query query, 
			Set<ResolvedFilter> filterComponent,
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
		return new XpathStateUpdater<EntityDescriptionDirectoryState>(".//entity:designation/core:value", "text()");
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
					return getResourceSummaries(
							ExistServiceUtils.createPath(state.getCodeSystemVersion()),
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
								".//entity:entityID/core:name", 
								"text()", 
								AllOrAny.ANY,
								restriction.getEntity()));
			}
			
			return this;
		}
	}

	private DescriptionInCodeSystem[] getDescriptionsInCodeSystemVersion(
			String codeSystem,
			String codeSystemVersion,
			EntityDescriptionBase entity){
		List<DescriptionInCodeSystem> returnList = new ArrayList<DescriptionInCodeSystem>();
		
		for(Designation designation : entity.getDesignation()){
			DescriptionInCodeSystem description = new DescriptionInCodeSystem();
			description.setDesignation(designation.getValue().getContent());
	
			designation.setAssertedInCodeSystemVersion(
					entity.getDescribingCodeSystemVersion());
			designation.getAssertedInCodeSystemVersion().
				getCodeSystem().
					setHref(this.getUrlConstructor().createCodeSystemUrl(codeSystem));
			
			designation.getAssertedInCodeSystemVersion().getVersion().
					setHref(this.getUrlConstructor().createCodeSystemVersionUrl(codeSystem,codeSystemVersion));
			
			description.setDescribingCodeSystemVersion(designation.getAssertedInCodeSystemVersion());
			
			returnList.add(description);
		}
		
		return Iterables.toArray(returnList, DescriptionInCodeSystem.class);
	}


	@Override
	protected StateUpdater<EntityDescriptionDirectoryState> getResourceNameStateUpdater() {
		return new XpathStateUpdater<EntityDescriptionDirectoryState>(".//entity:entityID/core:name", "text()");
	}

	@Override
	protected EntityDirectoryEntry createSummary() {
		return new EntityDirectoryEntry();
	}

	@Override
	protected ResourceInfo<EntityDescription, ?> getResourceInfo() {
		return this.entityDescriptionResourceInfo;
	}

}
