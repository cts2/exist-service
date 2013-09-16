package edu.mayo.cts2.framework.plugin.service.exist.profile;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xmldb.api.base.Resource;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistResourceDao;

public class CountingIncrementer implements Incrementer {
	
	private static final String COUNTER_NAME = "counter";
	private static final HashMap<String, AtomicInteger> indexes = new HashMap<String, AtomicInteger>();
	private static final Object syncLock = new Object();
	private static volatile DbUpdater updateThread;
	private static Exception dbUpdateException = null;
	private static Log log = LogFactory.getLog(CountingIncrementer.class);
	private static LinkedBlockingQueue<Update> itemsToWrite = new LinkedBlockingQueue<Update>();

	private final ExistResourceDao existDao_;
	private final String collectionPath_;
	
	private CountingIncrementer()
	{
		//only for internal use
		existDao_ = null;
		collectionPath_ = null;
	}
	
	public CountingIncrementer(ExistResourceDao existDao, String collectionPath){
		this.existDao_ = existDao;
		this.collectionPath_ = collectionPath;
		if (updateThread == null)
		{
			synchronized (syncLock)
			{
				if (updateThread == null)
				{
					//start up the write thread
					updateThread = new DbUpdater();
					Thread r = new Thread(updateThread, "CountingIncrementerDBStoreThread");
					r.setDaemon(true);
					r.start();
				}
			}
		}
	}
	
	@Override
	public String getNext() 
	{
		AtomicInteger ai = indexes.get(collectionPath_);
		if (ai == null)
		{
			synchronized (syncLock)
			{
				ai = indexes.get(collectionPath_);
				if (ai == null)
				{
					try
					{
						Resource resource = this.existDao_.getBinaryResource(this.collectionPath_, COUNTER_NAME);
						if (resource == null)
						{
							ai = new AtomicInteger(0);
						}
						else
						{
							ai = new AtomicInteger((int) toInt((byte[]) resource.getContent()));
						}
						indexes.put(collectionPath_, ai);
					}
					catch (Exception e)
					{
						throw new IllegalStateException(e);
					}
				}
			}
		}
		
		
		if (dbUpdateException != null)
		{
			//Clear it, in-case this was a transient error... maybe the next store request will work. 
			Exception e = dbUpdateException;
			dbUpdateException = null;
			throw new IllegalStateException("Had a failure trying to store the latest used ID to the DB.  Value currently in the DB is wrong.  " +
					"Manual intervention is required to correct the state of the DB!", e);
		}
		
		String next = Integer.toString(ai.incrementAndGet());
		
		//We can't just write to the DB here, because there is no guarantee we wouldn't muddle the value if multiple threads are calling this method
		//Don't want to synchronize the entire method, and don't want to wait for the DB to store the value... we will assume it stores properly.
		//Tell the background thread that the value has changed, and it should wake up and store it.
		itemsToWrite.add(new Update(collectionPath_, existDao_));
		return next;
	}
	
	protected static long toInt( byte[] bytes ) {
		return ByteBuffer.wrap(bytes).getInt();
	}
	
	protected static byte[] toBytes( int intValue ) {
		byte[] bytes = ByteBuffer.allocate(4).putInt(intValue).array();
		
		return bytes;
	}
	
	/**
	 * Blocks until all pending writes have been flushed.
	 * 
	 * This should be called before doing a collection remove, because existDB has a bug that can lead to a deadlock.
	 * https://sourceforge.net/p/exist/bugs/830/
	 */
	public static boolean waitForPendingWrites()
	{
		if (updateThread == null)
		{
			return true;
		}
		AtomicInteger blocked = new AtomicInteger(1);
		synchronized (blocked)
		{
			itemsToWrite.add(new CountingIncrementer().new Update(blocked));
			try
			{
				if (blocked.get() > 0)
				{
					blocked.wait();
				}
				return blocked.get() == 0;
			}
			catch (InterruptedException e)
			{
				return false;
			}
		}
	}
	
	private class Update
	{
		protected ExistResourceDao updateExistDao_;
		protected String updateCollectionPath_;
		private AtomicInteger syncBlock;
		
		protected Update(AtomicInteger count)
		{
			//Special handling to check for an empty list...
			this.syncBlock = count;
		}
		
		protected Update(String collectionPath, ExistResourceDao existDao)
		{
			updateCollectionPath_ = collectionPath;
			updateExistDao_ = existDao;
		}
	}

	private class DbUpdater implements Runnable
	{
		@Override
		public void run()
		{
			log.info("CountingIncrementer thread started");
			try
			{
				while (true)
				{
					try
					{
						Update item = itemsToWrite.take();
						if (item.syncBlock != null)
						{
							synchronized (item.syncBlock)
							{
								item.syncBlock.decrementAndGet();
								item.syncBlock.notify();
								continue;
							}
						}
						int value = indexes.get(item.updateCollectionPath_).get();
						item.updateExistDao_.storeBinaryResource(item.updateCollectionPath_, COUNTER_NAME, toBytes(value));
					}
					catch (Exception e)
					{
						// This is bad... we have already handed back a new value... but we couldn't store it to the DB.
						//Kick the exception up, so it fires on the next call to getNext();
						dbUpdateException = e;
					}
				}
			}
			catch (Exception e)
			{
				dbUpdateException = new Exception("CountingIncrementer thread died!", e);
			}
		}
	}
}
