package edu.mayo.cts2.framework.plugin.service.exist.profile.conceptdomain;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.conceptdomain.ConceptDomainCatalogEntry;
import edu.mayo.cts2.framework.model.service.core.EntityNameOrURI;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistDefaultReadService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.DefaultResourceInfo;
import edu.mayo.cts2.framework.service.profile.conceptdomain.ConceptDomainReadService;

@Component
public class ExistConceptDomainReadService 
	extends AbstractExistDefaultReadService<
		ConceptDomainCatalogEntry, 
		NameOrURI,
		edu.mayo.cts2.framework.model.service.conceptdomain.ConceptDomainReadService>
	implements ConceptDomainReadService {
		
	@Resource
	private ConceptDomainResourceInfo conceptDomainResourceInfo;
	
	
	@Override
	protected DefaultResourceInfo<ConceptDomainCatalogEntry, NameOrURI> getResourceInfo() {
		return this.conceptDomainResourceInfo;
	}


	@Override
	public ConceptDomainCatalogEntry readByDefiningEntity(
			EntityNameOrURI entity, ResolvedReadContext resolvedReadContext) {
		throw new UnsupportedOperationException();
	}


	@Override
	public boolean existsDefiningEntity(EntityNameOrURI entity,
			ResolvedReadContext resolvedReadContext) {
		throw new UnsupportedOperationException();
	}
}
