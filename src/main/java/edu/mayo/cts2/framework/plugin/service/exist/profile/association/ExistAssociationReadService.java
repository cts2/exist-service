package edu.mayo.cts2.framework.plugin.service.exist.profile.association;

import edu.mayo.cts2.framework.model.association.Association;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.ChangeableElementGroup;
import edu.mayo.cts2.framework.model.extension.LocalIdAssociation;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistLocalIdReadService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
import edu.mayo.cts2.framework.service.profile.association.AssociationReadService;
import edu.mayo.cts2.framework.service.profile.association.name.AssociationReadId;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ExistAssociationReadService 
	extends AbstractExistLocalIdReadService<
            Association,
            LocalIdAssociation,
            AssociationReadId,
            edu.mayo.cts2.framework.model.service.association.AssociationReadService>
	implements AssociationReadService {

	@Resource
	private AssociationResourceInfo associationResourceInfo;

	@Override
	protected ResourceInfo<AssociationReadId> getResourceInfo() {
		return this.associationResourceInfo;
	}

	@Override
	public LocalIdAssociation readByExternalStatementId(
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

    @Override
    protected ChangeableElementGroup getChangeableElementGroup(LocalIdAssociation resource) {
        return resource.getChangeableElementGroup();
    }

    @Override
    protected LocalIdAssociation createLocalIdResource(String id, Association resource) {
        return new LocalIdAssociation(id, resource);
    }
}
