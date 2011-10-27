package edu.mayo.cts2.framework.plugin.service.exist.profile.entitydescription;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.entity.EntityDescription;
import edu.mayo.cts2.framework.model.entity.EntityDescriptionBase;
import edu.mayo.cts2.framework.model.service.core.ReadContext;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistReadService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionReadService;
import edu.mayo.cts2.framework.service.profile.entitydescription.name.EntityDescriptionReadId;

@Component
public class ExistEntityDescriptionReadService 
	extends AbstractExistReadService<
		EntityDescription,
		EntityDescriptionReadId,
		edu.mayo.cts2.framework.model.service.entitydescription.EntityDescriptionReadService>   
	implements EntityDescriptionReadService {
	
	@Resource
	private EntityDescriptionResourceInfo entityDescriptionResourceInfo;

	@Override
	public EntityDescription read(EntityDescriptionReadId id, ReadContext readContext) {
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
	protected ResourceInfo<EntityDescription, EntityDescriptionReadId> getResourceInfo() {
		return this.entityDescriptionResourceInfo;
	}

}
