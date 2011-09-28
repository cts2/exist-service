package edu.mayo.cts2.framework.plugin.service.exist.integration;

import static org.junit.Assert.*

import org.junit.Test

import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry
import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntryMsg
import edu.mayo.cts2.framework.model.core.CodeSystemReference
import edu.mayo.cts2.framework.model.core.SourceAndNotation


class CodeSystemVersionReadServiceTestIT extends BaseServiceTestITBase {


	@Test void testGetCodeSystemVersionByNameCycle(){

		def resourceURI = server +  "codesystem/TESTCS/version/TESTCSVERSION"

		def entry = new CodeSystemVersionCatalogEntry(documentURI:"docuri", about:"testAbout", codeSystemVersionName:"TESTCSVERSION")
		entry.setSourceAndNotation(new SourceAndNotation());
		entry.setVersionOf(new CodeSystemReference());

		client.putCts2Resource(resourceURI, entry)

		def msg =
				client.getCts2Resource(resourceURI, CodeSystemVersionCatalogEntryMsg.class)

		assertEquals entry.documentURI, msg.getCodeSystemVersionCatalogEntry().getDocumentURI()
	}

	@Test void testGetCodeSystemVersionByNameWithNumberCycle(){

		def resourceURI = server +  "codesystem/TESTCS/version/2.0"

		def entry = new CodeSystemVersionCatalogEntry(documentURI:"docuri", about:"testAbout", codeSystemVersionName:"2.0")
		entry.setSourceAndNotation(new SourceAndNotation());
		entry.setVersionOf(new CodeSystemReference());

		client.putCts2Resource(resourceURI, entry)

		def msg =
				client.getCts2Resource(resourceURI, CodeSystemVersionCatalogEntryMsg.class)

		assertEquals entry.about, msg.getCodeSystemVersionCatalogEntry().getAbout()
	}
}
