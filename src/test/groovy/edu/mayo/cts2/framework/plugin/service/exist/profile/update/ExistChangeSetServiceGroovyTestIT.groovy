package edu.mayo.cts2.framework.plugin.service.exist.profile.update
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry
import edu.mayo.cts2.framework.model.core.ChangeDescription
import edu.mayo.cts2.framework.model.core.ChangeableElementGroup
import edu.mayo.cts2.framework.model.core.types.ChangeType
import edu.mayo.cts2.framework.model.core.types.FinalizableState
import edu.mayo.cts2.framework.model.updates.ChangeSet
import edu.mayo.cts2.framework.model.updates.ChangeableResource
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.plugin.service.exist.profile.BaseServiceDbCleaningBase
import edu.mayo.cts2.framework.plugin.service.exist.profile.codesystem.ExistCodeSystemReadService
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

class ExistChangeSetServiceGroovyTestIT extends BaseServiceDbCleaningBase {

	@Autowired
	ExistChangeSetService service
	
	@Autowired
	ExistCodeSystemReadService csService

	@Test void TestUpdateChangeSetMetadata(){
		
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()
		
		service.updateChangeSetMetadata(changeSetUri, null, ModelUtils.createOpaqueData("changed!!"), null);
		
		ChangeSet updatedChangeSet = changeSetService.readChangeSet(changeSetUri);
		
		assertEquals "changed!!", updatedChangeSet.changeSetElementGroup.changeInstructions.value.content;
	}
	
	@Test void TesImportChangeSet(){
		def changeable = new ChangeableResource()
		changeable.codeSystem = new CodeSystemCatalogEntry(codeSystemName:"csName", about:"http://test");
		changeable.setChangeableElementGroup(new ChangeableElementGroup(
			changeDescription: new ChangeDescription(
				changeType: ChangeType.CREATE,
				containingChangeSet: "urn:oid:12345",
				changeDate: new Date()
			)
		))
		changeable.entryOrder = 1
		
		def changeSet = new ChangeSet()
		changeSet.state = FinalizableState.OPEN
		changeSet.changeSetURI = "urn:oid:12345"
		changeSet.creationDate = new Date()
		changeSet.addMember(changeable)
	
		service.importChangeSet(changeSet)
		
		service.commitChangeSet(changeSet.changeSetURI);
		
		assertNotNull csService.read(ModelUtils.nameOrUriFromName("csName"), null)
	}
}
