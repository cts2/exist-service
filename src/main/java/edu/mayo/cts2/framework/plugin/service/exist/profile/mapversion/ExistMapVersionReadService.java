package edu.mayo.cts2.framework.plugin.service.exist.profile.mapversion;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.mapversion.MapVersion;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistDefaultReadService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.DefaultResourceInfo;
import edu.mayo.cts2.framework.service.profile.mapversion.MapVersionReadService;

@Component
public class ExistMapVersionReadService 
	extends AbstractExistDefaultReadService<
		MapVersion,
		NameOrURI,
		edu.mayo.cts2.framework.model.service.mapversion.MapVersionReadService>
	implements MapVersionReadService {

	@Resource
	private MapVersionResourceInfo mapVersionResourceInfo;

	@Override
	protected DefaultResourceInfo<MapVersion, NameOrURI> getResourceInfo() {
		return this.mapVersionResourceInfo;
	}

	@Override
	public boolean existsMapVersionForMap(
			NameOrURI map, 
			String tagName,
			ResolvedReadContext readContext) {
		throw new UnsupportedOperationException();
	}

	@Override
	public MapVersion readMapVersionForMap(
			NameOrURI map, 
			String tagName,
			ResolvedReadContext readContext) {
		throw new UnsupportedOperationException();
	}
}
