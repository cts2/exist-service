package edu.mayo.cts2.framework.plugin.service.exist.profile.update;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.xmldb.api.base.Resource;

import edu.mayo.cts2.framework.filter.match.StateAdjustingModelAttributeReference.StateUpdater;
import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.PredicateReference;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.service.core.BaseQueryService;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.model.updates.ChangeSet;
import edu.mayo.cts2.framework.model.updates.ChangeSetDirectoryEntry;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistQueryService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder.XpathState;
import edu.mayo.cts2.framework.service.profile.update.ChangeSetQueryExtension;

@Component
public class ExistChangeSetQueryService 
	extends AbstractExistQueryService
	<ChangeSet,
	ChangeSetDirectoryEntry,
	BaseQueryService,
	XpathState> 
	implements ChangeSetQueryExtension {
	
	@javax.annotation.Resource
	private ChangeSetResourceInfo changeSetResourceInfo;

	@Override
	public DirectoryResult<ChangeSetDirectoryEntry> getResourceSummaries(
			Query query, Set<ResolvedFilter> filterComponent,
			Void restrictions, 
			ResolvedReadContext readContext,
			Page page) {
		
		ChangeSetDirectoryBuilder builder = new ChangeSetDirectoryBuilder();

		return builder.
				restrict(filterComponent).
				restrict(query).
				addStart(page.getStart()).
				addMaxToReturn(page.getMaxToReturn()).
				resolve();
	}

	@Override
	public DirectoryResult<ChangeSet> getResourceList(Query query,
			Set<ResolvedFilter> filterComponent, Void restrictions, Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int count(Query query, Set<ResolvedFilter> filterComponent,
			Void restrictions) {
		throw new UnsupportedOperationException();
	}

	@Override
	public PredicateReference getPropertyReference(String nameOrUri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ChangeSetDirectoryEntry createSummary() {
		return new ChangeSetDirectoryEntry();
	}

	@Override
	protected ChangeSetDirectoryEntry doTransform(
			ChangeSet resource,
			ChangeSetDirectoryEntry summary, 
			Resource eXistResource) {
		summary.setChangeSetElementGroup(resource.getChangeSetElementGroup());
		summary.setChangeSetURI(resource.getChangeSetURI());
		summary.setCloseDate(resource.getCloseDate());
		summary.setCreationDate(resource.getCreationDate());
		summary.setEntryCount(resource.getEntryCount());
		summary.setOfficialEffectiveDate(resource.getOfficialEffectiveDate());
		summary.setResourceName(resource.getChangeSetURI());
		summary.setState(resource.getState());
		
		return summary;
	}

	@Override
	protected StateUpdater<XpathState> getResourceNameStateUpdater() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ResourceInfo<ChangeSet, ?> getResourceInfo() {
		return this.changeSetResourceInfo;
	}

	@Override
	protected List<? extends PredicateReference> getAvailablePredicateReferences() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private class ChangeSetDirectoryBuilder extends XpathDirectoryBuilder<XpathState,ChangeSetDirectoryEntry> {

		public ChangeSetDirectoryBuilder() {
			super(new XpathState(), 
					new Callback<XpathState, ChangeSetDirectoryEntry>() {

				@Override
				public DirectoryResult<ChangeSetDirectoryEntry> execute(
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
				
				getAvailableMatchAlgorithmReferences(),
				getAvailableModelAttributeReferences());
		}
	}

}
