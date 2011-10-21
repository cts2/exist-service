package edu.mayo.cts2.framework.plugin.service.exist.profile.map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.map.MapCatalogEntry;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.dao.MapExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistNameOrUriReadService;
import edu.mayo.cts2.framework.service.profile.map.MapReadService;

@Component
public class ExistMapReadService 
	extends AbstractExistNameOrUriReadService<
		MapCatalogEntry,
		NameOrURI,
		edu.mayo.cts2.framework.model.service.map.MapCatalogReadService>   
	implements MapReadService {

	@Resource
	private MapExistDao mapExistDao;

	@Override
	protected ExistDao<?, MapCatalogEntry> getExistDao() {
		return this.mapExistDao;
	}

	@Override
	protected String createPath(NameOrURI resourceIdentifier) {
		return "";
	}
}
