package edu.mayo.cts2.sdk.plugin.service.exist.integration;

import static org.junit.Assert.*

import org.junit.Test

import edu.mayo.cts2.sdk.model.core.MapReference
import edu.mayo.cts2.sdk.model.core.SourceAndNotation
import edu.mayo.cts2.sdk.model.mapversion.MapVersion
import edu.mayo.cts2.sdk.model.mapversion.MapVersionMsg


class MapVersionReadServiceTestIT extends BaseServiceTestITBase {
			
	@Test void testGetCodeSystemByNameCycle(){

		def resourceURI = server +  "map/TESTMAP/version/TESTMAPVERSION"
		
		def entry = new MapVersion(documentURI:"docuri", about:"testAbout", mapVersionName:"TESTMAPVERSION")
		entry.setSourceAndNotation(new SourceAndNotation());
		entry.setVersionOf(new MapReference())
		
		client.putCts2Resource(resourceURI, entry)
		
		def msg = 
			client.getCts2Resource(resourceURI,MapVersionMsg.class)
			
		assertEquals entry, msg.getMapVersion()
	}
}
