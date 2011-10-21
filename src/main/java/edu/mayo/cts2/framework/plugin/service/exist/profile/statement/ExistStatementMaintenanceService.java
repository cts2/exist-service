package edu.mayo.cts2.framework.plugin.service.exist.profile.statement;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.statement.Statement;
import edu.mayo.cts2.framework.plugin.service.exist.dao.StatementExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistService;
import edu.mayo.cts2.framework.service.profile.statement.StatementMaintenanceService;

@Component
public class ExistStatementMaintenanceService 
	extends AbstractExistService<edu.mayo.cts2.framework.model.service.statement.StatementMaintenanceService>
	implements StatementMaintenanceService {

	@Resource
	private StatementExistDao statementExistDao;

	@Override
	public void createResource(String changeSetUri, Statement resource) {
		this.statementExistDao.storeResource("", resource);
	}

	@Override
	public void updateResource(String changeSetUri, Statement resource) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteResource(String changeSetUri, String resourceName) {
		throw new UnsupportedOperationException();
	}
}
