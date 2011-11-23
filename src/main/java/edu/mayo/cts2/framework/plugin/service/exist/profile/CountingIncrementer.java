package edu.mayo.cts2.framework.plugin.service.exist.profile;

import java.nio.ByteBuffer;

import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;

import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistResourceDao;

public class CountingIncrementer implements Incrementer {
	
	private static String COUNTER_NAME = "counter";
	private static Integer START = 1;
	
	private ExistResourceDao existDao;
	private String collectionPath;

	public CountingIncrementer(ExistResourceDao existDao, String collectionPath){
		this.existDao = existDao;
		this.collectionPath = collectionPath;
	}
	
	@Override
	public String getNext() {
		Resource resource = 
				this.existDao.getBinaryResource(this.collectionPath, COUNTER_NAME);
		
		Integer next;

		if(resource == null){
			this.existDao.storeBinaryResource(
					this.collectionPath, 
					COUNTER_NAME, 
					toBytes(START));
			
			next = START;
		} else {
			int current;
			
			try {
				current = (int)toInt((byte[])resource.getContent());
			} catch (NumberFormatException e) {
				throw new IllegalStateException(e);
			} catch (XMLDBException e) {
				throw new IllegalStateException(e);
			}
			
			next = current+1;
			
			this.existDao.storeBinaryResource(
					this.collectionPath, 
					COUNTER_NAME, 
					toBytes(next));
		}
		
		return Integer.toString(next);
	}
	
	protected long toInt( byte[] bytes ) {
		return ByteBuffer.wrap(bytes).getInt();
	}
	
	protected byte[] toBytes( int intValue ) {
		byte[] bytes = ByteBuffer.allocate(4).putInt(intValue).array();
		
		return bytes;
	}

}
