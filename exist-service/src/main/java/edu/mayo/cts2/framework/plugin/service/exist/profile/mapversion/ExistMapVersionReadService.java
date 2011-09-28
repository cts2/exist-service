package edu.mayo.cts2.framework.plugin.service.exist.profile.mapversion;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.mapversion.MapVersion;
import edu.mayo.cts2.framework.plugin.service.exist.dao.MapVersionExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistService;
import edu.mayo.cts2.framework.service.profile.mapversion.MapVersionReadService;

@Component
public class ExistMapVersionReadService 
	extends AbstractExistService<edu.mayo.cts2.framework.model.service.mapversion.MapVersionReadService>
	implements MapVersionReadService {

	@Resource
	private MapVersionExistDao mapVersionExistDao;

	@Override
	public MapVersion read(String mapVersionName) {
		return this.mapVersionExistDao.getResource("", mapVersionName);
	}

	@Override
	public boolean exists(String identifier) {
		throw new UnsupportedOperationException();
	}
}
