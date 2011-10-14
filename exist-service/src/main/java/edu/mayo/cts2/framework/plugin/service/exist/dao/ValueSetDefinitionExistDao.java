package edu.mayo.cts2.framework.plugin.service.exist.dao;

import org.springframework.stereotype.Component;
import org.xmldb.api.base.Resource;

import edu.mayo.cts2.framework.model.service.exception.UnknownResourceReference;
import edu.mayo.cts2.framework.model.service.exception.UnknownValueSetDefinition;
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinition;
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinitionDirectoryEntry;

@Component
public class ValueSetDefinitionExistDao extends AbstractResourceExistDao<ValueSetDefinitionDirectoryEntry, ValueSetDefinition> {

	private static final String VALUESETDEFINITIONS_PATH = "/valuesetdefinitions";
	
	@Override
	protected String getName(ValueSetDefinition entry) {
		return entry.getDocumentURI();
	}

	@Override
	protected ValueSetDefinitionDirectoryEntry createSummary() {
		return new ValueSetDefinitionDirectoryEntry();
	}

	@Override
	protected ValueSetDefinitionDirectoryEntry doTransform(
			ValueSetDefinition resource,
			ValueSetDefinitionDirectoryEntry summary, Resource eXistResource) {
		summary = this.baseTransform(summary, resource);
		
		summary.setDocumentURI(summary.getDocumentURI());
		summary.setHref(getUrlConstructor().createValueSetDefinitionUrl(
				"TODO",
				resource.getDocumentURI()));
		
		return summary;
	}

	@Override
	protected String doGetResourceBasePath() {
		return VALUESETDEFINITIONS_PATH;
	}
	

	@Override
	protected Class<? extends UnknownResourceReference> getUnknownResourceExceptionClass() {
		return UnknownValueSetDefinition.class;
	}

	@Override
	protected String getResourceXpath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getUriXpath() {
		// TODO Auto-generated method stub
		return null;
	}
}
