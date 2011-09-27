package edu.mayo.cts2.framework.plugin.service.exist.profile.mapentry;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.plugin.service.exist.dao.MapEntryExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistService;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;
import edu.mayo.cts2.sdk.model.mapversion.MapEntry;
import edu.mayo.cts2.sdk.service.profile.mapentry.MapEntryReadService;
import edu.mayo.cts2.sdk.service.profile.mapentry.id.MapEntryId;

@Component
public class ExistMapEntryReadService 
	extends AbstractExistService<edu.mayo.cts2.sdk.model.service.mapentry.MapEntryReadService>
	implements MapEntryReadService {

	@Resource
	private MapEntryExistDao mapEntryExistDao;

	@Override
	public MapEntry read(MapEntryId identifier) {
		return this.mapEntryExistDao.getResource(
				identifier.getMapVersion(),  
				ExistServiceUtils.getExistEntityName(identifier.getMapsFrom()));
	}

	@Override
	public boolean exists(MapEntryId identifier) {
		throw new UnsupportedOperationException();
	}
}
