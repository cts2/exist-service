package edu.mayo.cts2.framework.plugin.service.exist.profile.statement;

import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.core.SortCriteria;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.statement.Statement;
import edu.mayo.cts2.framework.model.statement.StatementDirectoryEntry;
import edu.mayo.cts2.framework.model.statement.StatementListEntry;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistQueryService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.PathInfo;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder.XpathState;
import edu.mayo.cts2.framework.service.profile.ResourceQuery;
import edu.mayo.cts2.framework.service.profile.statement.StatementQueryService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;

@Component
public class ExistStatementQueryService
	extends AbstractExistQueryService
		<Statement,
		StatementDirectoryEntry,
		Void,
		XpathState>
	implements StatementQueryService {

	@Resource
	private StatementResourceInfo statementResourceInfo;
	
	private class StatementDirectoryBuilder extends XpathDirectoryBuilder<XpathState,StatementDirectoryEntry> {

		public StatementDirectoryBuilder(final String changeSetUri) {
			super(new XpathState(), new Callback<XpathState, StatementDirectoryEntry>() {

				@Override
				public DirectoryResult<StatementDirectoryEntry> execute(
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
	public DirectoryResult<StatementDirectoryEntry> getResourceSummaries(
			ResourceQuery query,
			SortCriteria sort,
			Page page) {
		StatementDirectoryBuilder builder = 
				new StatementDirectoryBuilder(
						this.getChangeSetUri(
								query.getReadContext()));
		
		return 
			builder.restrict(query.getFilterComponent()).
				restrict(query.getQuery()).
				addStart(page.getStart()).
				addMaxToReturn(page.getMaxToReturn()).
				resolve();
	}

	@Override
	public DirectoryResult<StatementListEntry> getResourceList(
			ResourceQuery query,
			SortCriteria sort,
			Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int count(
			ResourceQuery query) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected StatementDirectoryEntry createSummary() {
		return new StatementDirectoryEntry();
	}

	@Override
	protected StatementDirectoryEntry doTransform(Statement resource,
			StatementDirectoryEntry summary,
			org.xmldb.api.base.Resource eXistResource) {
		
		try {
			BeanUtils.copyProperties(summary, resource);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		} catch (InvocationTargetException e) {
			throw new IllegalStateException(e);
		}
		
		return summary;
	}

	@Override
	protected PathInfo getResourceInfo() {
		return this.statementResourceInfo;
	}
}
