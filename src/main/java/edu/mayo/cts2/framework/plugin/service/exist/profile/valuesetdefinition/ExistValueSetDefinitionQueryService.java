package edu.mayo.cts2.framework.plugin.service.exist.profile.valuesetdefinition;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.filter.match.StateAdjustingPropertyReference;
import edu.mayo.cts2.framework.filter.match.StateAdjustingPropertyReference.StateUpdater;
import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference;
import edu.mayo.cts2.framework.model.core.SortCriteria;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinition;
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinitionDirectoryEntry;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistQueryService;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder.XpathState;
import edu.mayo.cts2.framework.service.command.restriction.ValueSetDefinitionQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.meta.StandardModelAttributeReference;
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

		summary.setHref(getUrlConstructor().createValueSetDefinitionUrl(
				"TODO",
				resource.getDocumentURI()));
		
		return summary;
	}

	private class ValueSetNameStateUpdater implements StateUpdater<XpathState> {

		@Override
		public XpathState updateState(XpathState currentState, MatchAlgorithmReference matchAlgorithm, String queryString) {
			currentState.setXpath("valueset:ValueSetCatalogEntry/@valueSetName = '" + queryString + "'");
			
			return currentState;
		}	
	}
	
	@SuppressWarnings("unchecked")
	protected List<StateAdjustingPropertyReference<XpathState>> getAvailableModelAttributeReferences() {
		StateAdjustingPropertyReference<XpathState> refName = 
				StateAdjustingPropertyReference.toPropertyReference(
					StandardModelAttributeReference.RESOURCE_NAME.getPropertyReference(),
						new ValueSetNameStateUpdater());
		
		return Arrays.asList(refName);
	}
	
	private class ValueSetDefinitionDirectoryBuilder extends XpathDirectoryBuilder<XpathState,ValueSetDefinitionDirectoryEntry> {
		
		public ValueSetDefinitionDirectoryBuilder(final String changeSetUri) {
			super(new XpathState(), new Callback<XpathState, ValueSetDefinitionDirectoryEntry>() {

				@Override
				public DirectoryResult<ValueSetDefinitionDirectoryEntry> execute(
						XpathState state, 
						int start, 
						int maxResults) {
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
							query.getReadContext()));
		
		return 
			builder.restrict(query.getFilterComponent()).
				restrict(query.getQuery()).
				addStart(page.getStart()).
				addMaxToReturn(page.getMaxToReturn()).
				resolve();
	}

	@Override
	public DirectoryResult<ValueSetDefinition> getResourceList(
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
