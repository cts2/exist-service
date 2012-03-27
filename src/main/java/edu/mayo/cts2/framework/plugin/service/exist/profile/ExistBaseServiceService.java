package edu.mayo.cts2.framework.plugin.service.exist.profile;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmldb.api.base.Resource;

import edu.mayo.cts2.framework.model.core.OpaqueData;
import edu.mayo.cts2.framework.model.core.SourceReference;
import edu.mayo.cts2.framework.model.service.core.BaseService;
import edu.mayo.cts2.framework.model.service.core.DocumentedNamespaceReference;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistResourceDao;
import edu.mayo.cts2.framework.service.profile.BaseServiceService;

@Component
public class ExistBaseServiceService implements BaseServiceService {

	@javax.annotation.Resource
	private ExistResourceDao existResourceDao;
	
	@Autowired
	private ResourceUnmarshaller resourceUnmarshaller;
	
	@Override
	public BaseService getBaseService() {
		Resource resource =  existResourceDao.getResource("services", "service");
		
		if(resource != null){
			return (BaseService) 
					this.resourceUnmarshaller.unmarshallResource(resource);
		} else {
			return null;
		}
	}

	@Override
	public String getServiceName() {
		return null;
	}

	@Override
	public SourceReference getServiceProvider() {
		return null;
	}

	@Override
	public String getServiceVersion() {
		return null;
	}

	@Override
	public OpaqueData getServiceDescription() {
		return null;
	}

	@Override
	public List<DocumentedNamespaceReference> getKnownNamespaceList() {
		return null;
	}

}
