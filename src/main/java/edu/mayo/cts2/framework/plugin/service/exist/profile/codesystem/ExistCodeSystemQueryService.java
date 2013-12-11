package edu.mayo.cts2.framework.plugin.service.exist.profile.codesystem;

import edu.mayo.cts2.framework.filter.match.StateAdjustingComponentReference;
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry;
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntryListEntry;
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntrySummary;
import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference;
import edu.mayo.cts2.framework.model.core.SortCriteria;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistQueryService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.PathInfo;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder.XpathState;
import edu.mayo.cts2.framework.plugin.service.exist.xpath.XpathStateUpdater;
import edu.mayo.cts2.framework.service.profile.ResourceQuery;
import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemQueryService;
import org.springframework.stereotype.Component;
import org.xmldb.api.base.Resource;

@Component
public class ExistCodeSystemQueryService 
	extends AbstractExistQueryService
		<CodeSystemCatalogEntry,
		CodeSystemCatalogEntrySummary,
		Void,
		XpathState> 
	implements CodeSystemQueryService {

	@javax.annotation.Resource
	private CodeSystemResourceInfo codeSystemResourceInfo;

	@Override
	protected CodeSystemCatalogEntrySummary createSummary() {
		return new CodeSystemCatalogEntrySummary();
	}

    protected StateAdjustingComponentReference.StateUpdater<XpathState> getResourceSynopsisStateUpdater() {
        return new XpathStateUpdater<XpathState>("core:resourceSynopsis/core:value");
    }

	@Override
	protected CodeSystemCatalogEntrySummary doTransform(
			CodeSystemCatalogEntry resource,
			CodeSystemCatalogEntrySummary summary, Resource eXistResource) {
		summary = this.baseTransform(summary, resource);
		
		summary.setCodeSystemName(resource.getCodeSystemName());
		summary.setHref(getUrlConstructor().createCodeSystemUrl(resource.getCodeSystemName()));
		summary.setVersions(getUrlConstructor().createVersionsOfCodeSystemUrl(resource.getCodeSystemName()));
		
		CodeSystemVersionReference currentVersion = resource.getCurrentVersion();
		if(currentVersion != null){
			try {
				String codeSystemName = 
						resource.getCodeSystemName();
				
				String codeSystemVersionName = 
						summary.getCurrentVersion().getVersion().getContent();
				
				summary.getCurrentVersion().getCodeSystem().setHref(
						this.getUrlConstructor().createCodeSystemUrl(codeSystemName));
				
				summary.getCurrentVersion().getVersion().setHref(
						this.getUrlConstructor().
							createCodeSystemVersionUrl(
									codeSystemName, 
									codeSystemVersionName));
			} catch (Exception e) {
				//skip for now.
			}
		}
		
		return summary;
	}

	private class CodeSystemDirectoryBuilder extends XpathDirectoryBuilder<XpathState,CodeSystemCatalogEntrySummary> {

		public CodeSystemDirectoryBuilder(final String changeSetUri) {
			
			
			super(new XpathState(), 
					new Callback<XpathState, CodeSystemCatalogEntrySummary>() {

				@Override
				public DirectoryResult<CodeSystemCatalogEntrySummary> execute(
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
	public DirectoryResult<CodeSystemCatalogEntrySummary> getResourceSummaries(
			ResourceQuery query, 
			SortCriteria sortCriteria,
			Page page) {
	
		CodeSystemDirectoryBuilder builder = 
				new CodeSystemDirectoryBuilder(this.getChangeSetUri(
						query.getReadContext()));
		
		return 
			builder.restrict(query.getFilterComponent()).
				addStart(page.getStart()).
				addMaxToReturn(page.getMaxToReturn()).
				resolve();
	}

	@Override
	public DirectoryResult<CodeSystemCatalogEntryListEntry> getResourceList(
			ResourceQuery query, 
			SortCriteria sortCriteria,
			Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int count(ResourceQuery query) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected PathInfo getResourceInfo() {
		return this.codeSystemResourceInfo;
	}

}
