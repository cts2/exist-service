package edu.mayo.cts2.framework.plugin.service.exist.profile;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmldb.api.base.Resource;

import edu.mayo.cts2.framework.model.updates.ChangeSet;
import edu.mayo.cts2.framework.model.updates.ChangeableResourceChoice;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistResourceDao;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;
import edu.mayo.cts2.framework.service.profile.ChangeSetService;

@Component
public class ExistChangeSetService implements ChangeSetService{

	@Autowired
	private ExistResourceDao existResourceDao;
	
	@Autowired
	private ResourceUnmarshaller resourceUnmarshaller;
	
	@Override
	public ChangeSet readChangeSet(String changeSetUri) {
		Resource resource = existResourceDao.getResource("", changeSetUri);
		
		return (ChangeSet) this.resourceUnmarshaller.unmarshallResource(resource);
	}

	@Override
	public ChangeSet createChangeSet() {
		String changeSetUri = "urn:oid:" + UUID.randomUUID().toString();
		ChangeSet changeSet = new ChangeSet();
		changeSet.setChangeSetURI(changeSetUri);
		
		String name = ExistServiceUtils.getExistResourceName(changeSetUri);
		
		this.existResourceDao.storeResource("", name, changeSet);
		
		return changeSet;
	}

	public void addChangeToChangeSet(ChangeableResourceChoice changeable) {
		String changeSetUri = ModelUtils.
			getChangeableElementGroup(changeable).
				getChangeDescription().
					getContainingChangeSet();
		
		Resource resource = this.existResourceDao.getResource("", changeSetUri);
		
		ChangeSet changeSet = (ChangeSet) this.resourceUnmarshaller.unmarshallResource(resource);

		changeSet.addMember(changeable);
		
		String name = ExistServiceUtils.getExistResourceName(changeSetUri);
		
		this.existResourceDao.storeResource("", name, changeSet);
	}
	
}