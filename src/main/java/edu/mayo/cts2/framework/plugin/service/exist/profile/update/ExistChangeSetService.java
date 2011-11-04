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

import edu.mayo.cts2.framework.model.core.OpaqueData;
import edu.mayo.cts2.framework.model.core.types.FinalizableState;
import edu.mayo.cts2.framework.model.exception.UnspecifiedCts2RuntimeException;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.updates.ChangeSet;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistResourceDao;
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
	
	@Override
	public ChangeSet readChangeSet(String changeSetUri) {
		String name = ExistServiceUtils.uriToExistName(changeSetUri);
		
		Resource resource = existResourceDao.getResource(changeSetResourceInfo.getResourceBasePath(), name);
		
		return (ChangeSet) this.resourceUnmarshaller.unmarshallResource(resource);
	}

	@Override
	public ChangeSet createChangeSet() {
		String changeSetUri = "urn:oid:" + UUID.randomUUID().toString();
		ChangeSet changeSet = new ChangeSet();
		changeSet.setChangeSetURI(changeSetUri);
		changeSet.setCreationDate(new Date());
		changeSet.setState(FinalizableState.OPEN);
		
		String name = ExistServiceUtils.uriToExistName(changeSetUri);
		
		this.existResourceDao.storeResource(changeSetResourceInfo.getResourceBasePath(), name, changeSet);
		
		return changeSet;
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
				
				log.info("Moving resource: " + resource.getParentCollection().getName() + resourceId +
					"To: " + parentCollectionName + resourceId);
	
				this.existResourceDao.storeResource(parentCollectionName, resourceId, resource);
			}
			
			this.existResourceDao.removeCollection(changeSetDir);
		} catch (XMLDBException e) {
			throw new UnspecifiedCts2RuntimeException(e);
		}
	}

	@Override
	public String importChangeSet(URI changeSetUri) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateChangeSetMetadata(String changeSetUri, NameOrURI creator,
			OpaqueData changeInstructions, Date officialEffectiveDate) {
		//TODO:
	}

}