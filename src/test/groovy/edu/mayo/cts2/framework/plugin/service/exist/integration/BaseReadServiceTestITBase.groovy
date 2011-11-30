package edu.mayo.cts2.framework.plugin.service.exist.integration;

import org.junit.Test

import static org.junit.Assert.*

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

	abstract getResourceClass()
	
	abstract getReadByNameUrl()
	
	abstract getReadByUriUrl()
	
	abstract getCreateUrl()
	
	abstract getResource()
	
	abstract resourcesEqual(resource);
}
