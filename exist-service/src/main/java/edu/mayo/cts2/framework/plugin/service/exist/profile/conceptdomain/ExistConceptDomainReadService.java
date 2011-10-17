package edu.mayo.cts2.framework.plugin.service.exist.profile.conceptdomain;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.conceptdomain.ConceptDomainCatalogEntry;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ConceptDomainExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistReadService;
import edu.mayo.cts2.framework.service.name.Name;
import edu.mayo.cts2.framework.service.profile.conceptdomain.ConceptDomainReadService;

@Component
public class ExistConceptDomainReadService 
	extends AbstractExistReadService<
		ConceptDomainCatalogEntry, 
		Name,
		edu.mayo.cts2.framework.model.service.conceptdomain.ConceptDomainReadService>
	implements ConceptDomainReadService {

	@Resource
	private ConceptDomainExistDao conceptDomainExistDao;

	@Override
	public ConceptDomainCatalogEntry read(Name conceptDomainName) {
		return this.conceptDomainExistDao.getResource("", conceptDomainName.getResourceId());
	}

	@Override
	public boolean exists(Name identifier) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected ExistDao<?, ConceptDomainCatalogEntry> getExistDao() {
		return this.conceptDomainExistDao;
	}
}
