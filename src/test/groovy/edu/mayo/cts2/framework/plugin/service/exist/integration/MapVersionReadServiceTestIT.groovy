package edu.mayo.cts2.framework.plugin.service.exist.integration;

import static org.junit.Assert.*

import org.junit.Test

import edu.mayo.cts2.framework.model.core.MapReference
import edu.mayo.cts2.framework.model.core.SourceAndNotation
import edu.mayo.cts2.framework.model.mapversion.MapVersion
import edu.mayo.cts2.framework.model.mapversion.MapVersionMsg


class MapVersionReadServiceTestIT extends BaseServiceTestITBase {
			
	@Test void testGetCodeSystemByNameCycle(){

		def getResourceURI = server +  "map/TESTMAP/version/TESTMAPVERSION"
		def postResourceURI = "mapversion"
		
		def entry = new MapVersion(documentURI:"docuri", about:"testAbout", mapVersionName:"TESTMAPVERSION")
		entry.setSourceAndNotation(new SourceAndNotation());
		entry.setVersionOf(new MapReference())
		
		this.createResource(postResourceURI,entry)
		
		def msg = 
			client.getCts2Resource(getResourceURI,MapVersionMsg.class)
			
		assertEquals entry.getMapVersionName(), msg.getMapVersion().getMapVersionName()
	}
}
