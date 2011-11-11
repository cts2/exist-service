package edu.mayo.cts2.framework.plugin.service.exist.profile.codesystem;

import java.util.Set;

import org.springframework.stereotype.Component;
import org.xmldb.api.base.Resource;

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry;
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntrySummary;
import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference;
import edu.mayo.cts2.framework.model.core.PredicateReference;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistQueryService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder.XpathState;
import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemQueryService;

@Component
public class ExistCodeSystemQueryService 
	extends AbstractExistQueryService
		<CodeSystemCatalogEntry,
		CodeSystemCatalogEntrySummary,
		Void,
		edu.mayo.cts2.framework.model.service.codesystem.CodeSystemQueryService,
		XpathState> 
	implements CodeSystemQueryService {

	@javax.annotation.Resource
	private CodeSystemResourceInfo codeSystemResourceInfo;

	@Override
	protected CodeSystemCatalogEntrySummary createSummary() {
		return new CodeSystemCatalogEntrySummary();
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
	
	@Override
	public Set<? extends PredicateReference> getSupportedProperties() {
		return null;
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
				getSupportedModelAttributes());
		}
	}


	@Override
	public DirectoryResult<CodeSystemCatalogEntrySummary> getResourceSummaries(
			Query query, 
			Set<ResolvedFilter> filterComponent,
			Void restrictions, 
			ResolvedReadContext readContext,
			Page page) {

		String changeSetUri = null;
		if(readContext != null){
			changeSetUri = readContext.getChangeSetContextUri();
		}
		
		CodeSystemDirectoryBuilder builder = 
				new CodeSystemDirectoryBuilder(changeSetUri);
		
		return 
			builder.restrict(filterComponent).
				addStart(page.getStart()).
				addMaxToReturn(page.getMaxToReturn()).
				resolve();
	}

	@Override
	public DirectoryResult<CodeSystemCatalogEntry> getResourceList(
			Query query,
			Set<ResolvedFilter> filterComponent, 
			Void restrictions,
			ResolvedReadContext readContext,
			Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int count(Query query, Set<ResolvedFilter> filterComponent,
			Void restrictions) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected ResourceInfo<CodeSystemCatalogEntry, ?> getResourceInfo() {
		return this.codeSystemResourceInfo;
	}

}
