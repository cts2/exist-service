package edu.mayo.cts2.framework.plugin.service.exist.profile.conceptdomainbinding;

import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.conceptdomainbinding.ConceptDomainBinding;
import edu.mayo.cts2.framework.model.conceptdomainbinding.ConceptDomainBindingDirectoryEntry;
import edu.mayo.cts2.framework.model.conceptdomainbinding.ConceptDomainBindingListEntry;
import edu.mayo.cts2.framework.model.core.SortCriteria;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistQueryService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.PathInfo;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder.XpathState;
import edu.mayo.cts2.framework.service.command.restriction.ConceptDomainBindingQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.conceptdomainbinding.ConceptDomainBindingQuery;
import edu.mayo.cts2.framework.service.profile.conceptdomainbinding.ConceptDomainBindingQueryService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ExistConceptDomainBindingQueryService 
	extends AbstractExistQueryService
		<ConceptDomainBinding,
		ConceptDomainBindingDirectoryEntry,
		ConceptDomainBindingQueryServiceRestrictions,
		XpathState>
	implements ConceptDomainBindingQueryService {

	@Resource
	private ConceptDomainBindingResourceInfo conceptDomainBindingResourceInfo;
	
	private class ConceptDomainBindingDirectoryBuilder extends XpathDirectoryBuilder<XpathState,ConceptDomainBindingDirectoryEntry> {

		public ConceptDomainBindingDirectoryBuilder(final String changeSetUri) {
			super(new XpathState(), new Callback<XpathState, ConceptDomainBindingDirectoryEntry>() {

				@Override
				public DirectoryResult<ConceptDomainBindingDirectoryEntry> execute(
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
	public DirectoryResult<ConceptDomainBindingDirectoryEntry> getResourceSummaries(
			ConceptDomainBindingQuery query, 
			SortCriteria sortCriteria,
			Page page) {
		
		ConceptDomainBindingDirectoryBuilder builder = new ConceptDomainBindingDirectoryBuilder(
				this.getChangeSetUri(query.getReadContext()));

		return builder.
				restrict(query.getFilterComponent()).
				restrict(query.getQuery()).
				addStart(page.getStart()).
				addMaxToReturn(page.getMaxToReturn()).
				resolve();
	}

	@Override
	public DirectoryResult<ConceptDomainBindingListEntry> getResourceList(
			ConceptDomainBindingQuery query, SortCriteria sortCriteria,
			Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int count(ConceptDomainBindingQuery query) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected ConceptDomainBindingDirectoryEntry createSummary() {
		return new ConceptDomainBindingDirectoryEntry();
	}

	@Override
	protected ConceptDomainBindingDirectoryEntry doTransform(
			ConceptDomainBinding resource,
			ConceptDomainBindingDirectoryEntry summary,
			org.xmldb.api.base.Resource eXistResource) {
		
		summary.setAbout(resource.getBindingURI());
		summary.setApplicableContext(resource.getApplicableContext());
		summary.setBindingFor(resource.getBindingFor());
		summary.setBindingQualifier(resource.getBindingQualifier());
		summary.setBoundValueSet(resource.getBoundValueSet());
		summary.setResourceName(resource.getBindingURI());
		
		return summary;
	}

	@Override
	protected PathInfo getResourceInfo() {
		return this.conceptDomainBindingResourceInfo;
	}

}
