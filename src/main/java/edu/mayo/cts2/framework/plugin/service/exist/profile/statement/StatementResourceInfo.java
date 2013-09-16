package edu.mayo.cts2.framework.plugin.service.exist.profile.statement;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.extension.LocalIdStatement;
import edu.mayo.cts2.framework.plugin.service.exist.profile.LocalIdResourceInfo;
import edu.mayo.cts2.framework.service.profile.statement.name.StatementReadId;

@Component
public class StatementResourceInfo implements LocalIdResourceInfo<LocalIdStatement,StatementReadId> {

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
	public boolean isReadByUri(StatementReadId identifier) {
		return !(identifier.getUri() == null);
	}

	@Override
	public String createPath(StatementReadId id) {
		return "";
	}

	@Override
	public String createPathFromResource(LocalIdStatement resource) {
		return "";
	}

	@Override
	public String getExistResourceName(StatementReadId id) {
		return id.getName();
	}

	@Override
	public String getResourceUri(StatementReadId id) {
		return id.getUri();
	}
	
	@Override
	public String getUriXpath() {
		return "statement:statementURI";
	}

	@Override
	public String getResourceNameXpath() {
		return "statement:statementURI";
	}

}
