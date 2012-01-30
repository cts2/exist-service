package edu.mayo.cts2.framework.plugin.service.exist.profile.resolvedvalueset;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.extension.LocalIdValueSetResolution;
import edu.mayo.cts2.framework.model.valuesetdefinition.ResolvedValueSet;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistResourceReadingService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceUnmarshaller;
import edu.mayo.cts2.framework.service.profile.resolvedvalueset.ResolvedValueSetReadService;
import edu.mayo.cts2.framework.service.profile.resolvedvalueset.name.ResolvedValueSetReadId;

@Component
public class ExistResolvedValueSetReadService 
	extends AbstractExistResourceReadingService<
		LocalIdValueSetResolution,
		ResolvedValueSetReadId,
		edu.mayo.cts2.framework.model.service.conceptdomainbinding.ConceptDomainBindingReadService> 
	implements ResolvedValueSetReadService {
	
	@Resource
	private ResolvedValueSetResourceInfo resolvedValueSetResourceInfo;
	
	@Resource
	private ResourceUnmarshaller resourceUnmarshaller;

	@Override
	public ResolvedValueSet read(ResolvedValueSetReadId identifier) {
		org.xmldb.api.base.Resource eXistResource = this.getResource(identifier);
		
		if(eXistResource == null){
			return null;
		} else {
			return (ResolvedValueSet) 
					this.resourceUnmarshaller.unmarshallResource(eXistResource);
		}
	}

	@Override
	protected ResourceInfo<ResolvedValueSetReadId> getResourceInfo() {
		return this.resolvedValueSetResourceInfo;
	}

}
