package edu.mayo.cts2.framework.plugin.service.exist.profile.valueset;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntry;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistDefaultReadService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.DefaultResourceInfo;
import edu.mayo.cts2.framework.service.profile.valueset.ValueSetReadService;

@Component
public class ExistValueSetReadService 
	extends AbstractExistDefaultReadService<
	ValueSetCatalogEntry,
	NameOrURI,
	edu.mayo.cts2.framework.model.service.valueset.ValueSetReadService>
	implements ValueSetReadService {
	
	@Resource
	private ValueSetResourceInfo valueSetResourceInfo;

	@Override
	protected DefaultResourceInfo<ValueSetCatalogEntry, NameOrURI> getResourceInfo() {
		return this.valueSetResourceInfo;
	}

}
