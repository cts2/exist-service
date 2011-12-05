package edu.mayo.cts2.framework.plugin.service.exist.profile.conceptdomain;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.conceptdomain.ConceptDomainCatalogEntry;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.updates.ChangeableResourceChoice;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistDefaultMaintenanceService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.DefaultResourceInfo;
import edu.mayo.cts2.framework.service.profile.conceptdomain.ConceptDomainMaintenanceService;

@Component
public class ExistConceptDomainMaintenanceService 
	extends AbstractExistDefaultMaintenanceService<ConceptDomainCatalogEntry,NameOrURI,edu.mayo.cts2.framework.model.service.conceptdomain.ConceptDomainMaintenanceService>
	implements ConceptDomainMaintenanceService {

	@Resource
	private ConceptDomainResourceInfo conceptDomainResourceInfo;

	@Override
	protected DefaultResourceInfo<ConceptDomainCatalogEntry, NameOrURI> getResourceInfo() {
		return this.conceptDomainResourceInfo;
	}

	@Override
	protected void addResourceToChangeableResourceChoice(
			ChangeableResourceChoice choice, ConceptDomainCatalogEntry resource) {
		choice.setConceptDomain(resource);
	}

}
