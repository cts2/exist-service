package edu.mayo.cts2.framework.plugin.service.exist.profile.association;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.association.Association;
import edu.mayo.cts2.framework.plugin.service.exist.dao.AssociationExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistReadService;
import edu.mayo.cts2.framework.service.profile.association.AssociationReadService;
import edu.mayo.cts2.framework.service.profile.association.name.AssociationReadId;

@Component
public class ExistAssociationReadService 
	extends AbstractExistReadService<
		Association,
		AssociationReadId,
		edu.mayo.cts2.framework.model.service.association.AssociationReadService>
	implements AssociationReadService {

	@Resource
	private AssociationExistDao associationExistDao;

	protected boolean isReadByUri(AssociationReadId resourceIdentifier){
		return true;
	}

	@Override
	protected ExistDao<?, Association> getExistDao() {
		return this.associationExistDao;
	}

	@Override
	protected String createPath(AssociationReadId resourceIdentifier) {
		return "";
	}

	@Override
	protected String getResourceName(AssociationReadId resourceIdentifier) {
		throw new UnsupportedOperationException("Associations cannot be referenced by a Exist Name.");
	}

	@Override
	protected String getResourceUri(AssociationReadId resourceIdentifier) {
		return resourceIdentifier.getAssociationUri();
	}	
}