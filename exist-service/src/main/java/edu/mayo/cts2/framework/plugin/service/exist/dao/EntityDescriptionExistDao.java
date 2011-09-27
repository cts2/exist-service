package edu.mayo.cts2.framework.plugin.service.exist.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.xmldb.api.base.Resource;

import com.google.common.collect.Iterables;

import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;
import edu.mayo.cts2.sdk.model.core.DescriptionInCodeSystem;
import edu.mayo.cts2.sdk.model.core.ScopedEntityName;
import edu.mayo.cts2.sdk.model.entity.Designation;
import edu.mayo.cts2.sdk.model.entity.EntityDescription;
import edu.mayo.cts2.sdk.model.entity.EntityDescriptionBase;
import edu.mayo.cts2.sdk.model.entity.EntityDirectoryEntry;
import edu.mayo.cts2.sdk.model.entity.NamedEntityDescription;
import edu.mayo.cts2.sdk.model.service.exception.UnknownEntity;
import edu.mayo.cts2.sdk.model.service.exception.UnknownResourceReference;
import edu.mayo.cts2.sdk.model.util.RestModelUtils;

@Component
public class EntityDescriptionExistDao extends
		AbstractResourceExistDao<EntityDirectoryEntry, EntityDescription> {

	private static final String ENTITIES_PATH = "/entities";

	@Override
	public EntityDirectoryEntry doTransform(
			EntityDescription resource,
			EntityDirectoryEntry summary, Resource eXistResource) {
	
		ScopedEntityName scopedName = getScopedEntityName(resource);
		
		EntityDescriptionBase base = RestModelUtils.getEntity(resource);
		
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
	public EntityDescription getResource(String path, String name) {
		EntityDescription ed = 
				 super.getResource(path, name);
		
		EntityDescriptionBase entity = 
				RestModelUtils.getEntity(ed);
		
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
	protected String getName(EntityDescription entry) {
		NamedEntityDescription entity = (NamedEntityDescription) RestModelUtils.getEntity(entry);
		
		return ExistServiceUtils.getExistEntityName(entity);
	}

	protected ScopedEntityName getScopedEntityName(EntityDescription entry) {
		NamedEntityDescription entity = (NamedEntityDescription) RestModelUtils.getEntity(entry);
		
		return entity.getEntityID();
	}
	
	@Override
	protected String doGetResourceBasePath() {
		return ENTITIES_PATH;
	}

	@Override
	protected EntityDirectoryEntry createSummary() {
		return new EntityDirectoryEntry();
	}

	@Override
	protected Class<? extends UnknownResourceReference> getUnknownResourceExceptionClass() {
		return UnknownEntity.class;
	}
}