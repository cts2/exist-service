package edu.mayo.cts2.framework.plugin.service.exist.profile.valueset;

import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.PredicateReference;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntry;
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntrySummary;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistQueryService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder.XpathState;
import edu.mayo.cts2.framework.service.command.restriction.ValueSetQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.valueset.ValueSetQueryService;

@Component
public class ExistValueSetQueryService 
	extends AbstractExistQueryService
		<ValueSetCatalogEntry,
		ValueSetCatalogEntrySummary,
		ValueSetQueryServiceRestrictions,
		edu.mayo.cts2.framework.model.service.valueset.ValueSetQueryService,XpathState>
	implements ValueSetQueryService {

	@Resource
	private ValueSetResourceInfo valueSetResourceInfo;
	
	@Override
	protected ValueSetCatalogEntrySummary createSummary() {
		return new ValueSetCatalogEntrySummary();
	}

	@Override
	protected ValueSetCatalogEntrySummary doTransform(
			ValueSetCatalogEntry resource,
			ValueSetCatalogEntrySummary summary, org.xmldb.api.base.Resource eXistResource11) {
		summary = this.baseTransform(summary, resource);
		
		summary.setValueSetName(resource.getValueSetName());
		summary.setHref(getUrlConstructor().createValueSetUrl(resource.getValueSetName()));
		
		return summary;
	}

	private class ValueSetDirectoryBuilder extends XpathDirectoryBuilder<XpathState,ValueSetCatalogEntrySummary> {

		public ValueSetDirectoryBuilder() {
			super(new XpathState(), new Callback<XpathState, ValueSetCatalogEntrySummary>() {

				@Override
				public DirectoryResult<ValueSetCatalogEntrySummary> execute(
						XpathState state, 
						int start, 
						int maxResults) {
					return getResourceSummaries(
							"",
							state.getXpath(), 
							start, 
							maxResults);
				}

				@Override
				public int executeCount(XpathState state) {
					throw new UnsupportedOperationException();
				}},
				
				getSupportedMatchAlgorithms(),
				getSupportedModelAttributes());
		}
	}

	@Override
	public DirectoryResult<ValueSetCatalogEntrySummary> getResourceSummaries(
			Query query, 
			Set<ResolvedFilter> filterComponent, 
			ValueSetQueryServiceRestrictions restrictions,
			ResolvedReadContext readContext,
			Page page) {

	ValueSetDirectoryBuilder builder = new ValueSetDirectoryBuilder();
	
	return 
		builder.restrict(filterComponent).
			addStart(page.getStart()).
			addMaxToReturn(page.getMaxToReturn()).
			resolve();
	}

	@Override
	public DirectoryResult<ValueSetCatalogEntry> getResourceList(
			Query query,
			Set<ResolvedFilter> filterComponent,
			ValueSetQueryServiceRestrictions restrictions,
			ResolvedReadContext readContext,
			Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int count(
			Query query, 
			Set<ResolvedFilter> filterComponent,
			ValueSetQueryServiceRestrictions restrictions) {
		ValueSetDirectoryBuilder builder = new ValueSetDirectoryBuilder();
		
		return 
				builder.restrict(filterComponent).
					count();
	}

	@Override
	protected ResourceInfo<ValueSetCatalogEntry, ?> getResourceInfo() {
		return this.valueSetResourceInfo;
	}

	@Override
	public Set<? extends PredicateReference> getSupportedProperties() {
		// TODO Auto-generated method stub
		return null;
	}
}
