package edu.mayo.cts2.framework.plugin.service.exist.profile.conceptdomain;

import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.conceptdomain.ConceptDomainCatalogEntry;
import edu.mayo.cts2.framework.model.conceptdomain.ConceptDomainCatalogEntrySummary;
import edu.mayo.cts2.framework.model.core.PredicateReference;
import edu.mayo.cts2.framework.model.core.SortCriteria;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistQueryService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.PathInfo;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder.XpathState;
import edu.mayo.cts2.framework.service.profile.ResourceQuery;
import edu.mayo.cts2.framework.service.profile.conceptdomain.ConceptDomainQueryService;

@Component
public class ExistConceptDomainQueryService 
	extends AbstractExistQueryService
		<ConceptDomainCatalogEntry,
		ConceptDomainCatalogEntrySummary,
		Void,
		XpathState>
	implements ConceptDomainQueryService {
	
	@Resource
	private ConceptDomainResourceInfo conceptDomainResourceInfo;

	private class ConceptDomainDirectoryBuilder extends
			XpathDirectoryBuilder<XpathState,ConceptDomainCatalogEntrySummary> {

		public ConceptDomainDirectoryBuilder(final String changeSetUri) {
			super(new XpathState(), new Callback<XpathState, ConceptDomainCatalogEntrySummary>() {

				@Override
				public DirectoryResult<ConceptDomainCatalogEntrySummary> execute(
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
				}
			},

			getSupportedMatchAlgorithms(),
			getSupportedModelAttributes());
		}
	}

	@Override
	public DirectoryResult<ConceptDomainCatalogEntrySummary> getResourceSummaries(
			ResourceQuery query, 
			SortCriteria sort,
			Page page) {

		ConceptDomainDirectoryBuilder builder = new ConceptDomainDirectoryBuilder(
				this.getChangeSetUri(query.getReadContext()));

		return builder.
				restrict(query.getFilterComponent()).
				restrict(query.getQuery()).
				addStart(page.getStart()).
				addMaxToReturn(page.getMaxToReturn()).
				resolve();
	}

	
	@Override
	public DirectoryResult<ConceptDomainCatalogEntry> getResourceList(
			ResourceQuery query, SortCriteria sortCriteria, Page page) {
		throw new UnsupportedOperationException();
	}


	@Override
	public int count(ResourceQuery query) {
		throw new UnsupportedOperationException();
	}


	@Override
	protected ConceptDomainCatalogEntrySummary createSummary() {
		return new ConceptDomainCatalogEntrySummary();
	}

	@Override
	protected ConceptDomainCatalogEntrySummary doTransform(
			ConceptDomainCatalogEntry resource,
			ConceptDomainCatalogEntrySummary summary,
			org.xmldb.api.base.Resource eXistResource) {
		
		return this.baseTransform(summary, resource);
	}

	@Override
	protected PathInfo getResourceInfo() {
		return this.conceptDomainResourceInfo;
	}

	@Override
	public Set<? extends PredicateReference> getSupportedProperties() {
		// TODO Auto-generated method stub
		return null;
	}

}
