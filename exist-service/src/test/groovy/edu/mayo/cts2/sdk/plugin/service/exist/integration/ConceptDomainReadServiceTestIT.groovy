package edu.mayo.cts2.framework.plugin.service.exist.integration;

import static org.junit.Assert.*

import org.junit.Test

import edu.mayo.cts2.framework.model.conceptdomain.ConceptDomainCatalogEntry
import edu.mayo.cts2.framework.model.conceptdomain.ConceptDomainCatalogEntryMsg


class ConceptDomainReadServiceTestIT extends BaseServiceTestITBase {
			
	@Test void testGetConceptDomainByNameCycle(){

		def resourceURI = server +  "conceptdomain/TESTDOMAIN"
		
		def entry = new ConceptDomainCatalogEntry(conceptDomainName:"TESTDOMAIN", about:"http://about")
	
		client.putCts2Resource(resourceURI, entry)
		
		def msg = 
			client.getCts2Resource(resourceURI, ConceptDomainCatalogEntryMsg.class)
			
		assertEquals entry, msg.getConceptDomainCatalogEntry()
	}
}
