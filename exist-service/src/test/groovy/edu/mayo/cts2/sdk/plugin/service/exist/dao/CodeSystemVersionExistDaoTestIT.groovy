package edu.mayo.cts2.framework.plugin.service.exist.dao;

import static org.junit.Assert.*

import javax.annotation.Resource

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import edu.mayo.cts2.framework.plugin.service.exist.dao.CodeSystemVersionExistDao;
import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry
import edu.mayo.cts2.framework.model.core.CodeSystemReference
import edu.mayo.cts2.framework.model.core.SourceAndNotation

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value="classpath:/exist-test-context.xml")
class CodeSystemVersionExistDaoTestIT {

	@Resource
	CodeSystemVersionExistDao codeSystemVersionExistDao
	
	@Test
	void testNotNull(){
		assertNotNull codeSystemVersionExistDao
	
		for(int i=0;i<100;i++){
			def entry = new CodeSystemVersionCatalogEntry(documentURI:"docuri")
			def name = Integer.toString(i)
			
			entry.setSourceAndNotation(new SourceAndNotation());
			entry.setVersionOf(new CodeSystemReference());
			
			entry.setCodeSystemVersionName(name)
			entry.setAbout(name);
			
			def csName = Integer.toString(i % 10)
			
			codeSystemVersionExistDao.storeResource(csName, entry)
		}
	
		
		def summaries = codeSystemVersionExistDao.getResourceSummaries("", "", 0, 1000)
		
		assertEquals 100, summaries.entries.size
	}
	
	
}
