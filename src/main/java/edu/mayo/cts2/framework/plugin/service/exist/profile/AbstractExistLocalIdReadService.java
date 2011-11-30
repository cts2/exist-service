package edu.mayo.cts2.framework.plugin.service.exist.profile;

import org.springframework.stereotype.Component;
import org.xmldb.api.base.XMLDBException;

import edu.mayo.cts2.framework.model.core.ChangeableElementGroup;
import edu.mayo.cts2.framework.model.extension.LocalIdResource;
import edu.mayo.cts2.framework.model.service.core.BaseReadService;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;

@Component
public abstract class AbstractExistLocalIdReadService<C,R extends LocalIdResource<C>,I,T extends BaseReadService> 
	extends AbstractExistReadService<R,I,T> {

	protected boolean isDeleted(R resource){
		return super.isDeleted(this.getChangeableElementGroup(resource));
	}
	
	protected R doUnmarshall(org.xmldb.api.base.Resource resource){
		
		@SuppressWarnings("unchecked")
		C cts2Resource = (C) super.doUnmarshall(resource);
		
		String name;
		try {
			name = ExistServiceUtils.getNameFromResourceName(resource.getId());
		} catch (XMLDBException e) {
			throw new IllegalStateException(e);
		}
		
		return this.createLocalIdResource(name, cts2Resource);
	}
	
	protected abstract ChangeableElementGroup getChangeableElementGroup(R resource);
	
	protected abstract R createLocalIdResource(String id, C resource);

}
