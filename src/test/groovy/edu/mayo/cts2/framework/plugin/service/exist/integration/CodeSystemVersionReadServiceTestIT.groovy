package edu.mayo.cts2.framework.plugin.service.exist.integration;

import static org.junit.Assert.*

import org.junit.Ignore
import org.junit.Test

import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry
import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntryMsg
import edu.mayo.cts2.framework.model.core.CodeSystemReference
import edu.mayo.cts2.framework.model.core.SourceAndNotation


class CodeSystemVersionReadServiceTestIT extends BaseServiceTestITBase {

	def getResourceURI = server +  "codesystem/TESTCS/version/2.0"
	def postResourceURI = "codesystemversion"
	
	@Test void testGetCodeSystemVersionByNameCycleWithNonExpectedNameFormat(){

		def entry = new CodeSystemVersionCatalogEntry(documentURI:"docuri", about:"testAbout", codeSystemVersionName:"TESTCSVERSION")
		entry.setSourceAndNotation(new SourceAndNotation());
		entry.setVersionOf(new CodeSystemReference(content:"TESTCS"));
		entry.setOfficialResourceVersionId("2.0");

		this.createResource(postResourceURI, entry)	

		def msg =
				client.getCts2Resource(getResourceURI, CodeSystemVersionCatalogEntryMsg.class)

		assertEquals entry.documentURI, msg.getCodeSystemVersionCatalogEntry().getDocumentURI()
	}

	@Test void testGetCodeSystemVersionByNameWithExpectedNameFormat(){

		def entry = new CodeSystemVersionCatalogEntry(documentURI:"docuri", about:"testAbout", codeSystemVersionName:"cs_2.0")
		entry.setSourceAndNotation(new SourceAndNotation());
		entry.setVersionOf(new CodeSystemReference(content:"cs"));
		entry.setOfficialResourceVersionId("2.0");

		this.createResource(postResourceURI, entry)	

		def msg =
				client.getCts2Resource(getResourceURI, CodeSystemVersionCatalogEntryMsg.class)

		assertEquals entry.documentURI, msg.getCodeSystemVersionCatalogEntry().getDocumentURI()
	}
}
