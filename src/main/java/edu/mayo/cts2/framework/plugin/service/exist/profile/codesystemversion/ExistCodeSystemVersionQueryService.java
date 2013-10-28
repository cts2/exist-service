package edu.mayo.cts2.framework.plugin.service.exist.profile.codesystemversion;

import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry;
import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntryListEntry;
import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntrySummary;
import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.core.SortCriteria;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistQueryService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.PathInfo;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder.XpathState;
import edu.mayo.cts2.framework.service.command.restriction.CodeSystemVersionQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionQuery;
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionQueryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ExistCodeSystemVersionQueryService 
	extends AbstractExistQueryService
		<CodeSystemVersionCatalogEntry,
		CodeSystemVersionCatalogEntrySummary,
		CodeSystemVersionQueryServiceRestrictions,
		XpathState> 
	implements CodeSystemVersionQueryService {
	
	@Resource
	private CodeSystemVersionResourceInfo codeSystemVersionResourceInfo;
	
	private class CodeSystemVersionDirectoryBuilder extends XpathDirectoryBuilder<XpathState,CodeSystemVersionCatalogEntrySummary> {

		public CodeSystemVersionDirectoryBuilder(final String codeSystem, final String changeSetUri) {
			super(new XpathState(), 
					new Callback<XpathState, CodeSystemVersionCatalogEntrySummary>() {

				@Override
				public DirectoryResult<CodeSystemVersionCatalogEntrySummary> execute(
						XpathState state, 
						int start, 
						int maxResults) {
					return getResourceSummaries(
							getResourceInfo(),
							changeSetUri,
							"",
							getCodeSystemXpath(codeSystem), 
							start, 
							maxResults);
				}
				
				private String getCodeSystemXpath(final String codeSystem){
					if(StringUtils.isNotBlank(codeSystem)){
						return "[codesystemversion:versionOf = '"+codeSystem+"']";
					} else {
						return "";
					}
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
	protected CodeSystemVersionCatalogEntrySummary createSummary() {
		return new CodeSystemVersionCatalogEntrySummary();
	}

	@Override
	protected CodeSystemVersionCatalogEntrySummary doTransform(
			CodeSystemVersionCatalogEntry resource,
			CodeSystemVersionCatalogEntrySummary summary, org.xmldb.api.base.Resource eXistResource) {
		
		summary = this.baseTransformResourceVersion(summary, resource);
		summary.setCodeSystemVersionName(resource.getCodeSystemVersionName());

		summary.setHref(getUrlConstructor().createCodeSystemVersionUrl(
				resource.getVersionOf().getContent(),
				resource.getCodeSystemVersionName()));

		summary.setVersionOf(resource.getVersionOf());
		
		if(summary.getVersionOf() != null){
			if(StringUtils.isBlank(summary.getVersionOf().getHref())){
				summary.getVersionOf().setHref(
					this.getUrlConstructor().createCodeSystemUrl(
							resource.getVersionOf().getContent()));
			}
		}

		return summary;
	}

	@Override
	public DirectoryResult<CodeSystemVersionCatalogEntrySummary> getResourceSummaries(
			CodeSystemVersionQuery query, 
			SortCriteria sort,
			Page page) {
		CodeSystemVersionDirectoryBuilder builder = 
				new CodeSystemVersionDirectoryBuilder(
						this.getRestrictedCodeSystemName(query.getRestrictions()), 
						this.getChangeSetUri(query.getReadContext()));
		
		return builder.
				restrict(query.getFilterComponent()).
				restrict(query.getQuery()).
				addMaxToReturn(page.getEnd()).
				addStart(page.getStart()).
				resolve();
	}
	
	private String getRestrictedCodeSystemName(
			CodeSystemVersionQueryServiceRestrictions restrictions){
		if(restrictions != null && restrictions.getCodeSystem() != null){
			return restrictions.getCodeSystem().getName();
		}
		//TODO this is incomplete... it needs to handle URIs too...
		
		return null;
	}

	@Override
	public DirectoryResult<CodeSystemVersionCatalogEntryListEntry> getResourceList(
			CodeSystemVersionQuery query, 
			SortCriteria sort,
			Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int count(CodeSystemVersionQuery query) {
		throw new UnsupportedOperationException();

	}

	@Override
	protected PathInfo getResourceInfo() {
		return codeSystemVersionResourceInfo;
	}
	
}
