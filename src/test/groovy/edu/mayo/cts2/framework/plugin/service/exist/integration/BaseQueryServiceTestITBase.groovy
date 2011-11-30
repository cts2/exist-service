package edu.mayo.cts2.framework.plugin.service.exist.integration;

import static org.junit.Assert.*

import org.apache.commons.lang.StringUtils
import org.junit.Test

abstract class BaseQueryServiceTestITBase extends BaseServiceTestITBase {
	
	@Test
	void TestQuery(){
		def resources = getResources();
		resources.each {
			this.createResource(getCreateUrl(), it)
		}
		
		def directory = read(getQueryUrl(), getResourceClass());
		
		assertEquals resources.size(), directory.getEntryCount()
	}

	@Test
	void TestQueryAfterCommit(){
		def resourceUris = []
		
		def resources = getResources();
		resources.each {
			resourceUris.add(this.createResource(getCreateUrl(), it))
		}
		
		this.commitChangeSet();
		
		def directory = readCurrent(getQueryUrl(), getResourceClass());
		
		assertEquals resources.size(), directory.getEntryCount()
		
		this.createChangeSet()
		
		resourceUris.each {
			def url = StringUtils.substringBefore(it.toString(),"?")
			deleteResource(url)
		}
		
		commitChangeSet()
	}
	
	abstract getCreateUrl()
	
	abstract getResourceClass()
	
	abstract getQueryUrl()
	
	abstract List getResources()
}
