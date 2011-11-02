package edu.mayo.cts2.framework.plugin.service.exist.integration;

import static org.junit.Assert.*

import org.junit.Test

import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntry
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntryMsg


class ValueSetReadServiceTestIT extends BaseServiceTestITBase {
			
	@Test void testGetValueSetByNameCycle(){

		def getResourceURI = server +  "valueset/TESTVALUESET"
		def postResourceURI = "valueset"
		
		def entry = new ValueSetCatalogEntry(about:"testAbout", valueSetName:"TESTVALUESET")
	
		this.createResource(postResourceURI,entry)
		
		def msg = 
			client.getCts2Resource(getResourceURI,ValueSetCatalogEntryMsg.class)
			
		assertEquals entry.getValueSetName(), msg.getValueSetCatalogEntry().getValueSetName()
	}
}
