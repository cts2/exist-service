package edu.mayo.cts2.framework.plugin.service.exist.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmldb.api.base.Resource;

import edu.mayo.cts2.framework.model.service.core.BaseService;
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

}
