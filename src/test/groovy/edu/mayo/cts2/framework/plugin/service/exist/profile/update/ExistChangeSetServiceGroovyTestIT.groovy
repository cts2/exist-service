package edu.mayo.cts2.framework.plugin.service.exist.profile.update

import static org.junit.Assert.*

import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry
import edu.mayo.cts2.framework.model.core.ChangeableElementGroup
import edu.mayo.cts2.framework.model.core.types.FinalizableState
import edu.mayo.cts2.framework.model.mapversion.*
import edu.mayo.cts2.framework.model.updates.ChangeSet
import edu.mayo.cts2.framework.model.updates.ChangeableResource
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.plugin.service.exist.profile.BaseServiceDbCleaningBase
import edu.mayo.cts2.framework.plugin.service.exist.profile.codesystem.ExistCodeSystemReadService

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
		
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()
		
		def changeSet = new ChangeSet()
		changeSet.state = FinalizableState.OPEN
		changeSet.changeSetURI = "urn:oid:12345"
		changeSet.creationDate = new Date()
		changeSet.addMember(new ChangeableResource(
			entryOrder: 1,
			codeSystem: new CodeSystemCatalogEntry(codeSystemName:"csName", about:"http://test")
		)
		)
	
		service.importChangeSet(changeSet)
		
		changeSetService.commitChangeSet(changeSet.changeSetURI);
		
		assertNotNull csService.read(ModelUtils.nameOrUriFromName("csName"), null)
	}
}
