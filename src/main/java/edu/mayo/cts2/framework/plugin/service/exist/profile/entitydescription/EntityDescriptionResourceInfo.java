package edu.mayo.cts2.framework.plugin.service.exist.profile.entitydescription;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.entity.EntityDescription;
import edu.mayo.cts2.framework.model.entity.NamedEntityDescription;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;
import edu.mayo.cts2.framework.service.profile.entitydescription.name.EntityDescriptionReadId;

@Component
public class EntityDescriptionResourceInfo implements ResourceInfo<EntityDescription,EntityDescriptionReadId> {

	private static final String ENTITIES_PATH = "/entities";

	@Override
	public String getResourceBasePath() {
		return ENTITIES_PATH;
	}

	@Override
	public String getResourceXpath() {
		return "/entity:EntityDescription";
	}
	
	@Override
	public boolean isReadByUri(EntityDescriptionReadId identifier) {
		return StringUtils.isNotBlank(identifier.getUri());
	}

	@Override
	public String createPath(EntityDescriptionReadId id) {
		return ExistServiceUtils.createPath(id.getCodeSystemVersion().getName());
	}

	@Override
	public String createPathFromResource(EntityDescription resource) {
		String path = ModelUtils.getEntity(resource).getDescribingCodeSystemVersion().getVersion().getContent();
		
		return ExistServiceUtils.createPath(path);
	}

	@Override
	public String getExistResourceName(EntityDescriptionReadId id) {
		return ExistServiceUtils.getExistEntityName(id.getEntityName());
	}

	@Override
	public String getResourceUri(EntityDescriptionReadId id) {
		return id.getUri();
	}
	
	@Override
	public String getExistResourceNameFromResource(EntityDescription entry) {
		NamedEntityDescription entity = (NamedEntityDescription) ModelUtils.getEntity(entry);
		
		return ExistServiceUtils.getExistEntityName(entity);
	}
	
	@Override
	public String getUriXpath() {
		return "*/@about";
	}

}
