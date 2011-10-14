package edu.mayo.cts2.framework.plugin.service.exist.profile.statement;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.statement.Statement;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.dao.StatementExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistReadService;
import edu.mayo.cts2.framework.service.profile.statement.StatementReadService;

@Component
public class ExistStatementReadService 
	extends AbstractExistReadService<Statement,edu.mayo.cts2.framework.model.service.statement.StatementService>
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

	@Override
	protected ExistDao<?, Statement> getExistDao() {
		return this.statementExistDao;
	}
}
