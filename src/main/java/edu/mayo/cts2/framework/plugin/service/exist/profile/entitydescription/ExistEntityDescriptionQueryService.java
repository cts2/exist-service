package edu.mayo.cts2.framework.plugin.service.exist.profile.entitydescription;

import edu.mayo.cts2.framework.filter.match.StateAdjustingComponentReference.StateUpdater;
import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.*;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.entity.*;
import edu.mayo.cts2.framework.model.entity.types.DesignationRole;
import edu.mayo.cts2.framework.model.service.core.EntityNameOrURI;
import edu.mayo.cts2.framework.model.service.core.EntityNameOrURIList;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistQueryService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.PathInfo;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;
import edu.mayo.cts2.framework.plugin.service.exist.xpath.XpathStateBuildingRestriction;
import edu.mayo.cts2.framework.plugin.service.exist.xpath.XpathStateBuildingRestriction.AllOrAny;
import edu.mayo.cts2.framework.plugin.service.exist.xpath.XpathStateUpdater;
import edu.mayo.cts2.framework.service.command.restriction.EntityDescriptionQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.command.restriction.EntityDescriptionQueryServiceRestrictions.HierarchyRestriction.HierarchyType;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionQuery;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionQueryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class ExistEntityDescriptionQueryService 
	extends AbstractExistQueryService
		<EntityDescription,
		EntityDirectoryEntry,
		EntityDescriptionQueryServiceRestrictions,
		EntityDescriptionDirectoryState>    
	implements EntityDescriptionQueryService {

	@Resource
	private EntityDescriptionResourceInfo entityDescriptionResourceInfo;

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
		String codeSystemURI = base.getDescribingCodeSystemVersion().getCodeSystem().getUri();
		String codeSystemVersionName = 
				base.getDescribingCodeSystemVersion().getVersion().getContent();
		String codeSystemVersionURI = base.getDescribingCodeSystemVersion().getVersion().getUri();

		summary.setName(scopedName);
		summary.setAbout(base.getAbout());
		
		if(StringUtils.isNotBlank(codeSystemName) && 
				StringUtils.isNotBlank(codeSystemVersionName)){
			summary.setHref(getUrlConstructor().createEntityUrl(
					codeSystemName, 
					codeSystemVersionName, 
					ExistServiceUtils.getExternalEntityName(scopedName, codeSystemName)));
		}
		
		summary.addKnownEntityDescription(
				getDescriptionInCodeSystemVersion(
					 codeSystemName,
					 codeSystemURI,
					 codeSystemVersionName,
					 codeSystemVersionURI,
					 base));
		
		
		return summary;
	}
	
	@Override
	public DirectoryResult<EntityDirectoryEntry> getResourceSummaries(
			EntityDescriptionQuery query,
			SortCriteria sort,
			Page page) {
		
		EntityDescriptionDirectoryBuilder builder =
				new EntityDescriptionDirectoryBuilder(
						this.getChangeSetUri(query.getReadContext()));
		
		return builder.
				restrict(query.getRestrictions()).
				restrict(query.getFilterComponent()).
				restrict(query.getQuery()).
				addMaxToReturn(page.getEnd()).
				addStart(page.getStart()).
				resolve();
	}

	@Override
	public DirectoryResult<EntityListEntry> getResourceList(
            EntityDescriptionQuery query,
            SortCriteria sort,
            Page page) {
		throw new UnsupportedOperationException();

	}

	@Override
	public int count(
			EntityDescriptionQuery query){
        EntityDescriptionDirectoryBuilder builder =
                new EntityDescriptionDirectoryBuilder(
                        this.getChangeSetUri(query.getReadContext()));

        return builder.
                restrict(query.getRestrictions()).
                restrict(query.getFilterComponent()).
                restrict(query.getQuery()).
                count();
	}

	protected StateUpdater<EntityDescriptionDirectoryState> getResourceSynopsisStateUpdater() {
		return new XpathStateUpdater<EntityDescriptionDirectoryState>("*/entity:designation/core:value");
	}

	private class EntityDescriptionDirectoryBuilder extends XpathDirectoryBuilder<EntityDescriptionDirectoryState,EntityDirectoryEntry> {

		public EntityDescriptionDirectoryBuilder(final String changeSetUri) {
			super(new EntityDescriptionDirectoryState(), 
					new Callback<EntityDescriptionDirectoryState, EntityDirectoryEntry>() {

                private String getCodeSystemVersion(EntityDescriptionDirectoryState state){
                    String codeSystemVersion = "";

                    if(state.getCodeSystemVersions() != null &&
                            state.getCodeSystemVersions().size() == 1){
                        codeSystemVersion = state.getCodeSystemVersions().iterator().next().getName();
                    } else if(state.getCodeSystemVersions() != null &&
                            state.getCodeSystemVersions().size() > 1){
                        throw new UnsupportedOperationException("Cannot currently restrict to more than one CodeSystemVersion.");
                    }

                    return codeSystemVersion;
                }

				@Override
				public DirectoryResult<EntityDirectoryEntry> execute(
						EntityDescriptionDirectoryState state, 
						int start, 
						int maxResults) {
					String codeSystemVersion = getCodeSystemVersion(state);
					
					return getResourceSummaries(
							getResourceInfo(),
							changeSetUri,
							ExistServiceUtils.createPath(codeSystemVersion),
							state.getXpath(), 
							start, 
							maxResults);
				}

				@Override
				public int executeCount(EntityDescriptionDirectoryState state) {
                    String codeSystemVersion = getCodeSystemVersion(state);

                    return doCount(
                            getResourceInfo(),
                            changeSetUri,
                            ExistServiceUtils.createPath(codeSystemVersion),
                            state.getXpath());
				}},
				
				getSupportedMatchAlgorithms(),
				getSupportedSearchReferences());
		}
		
		public EntityDescriptionDirectoryBuilder restrict(final EntityDescriptionQueryServiceRestrictions restriction){
			getRestrictions().add(
					new StateBuildingRestriction<EntityDescriptionDirectoryState>() {

						@Override
						public EntityDescriptionDirectoryState restrict(EntityDescriptionDirectoryState currentState) {
							if(restriction != null &&
									restriction.getCodeSystemVersions() != null){
								currentState.setCodeSystemVersions(restriction.getCodeSystemVersions());
							}
							
							return currentState;
						}
					});
			if(restriction != null &&
					CollectionUtils.isNotEmpty(restriction.getEntities())){
				
				Set<ScopedEntityName> names = new HashSet<ScopedEntityName>();
                Set<String> uris = new HashSet<String>();
				for(EntityNameOrURI entityNameOrUri : restriction.getEntities()){
                    if(entityNameOrUri.getEntityName() != null){
                        names.add(entityNameOrUri.getEntityName());
                    } else {
                        uris.add(entityNameOrUri.getUri());
                    }
				}

                if(names.size() > 0){
                    for(final ScopedEntityName name : names){
                        getRestrictions().add(new StateBuildingRestriction<EntityDescriptionDirectoryState>() {
                            @Override
                            public EntityDescriptionDirectoryState restrict(EntityDescriptionDirectoryState state) {
                                boolean isBlankState = StringUtils.isBlank(state.getXpath());

                                String namespaceXpath = "";
                                if(! StringUtils.isBlank(name.getNamespace())){
                                    namespaceXpath = " and */entity:entityID/core:namespace = '" + name.getNamespace() + "'";
                                }

                                state.setXpath(
                                    state.getXpath() + (isBlankState ? "" : " | " + entityDescriptionResourceInfo.getResourceXpath()) +
                                            "[*/entity:entityID/core:name = '" + name.getName() + "'" + namespaceXpath + "]");

                                return state;
                            }
                        });
                    }
                }

                if(uris.size() > 0){
                    getRestrictions().add(
                            new XpathStateBuildingRestriction<EntityDescriptionDirectoryState>(
                                    "*",
                                    "@about",
                                    AllOrAny.ANY,
                                    uris));
                }
			}
			
			if(restriction != null && restriction.getHierarchyRestriction()!= null){

				if(restriction.getHierarchyRestriction().getHierarchyType() != HierarchyType.CHILDREN){
					throw new UnsupportedOperationException("Only CHILDREN queries supported.");
				}
				
				EntityNameOrURI parent = 
					restriction.getHierarchyRestriction().getEntity();
				
				if(parent.getEntityName() != null){
					getRestrictions().add(
						new XpathStateBuildingRestriction<EntityDescriptionDirectoryState>(
								"*/entity:parent/core:name",
								".", 
								AllOrAny.ANY,
								Arrays.asList(parent.getEntityName().getName())));
				} else {
					getRestrictions().add(
							new XpathStateBuildingRestriction<EntityDescriptionDirectoryState>(
								"*/entity:parent",
								"@uri",
								AllOrAny.ANY,
								Arrays.asList(parent.getUri())));
				}
			}
			return this;
		}
	}
	
	private Designation getPreferredDesignation(EntityDescriptionBase entity){
		if(entity.getDesignationCount() == 1){
			return entity.getDesignation(0);
		}
		
		for(Designation designation : entity.getDesignation()){
			if(designation.getDesignationRole().equals(DesignationRole.PREFERRED)){
				return designation;
			}
		}
		
		return null;
	}

	private DescriptionInCodeSystem getDescriptionInCodeSystemVersion(
			String codeSystem, 
			String codeSystemURI,
			String codeSystemVersion,
			String codeSystemVersionURI,
			EntityDescriptionBase entity) {

		Designation designation = this.getPreferredDesignation(entity);

		DescriptionInCodeSystem description = new DescriptionInCodeSystem();
		
		if(designation != null && designation.getValue() != null){
			description.setDesignation(designation.getValue().getContent());
		}

		description.setDescribingCodeSystemVersion(
				this.buildCodeSystemVersionReference(codeSystem, codeSystemURI, codeSystemVersion, codeSystemVersionURI));
		
		description.setHref(this.getUrlConstructor().createEntityUrl(codeSystem, codeSystemVersion, entity.getEntityID()));

		return description;
	}
	
	protected CodeSystemVersionReference buildCodeSystemVersionReference(String codeSystemName, String codeSystemURI, String codeSystemVersionName, 
			String codeSystemVersionURI){
		CodeSystemVersionReference ref = new CodeSystemVersionReference();
		
		ref.setCodeSystem(this.buildCodeSystemReference(codeSystemName, codeSystemURI));
		
		NameAndMeaningReference version = new NameAndMeaningReference();
		version.setContent(codeSystemVersionName);
		version.setUri(codeSystemVersionURI);
		version.setHref(this.getUrlConstructor().createCodeSystemVersionUrl(codeSystemName, codeSystemVersionName));
			
		ref.setVersion(version);
		
		return ref;
	}
	
	protected CodeSystemReference buildCodeSystemReference(String codeSystemName, String codeSystemURI){
		CodeSystemReference codeSystemReference = new CodeSystemReference();
		String codeSystemPath = this.getUrlConstructor().createCodeSystemUrl(codeSystemName);

		codeSystemReference.setContent(codeSystemName);
		codeSystemReference.setHref(codeSystemPath);
		codeSystemReference.setUri(codeSystemURI);
		
		return codeSystemReference;
	}
	
	@Override
	protected EntityDirectoryEntry createSummary() {
		return new EntityDirectoryEntry();
	}

	@Override
	protected PathInfo getResourceInfo() {
		return this.entityDescriptionResourceInfo;
	}

	@Override
	public Set<? extends VersionTagReference> getSupportedTags() {
		//TODO not actually supported, but the framework won't marshall unless this is populated
		return new HashSet<VersionTagReference>(Arrays.asList(new VersionTagReference[] {new VersionTagReference("CURRENT")}));
	}

	@Override
	public boolean isEntityInSet(
			EntityNameOrURI entity,
			EntityDescriptionQuery restrictions,
			ResolvedReadContext readContext) {
		throw new UnsupportedOperationException();
	}

	@Override
	public EntityReferenceList resolveAsEntityReferenceList(
			EntityDescriptionQuery restrictions,
			ResolvedReadContext readContext) {
		throw new UnsupportedOperationException();
	}

	@Override
	public EntityNameOrURIList intersectEntityList(
			Set<EntityNameOrURI> entities,
			EntityDescriptionQuery restrictions,
			ResolvedReadContext readContext) {
		throw new UnsupportedOperationException();
	}

}
