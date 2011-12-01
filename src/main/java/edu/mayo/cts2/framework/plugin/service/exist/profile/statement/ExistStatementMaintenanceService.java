package edu.mayo.cts2.framework.plugin.service.exist.profile.statement;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.core.ChangeableElementGroup;
import edu.mayo.cts2.framework.model.extension.LocalIdStatement;
import edu.mayo.cts2.framework.model.statement.Statement;
import edu.mayo.cts2.framework.model.updates.ChangeableResourceChoice;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistLocalIdMaintenanceService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
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
	protected ResourceInfo<LocalIdStatement, StatementReadId> getResourceInfo() {
		return this.statementResourceInfo;
	}

	@Override
	protected void addResourceToChangeableResourceChoice(
			ChangeableResourceChoice choice, LocalIdStatement resource) {
		choice.setStatement(resource.getResource());
	}

	@Override
	protected ChangeableElementGroup getChangeableElementGroup(
			LocalIdStatement resource) {
		return resource.getResource().getChangeableElementGroup();
	}

	@Override
	protected LocalIdStatement createLocalIdResource(String id,
			Statement resource) {
		return new LocalIdStatement(id, resource);
	}

}
