package edu.mayo.cts2.framework.plugin.service.exist.integration;

import static org.junit.Assert.*

import org.junit.Test

import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry
import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntryMsg
import edu.mayo.cts2.framework.model.core.CodeSystemReference
import edu.mayo.cts2.framework.model.core.SourceAndNotation


class CodeSystemVersionReadServiceTestIT extends BaseServiceTestITBase {


	@Test void testGetCodeSystemVersionByNameCycleWithNonExpectedNameFormat(){

		def resourceURI = server +  "codesystem/TESTCS/version/2.0"

		def entry = new CodeSystemVersionCatalogEntry(documentURI:"docuri", about:"testAbout", codeSystemVersionName:"TESTCSVERSION")
		entry.setSourceAndNotation(new SourceAndNotation());
		entry.setVersionOf(new CodeSystemReference(content:"TESTCS"));
		entry.setOfficialResourceVersionId("2.0");

		client.putCts2Resource(resourceURI, entry)

		def msg =
				client.getCts2Resource(resourceURI, CodeSystemVersionCatalogEntryMsg.class)

		assertEquals entry.documentURI, msg.getCodeSystemVersionCatalogEntry().getDocumentURI()
	}

	@Test void testGetCodeSystemVersionByNameWithExpectedNameFormat(){

		def resourceURI = server +  "codesystem/TESTCS/version/2.0"

		def entry = new CodeSystemVersionCatalogEntry(documentURI:"docuri", about:"testAbout", codeSystemVersionName:"cs_2.0")
		entry.setSourceAndNotation(new SourceAndNotation());
		entry.setVersionOf(new CodeSystemReference(content:"cs"));
		entry.setOfficialResourceVersionId("2.0");

		client.putCts2Resource(resourceURI, entry)

		def msg =
				client.getCts2Resource(resourceURI, CodeSystemVersionCatalogEntryMsg.class)

		assertEquals entry.documentURI, msg.getCodeSystemVersionCatalogEntry().getDocumentURI()
	}
}
