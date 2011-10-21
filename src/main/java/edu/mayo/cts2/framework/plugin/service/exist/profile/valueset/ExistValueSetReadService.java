package edu.mayo.cts2.framework.plugin.service.exist.profile.valueset;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntry;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ValueSetExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistNameOrUriReadService;
import edu.mayo.cts2.framework.service.profile.valueset.ValueSetReadService;

@Component
public class ExistValueSetReadService 
	extends AbstractExistNameOrUriReadService<
	ValueSetCatalogEntry,
	NameOrURI,
	edu.mayo.cts2.framework.model.service.valueset.ValueSetReadService>
	implements ValueSetReadService {

	@Resource
	private ValueSetExistDao valueSetExistDao;


	@Override
	protected ExistDao<?, ValueSetCatalogEntry> getExistDao() {
		return this.valueSetExistDao;
	}

	@Override
	protected String createPath(NameOrURI resourceIdentifier) {
		return "";
	}
}
