package edu.mayo.cts2.framework.plugin.service.exist.profile.valueset;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntry;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistMaintenanceService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
import edu.mayo.cts2.framework.service.profile.valueset.ValueSetMaintenanceService;

@Component
public class ExistValueSetMaintenanceService 
	extends AbstractExistMaintenanceService<ValueSetCatalogEntry,NameOrURI,edu.mayo.cts2.framework.model.service.valueset.ValueSetMaintenanceService>
	implements ValueSetMaintenanceService {

	@Resource
	private ValueSetResourceInfo valueSetResourceInfo;

	@Override
	protected ResourceInfo<ValueSetCatalogEntry, NameOrURI> getResourceInfo() {
		return this.valueSetResourceInfo;
	}

}
