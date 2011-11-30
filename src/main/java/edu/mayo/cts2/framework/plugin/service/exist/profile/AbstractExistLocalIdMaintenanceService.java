package edu.mayo.cts2.framework.plugin.service.exist.profile;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.xmldb.api.base.XMLDBException;

import edu.mayo.cts2.framework.model.core.ChangeableElementGroup;
import edu.mayo.cts2.framework.model.extension.LocalIdResource;
import edu.mayo.cts2.framework.model.service.core.BaseMaintenanceService;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;

@Component
public abstract class AbstractExistLocalIdMaintenanceService<C,R extends LocalIdResource<C>,I,T extends BaseMaintenanceService>
	extends AbstractExistMaintenanceService<R,I,T> {

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

	protected R resourceFromStorageResult(StorageResult result){
		String localId = result.getName();
		
		@SuppressWarnings("unchecked")
		C resource = (C) result.getResource().getChoiceValue();
	
		return this.createLocalIdResource(localId, resource);
	}
	
	protected abstract R createLocalIdResource(String id, C resource);

	protected String doGetResourceNameToStore(R resource){
		String name;
		
		if(StringUtils.isBlank(resource.getLocalID())){
			Incrementer incrementer = this.buildIncrementer(resource);
			
			name = incrementer.getNext();
		} else {
			name = resource.getLocalID();
		}

		return name;
	}
	
	protected Incrementer buildIncrementer(R resource){
		return new CountingIncrementer(this.getExistResourceDao(), 
				this.createPath(
						this.getResourceInfo().getResourceBasePath(), 
						this.getResourceInfo().createPathFromResource(resource)));
	}
}
