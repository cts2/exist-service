package edu.mayo.cts2.framework.plugin.service.exist.profile.statement;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.core.ChangeableElementGroup;
import edu.mayo.cts2.framework.model.extension.LocalIdStatement;
import edu.mayo.cts2.framework.model.statement.Statement;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistLocalIdReadService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.LocalIdResourceInfo;
import edu.mayo.cts2.framework.service.profile.statement.StatementReadService;
import edu.mayo.cts2.framework.service.profile.statement.name.StatementReadId;

@Component
public class ExistStatementReadService 
	extends AbstractExistLocalIdReadService<
		Statement,
		LocalIdStatement,
		StatementReadId,
		edu.mayo.cts2.framework.model.service.statement.StatementService>
	implements StatementReadService {

	@Resource
	private StatementResourceInfo statementResourceInfo;

	@Override
	protected LocalIdResourceInfo<LocalIdStatement, StatementReadId> getResourceInfo() {
		return this.statementResourceInfo;
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
