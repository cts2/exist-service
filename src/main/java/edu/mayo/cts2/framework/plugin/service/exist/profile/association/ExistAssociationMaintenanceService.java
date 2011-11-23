package edu.mayo.cts2.framework.plugin.service.exist.profile.association;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.association.Association;
import edu.mayo.cts2.framework.model.core.StatementTarget;
import edu.mayo.cts2.framework.model.updates.ChangeableResourceChoice;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistMaintenanceService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.CountingIncrementer;
import edu.mayo.cts2.framework.plugin.service.exist.profile.Incrementer;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
import edu.mayo.cts2.framework.service.profile.association.AssociationMaintenanceService;
import edu.mayo.cts2.framework.service.profile.association.name.AssociationReadId;

@Component
public class ExistAssociationMaintenanceService 
	extends AbstractExistMaintenanceService<Association,AssociationReadId,edu.mayo.cts2.framework.model.service.association.AssociationMaintenanceService>
	implements AssociationMaintenanceService {

	@Resource
	private AssociationResourceInfo associationResourceInfo;

	@Override
	protected ResourceInfo<Association, AssociationReadId> getResourceInfo() {
		return this.associationResourceInfo;
	}

	@Override
	public Association createResource(Association resource) {
		Incrementer incrementer = this.buildIncrementer(resource);
		
		String externalId = this.getExternalIdentifier(resource);
		
		if(externalId == null){
			externalId = incrementer.getNext();
		}
		
		resource.setLocalID(externalId);
		
		return super.createResource(resource);
	}
	
	protected Incrementer buildIncrementer(Association resource){
		return new CountingIncrementer(this.getExistResourceDao(), 
				this.createPath(
						this.getResourceInfo().getResourceBasePath(), 
						this.getResourceInfo().createPathFromResource(resource)));
	}
	
	protected String getExternalIdentifier(Association association){
		String externalId = null;
		for(StatementTarget target : association.getTarget()){
			String id = target.getExternalIdentifier();
			if(StringUtils.isNotBlank(id)){
				if(externalId == null){
					externalId = id;
				} else {
					throw new IllegalStateException("Multiple External Ids on Association.");
				}
			}
		}
		
		return externalId;
	}

	@Override
	protected void addResourceToChangeableResourceChoice(
			ChangeableResourceChoice choice, Association resource) {
		choice.setAssociation(resource);
	}
}
