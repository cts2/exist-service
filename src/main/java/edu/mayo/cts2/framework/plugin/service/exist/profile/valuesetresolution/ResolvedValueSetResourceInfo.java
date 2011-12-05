package edu.mayo.cts2.framework.plugin.service.exist.profile.valuesetresolution;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.core.ValueSetDefinitionReference;
import edu.mayo.cts2.framework.model.valuesetdefinition.ResolvedValueSet;
import edu.mayo.cts2.framework.plugin.service.exist.profile.LocalIdResourceInfo;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;
import edu.mayo.cts2.framework.service.profile.valuesetresolution.name.ResolvedValueSetReadId;

@Component
public class ResolvedValueSetResourceInfo implements LocalIdResourceInfo<ResolvedValueSet,ResolvedValueSetReadId> {

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
	public boolean isReadByUri(ResolvedValueSetReadId identifier) {
		return false;
	}

	@Override
	public String createPath(ResolvedValueSetReadId id) {
		return ExistServiceUtils.createPath(
				id.getValueSet().getName(),
				id.getValueSetDefinition().getName());
	}

	@Override
	public String createPathFromResource(ResolvedValueSet resource) {
		ValueSetDefinitionReference resolution = resource.getResolutionInfo().
			getResolutionOf();
		return ExistServiceUtils.createPath(
				resolution.
					getValueSet().
						getContent(),
				resolution.
					getValueSetDefinition().
						getContent());
	}

	@Override
	public String getExistResourceName(ResolvedValueSetReadId id) {
		return id.getLocalName();
	}

	@Override
	public String getResourceUri(ResolvedValueSetReadId id) {
		throw new UnsupportedOperationException();
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
