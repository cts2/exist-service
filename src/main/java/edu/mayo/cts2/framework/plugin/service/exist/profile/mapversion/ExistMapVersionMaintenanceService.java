package edu.mayo.cts2.framework.plugin.service.exist.profile.mapversion;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.mapversion.MapVersion;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistMaintenanceService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
import edu.mayo.cts2.framework.service.profile.mapversion.MapVersionMaintenanceService;

@Component
public class ExistMapVersionMaintenanceService 
	extends AbstractExistMaintenanceService<MapVersion,NameOrURI,edu.mayo.cts2.framework.model.service.mapversion.MapVersionMaintenanceService>
	implements MapVersionMaintenanceService {

	@Resource
	private MapVersionResourceInfo mapVersionResourceInfo;
	
	@Override
	protected ResourceInfo<MapVersion, NameOrURI> getResourceInfo() {
		return this.mapVersionResourceInfo;
	}

}
