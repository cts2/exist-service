package edu.mayo.cts2.framework.plugin.service.exist.profile.valuesetdefinition;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.plugin.service.exist.dao.ValueSetDefinitionExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistQueryService;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder.XpathState;
import edu.mayo.cts2.sdk.filter.match.StateAdjustingModelAttributeReference;
import edu.mayo.cts2.sdk.filter.match.StateAdjustingModelAttributeReference.StateUpdater;
import edu.mayo.cts2.sdk.model.core.FilterComponent;
import edu.mayo.cts2.sdk.model.core.MatchAlgorithmReference;
import edu.mayo.cts2.sdk.model.core.PredicateReference;
import edu.mayo.cts2.sdk.model.directory.DirectoryResult;
import edu.mayo.cts2.sdk.model.service.core.Query;
import edu.mayo.cts2.sdk.model.valuesetdefinition.ValueSetDefinition;
import edu.mayo.cts2.sdk.model.valuesetdefinition.ValueSetDefinitionDirectoryEntry;
import edu.mayo.cts2.sdk.service.command.Page;
import edu.mayo.cts2.sdk.service.command.restriction.ValueSetDefinitionQueryServiceRestrictions;
import edu.mayo.cts2.sdk.service.meta.StandardModelAttributeReference;
import edu.mayo.cts2.sdk.service.profile.valuesetdefinition.ValueSetDefinitionQueryService;

@Component
public class ExistValueSetDefinitionQueryService 
	extends AbstractExistQueryService
		<edu.mayo.cts2.sdk.model.service.valuesetdefinition.ValueSetDefinitionQueryService,XpathState>
	implements ValueSetDefinitionQueryService {

	@Resource
	private ValueSetDefinitionExistDao valueSetDefinitionExistDao;

	@Override
	public PredicateReference getPropertyReference(String nameOrUri) {
		// TODO Auto-generated method stub
		return null;
	}

	private class ValueSetNameStateUpdater implements StateUpdater<XpathState> {

		@Override
		public XpathState updateState(XpathState currentState, MatchAlgorithmReference matchAlgorithm, String queryString) {
			currentState.setXpath("valueset:ValueSetCatalogEntry/@valueSetName = '" + queryString + "'");
			
			return currentState;
		}	
	}
	
	@SuppressWarnings("unchecked")
	protected List<StateAdjustingModelAttributeReference<XpathState>> getAvailableModelAttributeReferences() {
		StateAdjustingModelAttributeReference<XpathState> refName = 
			StateAdjustingModelAttributeReference.toModelAttributeReference(
					StandardModelAttributeReference.RESOURCE_NAME.getModelAttributeReference(), 
						new ValueSetNameStateUpdater());
		
		return Arrays.asList(refName);
	}
	
	private class ValueSetDefinitionDirectoryBuilder extends XpathDirectoryBuilder<XpathState,ValueSetDefinitionDirectoryEntry> {
		
		public ValueSetDefinitionDirectoryBuilder() {
			super(new XpathState(), new Callback<XpathState, ValueSetDefinitionDirectoryEntry>() {

				@Override
				public DirectoryResult<ValueSetDefinitionDirectoryEntry> execute(
						XpathState state, 
						int start, 
						int maxResults) {
					return valueSetDefinitionExistDao.getResourceSummaries(
							"",
							state.getXpath(), 
							start, 
							maxResults);
				}

				@Override
				public int executeCount(XpathState state) {
					throw new UnsupportedOperationException();
				}},
				
				getAvailableMatchAlgorithmReferences(),
				getAvailableModelAttributeReferences());
		}
	}

	@Override
	public DirectoryResult<ValueSetDefinitionDirectoryEntry> getResourceSummaries(
			Query query, 
			FilterComponent filterComponent, 
			ValueSetDefinitionQueryServiceRestrictions restrictions,
			Page page) {
	ValueSetDefinitionDirectoryBuilder builder = new ValueSetDefinitionDirectoryBuilder();
		
		return 
			builder.restrict(filterComponent).
				restrict(query).
				addStart(page.getStart()).
				addMaxToReturn(page.getMaxtoreturn()).
				resolve();
	}

	@Override
	public DirectoryResult<ValueSetDefinition> getResourceList(
			Query query,
			FilterComponent filterComponent, 
			ValueSetDefinitionQueryServiceRestrictions restrictions, 
			Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int count(
			Query query, 
			FilterComponent filterComponent,
			ValueSetDefinitionQueryServiceRestrictions restrictions) {
	ValueSetDefinitionDirectoryBuilder builder = new ValueSetDefinitionDirectoryBuilder();
		
		return 
			builder.
			restrict(query).
			restrict(filterComponent).
				count();
	}

	@Override
	protected List<? extends PredicateReference> getAvailablePredicateReferences() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected StateUpdater<XpathState> getResourceNameStateUpdater() {
		// TODO Auto-generated method stub
		return null;
	}
}
