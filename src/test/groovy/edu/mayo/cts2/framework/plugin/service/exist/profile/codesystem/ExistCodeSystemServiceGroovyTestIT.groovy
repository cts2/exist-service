package edu.mayo.cts2.framework.plugin.service.exist.profile.codesystem

import static org.junit.Assert.*

import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry
import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.model.command.ResolvedReadContext
import edu.mayo.cts2.framework.model.core.ChangeDescription
import edu.mayo.cts2.framework.model.core.ChangeableElementGroup
import edu.mayo.cts2.framework.model.core.types.ChangeType
import edu.mayo.cts2.framework.model.mapversion.*
import edu.mayo.cts2.framework.model.service.core.NameOrURI
import edu.mayo.cts2.framework.plugin.service.exist.profile.BaseServiceDbCleaningBase

class ExistCodeSystemServiceGroovyTestIT extends BaseServiceDbCleaningBase {

	@Autowired
	ExistCodeSystemReadService read
	
	@Autowired
	ExistCodeSystemQueryService query
	
	@Autowired
	ExistCodeSystemMaintenanceService maint

	@Test void TestReadContextNoChangeSetURI(){
		
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()
		
		def cs = new CodeSystemCatalogEntry(about:"about",codeSystemName:"name")
		cs.setChangeableElementGroup(new ChangeableElementGroup(
			changeDescription: new ChangeDescription(
				changeType: ChangeType.CREATE,
				changeDate: new Date(),
				containingChangeSet: changeSetUri)))
		
		maint.createResource(cs)

		assertNull read.read(new NameOrURI(name:"name"), null)
	}

	@Test void TestReadContextWithURI(){
		
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()
		
		def cs = new CodeSystemCatalogEntry(about:"about",codeSystemName:"name")
		cs.setChangeableElementGroup(new ChangeableElementGroup(
			changeDescription: new ChangeDescription(
				changeType: ChangeType.CREATE,
				changeDate: new Date(),
				containingChangeSet: changeSetUri)))
		
		maint.createResource(cs)

		assertNotNull read.read(new NameOrURI(name:"name"), new ResolvedReadContext(changeSetContextUri:changeSetUri))
	}
	
	@Test void TestQueryWithURI(){
		
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()
		
		def cs = new CodeSystemCatalogEntry(about:"about",codeSystemName:"name")
		cs.setChangeableElementGroup(new ChangeableElementGroup(
			changeDescription: new ChangeDescription(
				changeType: ChangeType.CREATE,
				changeDate: new Date(),
				containingChangeSet: changeSetUri)))
		
		maint.createResource(cs)

		assertEquals 1, query.getResourceSummaries(
			null, null, null, new ResolvedReadContext(changeSetContextUri:changeSetUri), new Page()).entries.size()
	}
	
	@Test void TestQueryWithNoURI(){
		
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()
		
		def cs = new CodeSystemCatalogEntry(about:"about",codeSystemName:"name")
		cs.setChangeableElementGroup(new ChangeableElementGroup(
			changeDescription: new ChangeDescription(
				changeType: ChangeType.CREATE,
				changeDate: new Date(),
				containingChangeSet: changeSetUri)))
		
		maint.createResource(cs)

		assertEquals 0, query.getResourceSummaries(
			null, null, null, null, new Page()).entries.size()
	}
	
}
