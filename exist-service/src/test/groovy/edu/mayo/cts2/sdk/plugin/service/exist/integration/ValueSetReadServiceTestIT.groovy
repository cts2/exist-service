package edu.mayo.cts2.framework.plugin.service.exist.integration;

import static org.junit.Assert.*

import org.junit.Test

import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntry
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntryMsg


class ValueSetReadServiceTestIT extends BaseServiceTestITBase {
			
	@Test void testGetValueSetByNameCycle(){

		def resourceURI = server +  "valueset/TESTVALUESET"
		
		def entry = new ValueSetCatalogEntry(about:"testAbout", valueSetName:"TESTVALUESET")
	
		client.putCts2Resource(resourceURI, entry)
		
		def msg = 
			client.getCts2Resource(resourceURI,ValueSetCatalogEntryMsg.class)
			
		assertEquals entry, msg.getValueSetCatalogEntry()
	}
}
