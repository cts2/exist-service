package edu.mayo.cts2.framework.plugin.service.exist.profile.conceptdomain;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.filter.match.StateAdjustingModelAttributeReference.StateUpdater;
import edu.mayo.cts2.framework.model.conceptdomain.ConceptDomainCatalogEntry;
import edu.mayo.cts2.framework.model.conceptdomain.ConceptDomainCatalogEntrySummary;
import edu.mayo.cts2.framework.model.core.FilterComponent;
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference;
import edu.mayo.cts2.framework.model.core.PredicateReference;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ConceptDomainExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistQueryService;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder.XpathState;
import edu.mayo.cts2.framework.service.command.Page;
import edu.mayo.cts2.framework.service.profile.conceptdomain.ConceptDomainQueryService;

@Component
public class ExistConceptDomainQueryService 
	extends AbstractExistQueryService
		<edu.mayo.cts2.framework.model.service.conceptdomain.ConceptDomainQueryService,XpathState>
	implements ConceptDomainQueryService {

	@Resource
	private ConceptDomainExistDao conceptDomainExistDao;

	@Override
	public PredicateReference getPropertyReference(String nameOrUri) {
		// TODO Auto-generated method stub
		return null;
	}

	private class ConceptDomainNameStateUpdater implements StateUpdater<XpathState> {

		@Override
		public XpathState updateState(XpathState currentState, MatchAlgorithmReference matchAlgorithm, String queryString) {
			currentState.setXpath("conceptdomain:ConceptDomainCatalogEntry/@conceptDomainName = '"
					+ queryString + "'");
			
			return currentState;
		}
	}

	private class ConceptDomainDirectoryBuilder extends
			XpathDirectoryBuilder<XpathState,ConceptDomainCatalogEntrySummary> {

		public ConceptDomainDirectoryBuilder() {
			super(new XpathState(), new Callback<XpathState, ConceptDomainCatalogEntrySummary>() {

				@Override
				public DirectoryResult<ConceptDomainCatalogEntrySummary> execute(
						XpathState state, 
						int start, 
						int maxResults) {
					return conceptDomainExistDao.getResourceSummaries(
							"",
							state.getXpath(), 
							start, 
							maxResults);
				}

				@Override
				public int executeCount(XpathState state) {
					throw new UnsupportedOperationException();
				}
			},

			getAvailableMatchAlgorithmReferences(),
			getAvailableModelAttributeReferences());
		}
	}

	@Override
	public DirectoryResult<ConceptDomainCatalogEntrySummary> getResourceSummaries(
			Query query, FilterComponent filterComponent, Void restrictions,
			Page page) {

		ConceptDomainDirectoryBuilder builder = new ConceptDomainDirectoryBuilder();

		return builder.
				restrict(filterComponent).
				restrict(query).
				addStart(page.getStart()).
				addMaxToReturn(page.getMaxtoreturn()).
				resolve();
	}

	@Override
	public DirectoryResult<ConceptDomainCatalogEntry> getResourceList(
			Query query, FilterComponent filterComponent, Void restrictions,
			Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int count(Query query, FilterComponent filterComponent,
			Void restrictions) {
		ConceptDomainDirectoryBuilder builder = new ConceptDomainDirectoryBuilder();

		return builder.restrict(filterComponent).restrict(query).count();

	}

	@Override
	protected List<? extends PredicateReference> getAvailablePredicateReferences() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected StateUpdater<XpathState> getResourceNameStateUpdater() {
		return new ConceptDomainNameStateUpdater();
	}

}
