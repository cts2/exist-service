package edu.mayo.cts2.framework.plugin.service.exist.integration;

import static org.junit.Assert.*
import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry
import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntryDirectory
import edu.mayo.cts2.framework.model.core.CodeSystemReference
import edu.mayo.cts2.framework.model.core.SourceAndNotation

class CodeSystemVersionQueryServiceTestIT extends BaseQueryServiceTestITBase {

	@Override
	getResourceClass() {
		CodeSystemVersionCatalogEntryDirectory.class
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
	List getResources() {
		def entry1 = new CodeSystemVersionCatalogEntry(documentURI:"docuri1", about:"testAbout1", codeSystemVersionName:"TESTCSVERSION1")
		entry1.setSourceAndNotation(new SourceAndNotation());
		entry1.setVersionOf(new CodeSystemReference(content:"TESTCS1"));
		entry1.setOfficialResourceVersionId("1.0");
		
		def entry2 = new CodeSystemVersionCatalogEntry(documentURI:"docuri2", about:"testAbout2", codeSystemVersionName:"TESTCSVERSION2")
		entry2.setSourceAndNotation(new SourceAndNotation());
		entry2.setVersionOf(new CodeSystemReference(content:"TESTCS2"));
		entry2.setOfficialResourceVersionId("2.0");

		def entry3 = new CodeSystemVersionCatalogEntry(documentURI:"docuri3", about:"testAbout3", codeSystemVersionName:"TESTCSVERSION3")
		entry3.setSourceAndNotation(new SourceAndNotation());
		entry3.setVersionOf(new CodeSystemReference(content:"TESTCS3"));
		entry3.setOfficialResourceVersionId("3.0");
		
		[entry1,entry2,entry3]

	}

	
	
}
