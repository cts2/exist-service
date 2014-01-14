package edu.mayo.cts2.framework.plugin.service.exist.dao;

import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceSet;

public interface ExistResourceDao {
	
	public ResourceSet query(
			String collectionPath, 
			String xpath,
			int start, 
			int max);

    public int count(String collectionPath, String xpathQuery);
	
	public void storeResource(String path, String resourceName, Object resource);
	
	public void storeBinaryResource(String path, String resourceName, Object resource);
	
	public void deleteResource(String path, String name);
	
	public Resource getResource(String path, String name);
	
	public Resource getBinaryResource(String path, String name);
	
	public Resource getResourceByXpath(String collectionPath, String xpathQuery);

	public void removeCollection(String changeSetDir);
	
	/**
	 * @return The number of nodes modified
	 */
	public long update(String collectionPath, String commands);
	
	/**
	 * @return The number of nodes modified
	 */
	public long updateResource(String collectionPath, String resourceId, String commands);

}
