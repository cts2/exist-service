package edu.mayo.cts2.framework.plugin.service.exist.profile.update

import static org.junit.Assert.*

import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import edu.mayo.cts2.framework.model.mapversion.*
import edu.mayo.cts2.framework.model.updates.ChangeSet
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.plugin.service.exist.profile.BaseServiceDbCleaningBase

class ExistChangeSetServiceGroovyTestIT extends BaseServiceDbCleaningBase {

	@Autowired
	ExistChangeSetService service

	@Test void TestUpdateChangeSetMetadata(){
		
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()
		
		service.updateChangeSetMetadata(changeSetUri, null, ModelUtils.createOpaqueData("changed!!"), null);
		
		ChangeSet updatedChangeSet = changeSetService.readChangeSet(changeSetUri);
		
		assertEquals "changed!!", updatedChangeSet.changeSetElementGroup.changeInstructions.value.content;
	}

}
