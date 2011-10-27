package edu.mayo.cts2.framework.plugin.service.exist.profile.codesystem;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistMaintenanceService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemMaintenanceService;

@Component
public class ExistCodeSystemMaintenanceService 
	extends AbstractExistMaintenanceService<CodeSystemCatalogEntry,NameOrURI,edu.mayo.cts2.framework.model.service.codesystem.CodeSystemMaintenanceService>
	implements CodeSystemMaintenanceService {
	
	@Resource
	private CodeSystemResourceInfo codeSystemResourceInfo;

	@Override
	protected ResourceInfo<CodeSystemCatalogEntry, NameOrURI> getResourceInfo() {
		return this.codeSystemResourceInfo;
	}
	
}
