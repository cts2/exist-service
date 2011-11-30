package edu.mayo.cts2.framework.plugin.service.exist.profile.conceptdomainbinding;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.conceptdomainbinding.ConceptDomainBinding;
import edu.mayo.cts2.framework.model.core.ChangeableElementGroup;
import edu.mayo.cts2.framework.model.extension.LocalIdConceptDomainBinding;
import edu.mayo.cts2.framework.model.updates.ChangeableResourceChoice;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistLocalIdMaintenanceService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
import edu.mayo.cts2.framework.service.profile.conceptdomainbinding.ConceptDomainBindingMaintenanceService;
import edu.mayo.cts2.framework.service.profile.conceptdomainbinding.name.ConceptDomainBindingReadId;

@Component
public class ExistConceptDomainBindingMaintenanceService 
	extends AbstractExistLocalIdMaintenanceService<
	ConceptDomainBinding,
	LocalIdConceptDomainBinding,
	ConceptDomainBindingReadId,
	edu.mayo.cts2.framework.model.service.conceptdomainbinding.ConceptDomainBindingMaintenanceService> 
	implements ConceptDomainBindingMaintenanceService {

	@Resource
	private ConceptDomainBindingResourceInfo conceptDomainBindingResourceInfo;

	@Override
	protected ResourceInfo<LocalIdConceptDomainBinding, ConceptDomainBindingReadId> getResourceInfo() {
		return this.conceptDomainBindingResourceInfo;
	}

	@Override
	protected void addResourceToChangeableResourceChoice(
			ChangeableResourceChoice choice, LocalIdConceptDomainBinding resource) {
		choice.setConceptDomainBinding(resource.getResource());
	}
	
	protected ChangeableElementGroup getChangeableElementGroup(LocalIdConceptDomainBinding resource){
		return resource.getResource().getChangeableElementGroup();
	}

	@Override
	protected LocalIdConceptDomainBinding createLocalIdResource(String localId,
			ConceptDomainBinding resource) {
		return new LocalIdConceptDomainBinding(localId, resource);
	}
}
