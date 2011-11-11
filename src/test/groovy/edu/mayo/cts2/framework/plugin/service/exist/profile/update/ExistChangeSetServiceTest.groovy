package edu.mayo.cts2.framework.plugin.service.exist.profile.update;

import static org.junit.Assert.*

import javax.annotation.Resource

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import edu.mayo.cts2.framework.model.updates.ChangeSet


class ExistChangeSetServiceTest {

	ExistChangeSetService service = new ExistChangeSetService()
	
	@Test
	void testGetChangeSet(){
		def changeSet = service.doCreateNewChangeSet();
		
		assertNotNull changeSet;
		assertNotNull changeSet.getChangeSetURI();
		assertNotNull changeSet.getCreationDate();
		assertNull changeSet.getCloseDate();
	
	}
}
