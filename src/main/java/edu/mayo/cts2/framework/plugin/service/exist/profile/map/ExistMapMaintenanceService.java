package edu.mayo.cts2.framework.plugin.service.exist.profile.map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.map.MapCatalogEntry;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.updates.ChangeableResourceChoice;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistDefaultMaintenanceService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.DefaultResourceInfo;
import edu.mayo.cts2.framework.service.profile.map.MapMaintenanceService;

@Component
public class ExistMapMaintenanceService
	extends AbstractExistDefaultMaintenanceService<MapCatalogEntry,NameOrURI,edu.mayo.cts2.framework.model.service.map.MapCatalogMaintenanceService>  
	implements MapMaintenanceService {

	@Resource
	private MapResourceInfo mapResourceInfo;

	@Override
	protected DefaultResourceInfo<MapCatalogEntry, NameOrURI> getResourceInfo() {
		return this.mapResourceInfo;
	}

	@Override
	protected void addResourceToChangeableResourceChoice(
			ChangeableResourceChoice choice, MapCatalogEntry resource) {
		choice.setMap(resource);
	}

}
