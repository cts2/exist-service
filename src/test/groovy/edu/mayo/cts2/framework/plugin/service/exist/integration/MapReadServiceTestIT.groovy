package edu.mayo.cts2.framework.plugin.service.exist.integration;

import static org.junit.Assert.*

import org.junit.Test

import edu.mayo.cts2.framework.model.map.MapCatalogEntry
import edu.mayo.cts2.framework.model.map.MapCatalogEntryMsg


class MapReadServiceTestIT extends BaseServiceTestITBase {
			
	@Test void testGetCodeSystemByNameCycle(){

		def resourceURI = server +  "map/TESTMAP"
		
		def entry = new MapCatalogEntry(about:"testAbout", mapName:"TESTMAP")
	
		client.putCts2Resource(resourceURI, entry)
		
		def msg = 
			client.getCts2Resource(resourceURI,MapCatalogEntryMsg.class)
			
		assertEquals entry, msg.getMap()
	}
}
