package edu.mayo.cts2.framework.plugin.service.exist.profile.codesystemversion;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.plugin.service.exist.dao.CodeSystemVersionExistDao;
import edu.mayo.cts2.sdk.model.codesystemversion.CodeSystemVersionCatalogEntry;
import edu.mayo.cts2.sdk.service.profile.codesystemversion.CodeSystemVersionMaintenanceService;

@Component
public class ExistCodeSystemVersionMaintenanceService implements CodeSystemVersionMaintenanceService {

	@Resource
	private CodeSystemVersionExistDao codeSystemVersionExistDao;

	@Override
	public void createResource(String changeSetUri,
			CodeSystemVersionCatalogEntry resource) {
		this.codeSystemVersionExistDao.storeResource(
				"", 
				resource);
	}

	@Override
	public void updateResource(String changeSetUri,
			CodeSystemVersionCatalogEntry resource) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteResource(String changeSetUri, String resourceName) {
		throw new UnsupportedOperationException();
	}
}
