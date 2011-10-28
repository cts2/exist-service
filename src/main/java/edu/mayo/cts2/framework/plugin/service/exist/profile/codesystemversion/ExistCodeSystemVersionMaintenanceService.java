package edu.mayo.cts2.framework.plugin.service.exist.profile.codesystemversion;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.updates.ChangeableResourceChoice;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistMaintenanceService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionMaintenanceService;

@Component
public class ExistCodeSystemVersionMaintenanceService extends 
	AbstractExistMaintenanceService<CodeSystemVersionCatalogEntry,NameOrURI,edu.mayo.cts2.framework.model.service.codesystemversion.CodeSystemVersionMaintenanceService>
implements CodeSystemVersionMaintenanceService {
	
	@Resource
	private CodeSystemVersionResourceInfo codeSystemVersionResourceInfo;

	@Override
	protected ResourceInfo<CodeSystemVersionCatalogEntry, NameOrURI> getResourceInfo() {
		return this.codeSystemVersionResourceInfo;
	}

	@Override
	protected void addResourceToChangeableResourceChoice(
			ChangeableResourceChoice choice,
			CodeSystemVersionCatalogEntry resource) {
		choice.setCodeSystemVersion(resource);
	}

}
