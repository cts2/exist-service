package edu.mayo.cts2.framework.plugin.service.exist.integration;

import static org.junit.Assert.*

import org.junit.Test

import edu.mayo.cts2.framework.model.conceptdomain.ConceptDomainCatalogEntry
import edu.mayo.cts2.framework.model.conceptdomain.ConceptDomainCatalogEntryMsg


class ConceptDomainReadServiceTestIT extends BaseServiceTestITBase {
			
	@Test void testGetConceptDomainByNameCycle(){

		def getResourceURI = server +  "conceptdomain/TESTDOMAIN"
		def postResourceURI = "conceptdomain"

		def entry = new ConceptDomainCatalogEntry(conceptDomainName:"TESTDOMAIN", about:"http://about")
	
		this.createResource(postResourceURI, entry)	
		
		def msg = 
			client.getCts2Resource(getResourceURI, ConceptDomainCatalogEntryMsg.class)
			
		assertEquals entry.getConceptDomainName(), msg.getConceptDomainCatalogEntry().getConceptDomainName()
	
	}
}
