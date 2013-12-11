/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.mayo.cts2.framework.plugin.service.exist.profile.resolvedvalueset;

import edu.mayo.cts2.framework.filter.match.StateAdjustingComponentReference;
import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.core.SortCriteria;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.valuesetdefinition.ResolvedValueSet;
import edu.mayo.cts2.framework.model.valuesetdefinition.ResolvedValueSetDirectoryEntry;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistQueryService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.PathInfo;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder.XpathState;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;
import edu.mayo.cts2.framework.service.command.restriction.ResolvedValueSetQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.resolvedvalueset.ResolvedValueSetQuery;
import edu.mayo.cts2.framework.service.profile.resolvedvalueset.ResolvedValueSetQueryService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;
import org.xmldb.api.base.XMLDBException;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

@Component
public class ExistResolvedValueSetQueryService 
	extends AbstractExistQueryService
	<ResolvedValueSet,
	ResolvedValueSetDirectoryEntry,
	ResolvedValueSetQueryServiceRestrictions,
	XpathState> 
	implements ResolvedValueSetQueryService {

	@Resource
	private ResolvedValueSetResourceInfo resolvedValueSetResourceInfo;

	@Override
	public ResolvedValueSetDirectoryEntry doTransform(
			ResolvedValueSet resource,
			ResolvedValueSetDirectoryEntry summary, 
			org.xmldb.api.base.Resource eXistResource) {
		
		summary.setResolvedHeader(resource.getResolutionInfo());
		
		String valueSetName = 
				summary.getResolvedHeader().getResolutionOf().getValueSet().getContent();
		
		String valueSetDefinitionName = 
				summary.getResolvedHeader().getResolutionOf().getValueSetDefinition().getContent();
		
		String name;
		try {
			name = ExistServiceUtils.getNameFromResourceName(eXistResource.getId());
		} catch (XMLDBException e) {
			throw new IllegalStateException(e);
		}
		
		String url = this.getUrlConstructor().createResolvedValueSetUrl(
				valueSetName, 
				valueSetDefinitionName, 
				name);
		
		summary.setHref(url);
		summary.setResourceName(name);
		
		summary.setResolvedValueSetURI(url);

		return summary;
	}
	
	@Override
	protected ResolvedValueSetDirectoryEntry createSummary() {
		return new ResolvedValueSetDirectoryEntry();
	}

	private class ResolvedValueSetDirectoryBuilder extends
			XpathDirectoryBuilder<XpathState,ResolvedValueSetDirectoryEntry> {

		public ResolvedValueSetDirectoryBuilder(final String definitionName) {
			super(new XpathState(), new Callback<XpathState, ResolvedValueSetDirectoryEntry>() {

				@Override
				public DirectoryResult<ResolvedValueSetDirectoryEntry> execute(
						XpathState state, int start, int maxResults) {
					
					return getResourceSummaries(
							getResourceInfo(),
							null,
							definitionName, 
							state.getXpath(), 
							start,
							maxResults);
				}

				@Override
				public int executeCount(XpathState state) {
					throw new UnsupportedOperationException();
				}
			},

			getSupportedMatchAlgorithms(),
			getSupportedSearchReferences());
		}
	}

	@Override
	public DirectoryResult<ResolvedValueSetDirectoryEntry> getResourceSummaries(
			ResolvedValueSetQuery query, 
			SortCriteria sort,
			Page page) {
		ResolvedValueSetDirectoryBuilder builder;
		if(query != null && query.getResolvedValueSetQueryServiceRestrictions() != null
				&& CollectionUtils.isNotEmpty(
						query.getResolvedValueSetQueryServiceRestrictions().getValueSets())){
			String defName = 
				query.getResolvedValueSetQueryServiceRestrictions().getValueSets().iterator().next().getName();
			builder = new ResolvedValueSetDirectoryBuilder(defName);
		} else {
			builder = new ResolvedValueSetDirectoryBuilder("");
		}

		return builder.restrict(
				query.getFilterComponent()).
				addStart(page.getStart()).
				addMaxToReturn(page.getMaxToReturn()).
				resolve();
	}
	
	/* 
	 * Override this to eliminate name searches
	 * 
	 * (non-Javadoc)
	 * @see edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistQueryService#getSupportedModelAttributes()
	 */
	@Override
	public Set<StateAdjustingComponentReference<XpathState>> getSupportedSearchReferences(){
		return new HashSet<StateAdjustingComponentReference<XpathState>>();
	}


	@Override
	protected PathInfo getResourceInfo() {
		return this.resolvedValueSetResourceInfo;
	}

	@Override
	public int count(ResolvedValueSetQuery query) {
		throw new UnsupportedOperationException();
	}

}
