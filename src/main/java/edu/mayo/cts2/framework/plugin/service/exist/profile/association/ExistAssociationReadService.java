package edu.mayo.cts2.framework.plugin.service.exist.profile.association;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.association.Association;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistDefaultReadService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.DefaultResourceInfo;
import edu.mayo.cts2.framework.service.profile.association.AssociationReadService;
import edu.mayo.cts2.framework.service.profile.association.name.AssociationReadId;

@Component
public class ExistAssociationReadService 
	extends AbstractExistDefaultReadService<
		Association,
		AssociationReadId,
		edu.mayo.cts2.framework.model.service.association.AssociationReadService>
	implements AssociationReadService {

	@Resource
	private AssociationResourceInfo associationResourceInfo;

	protected boolean isReadByUri(AssociationReadId resourceIdentifier){
		return true;
	}

	@Override
	protected DefaultResourceInfo<Association, AssociationReadId> getResourceInfo() {
		return this.associationResourceInfo;
	}

	@Override
	public Association readByExternalStatementId(
			String exteralStatementId,
			String scopingNamespaceName, 
			ResolvedReadContext readContext) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean existsByExternalStatementId(
			String exteralStatementId,
			String scopingNamespaceName, 
			ResolvedReadContext readContext) {
		throw new UnsupportedOperationException();
	}
}
