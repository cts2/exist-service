package edu.mayo.cts2.framework.plugin.service.exist.dao;

import org.springframework.stereotype.Component;
import org.xmldb.api.base.Resource;

import edu.mayo.cts2.sdk.model.association.Association;
import edu.mayo.cts2.sdk.model.association.AssociationDirectoryEntry;
import edu.mayo.cts2.sdk.model.service.exception.UnknownAssociation;
import edu.mayo.cts2.sdk.model.service.exception.UnknownResourceReference;

@Component
public class AssociationExistDao extends AbstractResourceExistDao<AssociationDirectoryEntry,Association> {

	private static final String ASSOCIATIONS_PATH = "/associations";

	protected String doGetResourceBasePath(){
		return CTS2_RESOURCES_PATH + 
			ASSOCIATIONS_PATH;	
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
	protected Class<? extends UnknownResourceReference> getUnknownResourceExceptionClass() {
		return UnknownAssociation.class;
	}
}
