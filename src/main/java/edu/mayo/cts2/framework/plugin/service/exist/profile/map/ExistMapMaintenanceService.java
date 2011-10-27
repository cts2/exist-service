package edu.mayo.cts2.framework.plugin.service.exist.profile.map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.map.MapCatalogEntry;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistMaintenanceService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
import edu.mayo.cts2.framework.service.profile.map.MapMaintenanceService;

@Component
public class ExistMapMaintenanceService
	extends AbstractExistMaintenanceService<MapCatalogEntry,NameOrURI,edu.mayo.cts2.framework.model.service.map.MapCatalogMaintenanceService>  
	implements MapMaintenanceService {

	@Resource
	private MapResourceInfo mapResourceInfo;

	@Override
	protected ResourceInfo<MapCatalogEntry, NameOrURI> getResourceInfo() {
		return this.mapResourceInfo;
	}

}
