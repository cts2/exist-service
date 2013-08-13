package edu.mayo.cts2.framework.plugin.service.exist.profile.entitydescription;

import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.*;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.entity.EntityDescription;
import edu.mayo.cts2.framework.model.entity.EntityDescriptionBase;
import edu.mayo.cts2.framework.model.entity.EntityListEntry;
import edu.mayo.cts2.framework.model.service.core.EntityNameOrURI;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistDefaultReadService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.DefaultResourceInfo;
import edu.mayo.cts2.framework.plugin.service.exist.profile.association.AssociationResourceInfo;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionReadService;
import edu.mayo.cts2.framework.service.profile.entitydescription.name.EntityDescriptionReadId;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.xmldb.api.base.XMLDBException;

import javax.annotation.Resource;
import java.util.List;

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
            if(this.hasParents(entity.getAbout())){
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
                        this.getUrlConstructor().createChildrenUrl(
                                codeSystemName, codeSystemVersionName,
                                ExistServiceUtils.getExternalEntityName(entity.getEntityID(), codeSystemName)));
            }
        } catch(XMLDBException e){
            log.warn(e);
        }

        try {
            if(this.hasTargetOf(entity.getAbout())){
                entity.setTargetOf(
                        this.getUrlConstructor().createChildrenUrl(
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
                    "//association:Association/association:subject[@uri &= \"" + entityUri + "\"]", 0, 1).getIterator().hasMoreResources();
    }

    protected boolean hasTargetOf(String entityUri) throws XMLDBException {
        return this.getExistResourceDao().
                query(this.associationResourceInfo.getResourceBasePath(),
                        "//association:Association/association:target/core:entity[@uri &= \""+entityUri+"\"]", 0, 1).getIterator().hasMoreResources();
    }

    protected boolean hasParents(String entityUri) throws XMLDBException {
        return this.getExistResourceDao().
                query(this.getResourceInfo().getResourceBasePath(), "//entity:parent[@uri &= \""+entityUri+"\"]", 0, 1).getIterator().hasMoreResources();
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
			EntityNameOrURI entityId, SortCriteria sortCriteria,
			ResolvedReadContext readContext, Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public EntityReference availableDescriptions(EntityNameOrURI entityId,
			ResolvedReadContext readContext) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<EntityListEntry> readEntityDescriptions(EntityNameOrURI entityId,
			ResolvedReadContext readContext) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<CodeSystemReference> getKnownCodeSystems() {
		// TODO
		return null;
	}

	@Override
	public List<CodeSystemVersionReference> getKnownCodeSystemVersions() {
		// TODO
		return null;
	}

	@Override
	public List<VersionTagReference> getSupportedVersionTags() {
		// TODO
		return null;
	}
}
