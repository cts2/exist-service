package edu.mayo.cts2.framework.plugin.service.exist.profile.mapentry;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.mapversion.MapEntry;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistMaintenanceService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
import edu.mayo.cts2.framework.service.profile.mapentry.MapEntryMaintenanceService;
import edu.mayo.cts2.framework.service.profile.mapentry.name.MapEntryReadId;

@Component
public class ExistMapEntryMaintenanceService 
	extends AbstractExistMaintenanceService<MapEntry,MapEntryReadId,edu.mayo.cts2.framework.model.service.mapentry.MapEntryMaintenanceService>
	implements MapEntryMaintenanceService {

	@Resource
	private MapEntryResourceInfo mapEntryResourceInfo;

	@Override
	protected ResourceInfo<MapEntry, MapEntryReadId> getResourceInfo() {
		return this.mapEntryResourceInfo;
	}
}
