package edu.mayo.cts2.framework.plugin.service.exist.profile.update;

import java.net.URI;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.ant.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;

import edu.mayo.cts2.framework.model.core.ChangeDescription;
import edu.mayo.cts2.framework.model.core.ChangeSetElementGroup;
import edu.mayo.cts2.framework.model.core.OpaqueData;
import edu.mayo.cts2.framework.model.core.SourceReference;
import edu.mayo.cts2.framework.model.core.types.ChangeCommitted;
import edu.mayo.cts2.framework.model.core.types.ChangeType;
import edu.mayo.cts2.framework.model.core.types.FinalizableState;
import edu.mayo.cts2.framework.model.exception.UnspecifiedCts2RuntimeException;
import edu.mayo.cts2.framework.model.updates.ChangeSet;
import edu.mayo.cts2.framework.model.updates.ChangeableResourceChoice;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistResourceDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceMarshaller;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceUnmarshaller;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;
import edu.mayo.cts2.framework.service.profile.update.ChangeSetService;

@Component
public class ExistChangeSetService implements ChangeSetService {

	@javax.annotation.Resource
	private ChangeSetResourceInfo changeSetResourceInfo;
	
	protected Log log = LogFactory.getLog(getClass());

	@Autowired
	private ExistResourceDao existResourceDao;
	
	@Autowired
	private ResourceUnmarshaller resourceUnmarshaller;
	
	@Autowired
	private ResourceMarshaller resourceMarshaller;
	
	@Override
	public ChangeSet readChangeSet(String changeSetUri) {
		String name = ExistServiceUtils.uriToExistName(changeSetUri);
		
		Resource resource = existResourceDao.getResource(changeSetResourceInfo.getResourceBasePath(), name);
		
		return (ChangeSet) this.resourceUnmarshaller.unmarshallResource(resource);
	}

	@Override
	public ChangeSet createChangeSet() {
		ChangeSet changeSet = doCreateNewChangeSet();
		
		String name = this.changeSetResourceInfo.getExistResourceNameFromResource(changeSet);
		
		this.existResourceDao.storeResource(changeSetResourceInfo.getResourceBasePath(), name, changeSet);
		
		return changeSet;
	}
	
	protected ChangeSet doCreateNewChangeSet(){
		String changeSetUri = this.createNewChangeSetUri();
		ChangeSet changeSet = new ChangeSet();
		changeSet.setChangeSetURI(changeSetUri);
		changeSet.setCreationDate(new Date());
		changeSet.setState(FinalizableState.OPEN);
		
		return changeSet;
	}
	
	protected String createNewChangeSetUri(){
		return "urn:uuid:" + UUID.randomUUID().toString();
	}

	@Override
	public void rollbackChangeSet(String changeSetUri) {
		String name = ExistServiceUtils.uriToExistName(changeSetUri);
		
		this.existResourceDao.deleteResource(changeSetResourceInfo.getResourceBasePath(), name);
	}

	@Override
	public void commitChangeSet(String changeSetUri) {
		String changeSetDir = ExistServiceUtils.getTempChangeSetContentDirName(changeSetUri);
		
		ResourceSet resources = this.existResourceDao.query(changeSetDir, "/*", 0, -1);
		
		try {
			ResourceIterator itr = resources.getIterator();
			
			while(itr.hasMoreResources()){
				Resource resource = itr.nextResource();
				
				String parentCollectionName = resource.getParentCollection().getName();
				
				parentCollectionName = parentCollectionName.replace(changeSetDir, "");

				String resourceId = StringUtils.removeSuffix(resource.getId(), ".xml");
				
				if(log.isDebugEnabled()){
					log.debug("Moving resource: " + resource.getParentCollection().getName() + resourceId +
						"To: " + parentCollectionName + resourceId);
				}
		
				Object resourcObj = 
						this.resourceUnmarshaller.unmarshallResource(resource);
				
				ChangeDescription changeDescription = 
						ModelUtils.getChangeableElementGroup(resourcObj).
					getChangeDescription();
				
				if(changeDescription.getChangeType().equals(ChangeType.DELETE)){
					this.existResourceDao.deleteResource(parentCollectionName, resourceId);
				} else {
					changeDescription.setCommitted(ChangeCommitted.COMMITTED);
			
					resource.setContent(
							resourceMarshaller.marshallResource(resourcObj));
	
					this.existResourceDao.storeResource(parentCollectionName, resourceId, resource);
				}
			}
			
			this.existResourceDao.removeCollection(changeSetDir);
			
			ChangeSet changeSet = this.readChangeSet(changeSetUri);
			
			for(ChangeableResourceChoice change : changeSet.getMember()){
				ModelUtils.getChangeableElementGroup(change).
					getChangeDescription().setCommitted(ChangeCommitted.COMMITTED);
			}
	
			changeSet.setState(FinalizableState.FINAL);
			changeSet.setCloseDate(new Date());
			
			String name = this.changeSetResourceInfo.getExistResourceNameFromResource(changeSet);
			
			this.existResourceDao.storeResource(
					changeSetResourceInfo.getResourceBasePath(), name, changeSet);
			
		} catch (XMLDBException e) {
			throw new UnspecifiedCts2RuntimeException(e);
		}
	}

	@Override
	public String importChangeSet(URI changeSetUri) {
		throw new UnsupportedOperationException();
	}
	
	private void addChangeSetElementGroupIfNecessary(ChangeSet changeSet){
		if(changeSet.getChangeSetElementGroup() == null){
			changeSet.setChangeSetElementGroup(new ChangeSetElementGroup());
		}
	}

	@Override
	public void updateChangeSetMetadata(
			String changeSetUri, 
			SourceReference creator,
			OpaqueData changeInstructions, 
			Date officialEffectiveDate) {
		ChangeSet changeSet = this.readChangeSet(changeSetUri);
		
		if(creator != null){
			this.addChangeSetElementGroupIfNecessary(changeSet);
			changeSet.getChangeSetElementGroup().setCreator(creator);
		}
		if(changeInstructions != null){
			this.addChangeSetElementGroupIfNecessary(changeSet);
			changeSet.getChangeSetElementGroup().setChangeInstructions(changeInstructions);
		}
		if(officialEffectiveDate != null){
			changeSet.setOfficialEffectiveDate(officialEffectiveDate);
		}
		
		String name = this.changeSetResourceInfo.getExistResourceNameFromResource(changeSet);
		
		this.existResourceDao.storeResource(
				changeSetResourceInfo.getResourceBasePath(), name, changeSet);
	}

}