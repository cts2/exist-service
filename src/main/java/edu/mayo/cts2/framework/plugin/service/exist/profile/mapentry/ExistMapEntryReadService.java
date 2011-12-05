package edu.mayo.cts2.framework.plugin.service.exist.profile.mapentry;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.mapversion.MapEntry;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistDefaultReadService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.DefaultResourceInfo;
import edu.mayo.cts2.framework.service.profile.mapentry.MapEntryReadService;
import edu.mayo.cts2.framework.service.profile.mapentry.name.MapEntryReadId;

@Component
public class ExistMapEntryReadService 
	extends AbstractExistDefaultReadService<
	MapEntry,
	MapEntryReadId,
	edu.mayo.cts2.framework.model.service.mapentry.MapEntryReadService>
	implements MapEntryReadService {
	
	@Resource
	private MapEntryResourceInfo mapEntryResourceInfo;

	@Override
	protected DefaultResourceInfo<MapEntry, MapEntryReadId> getResourceInfo() {
		return this.mapEntryResourceInfo;
	}


}
