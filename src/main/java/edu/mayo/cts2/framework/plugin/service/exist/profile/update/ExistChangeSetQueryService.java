package edu.mayo.cts2.framework.plugin.service.exist.profile.update;

import edu.mayo.cts2.framework.filter.match.StateAdjustingComponentReference;
import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.core.SortCriteria;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.model.updates.ChangeSet;
import edu.mayo.cts2.framework.model.updates.ChangeSetDirectoryEntry;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistQueryService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.PathInfo;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder.XpathState;
import edu.mayo.cts2.framework.plugin.service.exist.xpath.XpathStateUpdater;
import edu.mayo.cts2.framework.service.command.restriction.ChangeSetQueryExtensionRestrictions;
import edu.mayo.cts2.framework.service.profile.update.ChangeSetQuery;
import edu.mayo.cts2.framework.service.profile.update.ChangeSetQueryExtension;
import org.springframework.stereotype.Component;
import org.xmldb.api.base.Resource;

import java.util.Set;

@Component
public class ExistChangeSetQueryService 
	extends AbstractExistQueryService
	<ChangeSet,
	ChangeSetDirectoryEntry,
	ChangeSetQueryExtensionRestrictions,
	XpathState> 
	implements ChangeSetQueryExtension {
	
	@javax.annotation.Resource
	private ChangeSetResourceInfo changeSetResourceInfo;

	@Override
	public DirectoryResult<ChangeSetDirectoryEntry> getResourceSummaries(
			ChangeSetQuery query, 
			SortCriteria sort,
			Page page) {
		
		ChangeSetDirectoryBuilder builder = new ChangeSetDirectoryBuilder();

		return builder.
				restrict(query.getFilterComponent()).
				restrict(query.getQuery()).
				addStart(page.getStart()).
				addMaxToReturn(page.getMaxToReturn()).
				resolve();
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
	protected PathInfo getResourceInfo() {
		return this.changeSetResourceInfo;
	}

	@Override
	public int count(Query query, Set<ResolvedFilter> filterComponent,
			ChangeSetQueryExtensionRestrictions restrictions) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Set<StateAdjustingComponentReference<XpathState>> getSupportedSearchReferences() {
		Set<StateAdjustingComponentReference<XpathState>> set =
				super.getSupportedSearchReferences();
		
		set.add(this.getFinalizableStateAdjustingReference());
		
		return set;
	}

	protected StateAdjustingComponentReference<XpathState> getFinalizableStateAdjustingReference(){
		XpathStateUpdater<XpathState> updater = new XpathStateUpdater<XpathState>("@state");

        StateAdjustingComponentReference<XpathState> stateAdjustor =
				new StateAdjustingComponentReference<XpathState>(updater);

		stateAdjustor.setAttributeReference("state");
		
		return stateAdjustor;
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
							getResourceInfo(),
							null,
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
				getSupportedSearchReferences());
		}
	}

}
