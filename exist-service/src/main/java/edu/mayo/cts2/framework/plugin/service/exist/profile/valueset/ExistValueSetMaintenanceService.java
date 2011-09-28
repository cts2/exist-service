package edu.mayo.cts2.framework.plugin.service.exist.profile.valueset;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntry;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ValueSetExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistService;
import edu.mayo.cts2.framework.service.profile.valueset.ValueSetMaintenanceService;

@Component
public class ExistValueSetMaintenanceService 
	extends AbstractExistService<edu.mayo.cts2.framework.model.service.valueset.ValueSetMaintenanceService>
	implements ValueSetMaintenanceService {

	@Resource
	private ValueSetExistDao valueSetExistDao;

	@Override
	public void createResource(String changeSetUri,
			ValueSetCatalogEntry resource) {
		this.valueSetExistDao.storeResource("", resource);
	}

	@Override
	public void updateResource(String changeSetUri,
			ValueSetCatalogEntry resource) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteResource(String changeSetUri, String resourceName) {
		throw new UnsupportedOperationException();
	}
}
