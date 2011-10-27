package edu.mayo.cts2.framework.plugin.service.exist.profile.conceptdomain;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.conceptdomain.ConceptDomainCatalogEntry;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistReadService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
import edu.mayo.cts2.framework.service.profile.conceptdomain.ConceptDomainReadService;

@Component
public class ExistConceptDomainReadService 
	extends AbstractExistReadService<
		ConceptDomainCatalogEntry, 
		NameOrURI,
		edu.mayo.cts2.framework.model.service.conceptdomain.ConceptDomainReadService>
	implements ConceptDomainReadService {
		
	@Resource
	private ConceptDomainResourceInfo conceptDomainResourceInfo;
	
	
	@Override
	protected ResourceInfo<ConceptDomainCatalogEntry, NameOrURI> getResourceInfo() {
		return this.conceptDomainResourceInfo;
	}
}
