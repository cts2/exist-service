package edu.mayo.cts2.framework.plugin.service.exist.profile;

import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.ChangeDescription;
import edu.mayo.cts2.framework.model.core.ChangeableElementGroup;
import edu.mayo.cts2.framework.model.core.IsChangeable;
import edu.mayo.cts2.framework.model.core.VersionTagReference;
import edu.mayo.cts2.framework.model.core.types.ChangeType;
import edu.mayo.cts2.framework.model.service.core.BaseReadService;
import edu.mayo.cts2.framework.service.profile.ReadService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmldb.api.base.Resource;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractExistReadService<
	C extends IsChangeable,
	R extends IsChangeable,
	I,
	T extends BaseReadService> 
	extends AbstractExistResourceReadingService<R,I,T> 
	implements ReadService<R,I> {

	@Autowired
	private ResourceUnmarshaller resourceUnmarshaller;

	protected interface GetResourceCallback {
		Resource getResource();
		Resource getResource(String changeSetUri);
	}

	public List<VersionTagReference> getSupportedTags(){
		return Arrays.asList(new VersionTagReference("CURRENT"));
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
		ChangeableElementGroup group = resource.getChangeableElementGroup();
		
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
	public boolean exists(I identifier, ResolvedReadContext readContext) {
		return this.read(identifier, readContext) != null;
	}

}
