package edu.mayo.cts2.framework.plugin.service.exist.integration;

import static org.junit.Assert.*

import org.junit.Test

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntryMsg

class CodeSystemReadServiceTestIT extends BaseServiceTestITBase {

			
	@Test void testGetCodeSystemByNameCycle(){

	
		def postResourceURI = "codesystem"
		def getResourceURI = server +  "codesystem/TESTCS"
		
		def entry = new CodeSystemCatalogEntry(about:"testAbout", codeSystemName:"TESTCS")

		this.createResource(postResourceURI, entry)	
			
		def msg = 
			client.getCts2Resource(getResourceURI, CodeSystemCatalogEntryMsg.class)
			
		assertEquals entry.getCodeSystemName(), msg.getCodeSystemCatalogEntry().getCodeSystemName()

	}
}
