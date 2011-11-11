package edu.mayo.cts2.framework.plugin.service.exist.integration;

import static org.junit.Assert.*

import org.junit.Test

import edu.mayo.cts2.framework.model.core.CodeSystemReference
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference
import edu.mayo.cts2.framework.model.core.ScopedEntityName
import edu.mayo.cts2.framework.model.core.URIAndEntityName
import edu.mayo.cts2.framework.model.entity.EntityDescription
import edu.mayo.cts2.framework.model.entity.EntityDirectory
import edu.mayo.cts2.framework.model.entity.NamedEntityDescription

class EntityDescriptionQueryServiceTestIT extends BaseServiceTestITBase {

	
	@Test void testGetEntitiesWithPageLimit(){

		def getResourceURI = server +  "codesystem/cs/version/2.0/entities?maxtoreturn=1" 
		def postResourceURI = "entity"
		
		def entry = new NamedEntityDescription(about:"about")
		entry.setEntityID(new ScopedEntityName(name:"name", namespace:"ns"))
		entry.setDescribingCodeSystemVersion(new CodeSystemVersionReference())
    	entry.getDescribingCodeSystemVersion().setVersion(new NameAndMeaningReference())
    	entry.getDescribingCodeSystemVersion().getVersion().setContent("cs_2.0")
    	entry.getDescribingCodeSystemVersion().setCodeSystem(new CodeSystemReference())
    	entry.getDescribingCodeSystemVersion().getCodeSystem().setContent("cs")

		entry.addEntityType(new URIAndEntityName(name:"name", namespace:"ns"))
		
		this.createResource(postResourceURI, new EntityDescription(namedEntity:entry))
		
		entry.getEntityID().setName("otherthing");
		this.createResource(postResourceURI, new EntityDescription(namedEntity:entry))
		
		EntityDirectory directory = 
			client.getCts2Resource(getResourceURI, EntityDirectory.class)
			
		assertEquals 1, directory.getEntryCount()
	}
}
