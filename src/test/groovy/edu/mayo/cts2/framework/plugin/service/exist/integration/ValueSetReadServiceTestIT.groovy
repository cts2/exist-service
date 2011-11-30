package edu.mayo.cts2.framework.plugin.service.exist.integration;

import static org.junit.Assert.*

import org.junit.Test

import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntry
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntryMsg


class ValueSetReadServiceTestIT extends BaseReadServiceTestITBase {
	
	@Override
	public Object getResourceClass() {
		ValueSetCatalogEntryMsg.class
	}

	@Override
	public Object getReadByNameUrl() {
		"valueset/TESTVALUESET"
	}

	@Override
	public Object getReadByUriUrl() {
		"valuesetbyuri?uri=http://vstestAbout"
	}

	@Override
	public Object getCreateUrl() {
		"valueset"
	}

	@Override
	public Object getResource() {
		new ValueSetCatalogEntry(about:"http://vstestAbout", valueSetName:"TESTVALUESET")
	}

	@Override
	public Object resourcesEqual(msg) {
		msg.getValueSetCatalogEntry().getValueSetName().equals("TESTVALUESET")
	}

}
