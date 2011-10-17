package edu.mayo.cts2.framework.plugin.service.exist.profile.mapversion;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.mapversion.MapVersion;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.dao.MapVersionExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistReadService;
import edu.mayo.cts2.framework.service.name.Name;
import edu.mayo.cts2.framework.service.profile.mapversion.MapVersionReadService;

@Component
public class ExistMapVersionReadService 
	extends AbstractExistReadService<
		MapVersion,
		Name,
		edu.mayo.cts2.framework.model.service.mapversion.MapVersionReadService>
	implements MapVersionReadService {

	@Resource
	private MapVersionExistDao mapVersionExistDao;

	@Override
	public MapVersion read(Name mapVersionName) {
		return this.mapVersionExistDao.getResource("", mapVersionName.getResourceId());
	}

	@Override
	public boolean exists(Name identifier) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected ExistDao<?, MapVersion> getExistDao() {
		return this.mapVersionExistDao;
	}
}
