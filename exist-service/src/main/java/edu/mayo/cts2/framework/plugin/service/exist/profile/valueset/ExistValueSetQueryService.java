package edu.mayo.cts2.framework.plugin.service.exist.profile.valueset;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.plugin.service.exist.dao.ValueSetExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistQueryService;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder.XpathState;
import edu.mayo.cts2.sdk.filter.match.StateAdjustingModelAttributeReference.StateUpdater;
import edu.mayo.cts2.sdk.model.core.FilterComponent;
import edu.mayo.cts2.sdk.model.core.MatchAlgorithmReference;
import edu.mayo.cts2.sdk.model.core.PredicateReference;
import edu.mayo.cts2.sdk.model.directory.DirectoryResult;
import edu.mayo.cts2.sdk.model.service.core.Query;
import edu.mayo.cts2.sdk.model.valueset.ValueSetCatalogEntry;
import edu.mayo.cts2.sdk.model.valueset.ValueSetCatalogEntrySummary;
import edu.mayo.cts2.sdk.service.command.Page;
import edu.mayo.cts2.sdk.service.command.restriction.ValueSetQueryServiceRestrictions;
import edu.mayo.cts2.sdk.service.profile.valueset.ValueSetQueryService;

@Component
public class ExistValueSetQueryService 
	extends AbstractExistQueryService
		<edu.mayo.cts2.sdk.model.service.valueset.ValueSetQueryService,XpathState>
	implements ValueSetQueryService {

	@Resource
	private ValueSetExistDao valueSetExistDao;

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
	
	@Override
	protected StateUpdater<XpathState> getResourceNameStateUpdater() {
		return new ValueSetNameStateUpdater();
	}

	private class ValueSetDirectoryBuilder extends XpathDirectoryBuilder<XpathState,ValueSetCatalogEntrySummary> {

		public ValueSetDirectoryBuilder() {
			super(new XpathState(), new Callback<XpathState, ValueSetCatalogEntrySummary>() {

				@Override
				public DirectoryResult<ValueSetCatalogEntrySummary> execute(
						XpathState state, 
						int start, 
						int maxResults) {
					return valueSetExistDao.getResourceSummaries(
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
	public DirectoryResult<ValueSetCatalogEntrySummary> getResourceSummaries(
			Query query, 
			FilterComponent filterComponent, 
			ValueSetQueryServiceRestrictions restrictions,
			Page page) {

	ValueSetDirectoryBuilder builder = new ValueSetDirectoryBuilder();
	
	return 
		builder.restrict(filterComponent).
			addStart(page.getStart()).
			addMaxToReturn(page.getMaxtoreturn()).
			resolve();
	}

	@Override
	public DirectoryResult<ValueSetCatalogEntry> getResourceList(
			Query query,
			FilterComponent filterComponent,
			ValueSetQueryServiceRestrictions restrictions,
			Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int count(
			Query query, 
			FilterComponent filterComponent,
			ValueSetQueryServiceRestrictions restrictions) {
		ValueSetDirectoryBuilder builder = new ValueSetDirectoryBuilder();
		
		return 
				builder.restrict(filterComponent).
					count();
	}

	@Override
	protected List<? extends PredicateReference> getAvailablePredicateReferences() {
		// TODO Auto-generated method stub
		return null;
	}
}
