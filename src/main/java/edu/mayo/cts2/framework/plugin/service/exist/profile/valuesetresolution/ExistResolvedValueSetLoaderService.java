package edu.mayo.cts2.framework.plugin.service.exist.profile.valuesetresolution;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.valuesetdefinition.ResolvedValueSet;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistResourceDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.CountingIncrementer;
import edu.mayo.cts2.framework.plugin.service.exist.profile.Incrementer;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;
import edu.mayo.cts2.framework.service.profile.valuesetresolution.ResolvedValueSetLoaderService;
import edu.mayo.cts2.framework.service.profile.valuesetresolution.ResolvedValueSetReference;
import edu.mayo.cts2.framework.service.profile.valuesetresolution.name.ResolvedValueSetReadId;

@Component
public class ExistResolvedValueSetLoaderService implements ResolvedValueSetLoaderService {

	@Resource
	private ResolvedValueSetResourceInfo resolvedValueSetResourceInfo;
	
	@javax.annotation.Resource
	private ExistResourceDao existResourceDao;
	
	@Override
	public ResolvedValueSetReference load(ResolvedValueSet resolvedValueSet) {
	
		String wholePath = ExistServiceUtils.createPath(
				this.resolvedValueSetResourceInfo.getResourceBasePath(), 
				this.resolvedValueSetResourceInfo.createPathFromResource(resolvedValueSet));
		
		Incrementer incrementer = this.buildIncrementer(wholePath);
		
		String name = incrementer.getNext();
		
		this.existResourceDao.storeResource(wholePath, name, resolvedValueSet);
		
		ResolvedValueSetReference ref = new ResolvedValueSetReference();
		
		ref.setLocalID(name);
		ref.setValueSetDefinitionReference(resolvedValueSet.getResolutionInfo().getResolutionOf());
		
		return ref;
	}
	
	protected Incrementer buildIncrementer(String path){
		return new CountingIncrementer(this.existResourceDao,path);
	}

	@Override
	public void delete(ResolvedValueSetReadId resolvedValueSetId) {
		String name = 
				this.resolvedValueSetResourceInfo.getExistResourceName(resolvedValueSetId);
		
		String basePath = 
				this.resolvedValueSetResourceInfo.getResourceBasePath();
		
		String path = 
				this.resolvedValueSetResourceInfo.createPath(resolvedValueSetId);

		existResourceDao.deleteResource(
				ExistServiceUtils.createPath(basePath, path), 
				name);
	}

}
