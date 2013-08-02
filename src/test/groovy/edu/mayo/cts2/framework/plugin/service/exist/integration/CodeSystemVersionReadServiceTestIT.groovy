package edu.mayo.cts2.framework.plugin.service.exist.integration
import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry
import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntryMsg
import edu.mayo.cts2.framework.model.core.CodeSystemReference
import edu.mayo.cts2.framework.model.core.SourceAndNotation
import org.junit.Test

import static org.junit.Assert.assertEquals


class CodeSystemVersionReadServiceTestIT extends BaseReadServiceTestITBase {

	def getResourceURI = "codesystem/TESTCS/version/2.0"
	def postResourceURI = "codesystemversion"
	
	@Test void testGetCodeSystemVersionByNameCycleWithNonExpectedNameFormat(){

		def entry = getResource()

		this.createResource(postResourceURI, entry)	

		def msg = read(getResourceURI,getResourceClass())

		assertEquals entry.documentURI, msg.getCodeSystemVersionCatalogEntry().getDocumentURI()
	}

	@Override
	public Object getResourceClass() {
		CodeSystemVersionCatalogEntryMsg.class
	}

	@Override
	public Object getReadByNameUrl() {
		getResourceURI
	}

	@Override
	public Object getReadByUriUrl() {
		"codesystemversionbyuri?uri=http://testAbout.org"
	}

	@Override
	public Object getCreateUrl() {
		"codesystemversion"
	}

	@Override
	public Object getResource() {
		def entry = new CodeSystemVersionCatalogEntry(about:"http://testAbout.org", codeSystemVersionName:"TESTCSVERSION")
		entry.setSourceAndNotation(new SourceAndNotation());
		entry.setVersionOf(new CodeSystemReference(content:"TESTCS"));
		entry.setOfficialResourceVersionId("2.0");
		
		entry
	}

	@Override
	public resourcesEqual(resource) {
		resource.getCodeSystemVersionCatalogEntry().getAbout().equals("http://testAbout.org")
	}
	
	
}
