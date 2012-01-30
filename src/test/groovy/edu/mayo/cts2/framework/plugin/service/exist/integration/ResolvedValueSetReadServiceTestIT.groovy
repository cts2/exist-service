package edu.mayo.cts2.framework.plugin.service.exist.integration;

import static org.junit.Assert.*

import org.junit.Test
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpStatusCodeException

import edu.mayo.cts2.framework.model.core.NameAndMeaningReference
import edu.mayo.cts2.framework.model.core.ValueSetDefinitionReference
import edu.mayo.cts2.framework.model.core.ValueSetReference
import edu.mayo.cts2.framework.model.valuesetdefinition.ResolvedValueSet
import edu.mayo.cts2.framework.model.valuesetdefinition.ResolvedValueSetHeader
import edu.mayo.cts2.framework.model.valuesetdefinition.ResolvedValueSetMsg

class ResolvedValueSetReadServiceTestIT extends BaseServiceTestITBase {
	
	void createChangeSet(){}
	void rollbackChangeSet(){}
	
	@Test void readByName(){
		def url = 
			client.postCts2Resource(server+"resolvedvalueset", getResource())
			
		def rvs = client.getCts2Resource(server+url, ResolvedValueSetMsg.class)
		
		assertNotNull rvs
		
		client.deleteCts2Resource(server+url)
		
		try {
			rvs = client.getCts2Resource(server+url, ResolvedValueSetMsg.class)
		} catch(HttpStatusCodeException e){
			assertEquals HttpStatus.NOT_FOUND , e.getStatusCode()
			return
		}

		fail()

	}


	def getResource() {
		def rvs = new ResolvedValueSet(
			resolutionInfo: new ResolvedValueSetHeader(
				resolutionOf: new ValueSetDefinitionReference(
					valueSetDefinition: new NameAndMeaningReference(content:"vsd"),
					valueSet: new ValueSetReference(content:"vs")
					)));
	}


}
