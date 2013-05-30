package edu.mayo.cts2.framework.plugin.service.exist.profile.entitydescription;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.CodeSystemReference;
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference;
import edu.mayo.cts2.framework.model.core.EntityReference;
import edu.mayo.cts2.framework.model.core.SortCriteria;
import edu.mayo.cts2.framework.model.core.VersionTagReference;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.entity.EntityDescription;
import edu.mayo.cts2.framework.model.entity.EntityDescriptionBase;
import edu.mayo.cts2.framework.model.entity.EntityListEntry;
import edu.mayo.cts2.framework.model.service.core.EntityNameOrURI;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistDefaultReadService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.DefaultResourceInfo;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionReadService;
import edu.mayo.cts2.framework.service.profile.entitydescription.name.EntityDescriptionReadId;

@Component
public class ExistEntityDescriptionReadService 
	extends AbstractExistDefaultReadService<
		EntityDescription,
		EntityDescriptionReadId,
		edu.mayo.cts2.framework.model.service.entitydescription.EntityDescriptionReadService>   
	implements EntityDescriptionReadService {
	
	@Resource
	private EntityDescriptionResourceInfo entityDescriptionResourceInfo;

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
	
		return ed;
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
