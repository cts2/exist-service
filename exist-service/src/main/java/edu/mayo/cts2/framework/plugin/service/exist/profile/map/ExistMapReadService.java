package edu.mayo.cts2.framework.plugin.service.exist.profile.map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.map.MapCatalogEntry;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.dao.MapExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistReadService;
import edu.mayo.cts2.framework.service.name.Name;
import edu.mayo.cts2.framework.service.profile.map.MapReadService;

@Component
public class ExistMapReadService 
	extends AbstractExistReadService<
		MapCatalogEntry,
		Name,
		edu.mayo.cts2.framework.model.service.map.MapCatalogReadService>   
	implements MapReadService {

	@Resource
	private MapExistDao mapExistDao;

	@Override
	public MapCatalogEntry read(Name mapName) {
		return this.mapExistDao.getResource("", mapName.getResourceId());
	}

	@Override
	public boolean exists(Name identifier) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected ExistDao<?, MapCatalogEntry> getExistDao() {
		return this.mapExistDao;
	}
}
