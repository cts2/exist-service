package edu.mayo.cts2.framework.plugin.service.exist.profile.statement;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.statement.Statement;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistMaintenanceService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
import edu.mayo.cts2.framework.service.profile.statement.StatementMaintenanceService;

@Component
public class ExistStatementMaintenanceService 
	extends AbstractExistMaintenanceService<Statement,String,edu.mayo.cts2.framework.model.service.statement.StatementMaintenanceService>
	implements StatementMaintenanceService {
	
	@Resource
	private StatementResourceInfo statementResourceInfo;

	
	@Override
	protected ResourceInfo<Statement, String> getResourceInfo() {
		return this.statementResourceInfo;
	}

}
