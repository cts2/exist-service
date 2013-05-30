package edu.mayo.cts2.framework.plugin.service.exist.profile.statement;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.extension.LocalIdStatement;
import edu.mayo.cts2.framework.model.statement.Statement;
import edu.mayo.cts2.framework.model.updates.ChangeableResource;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistLocalIdMaintenanceService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.LocalIdResourceInfo;
import edu.mayo.cts2.framework.service.profile.statement.StatementMaintenanceService;
import edu.mayo.cts2.framework.service.profile.statement.name.StatementReadId;

@Component
public class ExistStatementMaintenanceService 
	extends AbstractExistLocalIdMaintenanceService<
		Statement,
		LocalIdStatement,
		StatementReadId,
		edu.mayo.cts2.framework.model.service.statement.StatementMaintenanceService>
	implements StatementMaintenanceService {
	
	@Resource
	private StatementResourceInfo statementResourceInfo;

	
	@Override
	protected LocalIdResourceInfo<LocalIdStatement, StatementReadId> getResourceInfo() {
		return this.statementResourceInfo;
	}

	@Override
	protected void addResourceToChangeableResource(
			ChangeableResource choice, LocalIdStatement resource) {
		choice.setStatement(resource.getResource());
	}

	@Override
	protected LocalIdStatement createLocalIdResource(String id,
			Statement resource) {
		return new LocalIdStatement(id, resource);
	}

	@Override
	protected Statement getResourceFromChangeableResource(
			ChangeableResource choice) {
		return choice.getStatement();
	}

}
