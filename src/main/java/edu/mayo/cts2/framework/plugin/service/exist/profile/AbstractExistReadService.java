package edu.mayo.cts2.framework.plugin.service.exist.profile;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmldb.api.base.Resource;

import edu.mayo.cts2.framework.core.url.UrlConstructor;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.ChangeDescription;
import edu.mayo.cts2.framework.model.core.ChangeableElementGroup;
import edu.mayo.cts2.framework.model.core.types.ChangeType;
import edu.mayo.cts2.framework.model.service.core.BaseReadService;
import edu.mayo.cts2.framework.model.service.core.ReadContext;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.service.profile.ReadService;

public abstract class AbstractExistReadService<R,I,T extends BaseReadService> extends AbstractExistResourceReadingService<R,I,T> 
	implements ReadService<R,I> {

	@Autowired
	private ResourceUnmarshaller resourceUnmarshaller;
	
	@Autowired
	private UrlConstructor urlConstructor;
	
	private interface GetResourceCallback {
		Resource getResource();
		Resource getResource(String changeSetUri);
	}

	@Override
	public R read(final I resourceIdentifier, final ResolvedReadContext readContext) {
		
		return this.doRead(new GetResourceCallback(){

			@Override
			public Resource getResource() {
				return AbstractExistReadService.this.getResource(resourceIdentifier);
			}

			@Override
			public Resource getResource(String changeSetUri) {
				return AbstractExistReadService.this.getResource(resourceIdentifier, changeSetUri);
			}
			
		}, readContext);
		
	}

	@SuppressWarnings("unchecked")
	protected R doRead(GetResourceCallback callback, ResolvedReadContext readContext) {
		
		String changeSetUri = null;
		if(readContext != null){
			changeSetUri = readContext.getChangeSetContextUri();
		}
		
		Resource resource = null;
		if(StringUtils.isNotBlank(changeSetUri)){
			resource = callback.getResource(changeSetUri);
		} 
		
		if(resource == null){
			resource = callback.getResource();
		}
		
		if(resource == null){
			return null;
		}
		
		R cts2Resource = (R) this.doUnmarshall(resource);
		
		if(cts2Resource != null && !this.isDeleted(cts2Resource)){
			return cts2Resource;
		} else {
			return null;
		}
	}
	
	protected Object doUnmarshall(Resource resource){
		return this.resourceUnmarshaller.unmarshallResource(resource);
	}
	
	public R readByXpath(final String resourcePath, final String xpath, ResolvedReadContext readContext) {
		
		return this.doRead(new GetResourceCallback(){

			@Override
			public Resource getResource() {
				return AbstractExistReadService.this.getResourceByXpath(resourcePath, xpath);
			}

			@Override
			public Resource getResource(String changeSetUri) {
				return AbstractExistReadService.this.getResourceByXpath(resourcePath, xpath, changeSetUri);
			}
			
		}, readContext);
	}
	
	protected boolean isDeleted(R resource){
		ChangeableElementGroup group = 
				ModelUtils.getChangeableElementGroup(resource);
		
		return this.isDeleted(group);
	}
	
	protected boolean isDeleted(ChangeableElementGroup group){
		if(group != null){
			ChangeDescription description = 
					group.getChangeDescription();
			if(description != null){
				return description.getChangeType().equals(ChangeType.DELETE);
			}
		}
		
		return false;
	}
	
	@Override
	public boolean exists(I identifier, ReadContext readContext) {
		throw new UnsupportedOperationException();
	}

	protected UrlConstructor getUrlConstructor() {
		return urlConstructor;
	}

}
