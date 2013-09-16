package edu.mayo.cts2.framework.plugin.service.exist.profile;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmldb.api.base.Resource;

import edu.mayo.cts2.framework.core.url.UrlConstructor;
import edu.mayo.cts2.framework.model.service.core.BaseService;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistResourceDao;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;

public abstract class AbstractExistResourceReadingService<
	R,
	I,
	T extends BaseService> 
	extends AbstractExistService implements InitializingBean{

	@Autowired
	private ResourceUnmarshaller resourceUnmarshaller;
	
	@javax.annotation.Resource
	private ExistResourceDao existResourceDao;
	
	@javax.annotation.Resource
	private edu.mayo.cts2.framework.core.plugin.PluginConfigManager pluginConfigManager;
	
	private UrlConstructor urlConstructor;

	@Override
	public void afterPropertiesSet() throws Exception {
		this.urlConstructor = new UrlConstructor(this.pluginConfigManager.getServerContext());
	}

	protected Resource getResource(I resourceIdentifier) {
		return this.getResource(resourceIdentifier, null);
	}
	
	protected Resource getResource(I resourceIdentifier, String changeSetUri) {
		Resource resource;
		
		ResourceInfo<I> existResourceReader = getResourceInfo();
			
		String changeSetDir = null;
		if(StringUtils.isNotBlank(changeSetUri)){
			changeSetDir = ExistServiceUtils.getTempChangeSetContentDirName(changeSetUri);
		}
			
		if(! existResourceReader.isReadByUri(resourceIdentifier)){
			String resourcePath = existResourceReader.createPath(resourceIdentifier);
			
			String wholePath = this.createPath(changeSetDir, this.getResourceInfo().getResourceBasePath(), resourcePath);
			
			resource = this.getExistResourceDao().getResource(
					wholePath,
					existResourceReader.getExistResourceName(resourceIdentifier));
		} else {
			String uri = existResourceReader.getResourceUri(resourceIdentifier);
			
			//lookup by designated uri or alternateId
			resource = this.getExistResourceDao().getResourceByXpath(
					this.createPath(changeSetDir, this.getResourceInfo().getResourceBasePath(), this.getExtraPathForUriLookup(resourceIdentifier) ), 
					this.getResourceInfo().getResourceXpath() + 
					"[" + getResourceInfo().getUriXpath() + " = '" + uri + "' or core:alternateID = '" + uri + "']");
		}
		
		return resource;
	}
	
	protected String getExtraPathForUriLookup(I resourceIdentifier){
		return "";
	}
	
	protected Resource getResourceByXpath(String path, String xpath) {
		return this.getResourceByXpath(path, xpath, null);
	}
	
	protected Resource getResourceByXpath(String path, String xpath, String changeSetUri) {
		
		String changeSetDir = null;
		if(StringUtils.isNotBlank(changeSetUri)){
			changeSetDir = ExistServiceUtils.getTempChangeSetContentDirName(changeSetUri);
		}

		Resource resource = this.getExistResourceDao().getResourceByXpath(
			this.createPath(changeSetDir, this.getResourceInfo().getResourceBasePath(), path), 
				this.getResourceInfo().getResourceXpath() + xpath);
		
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
	
	protected abstract ResourceInfo<I> getResourceInfo();

	protected UrlConstructor getUrlConstructor() {
		return urlConstructor;
	}
}
