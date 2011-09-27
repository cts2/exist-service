package edu.mayo.cts2.sdk.plugin.service.exist.integration;

import static org.junit.Assert.*

import org.junit.Test

import edu.mayo.cts2.sdk.model.core.CodeSystemReference
import edu.mayo.cts2.sdk.model.core.CodeSystemVersionReference
import edu.mayo.cts2.sdk.model.core.NameAndMeaningReference
import edu.mayo.cts2.sdk.model.core.ScopedEntityName
import edu.mayo.cts2.sdk.model.core.URIAndEntityName
import edu.mayo.cts2.sdk.model.entity.EntityDescription
import edu.mayo.cts2.sdk.model.entity.EntityDescriptionMsg
import edu.mayo.cts2.sdk.model.entity.NamedEntityDescription

class EntityDescriptionReadServiceTestIT extends BaseServiceTestITBase {

	
	@Test void testGetEntityByNameCycle(){

		def resourceURI = server +  "codesystem/TESTCS/version/TESTCSVERSION/entity/ns:name"
		
		def entry = new NamedEntityDescription(about:"about")
		entry.setEntityID(new ScopedEntityName(name:"name", namespace:"ns"))
		entry.setDescribingCodeSystemVersion(new CodeSystemVersionReference())
    	entry.getDescribingCodeSystemVersion().setVersion(new NameAndMeaningReference())
    	entry.getDescribingCodeSystemVersion().getVersion().setContent("TESTCSVERSION")
    	entry.getDescribingCodeSystemVersion().setCodeSystem(new CodeSystemReference())
    	entry.getDescribingCodeSystemVersion().getCodeSystem().setContent("TESTCS")

		entry.addEntityType(new URIAndEntityName(name:"name", namespace:"ns"))
		
		client.putCts2Resource(resourceURI, new EntityDescription(namedEntity:entry))
		
		def msg = 
			client.getCts2Resource(resourceURI, EntityDescriptionMsg.class)
			
		assertEquals entry.entityID, msg.getEntityDescription().getNamedEntity().getEntityID()
	}
}
