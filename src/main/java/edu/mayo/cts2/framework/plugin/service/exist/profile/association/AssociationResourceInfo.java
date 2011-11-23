package edu.mayo.cts2.framework.plugin.service.exist.profile.association;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.association.Association;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;
import edu.mayo.cts2.framework.service.profile.association.name.AssociationReadId;

@Component
public class AssociationResourceInfo implements ResourceInfo<Association,AssociationReadId> {

	private static final String ASSOCIATIONS_PATH = "/associations";

	@Override
	public String getResourceBasePath(){
		return ASSOCIATIONS_PATH;	
	}

	@Override
	public String getResourceXpath() {
		return "/association:Association";
	}
	
	@Override
	public boolean isReadByUri(AssociationReadId identifier) {
		return !(identifier.getUri() == null);
	}

	@Override
	public String createPath(AssociationReadId id) {
		return ExistServiceUtils.createPath(id.getCodeSystemVersion().getName());
	}

	@Override
	public String createPathFromResource(Association resource) {
		return ExistServiceUtils.createPath(
				resource.getAssertedBy().getVersion().getContent());
	}

	@Override
	public String getExistResourceName(AssociationReadId id) {
		return id.getName();
	}

	@Override
	public String getResourceUri(AssociationReadId id) {
		return id.getUri();
	}


	@Override
	public String getExistResourceNameFromResource(Association entry) {
		return entry.getLocalID();
	}
	
	@Override
	public String getUriXpath() {
		return "@associationID";
	}

	@Override
	public String getResourceNameXpath() {
		return "@localID";
	}
	
}
