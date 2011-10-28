package edu.mayo.cts2.framework.plugin.service.exist.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmldb.api.base.Resource;

import edu.mayo.cts2.framework.model.core.ChangeDescription;
import edu.mayo.cts2.framework.model.updates.ChangeSet;
import edu.mayo.cts2.framework.model.updates.ChangeableResourceChoice;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistResourceDao;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;

@Component
public class StateChangeCallback {

	@Autowired
	private ExistResourceDao existResourceDao;
	
	@Autowired
	private ResourceUnmarshaller resourceUnmarshaller;
	
	public void resourceAdded(ChangeableResourceChoice changeable) {
		ChangeDescription changeDescription = ModelUtils.
			getChangeableElementGroup(changeable).
				getChangeDescription();
		
		String changeSetUri = changeDescription.
					getContainingChangeSet();
		
		String name = ExistServiceUtils.uriToExistName(changeSetUri);
		
		Resource resource = 
				this.existResourceDao.getResource(ExistChangeSetService.CHANGESETS_PATH, name);
		
		ChangeSet changeSet = (ChangeSet) this.resourceUnmarshaller.unmarshallResource(resource);
		
		long count = changeSet.getMemberCount();

		count++;
		
		changeable.setEntryOrder(count);
		changeSet.setEntryCount(count);
		
		changeSet.addMember(changeable);
		
		this.existResourceDao.storeResource(ExistChangeSetService.CHANGESETS_PATH, name, changeSet);
	}
	
}
