package edu.mayo.cts2.framework.plugin.service.exist.dao;

import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceSet;

public interface ExistResourceDao {
	
	public ResourceSet query(
			String collectionPath, 
			String xpath,
			int start, 
			int max);
	
	public void storeResource(String path, String resourceName, Object resource);
	
	public void deleteResource(String path, String name);
	
	public Resource getResource(String path, String name);
	
	public Resource getResourceByXpath(String collectionPath, String xpathQuery);

	public void removeCollection(String changeSetDir);

}
