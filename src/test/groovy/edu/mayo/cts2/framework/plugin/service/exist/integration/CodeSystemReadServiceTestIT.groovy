package edu.mayo.cts2.framework.plugin.service.exist.integration;

import static org.junit.Assert.*
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntryMsg

class CodeSystemReadServiceTestIT extends BaseReadServiceTestITBase {

	@Override
	public Object getReadByNameUrl() {
		"codesystem/TESTCS"
	}
	
	@Override
	public Object getReadByUriUrl() {
		"codesystembyuri?uri=testAbout"
	}

	@Override
	getCreateUrl() {
		"codesystem"
	}

	@Override
	getResource() {
		new CodeSystemCatalogEntry(about:"testAbout", codeSystemName:"TESTCS")
	}

	@Override
	resourcesEqual(returned) {
		returned.getCodeSystemCatalogEntry().getCodeSystemName().equals("TESTCS")
	}

	@Override
	public Object getResourceClass() {
		CodeSystemCatalogEntryMsg.class
	}
	
	
}
