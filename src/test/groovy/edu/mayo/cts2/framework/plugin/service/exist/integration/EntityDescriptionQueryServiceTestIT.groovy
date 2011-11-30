package edu.mayo.cts2.framework.plugin.service.exist.integration;

import static org.junit.Assert.*

import java.util.List

import edu.mayo.cts2.framework.model.core.CodeSystemReference
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference
import edu.mayo.cts2.framework.model.core.ScopedEntityName
import edu.mayo.cts2.framework.model.core.URIAndEntityName
import edu.mayo.cts2.framework.model.entity.EntityDescription
import edu.mayo.cts2.framework.model.entity.EntityDirectory
import edu.mayo.cts2.framework.model.entity.NamedEntityDescription

class EntityDescriptionQueryServiceTestIT extends BaseQueryServiceTestITBase {

	@Override
	public Object getCreateUrl() {
		"entity"
	}

	@Override
	public Object getResourceClass() {
		EntityDirectory.class
	}

	@Override
	public Object getQueryUrl() {
		"codesystem/cs/version/csventitytest/entities"
	}
	
	List getResources() {
		def returnList = []
		['n1','n2','n3'].each {
			returnList.add(create(it))
		}
		
		returnList
	}

	def create(name) {
		def entry = new NamedEntityDescription(about:"about")
		entry.setEntityID(new ScopedEntityName(name:name, namespace:"ns"))
		entry.setDescribingCodeSystemVersion(new CodeSystemVersionReference())
    	entry.getDescribingCodeSystemVersion().setVersion(new NameAndMeaningReference())
    	entry.getDescribingCodeSystemVersion().getVersion().setContent("csventitytest")
    	entry.getDescribingCodeSystemVersion().setCodeSystem(new CodeSystemReference())
    	entry.getDescribingCodeSystemVersion().getCodeSystem().setContent("cs")

		entry.addEntityType(new URIAndEntityName(name:"name", namespace:"ns"))
		
		EntityDescription desc = new EntityDescription()
		desc.setNamedEntity(entry)
		
		desc
	}
	
	
}
