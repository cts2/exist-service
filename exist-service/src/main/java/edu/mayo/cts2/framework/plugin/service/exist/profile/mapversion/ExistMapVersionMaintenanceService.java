package edu.mayo.cts2.framework.plugin.service.exist.profile.mapversion;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.mapversion.MapVersion;
import edu.mayo.cts2.framework.plugin.service.exist.dao.MapVersionExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistService;
import edu.mayo.cts2.framework.service.profile.mapversion.MapVersionMaintenanceService;

@Component
public class ExistMapVersionMaintenanceService 
	extends AbstractExistService<edu.mayo.cts2.framework.model.service.mapversion.MapVersionMaintenanceService>
	implements MapVersionMaintenanceService {

	@Resource
	private MapVersionExistDao mapVersionExistDao;

	@Override
	public void createResource(String changeSetUri, MapVersion resource) {
		this.mapVersionExistDao.storeResource("", resource);
		
	}

	@Override
	public void updateResource(String changeSetUri, MapVersion resource) {
		this.mapVersionExistDao.storeResource(resource.getVersionOf().getContent(), resource);
	}

	@Override
	public void deleteResource(String changeSetUri, String resourceName) {
		throw new UnsupportedOperationException();
	}
}
