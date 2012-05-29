package edu.mayo.cts2.framework.plugin.service.exist.integration;

import static org.junit.Assert.*

import org.junit.Test

import edu.mayo.cts2.framework.model.core.CodeSystemReference
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference
import edu.mayo.cts2.framework.model.core.ScopedEntityName
import edu.mayo.cts2.framework.model.core.URIAndEntityName
import edu.mayo.cts2.framework.model.entity.EntityDescription
import edu.mayo.cts2.framework.model.entity.EntityDescriptionMsg
import edu.mayo.cts2.framework.model.entity.NamedEntityDescription

class EntityDescriptionReadServiceTestIT extends BaseReadServiceTestITBase {
	
	@Override
	public Object getResourceClass() {
		EntityDescriptionMsg.class
	}

	@Override
	public Object getReadByNameUrl() {
		"codesystem/cs/version/csversionentitycsv/entity/ns:name" 
	}

	@Override
	public Object getReadByUriUrl() {
		"codesystem/cs/version/csversionentitycsv/entitybyuri?uri=http://entity/about"
	}

	@Override
	public Object getCreateUrl() {
		"entity"
	}

	@Override
	public Object getResource() {
		def entry = new NamedEntityDescription(about:"http://entity/about")
		entry.setEntityID(new ScopedEntityName(name:"name", namespace:"ns"))
		entry.setDescribingCodeSystemVersion(new CodeSystemVersionReference())
    	entry.getDescribingCodeSystemVersion().setVersion(new NameAndMeaningReference())
    	entry.getDescribingCodeSystemVersion().getVersion().setContent("csversionentitycsv")
    	entry.getDescribingCodeSystemVersion().setCodeSystem(new CodeSystemReference())
    	entry.getDescribingCodeSystemVersion().getCodeSystem().setContent("cs")

		entry.addEntityType(new URIAndEntityName(name:"name", namespace:"ns", uri:"uri"))
		
		EntityDescription ed = new EntityDescription()
		ed.setNamedEntity(entry)
		
		ed
	}

	@Override
	public Object resourcesEqual(msg) {
		msg.getEntityDescription().getNamedEntity().getEntityID().getName().equals("name")
	}
	
	
}
