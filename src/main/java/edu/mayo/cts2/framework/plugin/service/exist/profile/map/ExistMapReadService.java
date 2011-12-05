package edu.mayo.cts2.framework.plugin.service.exist.profile.map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.map.MapCatalogEntry;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistDefaultReadService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.DefaultResourceInfo;
import edu.mayo.cts2.framework.service.profile.map.MapReadService;

@Component
public class ExistMapReadService 
	extends AbstractExistDefaultReadService<
		MapCatalogEntry,
		NameOrURI,
		edu.mayo.cts2.framework.model.service.map.MapCatalogReadService>   
	implements MapReadService {

	@Resource
	private MapResourceInfo mapResourceInfo;
	
	@Override
	protected DefaultResourceInfo<MapCatalogEntry, NameOrURI> getResourceInfo() {
		return this.mapResourceInfo;
	}

	
}
