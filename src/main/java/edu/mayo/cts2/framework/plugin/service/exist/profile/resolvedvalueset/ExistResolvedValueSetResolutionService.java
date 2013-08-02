package edu.mayo.cts2.framework.plugin.service.exist.profile.resolvedvalueset;

import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.core.*;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.entity.EntityDescription;
import edu.mayo.cts2.framework.model.entity.EntityDirectoryEntry;
import edu.mayo.cts2.framework.model.extension.LocalIdValueSetResolution;
import edu.mayo.cts2.framework.model.valuesetdefinition.ResolvedValueSet;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistResourceReadingService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceUnmarshaller;
import edu.mayo.cts2.framework.service.profile.resolvedvalueset.ResolvedValueSetResolutionService;
import edu.mayo.cts2.framework.service.profile.resolvedvalueset.name.ResolvedValueSetReadId;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ResolvedValueSetResolutionEntityQuery;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ResolvedValueSetResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
	public ResolvedValueSetResult<URIAndEntityName> getResolution(
			ResolvedValueSetReadId identifier,
			Set<ResolvedFilter> filterComponent, 
			Page page) {
		
		ResolvedValueSet resolvedValueSet = this.getResolution(identifier);
		
		if(resolvedValueSet == null){
			return null;
		}
	
		List<URIAndEntityName> slice = this.slice(
				resolvedValueSet.getEntryAsReference(),
				page.getStart(), 
				page.getMaxToReturn());

		ResolvedValueSetResult<URIAndEntityName> result =
			new ResolvedValueSetResult<URIAndEntityName>(
				resolvedValueSet.getResolutionInfo(),
				slice,
				resolvedValueSet.getEntryAsReference().size() == page.getEnd()
				);
		
		return result;
	}
	
	public <T> List<T> slice(List<T> list, int index, int count) {
	    List<T> result = new ArrayList<T>();
	    if (index >= 0 && index < list.size()) {
	      int end = index + count < list.size() ? index + count : list.size();
	      for (int i = index; i < end; i++) {
	        result.add(list.get(i));
	      }
	    }
	    return result;
	  }

	@Override
	public Set<? extends MatchAlgorithmReference> getSupportedMatchAlgorithms() {
		return null;
	}

	@Override
	public Set<? extends ComponentReference> getSupportedSearchReferences() {
		return null;
	}

	@Override
	public Set<? extends ComponentReference> getSupportedSortReferences() {
		return null;
	}

	@Override
	public Set<PredicateReference> getKnownProperties() {
		return null;
	}

	@Override
	protected ResourceInfo<ResolvedValueSetReadId> getResourceInfo() {
		return this.resolvedValueSetResourceInfo;
	}

	@Override
	public ResolvedValueSetResult<EntityDirectoryEntry> getEntities(
			ResolvedValueSetReadId identifier,
			ResolvedValueSetResolutionEntityQuery query,
			SortCriteria sortCriteria, Page page) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public DirectoryResult<EntityDescription> getEntityList(
			ResolvedValueSetReadId identifier,
			ResolvedValueSetResolutionEntityQuery query,
			SortCriteria sortCriteria, Page page) {
		
		throw new UnsupportedOperationException();
	}


}
