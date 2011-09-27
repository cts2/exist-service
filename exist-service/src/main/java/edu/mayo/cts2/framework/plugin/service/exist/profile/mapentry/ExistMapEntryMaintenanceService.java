package edu.mayo.cts2.framework.plugin.service.exist.profile.mapentry;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.plugin.service.exist.dao.MapEntryExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistService;
import edu.mayo.cts2.sdk.model.mapversion.MapEntry;
import edu.mayo.cts2.sdk.service.profile.mapentry.MapEntryMaintenanceService;

@Component
public class ExistMapEntryMaintenanceService 
	extends AbstractExistService<edu.mayo.cts2.sdk.model.service.mapentry.MapEntryMaintenanceService>
	implements MapEntryMaintenanceService {

	@Resource
	private MapEntryExistDao mapEntryExistDao;

	@Override
	public void createResource(String changeSetUri, MapEntry resource) {
		//TODO
		this.mapEntryExistDao.storeResource(
				this.createPath(
				resource.getAssertedBy().getMapVersion().getContent()),
				resource);
	}

	@Override
	public void updateResource(String changeSetUri, MapEntry resource) {
		//TODO
		this.mapEntryExistDao.storeResource("", resource);
	}

	@Override
	public void deleteResource(String changeSetUri, String resourceName) {
		throw new UnsupportedOperationException();
	}
}
