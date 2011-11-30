package edu.mayo.cts2.framework.plugin.service.exist.integration;

import static org.junit.Assert.*

import org.junit.Test

import edu.mayo.cts2.framework.model.core.MapReference
import edu.mayo.cts2.framework.model.core.SourceAndNotation
import edu.mayo.cts2.framework.model.mapversion.MapVersion
import edu.mayo.cts2.framework.model.mapversion.MapVersionMsg


class MapVersionReadServiceTestIT extends BaseReadServiceTestITBase {

	@Override
	public Object getResourceClass() {
		MapVersionMsg.class
	}

	@Override
	public Object getReadByNameUrl() {
		 "map/TESTMAP/version/TESTMAPVERSION"
	}

	@Override
	public Object getReadByUriUrl() {
		 "mapversionbyuri?uri=http://docuri"
	}

	@Override
	public Object getCreateUrl() {
		"mapversion"
	}

	@Override
	public Object getResource() {
		def entry = new MapVersion(documentURI:"http://docuri", about:"http://testAbout", mapVersionName:"TESTMAPVERSION")
		entry.setSourceAndNotation(new SourceAndNotation());
		entry.setVersionOf(new MapReference())
		
		entry
	}

	@Override
	public Object resourcesEqual(msg) {
		msg.getMapVersion().getMapVersionName().equals("TESTMAPVERSION")
	}
}
