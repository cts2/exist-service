package edu.mayo.cts2.framework.plugin.service.exist.profile.codesystemversion;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.filter.match.StateAdjustingModelAttributeReference.StateUpdater;
import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry;
import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntrySummary;
import edu.mayo.cts2.framework.model.core.FilterComponent;
import edu.mayo.cts2.framework.model.core.PredicateReference;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.plugin.service.exist.dao.CodeSystemVersionExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistQueryService;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder.XpathState;
import edu.mayo.cts2.framework.service.command.Page;
import edu.mayo.cts2.framework.service.command.restriction.CodeSystemVersionQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionQueryService;

@Component
public class ExistCodeSystemVersionQueryService 
	extends AbstractExistQueryService
		<edu.mayo.cts2.framework.model.service.codesystemversion.CodeSystemVersionQueryService,XpathState> 
	implements CodeSystemVersionQueryService {

	@Resource
	private CodeSystemVersionExistDao codeSystemVersionExistDao;
	
	private class CodeSystemVersionDirectoryBuilder extends XpathDirectoryBuilder<XpathState,CodeSystemVersionCatalogEntrySummary> {

		public CodeSystemVersionDirectoryBuilder(final String codeSystem) {
			super(new XpathState(), 
					new Callback<XpathState, CodeSystemVersionCatalogEntrySummary>() {

				@Override
				public DirectoryResult<CodeSystemVersionCatalogEntrySummary> execute(
						XpathState state, 
						int start, 
						int maxResults) {
					return codeSystemVersionExistDao.getResourceSummaries(
							"",
							getCodeSystemXpath(codeSystem), 
							start, 
							maxResults);
				}
				
				private String getCodeSystemXpath(final String codeSystem){
					if(StringUtils.isNotBlank(codeSystem)){
						return "/codesystemversion:CodeSystemVersionCatalogEntry[codesystemversion:versionOf[text() = '"+codeSystem+"']]";
					} else {
						return "";
					}
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
	public PredicateReference getPropertyReference(String nameOrUri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DirectoryResult<CodeSystemVersionCatalogEntrySummary> getResourceSummaries(
			Query query, FilterComponent filterComponent,
			CodeSystemVersionQueryServiceRestrictions restrictions, Page page) {
		CodeSystemVersionDirectoryBuilder builder = 
				new CodeSystemVersionDirectoryBuilder(restrictions.getCodesystem());
		
		return builder.
				restrict(filterComponent).
				restrict(query).
				addMaxToReturn(page.getEnd()).
				addStart(page.getStart()).
				resolve();
	}

	@Override
	public DirectoryResult<CodeSystemVersionCatalogEntry> getResourceList(
			Query query, FilterComponent filterComponent,
			CodeSystemVersionQueryServiceRestrictions restrictions, Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int count(Query query, FilterComponent filterComponent,
			CodeSystemVersionQueryServiceRestrictions restrictions) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected StateUpdater<XpathState> getResourceNameStateUpdater() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<? extends PredicateReference> getAvailablePredicateReferences() {
		// TODO Auto-generated method stub
		return null;
	}	
}
