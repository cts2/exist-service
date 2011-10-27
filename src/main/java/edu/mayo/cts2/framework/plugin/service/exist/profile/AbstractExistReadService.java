package edu.mayo.cts2.framework.plugin.service.exist.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.xmldb.api.base.Resource;

import edu.mayo.cts2.framework.core.url.UrlConstructor;
import edu.mayo.cts2.framework.model.service.core.BaseReadService;
import edu.mayo.cts2.framework.model.service.core.ReadContext;
import edu.mayo.cts2.framework.service.profile.ReadService;

public abstract class AbstractExistReadService<R,I,T extends BaseReadService > extends AbstractExistResourceReadingService<R,I,T> 
	implements ReadService<R,I> {

	@Autowired
	private ResourceUnmarshaller resourceUnmarshaller;
	
	@Autowired
	private UrlConstructor urlConstructor;
	
	@SuppressWarnings("unchecked")
	@Override
	public R read(I resourceIdentifier, ReadContext readContext) {
		Resource resource = this.getResource(resourceIdentifier);
		
		return (R) this.resourceUnmarshaller.unmarshallResource(resource);
	}
	
	@Override
	public boolean exists(I identifier, ReadContext readContext) {
		throw new UnsupportedOperationException();
	}

	protected UrlConstructor getUrlConstructor() {
		return urlConstructor;
	}

}
