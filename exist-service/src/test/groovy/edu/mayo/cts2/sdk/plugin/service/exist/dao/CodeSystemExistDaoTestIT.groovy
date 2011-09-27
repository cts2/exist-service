package edu.mayo.cts2.sdk.plugin.service.exist.dao;

import static org.junit.Assert.*

import javax.annotation.Resource

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import edu.mayo.cts2.framework.plugin.service.exist.dao.CodeSystemExistDao;
import edu.mayo.cts2.sdk.model.codesystem.CodeSystemCatalogEntry

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value="classpath:/exist-test-context.xml")
class CodeSystemExistDaoTestIT {

	@Resource
	CodeSystemExistDao codeSystemExistDao
	
	@Test
	void testStoreCodeSystems(){
		assertNotNull codeSystemExistDao

		for(int i=0;i<100;i++){
			CodeSystemCatalogEntry entry = new CodeSystemCatalogEntry()
			def name = Integer.toString(i)
			
			entry.setCodeSystemName(name)
			entry.setAbout(name);
			
			codeSystemExistDao.storeResource("", entry)
		}
		
		def summaries = codeSystemExistDao.getResourceSummaries("", "", 0, 1000)
		
		assertEquals 100, summaries.entries.size
	}
	
	
}
