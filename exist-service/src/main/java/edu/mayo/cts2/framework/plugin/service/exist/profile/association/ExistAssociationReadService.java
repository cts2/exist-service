package edu.mayo.cts2.framework.plugin.service.exist.profile.association;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.plugin.service.exist.dao.AssociationExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistService;
import edu.mayo.cts2.sdk.model.association.Association;
import edu.mayo.cts2.sdk.service.profile.association.AssociationReadService;
import edu.mayo.cts2.sdk.service.profile.association.id.AssociationId;

@Component
public class ExistAssociationReadService 
	extends AbstractExistService<edu.mayo.cts2.sdk.model.service.association.AssociationReadService>
	implements AssociationReadService {

	@Resource
	private AssociationExistDao associationExistDao;


	@Override
	public Association read(AssociationId identifier) {
		return this.associationExistDao.getResource(
				this.createPath(identifier.getCodeSystemVersion()), identifier.getAssociationUri());
	}

	@Override
	public boolean exists(AssociationId identifier) {
		throw new UnsupportedOperationException();
	}
}