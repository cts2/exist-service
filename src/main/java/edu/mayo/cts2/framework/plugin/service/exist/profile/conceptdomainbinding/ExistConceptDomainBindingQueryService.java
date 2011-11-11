package edu.mayo.cts2.framework.plugin.service.exist.profile.conceptdomainbinding;

import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.conceptdomainbinding.ConceptDomainBinding;
import edu.mayo.cts2.framework.model.conceptdomainbinding.ConceptDomainBindingDirectoryEntry;
import edu.mayo.cts2.framework.model.core.PredicateReference;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistQueryService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder.XpathState;
import edu.mayo.cts2.framework.service.command.restriction.ConceptDomainBindingQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.conceptdomainbinding.ConceptDomainBindingQueryService;

@Component
public class ExistConceptDomainBindingQueryService 
	extends AbstractExistQueryService
		<ConceptDomainBinding,
		ConceptDomainBindingDirectoryEntry,
		ConceptDomainBindingQueryServiceRestrictions,
		edu.mayo.cts2.framework.model.service.conceptdomainbinding.ConceptDomainBindingQueryService,XpathState>
	implements ConceptDomainBindingQueryService {

	@Resource
	private ConceptDomainBindingResourceInfo conceptDomainBindingResourceInfo;

	@Override
	public DirectoryResult<ConceptDomainBindingDirectoryEntry> getResourceSummaries(
			Query query, 
			Set<ResolvedFilter> filterComponent, 
			ConceptDomainBindingQueryServiceRestrictions restrictions,
			ResolvedReadContext readContext,
			Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DirectoryResult<ConceptDomainBinding> getResourceList(
			Query query,
			Set<ResolvedFilter> filterComponent, 
			ConceptDomainBindingQueryServiceRestrictions restrictions, 
			ResolvedReadContext readContext,
			Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int count(
			Query query, 
			Set<ResolvedFilter> filterComponent,
			ConceptDomainBindingQueryServiceRestrictions restrictions) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected ConceptDomainBindingDirectoryEntry createSummary() {
		return new ConceptDomainBindingDirectoryEntry();
	}

	@Override
	protected ConceptDomainBindingDirectoryEntry doTransform(
			ConceptDomainBinding resource,
			ConceptDomainBindingDirectoryEntry summary,
			org.xmldb.api.base.Resource eXistResource) {
		
		return summary;
	}

	@Override
	protected ResourceInfo<ConceptDomainBinding, ?> getResourceInfo() {
		return this.conceptDomainBindingResourceInfo;
	}

	@Override
	public Set<? extends PredicateReference> getSupportedProperties() {
		// TODO Auto-generated method stub
		return null;
	}

}
