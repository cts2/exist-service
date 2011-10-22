package edu.mayo.cts2.framework.plugin.service.exist.dao;

import org.springframework.stereotype.Component;
import org.xmldb.api.base.Resource;

import edu.mayo.cts2.framework.model.association.Association;
import edu.mayo.cts2.framework.model.association.AssociationDirectoryEntry;

@Component
public class AssociationExistDao extends AbstractResourceExistDao<AssociationDirectoryEntry,Association> {

	private static final String ASSOCIATIONS_PATH = "/associations";

	protected String doGetResourceBasePath(){
		return ASSOCIATIONS_PATH;	
	}

	@Override
	protected String getName(Association entry) {
		return entry.getAssociationID();
	}

	@Override
	protected AssociationDirectoryEntry createSummary() {
		return new AssociationDirectoryEntry();
	}

	@Override
	protected AssociationDirectoryEntry doTransform(Association resource,
			AssociationDirectoryEntry summary, Resource eXistResource) {
		
		return summary;
	}

	@Override
	protected String getResourceXpath() {
		return "/association:Association";
	}

	@Override
	protected String getUriXpath() {
		return "@associationID";
	}
}
