package edu.mayo.cts2.framework.plugin.service.exist.profile.association;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.association.Association;
import edu.mayo.cts2.framework.plugin.service.exist.dao.AssociationExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistReadService;
import edu.mayo.cts2.framework.service.profile.association.AssociationReadService;
import edu.mayo.cts2.framework.service.profile.association.name.AssociationId;

@Component
public class ExistAssociationReadService 
	extends AbstractExistReadService<
		Association,
		AssociationId,
		edu.mayo.cts2.framework.model.service.association.AssociationReadService>
	implements AssociationReadService {

	@Resource
	private AssociationExistDao associationExistDao;

	@Override
	public Association read(AssociationId uri) {
		return this.associationExistDao.getResource(
				this.createPath(uri.getCodeSystemVersion()), uri.getResourceId());
	}

	@Override
	public boolean exists(AssociationId identifier) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected ExistDao<?, Association> getExistDao() {
		return this.associationExistDao;
	}
}
