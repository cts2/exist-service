package edu.mayo.cts2.framework.plugin.service.exist.profile.valuesetresolution;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.extension.LocalIdValueSetResolution;
import edu.mayo.cts2.framework.plugin.service.exist.profile.LocalIdResourceInfo;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;
import edu.mayo.cts2.framework.service.profile.valuesetresolution.name.ValueSetResolutionReadId;

@Component
public class ValueSetResolutionResourceInfo implements LocalIdResourceInfo<LocalIdValueSetResolution,ValueSetResolutionReadId> {

	private static final String VALUESETRESOLUTIONS_PATH = "/valuesetresolutions";

	@Override
	public String getResourceBasePath() {
		return VALUESETRESOLUTIONS_PATH;
	}

	@Override
	public String getResourceXpath() {
		return "/valuesetdefinition:ResolvedValueSet";
	}
	
	@Override
	public boolean isReadByUri(ValueSetResolutionReadId identifier) {
		return !(identifier.getUri() == null);
	}

	@Override
	public String createPath(ValueSetResolutionReadId id) {
		return ExistServiceUtils.createPath(id.getValueSet().getName());
	}

	@Override
	public String createPathFromResource(LocalIdValueSetResolution resource) {
		return ExistServiceUtils.createPath(
				resource.getResource().
					getResolutionInfo().
						getResolutionOf().
							getValueSetDefinition().
								getContent());
	}

	@Override
	public String getExistResourceName(ValueSetResolutionReadId id) {
		return id.getName();
	}

	@Override
	public String getResourceUri(ValueSetResolutionReadId id) {
		return id.getUri();
	}
	
	@Override
	public String getUriXpath() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getResourceNameXpath() {
		throw new UnsupportedOperationException();
	}
	
}
