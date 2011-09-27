package edu.mayo.cts2.framework.plugin.service.exist.profile.statement;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.plugin.service.exist.dao.StatementExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistService;
import edu.mayo.cts2.sdk.model.statement.Statement;
import edu.mayo.cts2.sdk.service.profile.statement.StatementReadService;

@Component
public class ExistStatementReadService 
	extends AbstractExistService<edu.mayo.cts2.sdk.model.service.statement.StatementService>
	implements StatementReadService {

	@Resource
	private StatementExistDao statementExistDao;

	@Override
	public Statement read(String identifier) {
		return statementExistDao.getResource("", identifier);
	}

	@Override
	public boolean exists(String identifier) {
		throw new UnsupportedOperationException();
	}
}
