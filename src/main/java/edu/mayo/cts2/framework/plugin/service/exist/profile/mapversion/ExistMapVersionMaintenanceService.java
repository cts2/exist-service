package edu.mayo.cts2.framework.plugin.service.exist.profile.mapversion;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.mapversion.MapVersion;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.updates.ChangeableResource;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistDefaultMaintenanceService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.DefaultResourceInfo;
import edu.mayo.cts2.framework.service.profile.mapversion.MapVersionMaintenanceService;

@Component
public class ExistMapVersionMaintenanceService 
	extends AbstractExistDefaultMaintenanceService<MapVersion,NameOrURI,edu.mayo.cts2.framework.model.service.mapversion.MapVersionMaintenanceService>
	implements MapVersionMaintenanceService {

	@Resource
	private MapVersionResourceInfo mapVersionResourceInfo;
	
	@Override
	protected DefaultResourceInfo<MapVersion, NameOrURI> getResourceInfo() {
		return this.mapVersionResourceInfo;
	}

	@Override
	protected void addResourceToChangeableResource(
			ChangeableResource choice, MapVersion resource) {
		choice.setMapVersion(resource);
	}

	@Override
	protected MapVersion getResourceFromChangeableResource(
			ChangeableResource choice) {
		return choice.getMapVersion();
	}
	
}
