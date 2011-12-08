package edu.mayo.cts2.framework.plugin.service.exist.profile.validator;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;

import edu.mayo.cts2.framework.model.core.types.FinalizableState;
import edu.mayo.cts2.framework.model.exception.changeset.ChangeSetIsNotOpenException;
import edu.mayo.cts2.framework.model.exception.changeset.UnknownChangeSetException;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistResourceDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.update.ChangeSetResourceInfo;

@Component
public class ChangeSetUriValidator {
	
	@javax.annotation.Resource
	private ChangeSetResourceInfo changeSetResourceInfo;
	
	@javax.annotation.Resource
	private ExistResourceDao existResourceDao;

	public void validateChangeSet(String changeSetUri){
		Resource resource = this.existResourceDao.getResourceByXpath(
				this.changeSetResourceInfo.getResourceBasePath(), 
				this.buildXquery(changeSetUri));
		
		if(resource == null){
			throw new UnknownChangeSetException();
		}

		String resourceContent;
		try {
			resourceContent = resource.getContent().toString();
		} catch (XMLDBException e) {
			throw new IllegalStateException(e);
		}
		
		if(StringUtils.isBlank(resourceContent)){
			throw new UnknownChangeSetException();
		}
		
		FinalizableState state = FinalizableState.fromValue(resourceContent);
		
		if(state.equals(FinalizableState.FINAL)){
			throw new ChangeSetIsNotOpenException(changeSetUri);
		}
	}
	
	protected String buildXquery(String changeSetUri){
		String basePath = this.changeSetResourceInfo.getResourceXpath();
		String uriPath = this.changeSetResourceInfo.getUriXpath();
		
		return "string("+basePath+"["+uriPath+" = '"+changeSetUri+"']/@state)";
	}
}
