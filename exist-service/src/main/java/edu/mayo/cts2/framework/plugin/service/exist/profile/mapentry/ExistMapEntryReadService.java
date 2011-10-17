package edu.mayo.cts2.framework.plugin.service.exist.profile.mapentry;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.mapversion.MapEntry;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.dao.MapEntryExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistReadService;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;
import edu.mayo.cts2.framework.service.profile.mapentry.MapEntryReadService;
import edu.mayo.cts2.framework.service.profile.mapentry.name.MapEntryName;

@Component
public class ExistMapEntryReadService 
	extends AbstractExistReadService<
	MapEntry,
	MapEntryName,
	edu.mayo.cts2.framework.model.service.mapentry.MapEntryReadService>
	implements MapEntryReadService {

	@Resource
	private MapEntryExistDao mapEntryExistDao;

	@Override
	public MapEntry read(MapEntryName identifier) {
		return this.mapEntryExistDao.getResource(
				identifier.getMapVersionName(),  
				ExistServiceUtils.getExistEntityName(identifier.getResourceId()));
	}

	@Override
	public boolean exists(MapEntryName identifier) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected ExistDao<?, MapEntry> getExistDao() {
		return this.mapEntryExistDao;
	}
}
