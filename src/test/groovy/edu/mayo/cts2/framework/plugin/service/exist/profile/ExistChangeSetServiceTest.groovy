package edu.mayo.cts2.framework.plugin.service.exist.profile;

import static org.junit.Assert.*

import javax.annotation.Resource

import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@RunWith(SpringJUnit4ClassRunner)
@ContextConfiguration(
locations = ["/exist-test-context.xml"])
class ExistChangeSetServiceTest {

	@Resource
	ExistChangeSetServiceTest service
	
	@Test
	@Ignore
	void testGetChangeSet(){
		def changeSet = service.createChangeSet();
		
		assertNotNull changeSet;
	}
}
