package edu.mayo.cts2.framework.plugin.service.exist.profile.entitydescription;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.xmldb.api.base.XMLDBException;
import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.CodeSystemReference;
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference;
import edu.mayo.cts2.framework.model.core.DescriptionInCodeSystem;
import edu.mayo.cts2.framework.model.core.EntityReference;
import edu.mayo.cts2.framework.model.core.SortCriteria;
import edu.mayo.cts2.framework.model.core.VersionTagReference;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.entity.EntityDescription;
import edu.mayo.cts2.framework.model.entity.EntityDescriptionBase;
import edu.mayo.cts2.framework.model.entity.EntityDirectoryEntry;
import edu.mayo.cts2.framework.model.entity.EntityListEntry;
import edu.mayo.cts2.framework.model.service.core.EntityNameOrURI;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistDefaultReadService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.DefaultResourceInfo;
import edu.mayo.cts2.framework.plugin.service.exist.profile.association.AssociationResourceInfo;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;
import edu.mayo.cts2.framework.service.command.restriction.EntityDescriptionQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntitiesFromAssociationsQuery;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionQuery;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionQueryService;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionReadService;
import edu.mayo.cts2.framework.service.profile.entitydescription.name.EntityDescriptionReadId;

@Component
public class ExistEntityDescriptionReadService 
	extends AbstractExistDefaultReadService<
		EntityDescription,
		EntityDescriptionReadId,
		edu.mayo.cts2.framework.model.service.entitydescription.EntityDescriptionReadService>   
	implements EntityDescriptionReadService {

    protected final Log log = LogFactory.getLog(getClass().getName());
	
	@Resource
	private EntityDescriptionResourceInfo entityDescriptionResourceInfo;

    @Resource
    private AssociationResourceInfo associationResourceInfo;

    @Resource
    private EntityDescriptionQueryService entityDescriptionQueryService;

	@Override
	public EntityDescription read(EntityDescriptionReadId id, ResolvedReadContext readContext) {
		EntityDescription ed = 
				 super.read(id, readContext);
		
		if(ed == null){
			return null;
		}
		
		EntityDescriptionBase entity = 
				ModelUtils.getEntity(ed);
		
		String codeSystemName = entity.getDescribingCodeSystemVersion().
				getCodeSystem().getContent();
		
		String codeSystemVersionName = entity.getDescribingCodeSystemVersion().
				getVersion().getContent();
		
		entity.getDescribingCodeSystemVersion().
			getCodeSystem().
			setHref(this.getUrlConstructor().createCodeSystemUrl(codeSystemName));
		
		entity.getDescribingCodeSystemVersion().
			getVersion().
			setHref(this.getUrlConstructor().createCodeSystemVersionUrl(codeSystemName, codeSystemVersionName));

        try {
            if(this.hasChildren(entity.getAbout())){
                entity.setChildren(
                    this.getUrlConstructor().createChildrenUrl(
                        codeSystemName, codeSystemVersionName,
                            ExistServiceUtils.getExternalEntityName(entity.getEntityID(), codeSystemName)));
            }
        } catch(XMLDBException e){
            log.warn(e);
        }

        try {
            if(this.hasSubjectOf(entity.getAbout())){
                entity.setSubjectOf(
                        this.getUrlConstructor().createSubjectOfUrl(
                                codeSystemName, codeSystemVersionName,
                                ExistServiceUtils.getExternalEntityName(entity.getEntityID(), codeSystemName)));
            }
        } catch(XMLDBException e){
            log.warn(e);
        }

        try {
            if(this.hasTargetOf(entity.getAbout())){
                entity.setTargetOf(
                        this.getUrlConstructor().createTargetOfUrl(
                                codeSystemName, codeSystemVersionName,
                                ExistServiceUtils.getExternalEntityName(entity.getEntityID(), codeSystemName)));
            }
        } catch(XMLDBException e){
            log.warn(e);
        }


        return ed;
	}

    protected boolean hasSubjectOf(String entityUri) throws XMLDBException {
        return this.getExistResourceDao().
            query(this.associationResourceInfo.getResourceBasePath(),
                    "//association:Association/association:subject[@uri = '" + entityUri + "']", 0, 1).getIterator().hasMoreResources();
    }

    protected boolean hasTargetOf(String entityUri) throws XMLDBException {
        return this.getExistResourceDao().
                query(this.associationResourceInfo.getResourceBasePath(),
                        "//association:Association/association:target/core:entity[@uri = '"+entityUri+"']", 0, 1).getIterator().hasMoreResources();
    }

    protected boolean hasChildren(String entityUri) throws XMLDBException {
        return this.getExistResourceDao().
                query(this.getResourceInfo().getResourceBasePath(), "//entity:parent[@uri = '"+entityUri+"']", 0, 1).getIterator().hasMoreResources();
    }

	@Override
	protected String getExtraPathForUriLookup(EntityDescriptionReadId resourceIdentifier){
		return this.getResourceInfo().createPath(resourceIdentifier);
	}
	
	@Override
	protected DefaultResourceInfo<EntityDescription, EntityDescriptionReadId> getResourceInfo() {
		return this.entityDescriptionResourceInfo;
	}

	@Override
	public DirectoryResult<EntityListEntry> readEntityDescriptions(
			final EntityNameOrURI entityId,
            SortCriteria sortCriteria,
			final ResolvedReadContext readContext,
            Page page) {
        throw new UnsupportedOperationException();
	}

	@Override
	public EntityReference availableDescriptions(
            final EntityNameOrURI entityId,
			final ResolvedReadContext readContext) {
        Page page = new Page();
        page.setMaxToReturn(Integer.MAX_VALUE);

        DirectoryResult<EntityDirectoryEntry> result = this.entityDescriptionQueryService.getResourceSummaries(new EntityDescriptionQuery() {
            @Override
            public EntitiesFromAssociationsQuery getEntitiesFromAssociationsQuery() {
                return null;
            }

            @Override
            public EntityDescriptionQueryServiceRestrictions getRestrictions() {
                EntityDescriptionQueryServiceRestrictions restrictions = new EntityDescriptionQueryServiceRestrictions();
                restrictions.setEntities(new HashSet<EntityNameOrURI>(Arrays.asList(entityId)));

                return restrictions;
            }

            @Override
            public Query getQuery() {
                return null;
            }

            @Override
            public Set<ResolvedFilter> getFilterComponent() {
                return null;
            }

            @Override
            public ResolvedReadContext getReadContext() {
                return readContext;
            }
        }, null, page);

        if(result == null || CollectionUtils.isEmpty(result.getEntries())){
            return null;
        } else {
            EntityReference reference = new EntityReference();

            EntityDirectoryEntry firstEntry = result.getEntries().get(0);

            reference.setAbout(firstEntry.getAbout());
            reference.setName(firstEntry.getName());

            for(EntityDirectoryEntry entry : result.getEntries()){
                for(DescriptionInCodeSystem description : entry.getKnownEntityDescription()){
                    reference.addKnownEntityDescription(description);
                }
            }

            return reference;
        }
	}

	@Override
	public List<EntityListEntry> readEntityDescriptions(EntityNameOrURI entityId,
                ResolvedReadContext readContext) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<CodeSystemReference> getKnownCodeSystems() {
		// TODO
		return new ArrayList<CodeSystemReference>();
	}

	@Override
	public List<CodeSystemVersionReference> getKnownCodeSystemVersions() {
		// TODO
		return new ArrayList<CodeSystemVersionReference>();
	}

	@Override
	public List<VersionTagReference> getSupportedVersionTags() {
		// tag isn't supported yet, but Castor requires this list to be populated. 
		return Arrays.asList(new VersionTagReference[] {new VersionTagReference("THIS_DOESN'T_WORK")});
	}
}
