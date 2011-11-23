package edu.mayo.cts2.framework.plugin.service.exist.profile.valuesetdefinition;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinition;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;

@Component
public class ValueSetDefinitionResourceInfo implements ResourceInfo<ValueSetDefinition,String> {

	private static final String VALUESETDEFINITIONS_PATH = "/valuesetdefinitions";

	@Override
	public String getResourceBasePath() {
		return VALUESETDEFINITIONS_PATH;
	}

	@Override
	public String getResourceXpath() {
		return "/valuesetdefinition:ValueSetDefinition";
	}
	
	@Override
	public boolean isReadByUri(String identifier) {
		return false;
	}

	@Override
	public String createPath(String id) {
		return "";
	}

	@Override
	public String createPathFromResource(ValueSetDefinition resource) {
		return "";
	}

	@Override
	public String getExistResourceName(String id) {
		return ExistServiceUtils.uriToExistName(id);
	}

	@Override
	public String getResourceUri(String id) {
		return id;
	}
	
	@Override
	public String getExistResourceNameFromResource(ValueSetDefinition resource) {
		return ExistServiceUtils.uriToExistName(resource.getDocumentURI());
	}
	
	@Override
	public String getUriXpath() {
		return "@documentURI";
	}

	@Override
	public String getResourceNameXpath() {
		return "@documentURI";
	}
	
}
