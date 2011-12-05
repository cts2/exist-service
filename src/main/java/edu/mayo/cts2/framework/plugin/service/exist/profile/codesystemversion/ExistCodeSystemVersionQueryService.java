package edu.mayo.cts2.framework.plugin.service.exist.profile.codesystemversion;

import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry;
import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntrySummary;
import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.PredicateReference;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistQueryService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.PathInfo;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder.XpathState;
import edu.mayo.cts2.framework.service.command.restriction.CodeSystemVersionQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionQueryService;

@Component
public class ExistCodeSystemVersionQueryService 
	extends AbstractExistQueryService
		<CodeSystemVersionCatalogEntry,
		CodeSystemVersionCatalogEntrySummary,
		CodeSystemVersionQueryServiceRestrictions,
		edu.mayo.cts2.framework.model.service.codesystemversion.CodeSystemVersionQueryService,XpathState> 
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
						return "[codesystemversion:versionOf[text() = '"+codeSystem+"']]";
					} else {
						return "";
					}
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
			Query query, Set<ResolvedFilter> filterComponent,
			CodeSystemVersionQueryServiceRestrictions restrictions,
			ResolvedReadContext readContext,
			Page page) {
		CodeSystemVersionDirectoryBuilder builder = 
				new CodeSystemVersionDirectoryBuilder(
						this.getRestrictedCodeSystemName(restrictions), 
						this.getChangeSetUri(readContext));
		
		return builder.
				restrict(filterComponent).
				restrict(query).
				addMaxToReturn(page.getEnd()).
				addStart(page.getStart()).
				resolve();
	}
	
	private String getRestrictedCodeSystemName(
			CodeSystemVersionQueryServiceRestrictions restrictions){
		if(restrictions != null && restrictions.getCodeSystem() != null){
			return restrictions.getCodeSystem().getName();
		}
		
		return null;
	}

	@Override
	public DirectoryResult<CodeSystemVersionCatalogEntry> getResourceList(
			Query query, Set<ResolvedFilter> filterComponent,
			CodeSystemVersionQueryServiceRestrictions restrictions, 
			ResolvedReadContext readContext,
			Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int count(Query query, Set<ResolvedFilter> filterComponent,
			CodeSystemVersionQueryServiceRestrictions restrictions) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected PathInfo getResourceInfo() {
		return codeSystemVersionResourceInfo;
	}

	@Override
	public Set<? extends PredicateReference> getSupportedProperties() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
