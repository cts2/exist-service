package edu.mayo.cts2.framework.plugin.service.exist.integration;

import static org.junit.Assert.*
import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry
import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntryMsg
import edu.mayo.cts2.framework.model.core.CodeSystemReference
import edu.mayo.cts2.framework.model.core.SourceAndNotation

class CodeSystemVersionMaintenanceServiceTestIT extends BaseMaintenanceServiceTestITBase {

	@Override
	getResourceClass() {
		CodeSystemVersionCatalogEntryMsg.class
	}

	@Override
	getQueryUrl() {
		"codesystemversions"
	}
	
	@Override
	getCreateUrl(){
		"codesystemversion"
	}

	@Override
	getReadByNameUrl() {
		"codesystem/TESTCS/version/TESTVERSION"
	}

	@Override
	getResource() {
		def entry = new CodeSystemVersionCatalogEntry(
			documentURI:"http://test/docuri.org", 
			about:"http://testAbout.org", 
			codeSystemVersionName:"TESTVERSION")
		entry.setSourceAndNotation(new SourceAndNotation());
		entry.setVersionOf(new CodeSystemReference(content:"TESTCS"));
		entry.setOfficialResourceVersionId("2.0");
		
		entry
	}

	@Override
	public Object getUpdatedResource() {
		def updated = getResource()
		updated.setFormalName("test")
		
		updated
	}

	@Override
	checkCreate(resource) {
		resource.getCodeSystemVersionCatalogEntry().getCodeSystemVersionName().equals("TESTVERSION")
	}

	@Override
	checkUpdate(resource) {
		resource.getCodeSystemVersionCatalogEntry().getFormalName().equals("test")
	}

	
	
}
