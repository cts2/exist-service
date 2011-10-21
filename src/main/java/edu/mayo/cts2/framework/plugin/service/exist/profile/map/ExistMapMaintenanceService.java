package edu.mayo.cts2.framework.plugin.service.exist.profile.map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.map.MapCatalogEntry;
import edu.mayo.cts2.framework.plugin.service.exist.dao.MapExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistService;
import edu.mayo.cts2.framework.service.profile.map.MapMaintenanceService;

@Component
public class ExistMapMaintenanceService
	extends AbstractExistService<edu.mayo.cts2.framework.model.service.map.MapCatalogMaintenanceService>  
	implements MapMaintenanceService {

	@Resource
	private MapExistDao mapExistDao;


	@Override
	public void createResource(String changeSetUri, MapCatalogEntry resource) {
		this.mapExistDao.storeResource("", resource);
	}

	@Override
	public void updateResource(String changeSetUri, MapCatalogEntry resource) {
		this.mapExistDao.storeResource("", resource);
	}

	@Override
	public void deleteResource(String changeSetUri, String resourceName) {
		throw new UnsupportedOperationException();
	}
}
