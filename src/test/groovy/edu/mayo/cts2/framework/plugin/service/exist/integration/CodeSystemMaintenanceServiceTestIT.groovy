package edu.mayo.cts2.framework.plugin.service.exist.integration;

import static org.junit.Assert.*
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntryMsg
import edu.mayo.cts2.framework.model.core.ChangeDescription
import edu.mayo.cts2.framework.model.core.ChangeableElementGroup
import edu.mayo.cts2.framework.model.core.types.ChangeType

class CodeSystemMaintenanceServiceTestIT extends BaseMaintenanceServiceTestITBase {

	@Override
	getResourceClass() {
		CodeSystemCatalogEntryMsg.class
	}

	@Override
	getQueryUrl() {
		"codesystems"
	}
	
	@Override
	getCreateUrl(){
		"codesystem"
	}

	@Override
	getReadByNameUrl() {
		"codesystem/TESTCS"
	}

	@Override
	getResource() {
		new CodeSystemCatalogEntry(about:"testAbout", codeSystemName:"TESTCS")
	}

	@Override
	public Object getUpdatedResource() {
		new CodeSystemCatalogEntry(
			about:"testAbout", 
			codeSystemName:"TESTCS", 
			formalName:"testFormalName"
			)
	}

	@Override
	checkCreate(resource) {
		resource.getCodeSystemCatalogEntry().getCodeSystemName().equals("TESTCS")
	}

	@Override
	checkUpdate(resource) {
		resource.getCodeSystemCatalogEntry().getFormalName().equals("testFormalName")
	}

	
	
}
