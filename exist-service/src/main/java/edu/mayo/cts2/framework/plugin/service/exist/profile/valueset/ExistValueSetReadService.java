package edu.mayo.cts2.framework.plugin.service.exist.profile.valueset;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntry;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ValueSetExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistReadService;
import edu.mayo.cts2.framework.service.name.Name;
import edu.mayo.cts2.framework.service.profile.valueset.ValueSetReadService;

@Component
public class ExistValueSetReadService 
	extends AbstractExistReadService<
	ValueSetCatalogEntry,
	Name,
	edu.mayo.cts2.framework.model.service.valueset.ValueSetReadService>
	implements ValueSetReadService {

	@Resource
	private ValueSetExistDao valueSetExistDao;

	@Override
	public ValueSetCatalogEntry read(Name identifier) {
		return this.valueSetExistDao.getResource("", identifier.getResourceId());
	}

	@Override
	public boolean exists(Name identifier) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected ExistDao<?, ValueSetCatalogEntry> getExistDao() {
		return this.valueSetExistDao;
	}
}
