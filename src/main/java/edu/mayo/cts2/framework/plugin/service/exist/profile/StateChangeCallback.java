package edu.mayo.cts2.framework.plugin.service.exist.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmldb.api.base.Resource;

import edu.mayo.cts2.framework.model.core.ChangeDescription;
import edu.mayo.cts2.framework.model.updates.ChangeSet;
import edu.mayo.cts2.framework.model.updates.ChangeableResource;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistResourceDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.update.ChangeSetResourceInfo;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;

@Component
public class StateChangeCallback {

	@Autowired
	private ExistResourceDao existResourceDao;
	
	@Autowired
	private ResourceUnmarshaller resourceUnmarshaller;
	
	@javax.annotation.Resource
	private ChangeSetResourceInfo changeSetResourceInfo;
	
	public void resourceAdded(ChangeableResource changeable) {
		ChangeDescription changeDescription = 
				changeable.getChangeableElementGroup().getChangeDescription();
		
		String changeSetUri = changeDescription.
					getContainingChangeSet();
		
		this.addToChangeSet(changeSetUri, changeable);
	}
	
	public void resourceUpdated(ChangeableResource changeable) {
		ChangeDescription changeDescription = 
				changeable.getChangeableElementGroup().getChangeDescription();
		
		String changeSetUri = changeDescription.
					getContainingChangeSet();
		
		this.addToChangeSet(changeSetUri, changeable);
	}
	
	public void resourceDeleted(ChangeableResource changeable, String changeSetUri) {

		this.addToChangeSet(changeSetUri, changeable);
	}
	
	protected void addToChangeSet(String changeSetUri, ChangeableResource changeable){
		String name = ExistServiceUtils.uriToExistName(changeSetUri);
		
		Resource resource = 
				this.existResourceDao.getResource(changeSetResourceInfo.getResourceBasePath(), name);
		
		ChangeSet changeSet = (ChangeSet) this.resourceUnmarshaller.unmarshallResource(resource);
		
		long count = changeSet.getMemberCount();

		count++;
		
		changeable.setEntryOrder(count);
		changeSet.setEntryCount(count);
		
		changeSet.addMember(changeable);

		this.existResourceDao.storeResource(changeSetResourceInfo.getResourceBasePath(), name, changeSet);
	}
	
}
