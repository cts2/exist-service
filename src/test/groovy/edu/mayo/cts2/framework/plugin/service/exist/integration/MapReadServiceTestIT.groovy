package edu.mayo.cts2.framework.plugin.service.exist.integration;

import static org.junit.Assert.*

import org.junit.Test

import edu.mayo.cts2.framework.model.map.MapCatalogEntry
import edu.mayo.cts2.framework.model.map.MapCatalogEntryMsg


class MapReadServiceTestIT extends BaseReadServiceTestITBase {

	@Override
	public Object getResourceClass() {
		MapCatalogEntryMsg.class
	}

	@Override
	public Object getReadByNameUrl() {
		"map/TESTMAP"
	}

	@Override
	public Object getReadByUriUrl() {
		"mapbyuri?uri=http://maptestAbout"
	}

	@Override
	public Object getCreateUrl() {
		"map"
	}

	@Override
	public Object getResource() {
		new MapCatalogEntry(about:"http://maptestAbout", mapName:"TESTMAP")
	}

	@Override
	public Object resourcesEqual(msg) {
		msg.getMap().getMapName().equals("TESTMAP")
	}
	
	
}
