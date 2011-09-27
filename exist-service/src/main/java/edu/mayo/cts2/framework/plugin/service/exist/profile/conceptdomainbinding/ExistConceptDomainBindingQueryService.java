package edu.mayo.cts2.framework.plugin.service.exist.profile.conceptdomainbinding;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.plugin.service.exist.dao.ConceptDomainBindingExistDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistQueryService;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder.XpathState;
import edu.mayo.cts2.sdk.filter.match.StateAdjustingModelAttributeReference.StateUpdater;
import edu.mayo.cts2.sdk.model.conceptdomainbinding.ConceptDomainBinding;
import edu.mayo.cts2.sdk.model.conceptdomainbinding.ConceptDomainBindingDirectoryEntry;
import edu.mayo.cts2.sdk.model.core.FilterComponent;
import edu.mayo.cts2.sdk.model.core.PredicateReference;
import edu.mayo.cts2.sdk.model.directory.DirectoryResult;
import edu.mayo.cts2.sdk.model.service.core.Query;
import edu.mayo.cts2.sdk.service.command.Page;
import edu.mayo.cts2.sdk.service.command.restriction.ConceptDomainBindingQueryServiceRestrictions;
import edu.mayo.cts2.sdk.service.profile.conceptdomainbinding.ConceptDomainBindingQueryService;

@Component
public class ExistConceptDomainBindingQueryService 
	extends AbstractExistQueryService
		<edu.mayo.cts2.sdk.model.service.conceptdomainbinding.ConceptDomainBindingQueryService,XpathState>
	implements ConceptDomainBindingQueryService {

	@Resource
	private ConceptDomainBindingExistDao conceptDomainBindingExistDao;

	@Override
	public PredicateReference getPropertyReference(String nameOrUri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DirectoryResult<ConceptDomainBindingDirectoryEntry> getResourceSummaries(
			Query query, 
			FilterComponent filterComponent, 
			ConceptDomainBindingQueryServiceRestrictions restrictions,
			Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DirectoryResult<ConceptDomainBinding> getResourceList(
			Query query,
			FilterComponent filterComponent, 
			ConceptDomainBindingQueryServiceRestrictions restrictions, 
			Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int count(
			Query query, 
			FilterComponent filterComponent,
			ConceptDomainBindingQueryServiceRestrictions restrictions) {
		throw new UnsupportedOperationException();
	}

	protected List<? extends PredicateReference> getAvailablePredicateReferences() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected StateUpdater<XpathState> getResourceNameStateUpdater() {
		// TODO Auto-generated method stub
		return null;
	}
}
