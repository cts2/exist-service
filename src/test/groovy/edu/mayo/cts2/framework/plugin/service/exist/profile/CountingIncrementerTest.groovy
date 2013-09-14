package edu.mayo.cts2.framework.plugin.service.exist.profile;

import java.nio.ByteBuffer;
import org.easymock.EasyMock
import static org.junit.Assert.*
import org.junit.Test
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistResourceDao;

class CountingIncrementerTest {
	
	@Test
	void TestFirstIncrement(){

		def dao = EasyMock.createMock(ExistResourceDao)
		EasyMock.expect(dao.getBinaryResource('testPath', 'counter')).andReturn(null).once()

		def i = new CountingIncrementer(dao, "testPath")
		
		byte[] bytes = i.toBytes(1)
		
		EasyMock.expect(dao.storeBinaryResource(EasyMock.eq('testPath'), EasyMock.eq('counter'), EasyMock.aryEq(bytes)))
		
		EasyMock.replay(dao)
		
		assertEquals "1", i.getNext();
	}
	
	@Test
	void TestMultipleIncrement(){
		def resource = EasyMock.createMock(org.xmldb.api.base.Resource)
		
		def dao = EasyMock.createMock(ExistResourceDao)
		
		//Use a different path, because the CountingIncrementer has a static cache that was already incremented in the test above...
		def i = new CountingIncrementer(dao, "testPath2")
		
		byte[] bytes1 = i.toBytes(1)
		byte[] bytes2 = i.toBytes(2)

		EasyMock.expect(dao.getBinaryResource('testPath2', 'counter')).andReturn(null).once()
		EasyMock.expect(dao.storeBinaryResource(EasyMock.eq('testPath2'), EasyMock.eq('counter'), EasyMock.aryEq(bytes1)))
		EasyMock.expect(dao.storeBinaryResource(EasyMock.eq('testPath2'), EasyMock.eq('counter'), EasyMock.aryEq(bytes2)))
		EasyMock.expect(dao.getBinaryResource('testPath2', 'counter')).andReturn(resource).once()
		EasyMock.expect(resource.getContent()).andReturn(bytes2)
		EasyMock.expect(dao.storeBinaryResource(EasyMock.eq('testPath2'), EasyMock.eq('counter'), EasyMock.aryEq(i.toBytes(3))))
		
		EasyMock.replay(dao, resource)

		i.getNext();

		CountingIncrementer.waitForPendingWrites();
		
		assertEquals "2", i.getNext();
		
		//This new CountingIncrementer will have to read the value back from the dao
		i = new CountingIncrementer(dao, "testPath2")
		CountingIncrementer.waitForPendingWrites();
		assertEquals "3", i.getNext();
	}
	
	@Test
	void TestToInt1(){
		def i = new CountingIncrementer(null,null)
		
		int integer = 1;
		
		byte[] bytes = i.toBytes(integer)
		
		assertEquals integer, i.toInt(bytes)
	}
	
	@Test
	void TestToInt2(){
		def i = new CountingIncrementer(null,null)
		
		int integer = 2;
		
		byte[] bytes = i.toBytes(integer)
		
		assertEquals integer, i.toInt(bytes)
	}
	
	@Test
	void TestToInt113(){
		def i = new CountingIncrementer(null,null)
		
		int integer = 113;
		
		byte[] bytes = i.toBytes(integer)
		
		assertEquals integer, i.toInt(bytes)
	}
	
	@Test
	void TestToInt1234(){
		def i = new CountingIncrementer(null,null)
		
		int integer = 12;
		
		byte[] bytes = i.toBytes(integer)

		assertEquals integer, i.toInt(bytes)
	}
	
	@Test
	void TestToInt202324(){
		def i = new CountingIncrementer(null,null)
		
		int integer = 202324;
		
		byte[] bytes = i.toBytes(integer)
		
		assertEquals integer, i.toInt(bytes)
	}
}
