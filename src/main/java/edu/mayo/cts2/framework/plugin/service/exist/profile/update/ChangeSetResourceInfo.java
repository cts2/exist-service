package edu.mayo.cts2.framework.plugin.service.exist.profile.update;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.updates.ChangeSet;
import edu.mayo.cts2.framework.plugin.service.exist.profile.DefaultResourceInfo;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;

@Component
public class ChangeSetResourceInfo implements DefaultResourceInfo<ChangeSet,String> {

	protected static final String CHANGESETS_PATH = "/changesets";

	@Override
	public String getResourceBasePath() {
		return CHANGESETS_PATH;
	}

	@Override
	public String getResourceXpath() {
		return "/updates:ChangeSet";
	}
	
	@Override
	public String createPath(String resourceIdentifier) {
		return "";
	}

	@Override
	public String createPathFromResource(ChangeSet resource) {
		return "";
	}

	@Override
	public String getExistResourceNameFromResource(ChangeSet resource) {
		return ExistServiceUtils.uriToExistName(resource.getChangeSetURI());
	}

	@Override
	public String getUriXpath() {
		return "@changeSetURI";
	}

	@Override
	public boolean isReadByUri(String identifier) {
		return true;
	}

	@Override
	public String getExistResourceName(String resourceIdentifier) {
		return ExistServiceUtils.uriToExistName(resourceIdentifier);
	}

	@Override
	public String getResourceUri(String resourceIdentifier) {
		return resourceIdentifier;
	}

	@Override
	public String getResourceNameXpath() {
		return "@changeSetURI";
	}
}
