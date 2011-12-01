package edu.mayo.cts2.framework.plugin.service.exist.profile.statement;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.PredicateReference;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.extension.LocalIdStatement;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.model.statement.Statement;
import edu.mayo.cts2.framework.model.statement.StatementDirectoryEntry;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistQueryService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder.XpathState;
import edu.mayo.cts2.framework.service.profile.statement.StatementQueryService;

@Component
public class ExistStatementQueryService
	extends AbstractExistQueryService
		<Statement,
		StatementDirectoryEntry,
		Void,
		edu.mayo.cts2.framework.model.service.statement.StatementQueryService,XpathState>
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
				getSupportedModelAttributes());
		}
	}

	@Override
	public DirectoryResult<StatementDirectoryEntry> getResourceSummaries(
			Query query,
			Set<ResolvedFilter> filterComponent, 
			Void restrictions,
			ResolvedReadContext readContext,
			Page page) {
		StatementDirectoryBuilder builder = 
				new StatementDirectoryBuilder(this.getChangeSetUri(readContext));
		
		return 
			builder.restrict(filterComponent).
				restrict(query).
				addStart(page.getStart()).
				addMaxToReturn(page.getMaxToReturn()).
				resolve();
	}

	@Override
	public DirectoryResult<Statement> getResourceList(
			Query query,
			Set<ResolvedFilter> filterComponent, 
			Void restrictions,
			ResolvedReadContext readContext,
			Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int count(
			Query query, 
			Set<ResolvedFilter> filterComponent,
			Void restrictions) {
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
	protected ResourceInfo<LocalIdStatement, ?> getResourceInfo() {
		return this.statementResourceInfo;
	}

	@Override
	public Set<? extends PredicateReference> getSupportedProperties() {
		// TODO Auto-generated method stub
		return null;
	}
}
