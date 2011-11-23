/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package edu.mayo.cts2.framework.plugin.service.exist.profile;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.xmldb.api.base.Resource;

import edu.mayo.cts2.framework.model.core.ChangeDescription;
import edu.mayo.cts2.framework.model.core.ChangeableElementGroup;
import edu.mayo.cts2.framework.model.core.types.ChangeCommitted;
import edu.mayo.cts2.framework.model.core.types.ChangeType;
import edu.mayo.cts2.framework.model.service.core.BaseMaintenanceService;
import edu.mayo.cts2.framework.model.updates.ChangeableResourceChoice;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;
import edu.mayo.cts2.framework.service.profile.UpdateChangeableMetadataRequest;

/**
 * The Class AbstractService.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractExistMaintenanceService<R,I,T extends BaseMaintenanceService> 
	extends AbstractExistResourceReadingService<R,I,T> implements edu.mayo.cts2.framework.service.profile.BaseMaintenanceService<R,I> {

	@javax.annotation.Resource
	private StateChangeCallback stateChangeCallback;
	
	@Override
	public void updateChangeableMetadata(I identifier,
			UpdateChangeableMetadataRequest request) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteResource(I identifier, String changeSetUri) {
		Resource resource = this.getResource(identifier);
		
		@SuppressWarnings("unchecked")
		R changeable = (R) this.getResourceUnmarshaller().unmarshallResource(resource);
		
		ChangeableElementGroup group = ModelUtils.
				getChangeableElementGroup(changeable);
		
		ChangeDescription changeDescription = new ChangeDescription();
		
		changeDescription.setChangeDate(new Date());
		changeDescription.setChangeType(ChangeType.DELETE);
		changeDescription.setCommitted(ChangeCommitted.PENDING);
		changeDescription.setContainingChangeSet(changeSetUri);
		
		group.setChangeDescription(changeDescription);

		ChangeableResourceChoice choice = this.doStoreResource(changeable);
		
		this.stateChangeCallback.resourceDeleted(choice, changeSetUri);
	}

	@Override
	public void updateResource(R resource) {
		this.createResource(resource);
	}

	@Override
	public R createResource(R resource) {
	
		ChangeableResourceChoice choice = this.doStoreResource(resource);
		
		this.stateChangeCallback.resourceAdded(choice);
		
		return resource;
	}
	
	protected ChangeableResourceChoice doStoreResource(R resource){
		String path = 
				this.getResourceInfo().createPathFromResource(resource);

		String name = this.getResourceInfo().getExistResourceNameFromResource(resource);

		ChangeableResourceChoice choice = new ChangeableResourceChoice();
		
		this.addResourceToChangeableResourceChoice(choice, resource);
		
		ChangeableElementGroup group = ModelUtils.getChangeableElementGroup(choice);
		
		String changeSetUri = group.getChangeDescription().getContainingChangeSet();
		
		String changeSetDir = null;
		if(StringUtils.isNotBlank(changeSetUri)){
			changeSetDir = ExistServiceUtils.getTempChangeSetContentDirName(changeSetUri);
		}
		
		String wholePath = this.createPath(changeSetDir, this.getResourceInfo().getResourceBasePath(), path);
	
		this.getExistResourceDao().storeResource(wholePath, name, resource);
		
		return choice;
	}

	protected abstract void addResourceToChangeableResourceChoice(ChangeableResourceChoice choice, R resource);

}
