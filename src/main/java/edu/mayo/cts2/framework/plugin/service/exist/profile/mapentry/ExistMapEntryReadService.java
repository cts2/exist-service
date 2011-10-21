package edu.mayo.cts2.framework.plugin.service.exist.profile.mapentry;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.mapversion.MapEntry;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.dao.MapEntryExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistReadService;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;
import edu.mayo.cts2.framework.service.profile.mapentry.MapEntryReadService;
import edu.mayo.cts2.framework.service.profile.mapentry.name.MapEntryReadId;

@Component
public class ExistMapEntryReadService 
	extends AbstractExistReadService<
	MapEntry,
	MapEntryReadId,
	edu.mayo.cts2.framework.model.service.mapentry.MapEntryReadService>
	implements MapEntryReadService {

	@Resource
	private MapEntryExistDao mapEntryExistDao;


	@Override
	protected ExistDao<?, MapEntry> getExistDao() {
		return this.mapEntryExistDao;
	}


	@Override
	protected boolean isReadByUri(MapEntryReadId id) {
		return StringUtils.isNotBlank(id.getUri());
	}


	@Override
	protected String createPath(MapEntryReadId id) {
		return this.createPath(id.getMapVersionName());
	}


	@Override
	protected String getResourceName(
			MapEntryReadId id) {
		return ExistServiceUtils.getExistEntityName(id.getEntityName());
	}


	@Override
	protected String getResourceUri(MapEntryReadId id) {
		return id.getUri();
	}
}
