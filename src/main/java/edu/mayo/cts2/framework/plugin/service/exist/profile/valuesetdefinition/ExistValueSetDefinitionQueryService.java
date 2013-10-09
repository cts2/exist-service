package edu.mayo.cts2.framework.plugin.service.exist.profile.valuesetdefinition;

import javax.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.xmldb.api.base.XMLDBException;
import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.core.SortCriteria;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.exception.UnspecifiedCts2Exception;
import edu.mayo.cts2.framework.model.service.exception.UnknownValueSetDefinition;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinition;
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinitionDirectoryEntry;
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinitionListEntry;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistQueryService;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder.XpathState;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;
import edu.mayo.cts2.framework.service.command.restriction.ValueSetDefinitionQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionQuery;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionQueryService;

@Component
public class ExistValueSetDefinitionQueryService 
	extends AbstractExistQueryService
		<ValueSetDefinition,
		ValueSetDefinitionDirectoryEntry,
		ValueSetDefinitionQueryServiceRestrictions,
		XpathState>
	implements ValueSetDefinitionQueryService {

	@Resource
	private ValueSetDefinitionResourceInfo valueSetDefinitionResourceInfo;
	
	@Override
	protected ValueSetDefinitionDirectoryEntry createSummary() {
		return new ValueSetDefinitionDirectoryEntry();
	}

	@Override
	protected ValueSetDefinitionDirectoryEntry doTransform(
			ValueSetDefinition resource,
			ValueSetDefinitionDirectoryEntry summary, org.xmldb.api.base.Resource eXistResource) {
		summary = this.baseTransformResourceVersion(summary, resource);
		
		summary.setDefinedValueSet(resource.getDefinedValueSet());
		summary.setVersionTag(resource.getVersionTag());

		String name;
		try {
			name = ExistServiceUtils.getNameFromResourceName(eXistResource.getId());
		} catch (XMLDBException e) {
			throw new IllegalStateException(e);
	}
	
	summary.setHref(
		this.getUrlConstructor().createValueSetDefinitionUrl(resource.getDefinedValueSet().getContent(), name));
		
		return summary;
	}

	private class ValueSetDefinitionDirectoryBuilder extends XpathDirectoryBuilder<XpathState,ValueSetDefinitionDirectoryEntry> {
		
		public ValueSetDefinitionDirectoryBuilder(final String changeSetUri, final ValueSetDefinitionQueryServiceRestrictions vsdqServiceRestrictions) {
			super(new XpathState(), new Callback<XpathState, ValueSetDefinitionDirectoryEntry>() {

				@Override
				public DirectoryResult<ValueSetDefinitionDirectoryEntry> execute(
						XpathState state, 
						int start, 
						int maxResults) {
					
					if (vsdqServiceRestrictions != null && vsdqServiceRestrictions.getValueSet() != null)
					{
						if (StringUtils.isNotBlank(state.getXpath()))
						{
							throw new UnspecifiedCts2Exception("Unsupported operation - currently cannot combine state filters from a restriction with "
									+ "a valueSetDefinition restriction");
						}
						
						if (StringUtils.isNotBlank(vsdqServiceRestrictions.getValueSet().getName()))
						{
							state.setXpath("[valuesetdefinition:definedValueSet = '" + vsdqServiceRestrictions.getValueSet().getName() + "']");
						}
						else if (StringUtils.isNotBlank(vsdqServiceRestrictions.getValueSet().getUri()))
						{
							state.setXpath("[valuesetdefinition:definedValueSet/@uri = '" + vsdqServiceRestrictions.getValueSet().getUri() + "']");
						}
						else
						{
							UnknownValueSetDefinition uvsd = new UnknownValueSetDefinition();
							uvsd.setCts2Message(ModelUtils.createOpaqueData("No identifier was specified for the ValueSet"));
							throw uvsd;
						}
					}
					
					return getResourceSummaries(
							getResourceInfo(),
							changeSetUri,
							"",
							state.getXpath(), 
							start, 
							maxResults);
				}

				@Override
				public int executeCount(XpathState state) {
					throw new UnsupportedOperationException();
				}},
				
				getSupportedMatchAlgorithms(),
				getSupportedSearchReferences());
		}
	}

	@Override
	public DirectoryResult<ValueSetDefinitionDirectoryEntry> getResourceSummaries(
			ValueSetDefinitionQuery query, 
			SortCriteria sort,
			Page page) {
		ValueSetDefinitionDirectoryBuilder builder = 
			new ValueSetDefinitionDirectoryBuilder(
					this.getChangeSetUri(
							query.getReadContext()), query.getRestrictions());
		//TODO implement sort
		return 
			builder.restrict(query.getFilterComponent()).
				restrict(query.getQuery()).
				addStart(page.getStart()).
				addMaxToReturn(page.getMaxToReturn()).
				resolve();
	}

	@Override
	public DirectoryResult<ValueSetDefinitionListEntry> getResourceList(
			ValueSetDefinitionQuery query, 
			SortCriteria sort,
			Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int count(
			ValueSetDefinitionQuery query) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected ValueSetDefinitionResourceInfo getResourceInfo() {
		return this.valueSetDefinitionResourceInfo;
	}
}
