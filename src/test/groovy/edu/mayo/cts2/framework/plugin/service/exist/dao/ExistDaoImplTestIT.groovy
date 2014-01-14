package edu.mayo.cts2.framework.plugin.service.exist.dao;

import static org.junit.Assert.*

import javax.annotation.Resource

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value="classpath:/exist-test-context.xml")
class ExistDaoImplTestIT {

	@Resource
	ExistDaoImpl existDaoImpl
	
	@Test
	void testQuery(){
		assertNotNull existDaoImpl

		for(int i=0;i<100;i++){
			CodeSystemCatalogEntry entry = new CodeSystemCatalogEntry()
			def name = Integer.toString(i)
			
			entry.setCodeSystemName(name)
			entry.setAbout(name);
			
			existDaoImpl.storeResource("testcollection", name, entry)
		}
		
		def summaries = existDaoImpl.query("testcollection", "/codesystem:CodeSystemCatalogEntry", 0, 1000)
		
		assertEquals 100, summaries.getSize()
	}

    @Test
    void testCount(){
        assertNotNull existDaoImpl

        for(int i=0;i<100;i++){
            CodeSystemCatalogEntry entry = new CodeSystemCatalogEntry()
            def name = Integer.toString(i)

            entry.setCodeSystemName(name)
            entry.setAbout(name);

            existDaoImpl.storeResource("testcollection", name, entry)
        }

        def count = existDaoImpl.count("testcollection", "/codesystem:CodeSystemCatalogEntry")

        assertEquals 100, count
    }

    @Test
    void testCountNone(){
        assertNotNull existDaoImpl

        for(int i=0;i<100;i++){
            CodeSystemCatalogEntry entry = new CodeSystemCatalogEntry()
            def name = Integer.toString(i)

            entry.setCodeSystemName(name)
            entry.setAbout(name);

            existDaoImpl.storeResource("testcollection", name, entry)
        }

        def count = existDaoImpl.count("testcollection", "/entity:EntityDescription")

        assertEquals 0, count
    }

}
