package edu.mayo.cts2.framework.plugin.service.exist.dao;

import org.springframework.stereotype.Component;
import org.xmldb.api.base.Resource;

import edu.mayo.cts2.framework.model.service.exception.UnknownResourceReference;
import edu.mayo.cts2.framework.model.service.exception.UnknownValueSet;
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntry;
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntrySummary;

@Component
public class ValueSetExistDao extends AbstractResourceExistDao<ValueSetCatalogEntrySummary, ValueSetCatalogEntry> {

	private static final String CODESYSTEMS_PATH = "/valuesets";
	
	@Override
	protected String getName(ValueSetCatalogEntry entry) {
		return entry.getValueSetName();
	}

	@Override
	protected ValueSetCatalogEntrySummary createSummary() {
		return new ValueSetCatalogEntrySummary();
	}

	@Override
	protected ValueSetCatalogEntrySummary doTransform(
			ValueSetCatalogEntry resource,
			ValueSetCatalogEntrySummary summary, Resource eXistResource11) {
		summary = this.baseTransform(summary, resource);
		
		summary.setValueSetName(resource.getValueSetName());
		summary.setHref(getUrlConstructor().createValueSetUrl(resource.getValueSetName()));
		
		return summary;
	}

	@Override
	protected String doGetResourceBasePath() {
		return CODESYSTEMS_PATH;
	}
	
	@Override
	protected Class<? extends UnknownResourceReference> getUnknownResourceExceptionClass() {
		return UnknownValueSet.class;
	}
}
