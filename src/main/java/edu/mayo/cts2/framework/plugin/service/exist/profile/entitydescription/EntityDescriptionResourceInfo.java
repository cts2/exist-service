package edu.mayo.cts2.framework.plugin.service.exist.profile.entitydescription;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry;
import edu.mayo.cts2.framework.model.entity.EntityDescription;
import edu.mayo.cts2.framework.model.entity.NamedEntityDescription;
import edu.mayo.cts2.framework.model.service.exception.UnknownCodeSystemVersion;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.plugin.service.exist.profile.DefaultResourceInfo;
import edu.mayo.cts2.framework.plugin.service.exist.profile.codesystemversion.ExistCodeSystemVersionReadService;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;
import edu.mayo.cts2.framework.plugin.service.valueSetDefinitionResolutionServices.ctsUtility.queryBuilders.CodeSystemVersionQueryBuilder;
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionQuery;
import edu.mayo.cts2.framework.service.profile.entitydescription.name.EntityDescriptionReadId;

@Component
public class EntityDescriptionResourceInfo implements DefaultResourceInfo<EntityDescription,EntityDescriptionReadId> {

	@Autowired
	ExistCodeSystemVersionReadService ecsvrs;
	
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
		if (id.getCodeSystemVersion() != null)
		{
			if (StringUtils.isNotEmpty(id.getCodeSystemVersion().getName()))
			{
				return ExistServiceUtils.createPath(id.getCodeSystemVersion().getName());
			}
			else if (StringUtils.isNotEmpty(id.getCodeSystemVersion().getUri()))
			{
				//TODO move this builder to some proper shared code... (other buiders too, probably)
				CodeSystemVersionQuery query = CodeSystemVersionQueryBuilder.build(null);
				query.getRestrictions().setCodeSystem(id.getCodeSystemVersion());
				CodeSystemVersionCatalogEntry result = ecsvrs.read(id.getCodeSystemVersion(), null);
				
				if (result != null)
				{
					return ExistServiceUtils.createPath(result.getCodeSystemVersionName());
				}
				else
				{
					throw new UnknownCodeSystemVersion();
				}
			}
		}
		return "";
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

	@Override
	public String getResourceNameXpath() {
		return ".//entity:entityID/core:name";
	}

}
