package edu.mayo.cts2.framework.plugin.service.exist.profile.codesystem;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.filter.match.StateAdjustingModelAttributeReference.StateUpdater;
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry;
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntrySummary;
import edu.mayo.cts2.framework.model.core.FilterComponent;
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference;
import edu.mayo.cts2.framework.model.core.PredicateReference;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.plugin.service.exist.dao.CodeSystemExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistQueryService;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder.XpathState;
import edu.mayo.cts2.framework.service.command.Page;
import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemQueryService;

@Component
public class ExistCodeSystemQueryService 
	extends AbstractExistQueryService
		<edu.mayo.cts2.framework.model.service.codesystem.CodeSystemQueryService,XpathState> 
	implements CodeSystemQueryService {

	@Resource
	private CodeSystemExistDao codeSystemExistDao;

	@Override
	public PredicateReference getPropertyReference(String nameOrUri) {
		// TODO Auto-generated method stub
		return null;
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
	protected List<? extends PredicateReference> getAvailablePredicateReferences() {
		return null;
	}
	
	private class CodeSystemDirectoryBuilder extends XpathDirectoryBuilder<XpathState,CodeSystemCatalogEntrySummary> {

		public CodeSystemDirectoryBuilder() {
			super(new XpathState(), 
					new Callback<XpathState, CodeSystemCatalogEntrySummary>() {

				@Override
				public DirectoryResult<CodeSystemCatalogEntrySummary> execute(
						XpathState state, 
						int start, 
						int maxResults) {
					return codeSystemExistDao.getResourceSummaries(
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
	public DirectoryResult<CodeSystemCatalogEntrySummary> getResourceSummaries(
			Query query, 
			FilterComponent filterComponent,
			Void restrictions, 
			Page page) {

		CodeSystemDirectoryBuilder builder = new CodeSystemDirectoryBuilder();
		
		return 
			builder.restrict(filterComponent).
				addStart(page.getStart()).
				addMaxToReturn(page.getMaxtoreturn()).
				resolve();
	}

	@Override
	public DirectoryResult<CodeSystemCatalogEntry> getResourceList(
			Query query,
			FilterComponent filterComponent, 
			Void restrictions,
			Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int count(Query query, FilterComponent filterComponent,
			Void restrictions) {
		throw new UnsupportedOperationException();
	}


	@Override
	protected StateUpdater<XpathState> getResourceNameStateUpdater() {
		return new CodeSystemNameStateUpdater();
	}
}
