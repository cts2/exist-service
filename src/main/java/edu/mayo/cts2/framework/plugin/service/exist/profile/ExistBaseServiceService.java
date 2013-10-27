package edu.mayo.cts2.framework.plugin.service.exist.profile;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xmldb.api.base.Resource;
import edu.mayo.cts2.framework.model.core.FormatReference;
import edu.mayo.cts2.framework.model.core.LanguageReference;
import edu.mayo.cts2.framework.model.core.OpaqueData;
import edu.mayo.cts2.framework.model.core.SourceReference;
import edu.mayo.cts2.framework.model.core.TsAnyType;
import edu.mayo.cts2.framework.model.service.core.BaseService;
import edu.mayo.cts2.framework.model.service.core.DocumentedNamespaceReference;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistResourceDao;
import edu.mayo.cts2.framework.service.profile.BaseServiceService;

@Component
public class ExistBaseServiceService implements BaseServiceService {

	@Value("#{existBuildProperties.buildversion}") 
	protected String buildVersion;

	@Value("#{existBuildProperties.name}") 
	protected String buildName;

	@Value("#{existBuildProperties.description}") 
	protected String buildDescription;
	
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
		return this.getClass().getSimpleName() + " - " + buildName;
	}

	@Override
	public SourceReference getServiceProvider() {
		return new SourceReference("Mayo Clinic");
	}

	@Override
	public String getServiceVersion() {
		return buildVersion;
	}

	@Override
	public OpaqueData getServiceDescription() {
		OpaqueData od = new OpaqueData();
		od.setFormat(new FormatReference("text/plain"));
		od.setLanguage(new LanguageReference("en"));
		TsAnyType any = new TsAnyType();
		any.setContent(buildDescription);
		od.setValue(any);
		return od;
	}

	@Override
	public List<DocumentedNamespaceReference> getKnownNamespaceList() {
		return new ArrayList<DocumentedNamespaceReference>();
	}

}
