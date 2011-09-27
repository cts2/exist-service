package edu.mayo.cts2.sdk.plugin.service.exist.profile

import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import edu.mayo.cts2.sdk.plugin.service.exist.dao.ExistManager

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value="classpath:/exist-test-context.xml")
class BaseServiceTestGroovy {

	@Autowired 
	ExistManager existManager
	
	@Before
	void cleanExist() {
		  existManager.getCollectionManagementService().removeCollection("/db");
	}
}
