package edu.mayo.cts2.framework.plugin.service.exist.dao;

import edu.mayo.cts2.framework.model.directory.DirectoryResult;

public interface ExistDao<S, R> {
	
	public DirectoryResult<S> getResourceSummaries(String path, String xpath,
			int start, int max);
	
	public void storeResource(String path, R entry);
	
	public R getResource(String path, String name);
	
	public R getResourceByUri(String path, String uri);

}
