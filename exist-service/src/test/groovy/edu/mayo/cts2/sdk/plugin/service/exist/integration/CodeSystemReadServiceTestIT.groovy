package edu.mayo.cts2.framework.plugin.service.exist.integration;

import static org.junit.Assert.*

import org.junit.Test

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntryMsg

class CodeSystemReadServiceTestIT extends BaseServiceTestITBase {

			
	@Test void testGetCodeSystemByNameCycle(){

		def resourceURI = server +  "codesystem/TESTCS"
		
		def entry = new CodeSystemCatalogEntry(about:"testAbout", codeSystemName:"TESTCS")
	
		client.putCts2Resource(resourceURI, entry)
		
		def msg = 
			client.getCts2Resource(resourceURI, CodeSystemCatalogEntryMsg.class)
			
		assertEquals entry, msg.getCodeSystemCatalogEntry()
	}
}
