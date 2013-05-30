package edu.mayo.cts2.framework.plugin.service.exist.profile.codesystemversion;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.updates.ChangeableResource;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistDefaultMaintenanceService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.DefaultResourceInfo;
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionMaintenanceService;

@Component
public class ExistCodeSystemVersionMaintenanceService extends 
	AbstractExistDefaultMaintenanceService<CodeSystemVersionCatalogEntry,NameOrURI,edu.mayo.cts2.framework.model.service.codesystemversion.CodeSystemVersionMaintenanceService>
implements CodeSystemVersionMaintenanceService {
	
	@Resource
	private CodeSystemVersionResourceInfo codeSystemVersionResourceInfo;

	@Override
	protected DefaultResourceInfo<CodeSystemVersionCatalogEntry, NameOrURI> getResourceInfo() {
		return this.codeSystemVersionResourceInfo;
	}

	@Override
	protected void addResourceToChangeableResource(
			ChangeableResource choice,
			CodeSystemVersionCatalogEntry resource) {
		choice.setCodeSystemVersion(resource);
	}

	@Override
	protected CodeSystemVersionCatalogEntry getResourceFromChangeableResource(
			ChangeableResource choice) {
		return choice.getCodeSystemVersion();
	}

}
