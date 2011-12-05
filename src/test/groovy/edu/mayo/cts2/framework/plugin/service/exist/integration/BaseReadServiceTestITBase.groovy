package edu.mayo.cts2.framework.plugin.service.exist.integration;

import static org.junit.Assert.*

import org.junit.Test
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpStatusCodeException

abstract class BaseReadServiceTestITBase extends BaseServiceTestITBase {

	
	@Test
	void TestReadByName(){
		def url = getReadByNameUrl()
		if(url != null){
			this.createResource(getCreateUrl(), getResource())
			
			assertTrue resourcesEqual(read(getReadByNameUrl(), getResourceClass()))
		}
	}
	
	@Test
	void TestReadByNameNotFound(){
		def url = getReadByNameUrl()
		if(url != null){
			this.createResource(getCreateUrl(), getResource())
			
			try{
				read(getReadByNameUrl()+"INVALID", getResourceClass())
			} catch(HttpStatusCodeException e){
				assertEquals HttpStatus.NOT_FOUND , e.getStatusCode()
				return
			}
			
			fail()
		}
	}
	
	@Test
	void TestReadByNameAfterCommit(){
		def url = getReadByNameUrl()
		if(url != null){
			this.createResource(getCreateUrl(), getResource())
			this.commitChangeSet();
			
			assertTrue resourcesEqual(readCurrent(getReadByNameUrl(), getResourceClass()))
			
			this.createChangeSet();
			this.deleteResource(getReadByNameUrl());
			commitChangeSet()
		}
	}
	
	@Test
	void TestReadByUri(){
		this.createResource(getCreateUrl(), getResource())
		
		assertTrue resourcesEqual(read(getReadByUriUrl(), getResourceClass()))
		
	}
	
	@Test
	void TestReadByUriNotFound(){
		this.createResource(getCreateUrl(), getResource())
		
		try{
			assertTrue resourcesEqual(read(getReadByUriUrl()+"INVALID", getResourceClass()))
		} catch(HttpStatusCodeException e){
			assertEquals HttpStatus.NOT_FOUND , e.getStatusCode()
			return
		}

		fail()
		
	}

	abstract getResourceClass()
	
	abstract getReadByNameUrl()
	
	abstract getReadByUriUrl()
	
	abstract getCreateUrl()
	
	abstract getResource()
	
	abstract resourcesEqual(resource);
}
