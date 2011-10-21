package edu.mayo.cts2.framework.plugin.service.exist.profile.entitydescription;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.entity.EntityDescription;
import edu.mayo.cts2.framework.plugin.service.exist.dao.EntityDescriptionExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistReadService;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;
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
	private EntityDescriptionExistDao entityDescriptionExistDao;

	@Override
	protected ExistDao<?, EntityDescription> getExistDao() {
		return this.entityDescriptionExistDao;
	}

	@Override
	protected boolean isReadByUri(EntityDescriptionReadId id) {
		return StringUtils.isNotBlank(id.getUri());
	}

	@Override
	protected String createPath(EntityDescriptionReadId id) {
		return this.createPath(id.getCodeSystemVersion().getName());
	}

	@Override
	protected String getResourceName(
			EntityDescriptionReadId id) {
		return ExistServiceUtils.getExistEntityName(id.getEntityName());
	}

	@Override
	protected String getResourceUri(EntityDescriptionReadId id) {
		return id.getUri();
	}
}
