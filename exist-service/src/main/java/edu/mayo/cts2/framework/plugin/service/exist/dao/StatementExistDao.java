package edu.mayo.cts2.framework.plugin.service.exist.dao;

import org.springframework.stereotype.Component;
import org.xmldb.api.base.Resource;

import edu.mayo.cts2.framework.model.service.exception.UnknownResourceReference;
import edu.mayo.cts2.framework.model.service.exception.UnknownStatement;
import edu.mayo.cts2.framework.model.statement.Statement;
import edu.mayo.cts2.framework.model.statement.StatementDirectoryEntry;

@Component
public class StatementExistDao extends
		AbstractResourceExistDao<StatementDirectoryEntry, Statement> {

	private static final String STATEMENTS_PATH = "/statements";

	@Override
	public StatementDirectoryEntry doTransform(Statement resource,
			StatementDirectoryEntry summary, Resource eXistResource) {
	
		return summary;
	}

	@Override
	protected String getName(Statement entry) {
		throw new UnsupportedOperationException("Cannot ask for Statement by name.");
	}

	@Override
	protected String doGetResourceBasePath() {
		return STATEMENTS_PATH;
	}

	@Override
	protected StatementDirectoryEntry createSummary() {
		return new StatementDirectoryEntry();
	}

	@Override
	protected Class<? extends UnknownResourceReference> getUnknownResourceExceptionClass() {
		return UnknownStatement.class;
	}

	@Override
	protected String getResourceXpath() {
		return "/statement:Statement";
	}

	@Override
	protected String getUriXpath() {
		return "@statementURI";
	}
}
