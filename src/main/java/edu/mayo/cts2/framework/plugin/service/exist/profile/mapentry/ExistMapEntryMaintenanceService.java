package edu.mayo.cts2.framework.plugin.service.exist.profile.mapentry;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.mapversion.MapEntry;
import edu.mayo.cts2.framework.model.updates.ChangeableResource;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistDefaultMaintenanceService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.DefaultResourceInfo;
import edu.mayo.cts2.framework.service.profile.mapentry.MapEntryMaintenanceService;
import edu.mayo.cts2.framework.service.profile.mapentry.name.MapEntryReadId;

@Component
public class ExistMapEntryMaintenanceService 
	extends AbstractExistDefaultMaintenanceService<MapEntry,MapEntryReadId,edu.mayo.cts2.framework.model.service.mapentry.MapEntryMaintenanceService>
	implements MapEntryMaintenanceService {

	@Resource
	private MapEntryResourceInfo mapEntryResourceInfo;

	@Override
	protected DefaultResourceInfo<MapEntry, MapEntryReadId> getResourceInfo() {
		return this.mapEntryResourceInfo;
	}

	@Override
	protected void addResourceToChangeableResource(
			ChangeableResource choice, MapEntry resource) {
		choice.setMapEntry(resource);
	}

	@Override
	protected MapEntry getResourceFromChangeableResource(
			ChangeableResource choice) {
		return choice.getMapEntry();
	}

}
