package edu.mayo.cts2.framework.plugin.service.exist.profile.resolvedvalueset;

import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.extension.LocalIdValueSetResolution;
import edu.mayo.cts2.framework.model.valuesetdefinition.ResolvedValueSet;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistResourceReadingService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceUnmarshaller;
import edu.mayo.cts2.framework.service.profile.resolvedvalueset.ResolvedValueSetResolutionService;
import edu.mayo.cts2.framework.service.profile.resolvedvalueset.name.ResolvedValueSetReadId;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ResolvedValueSetResult;

@Component
public class ExistResolvedValueSetResolutionService 
	extends AbstractExistResourceReadingService<
		LocalIdValueSetResolution,
		ResolvedValueSetReadId,
		edu.mayo.cts2.framework.model.service.conceptdomainbinding.ConceptDomainBindingReadService> 
	implements ResolvedValueSetResolutionService {
	
	@Resource
	private ResolvedValueSetResourceInfo resolvedValueSetResourceInfo;
	
	@Resource
	private ResourceUnmarshaller resourceUnmarshaller;

	@Override
	public ResolvedValueSet getResolution(ResolvedValueSetReadId identifier) {
		org.xmldb.api.base.Resource eXistResource = this.getResource(identifier);
		
		if(eXistResource == null){
			return null;
		} else {
			return (ResolvedValueSet) 
					this.resourceUnmarshaller.unmarshallResource(eXistResource);
		}
	}

	@Override
	public ResolvedValueSetResult getResolution(
			ResolvedValueSetReadId identifier,
			Set<ResolvedFilter> filterComponent, 
			Page page) {
		
		ResolvedValueSet resolvedValueSet = this.getResolution(identifier);
		
		if(resolvedValueSet == null){
			return null;
		}
	
		//TODO: Fix this
		ResolvedValueSetResult result = new ResolvedValueSetResult(
				resolvedValueSet.getResolutionInfo(),
				resolvedValueSet.getMemberAsReference(),
				true
				);
		
		return result;
	}

	@Override
	protected ResourceInfo<ResolvedValueSetReadId> getResourceInfo() {
		return this.resolvedValueSetResourceInfo;
	}


}
