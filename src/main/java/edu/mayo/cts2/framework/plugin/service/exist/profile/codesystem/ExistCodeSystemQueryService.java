package edu.mayo.cts2.framework.plugin.service.exist.profile.codesystem;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.xmldb.api.base.Resource;

import edu.mayo.cts2.framework.filter.match.StateAdjustingModelAttributeReference.StateUpdater;
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry;
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntrySummary;
import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference;
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
		
		return summary;
	}
	
	private static class CodeSystemNameStateUpdater implements StateUpdater<XpathState> {

		@Override
		public XpathState updateState(XpathState state, MatchAlgorithmReference matchAlgorithm, String queryString) {
			String currentState = state.getXpath();
			StringBuilder sb = new StringBuilder();
			
			if(StringUtils.isNotBlank(currentState)){
				sb.append(currentState);
				sb.append(" ");
			}
			
			sb.append("//codesystem:CodeSystemCatalogEntry[@codeSystemName = '" + queryString + "']");
			
			state.setXpath(sb.toString());
			
			return state;
		}	
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
	protected StateUpdater<XpathState> getResourceNameStateUpdater() {
		return new CodeSystemNameStateUpdater();
	}

	@Override
	protected ResourceInfo<CodeSystemCatalogEntry, ?> getResourceInfo() {
		return this.codeSystemResourceInfo;
	}

}
