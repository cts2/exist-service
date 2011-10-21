package edu.mayo.cts2.framework.plugin.service.exist.profile.codesystem;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry;
import edu.mayo.cts2.framework.plugin.service.exist.dao.CodeSystemExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistService;
import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemMaintenanceService;

@Component
public class ExistCodeSystemMaintenanceService 
	extends AbstractExistService<edu.mayo.cts2.framework.model.service.codesystem.CodeSystemMaintenanceService>
	implements CodeSystemMaintenanceService {

	@Resource
	private CodeSystemExistDao codeSystemExistDao;

	@Override
	public void createResource(String changeSetUri,
			CodeSystemCatalogEntry resource) {
		this.codeSystemExistDao.storeResource("", resource);
	}

	@Override
	public void updateResource(String changeSetUri,
			CodeSystemCatalogEntry resource) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteResource(String changeSetUri, String resourceName) {
		// TODO Auto-generated method stub
		
	}
}
