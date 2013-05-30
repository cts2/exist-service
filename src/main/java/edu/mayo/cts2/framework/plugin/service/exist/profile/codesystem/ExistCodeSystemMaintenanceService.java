package edu.mayo.cts2.framework.plugin.service.exist.profile.codesystem;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.updates.ChangeableResource;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistDefaultMaintenanceService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.DefaultResourceInfo;
import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemMaintenanceService;

@Component
public class ExistCodeSystemMaintenanceService 
	extends AbstractExistDefaultMaintenanceService<CodeSystemCatalogEntry,NameOrURI,edu.mayo.cts2.framework.model.service.codesystem.CodeSystemMaintenanceService>
	implements CodeSystemMaintenanceService {
	
	@Resource
	private CodeSystemResourceInfo codeSystemResourceInfo;

	@Override
	protected DefaultResourceInfo<CodeSystemCatalogEntry,NameOrURI> getResourceInfo() {
		return this.codeSystemResourceInfo;
	}
	
	@Override
	protected void addResourceToChangeableResource(
			ChangeableResource choice, CodeSystemCatalogEntry resource) {
		choice.setCodeSystem(resource);
	}

	@Override
	protected CodeSystemCatalogEntry getResourceFromChangeableResource(
			ChangeableResource choice) {
		return choice.getCodeSystem();
	}

}
