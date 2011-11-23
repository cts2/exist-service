package edu.mayo.cts2.framework.plugin.service.exist.profile.statement;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.statement.Statement;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;

@Component
public class StatementResourceInfo implements ResourceInfo<Statement,String> {

	private static final String STATEMENTS_PATH = "/statements";

	@Override
	public String getResourceBasePath() {
		return STATEMENTS_PATH;
	}

	@Override
	public String getResourceXpath() {
		return "/statement:Statement";
	}
	
	@Override
	public boolean isReadByUri(String identifier) {
		return false;
	}

	@Override
	public String createPath(String id) {
		return "";
	}

	@Override
	public String createPathFromResource(Statement resource) {
		return "";
	}

	@Override
	public String getExistResourceName(String id) {
		return ExistServiceUtils.uriToExistName(id);
	}

	@Override
	public String getResourceUri(String id) {
		return id;
	}

	@Override
	public String getExistResourceNameFromResource(Statement resource) {
		return ExistServiceUtils.uriToExistName(resource.getStatementURI());
	}
	
	@Override
	public String getUriXpath() {
		return "@statementURI";
	}

	@Override
	public String getResourceNameXpath() {
		return "@statementURI";
	}

}
