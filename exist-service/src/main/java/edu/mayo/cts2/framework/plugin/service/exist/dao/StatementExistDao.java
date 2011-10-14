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
		return null;//entry.getChangeableElementGroup().
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getUriXpath() {
		// TODO Auto-generated method stub
		return null;
	}
}
