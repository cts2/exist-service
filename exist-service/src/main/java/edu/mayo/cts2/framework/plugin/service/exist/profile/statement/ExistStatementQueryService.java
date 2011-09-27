package edu.mayo.cts2.framework.plugin.service.exist.profile.statement;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.plugin.service.exist.dao.StatementExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistQueryService;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder.XpathState;
import edu.mayo.cts2.sdk.filter.match.StateAdjustingModelAttributeReference.StateUpdater;
import edu.mayo.cts2.sdk.model.core.FilterComponent;
import edu.mayo.cts2.sdk.model.core.PredicateReference;
import edu.mayo.cts2.sdk.model.directory.DirectoryResult;
import edu.mayo.cts2.sdk.model.service.core.Query;
import edu.mayo.cts2.sdk.model.statement.Statement;
import edu.mayo.cts2.sdk.model.statement.StatementDirectoryEntry;
import edu.mayo.cts2.sdk.service.command.Page;
import edu.mayo.cts2.sdk.service.profile.statement.StatementQueryService;

@Component
public class ExistStatementQueryService
	extends AbstractExistQueryService
		<edu.mayo.cts2.sdk.model.service.statement.StatementQueryService,XpathState>
	implements StatementQueryService {

	@Resource
	private StatementExistDao statementExistDao;

	@Override
	public PredicateReference getPropertyReference(String nameOrUri) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private class StatementDirectoryBuilder extends XpathDirectoryBuilder<XpathState,StatementDirectoryEntry> {

		public StatementDirectoryBuilder() {
			super(new XpathState(), new Callback<XpathState, StatementDirectoryEntry>() {

				@Override
				public DirectoryResult<StatementDirectoryEntry> execute(
						XpathState state, 
						int start, 
						int maxResults) {
					return statementExistDao.getResourceSummaries(
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
	public DirectoryResult<StatementDirectoryEntry> getResourceSummaries(
			Query query,
			FilterComponent filterComponent, 
			Void restrictions,
			Page page) {
		StatementDirectoryBuilder builder = new StatementDirectoryBuilder();
		
		return 
			builder.restrict(filterComponent).
				restrict(query).
				addStart(page.getStart()).
				addMaxToReturn(page.getMaxtoreturn()).
				resolve();
	}

	@Override
	public DirectoryResult<Statement> getResourceList(Query query,
			FilterComponent filterComponent, Void restrictions, Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int count(
			Query query, 
			FilterComponent filterComponent,
			Void restrictions) {
		StatementDirectoryBuilder builder = new StatementDirectoryBuilder();
		
		return 
				builder.restrict(filterComponent).
					restrict(query).
					count();
	}

	@Override
	protected List<? extends PredicateReference> getAvailablePredicateReferences() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected StateUpdater<XpathState> getResourceNameStateUpdater() {
		// TODO Auto-generated method stub
		return null;
	}
}
