package edu.mayo.cts2.framework.plugin.service.exist.profile;

import static org.junit.Assert.*

import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistDaoImpl

class CountingIncrementerTestIT extends BaseServiceDbCleaningBase {
	
	@Autowired
	ExistDaoImpl dao;
	
	@Test
	void TestFirstIncrement(){
		def i = new CountingIncrementer(dao, "testPath1")
		
		assertEquals "1", i.getNext();
	}
	
	@Test
	void TestMultipleIncrement(){
		
		
		def i = new CountingIncrementer(dao, "testPath2")
		
		i.getNext();
		
		assertEquals "2", i.getNext();
	}
	
	@Test
	void TestLargeIncrement(){
		
		def inc = new CountingIncrementer(dao, "testPath3")
		
		for(int i=0;i<100;i++){
			inc.getNext()
		}

		assertEquals "101", inc.getNext();
	}
}
