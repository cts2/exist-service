package edu.mayo.cts2.framework.plugin.service.exist.integration;

import static org.junit.Assert.*

import org.junit.Test

import edu.mayo.cts2.framework.model.map.MapCatalogEntry
import edu.mayo.cts2.framework.model.map.MapCatalogEntryMsg


class MapReadServiceTestIT extends BaseServiceTestITBase {
			
	@Test void testGetCodeSystemByNameCycle(){

		def getResourceURI = server +  "map/TESTMAP"
		def postResourceURI = "map"
		
		def entry = new MapCatalogEntry(about:"testAbout", mapName:"TESTMAP")
	
		this.createResource(postResourceURI,entry)
		
		def msg = 
			client.getCts2Resource(getResourceURI,MapCatalogEntryMsg.class)
			
		assertEquals entry.getMapName(), msg.getMap().getMapName()
	}
}
