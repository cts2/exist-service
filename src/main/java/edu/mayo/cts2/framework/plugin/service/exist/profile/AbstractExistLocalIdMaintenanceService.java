package edu.mayo.cts2.framework.plugin.service.exist.profile;

import org.springframework.stereotype.Component;
import org.xmldb.api.base.XMLDBException;

import edu.mayo.cts2.framework.model.core.IsChangeable;
import edu.mayo.cts2.framework.model.extension.ChangeableLocalIdResource;
import edu.mayo.cts2.framework.model.service.core.BaseMaintenanceService;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;
import edu.mayo.cts2.framework.service.profile.LocalIdMaintenanceService;

@Component
public abstract class AbstractExistLocalIdMaintenanceService<
	C extends IsChangeable,
	R extends ChangeableLocalIdResource<C>,
	I,
	T extends BaseMaintenanceService>
	extends AbstractExistMaintenanceService<R,C,I,T> implements LocalIdMaintenanceService<R,C,I> {

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
	
    protected C processResourceBeforeStore(R resource){
    	return resource.getResource();
    }
	
	protected abstract LocalIdResourceInfo<R,I> getResourceInfo();
	
    protected String getPathFromResource(R resource){
    	return this.getResourceInfo().createPathFromResource(resource);
    }

	@Override
	protected R resourceToIndentifiedResource(
			C resource) {
		R localIdResource = this.createLocalIdResource(null, resource);
		
		Incrementer incrementer = this.buildIncrementer(localIdResource);
		
		localIdResource.setLocalID(incrementer.getNext());
		
		return localIdResource;
	}

	
	protected abstract R createLocalIdResource(String id, C resource);

	protected String getExistStorageNameForResource(R resource){
		return resource.getLocalID();
	}
	
	protected Incrementer buildIncrementer(R resource){
		return new CountingIncrementer(this.getExistResourceDao(), 
				this.createPath(
						this.getResourceInfo().getResourceBasePath(), 
						this.getPathFromResource(resource)));
	}
}
