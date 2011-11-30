package edu.mayo.cts2.framework.plugin.service.exist.integration;

import static org.junit.Assert.*

import org.junit.Test
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException

import edu.mayo.cts2.framework.model.core.ChangeDescription
import edu.mayo.cts2.framework.model.core.ChangeableElementGroup
import edu.mayo.cts2.framework.model.core.types.ChangeType
import edu.mayo.cts2.framework.model.util.ModelUtils

abstract class BaseMaintenanceServiceTestITBase extends BaseServiceTestITBase {
	
	@Test
	void TestCreate(){
		this.createResource(getCreateUrl(), getResource())
		
		assertTrue checkCreate(read(getReadByNameUrl(),getResourceClass()))
	}
	
	@Test
	void TestUpdate(){
		def updated = getUpdatedResource()
		
		def choice = ModelUtils.createChangeableResourceChoice(updated)
		
		ModelUtils.setChangeableElementGroup(choice, 
			new ChangeableElementGroup(
				changeDescription:new ChangeDescription(
					changeDate:new Date(),
					containingChangeSet:changeSetUri,
					changeType:ChangeType.UPDATE)))
		
		client.putCts2Resource(server+getReadByNameUrl(), updated)
		
		assertTrue checkUpdate(read(getReadByNameUrl(),getResourceClass()))
		
	}
	
	@Test
	void TestDelete(){
		this.createResource(getCreateUrl(), getResource())
		
		deleteResource(getReadByNameUrl())
		
		try{
		def returned = read(getReadByNameUrl(),getResourceClass())
		println returned
		} catch(HttpClientErrorException ex) {
			assertEquals ex.getStatusCode(), HttpStatus.NOT_FOUND
			return
		}
	
		fail()
	}
	
	void TestDeleteAfterCommit(){
		this.createResource(getCreateUrl(), getResource())
		
		deleteResource(getReadByNameUrl())
		
		try{
		def returned = read(getReadByNameUrl(),getResourceClass())

		} catch(HttpClientErrorException ex) {
			assertEquals ex.getStatusCode(), HttpStatus.NOT_FOUND
			return
		}
	
		fail()
	}

	abstract getResourceClass()
	
	abstract getReadByNameUrl()

	abstract getCreateUrl()
	
	abstract getResource()
	
	abstract getUpdatedResource()
	
	abstract checkCreate(resource);
	
	abstract checkUpdate(resource);
}
