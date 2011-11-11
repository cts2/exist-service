package edu.mayo.cts2.framework.plugin.service.exist.profile;

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class AbstractExistQueryServiceTest {

	def builder
	
	@Before
	void setup(){
		builder = new AbstractExistQueryService.XqueryExceptClauseBuilder("@someXpath")
	}
	
	@Test
	void TestOneClause(){
		builder.except("name1")
		
		assertEquals "[ @someXpath = 'name1']", builder.build() 
	}
	
	@Test
	void TestOneMultipleClauses(){
		builder.except("name1")
		builder.except("name2")
		builder.except("name3")
		
		assertEquals "[ @someXpath = 'name1' or @someXpath = 'name2' or @someXpath = 'name3']", builder.build()
	}
	
	@Test
	void TestNoClauses(){
		
		assertEquals "", builder.build()
	}
	
}
