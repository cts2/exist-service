package edu.mayo.cts2.framework.plugin.service.exist.profile.mapversion;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.mapversion.MapVersion;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.dao.MapVersionExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistNameOrUriReadService;
import edu.mayo.cts2.framework.service.profile.mapversion.MapVersionReadService;

@Component
public class ExistMapVersionReadService 
	extends AbstractExistNameOrUriReadService<
		MapVersion,
		NameOrURI,
		edu.mayo.cts2.framework.model.service.mapversion.MapVersionReadService>
	implements MapVersionReadService {

	@Resource
	private MapVersionExistDao mapVersionExistDao;

	
	@Override
	protected ExistDao<?, MapVersion> getExistDao() {
		return this.mapVersionExistDao;
	}

	@Override
	protected String createPath(NameOrURI resourceIdentifier) {
		return "";
	}
}
