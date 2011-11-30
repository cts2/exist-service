package edu.mayo.cts2.framework.plugin.service.exist.integration;

import static org.junit.Assert.*
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntryDirectory

class CodeSystemQueryServiceTestIT extends BaseQueryServiceTestITBase {

	@Override
	getResourceClass() {
		CodeSystemCatalogEntryDirectory.class
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
	List getResources() {
		[new CodeSystemCatalogEntry(about:"testAbout1", codeSystemName:"TESTCS1"),
			new CodeSystemCatalogEntry(about:"testAbout2", codeSystemName:"TESTCS2"),
			new CodeSystemCatalogEntry(about:"testAbout3", codeSystemName:"TESTCS3")]
	}

	
	
}
