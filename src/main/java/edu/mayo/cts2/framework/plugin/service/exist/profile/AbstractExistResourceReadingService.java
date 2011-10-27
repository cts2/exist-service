package edu.mayo.cts2.framework.plugin.service.exist.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.xmldb.api.base.Resource;

import edu.mayo.cts2.framework.model.service.core.BaseService;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistResourceDao;

public abstract class AbstractExistResourceReadingService<R,I,T extends BaseService > extends AbstractExistService<T> {

	@Autowired
	private ResourceUnmarshaller resourceUnmarshaller;
	
	@javax.annotation.Resource
	private ExistResourceDao existResourceDao;

	protected Resource getResource(I resourceIdentifier) {
		Resource resource;
		
		ResourceInfo<R,I> existResourceReader = getResourceInfo();
		
		String resourcePath = existResourceReader.createPath(resourceIdentifier);
		
		String wholePath = this.createPath(this.getResourceInfo().getResourceBasePath(), resourcePath);
		
		if(! existResourceReader.isReadByUri(resourceIdentifier)){
			resource = this.getExistResourceDao().getResource(
					wholePath,
					existResourceReader.getExistResourceName(resourceIdentifier));
		} else {
			String uri = existResourceReader.getResourceUri(resourceIdentifier);
			
			resource = this.getExistResourceDao().getResourceByXpath(
					wholePath, this.getResourceInfo().getResourceXpath() + "[" + getResourceInfo().getUriXpath() + " &= '" + uri + "']");
		}
		
		return resource;
	}

	protected ExistResourceDao getExistResourceDao() {
		return existResourceDao;
	}

	protected void setExistResourceDao(ExistResourceDao existResourceDao) {
		this.existResourceDao = existResourceDao;
	}

	protected ResourceUnmarshaller getResourceUnmarshaller() {
		return resourceUnmarshaller;
	}
	
	protected abstract ResourceInfo<R,I> getResourceInfo();

}
