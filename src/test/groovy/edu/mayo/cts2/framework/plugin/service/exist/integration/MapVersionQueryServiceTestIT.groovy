package edu.mayo.cts2.framework.plugin.service.exist.integration;

import static org.junit.Assert.*
import edu.mayo.cts2.framework.model.core.MapReference
import edu.mayo.cts2.framework.model.core.SourceAndNotation
import edu.mayo.cts2.framework.model.mapversion.MapVersion
import edu.mayo.cts2.framework.model.mapversion.MapVersionDirectory

class MapVersionQueryServiceTestIT extends BaseQueryServiceTestITBase {

	@Override
	getResourceClass() {
		MapVersionDirectory.class
	}

	@Override
	getQueryUrl() {
		"mapversions"
	}
	
	@Override
	getCreateUrl(){
		"mapversion"
	}

	@Override
	List getResources() {
		[createEntry("1", "http://testAbout1"),
			createEntry("2", "http://testAbout2"),
			createEntry("3", "http://testAbout3")]
	}

	def createEntry(name,uri) {
		def entry = new MapVersion(documentURI:uri, about:"http://testAbout", mapVersionName:name)
		entry.setSourceAndNotation(new SourceAndNotation());
		entry.setVersionOf(new MapReference(content:"testmap"))
		
		entry
	}
	
}
