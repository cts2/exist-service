package edu.mayo.cts2.framework.plugin.service.exist.profile.statement;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.statement.Statement;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistReadService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
import edu.mayo.cts2.framework.service.profile.statement.StatementReadService;

@Component
public class ExistStatementReadService 
	extends AbstractExistReadService<
		Statement,
		String,
		edu.mayo.cts2.framework.model.service.statement.StatementService>
	implements StatementReadService {

	@Resource
	private StatementResourceInfo statementResourceInfo;

	@Override
	protected ResourceInfo<Statement, String> getResourceInfo() {
		return this.statementResourceInfo;
	}

}
