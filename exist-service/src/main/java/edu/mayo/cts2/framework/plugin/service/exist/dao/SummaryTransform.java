package edu.mayo.cts2.framework.plugin.service.exist.dao;

import org.xmldb.api.base.Resource;


public interface SummaryTransform<S, R> {
	
	public S transform(R resource, Resource existResource);
	
	public S createSummary();

}
