package edu.mayo.cts2.framework.plugin.service.exist.profile.conceptdomain;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.conceptdomain.ConceptDomainCatalogEntry;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ConceptDomainExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistNameOrUriReadService;
import edu.mayo.cts2.framework.service.profile.conceptdomain.ConceptDomainReadService;

@Component
public class ExistConceptDomainReadService 
	extends AbstractExistNameOrUriReadService<
		ConceptDomainCatalogEntry, 
		NameOrURI,
		edu.mayo.cts2.framework.model.service.conceptdomain.ConceptDomainReadService>
	implements ConceptDomainReadService {

	@Resource
	private ConceptDomainExistDao conceptDomainExistDao;

	@Override
	protected String createPath(NameOrURI resourceIdentifier) {
		return "";
	}

	
	@Override
	protected ExistDao<?, ConceptDomainCatalogEntry> getExistDao() {
		return this.conceptDomainExistDao;
	}
}
