package edu.mayo.cts2.framework.plugin.service.exist.profile.map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.map.MapCatalogEntry;
import edu.mayo.cts2.framework.plugin.service.exist.dao.MapExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistService;
import edu.mayo.cts2.framework.service.profile.map.MapReadService;

@Component
public class ExistMapReadService 
	extends AbstractExistService<edu.mayo.cts2.framework.model.service.map.MapCatalogReadService>   
	implements MapReadService {

	@Resource
	private MapExistDao mapExistDao;

	@Override
	public MapCatalogEntry read(String mapName) {
		return this.mapExistDao.getResource("", mapName);
	}

	@Override
	public boolean exists(String identifier) {
		throw new UnsupportedOperationException();
	}
}
