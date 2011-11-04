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
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package edu.mayo.cts2.framework.plugin.service.exist.profile;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;

import edu.mayo.cts2.framework.core.url.UrlConstructor;
import edu.mayo.cts2.framework.filter.match.StateAdjustingModelAttributeReference;
import edu.mayo.cts2.framework.filter.match.StateAdjustingModelAttributeReference.StateUpdater;
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference;
import edu.mayo.cts2.framework.model.core.ResourceDescription;
import edu.mayo.cts2.framework.model.core.ResourceDescriptionDirectoryEntry;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.service.core.BaseQueryService;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistResourceDao;
import edu.mayo.cts2.framework.plugin.service.exist.dao.SummaryTransform;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder.XpathState;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;
import edu.mayo.cts2.framework.service.meta.StandardMatchAlgorithmReference;
import edu.mayo.cts2.framework.service.meta.StandardModelAttributeReference;
import edu.mayo.cts2.framework.service.profile.AbstractQueryService;

/**
 * The Class AbstractService.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractExistQueryService
	<R,Summary,T extends BaseQueryService,S extends XpathState> 
	extends AbstractQueryService<T> {
	
	@Autowired
	private UrlConstructor urlConstructor;
	
	@javax.annotation.Resource
	private ExistResourceDao existResourceDao;
	
	protected ExistResourceDao getExistResourceDao() {
		return existResourceDao;
	}
	protected void setExistResourceDao(ExistResourceDao existResourceDao) {
		this.existResourceDao = existResourceDao;
	}

	@Autowired
	private ResourceUnmarshaller resourceUnmarshaller;
	
	private static final String MAYO = "Mayo Clinic";
	private static final String DEFAULT_VERSION = "1.0";
	private static final String DESCRIPTION = "CTS2 Service using eXist xml database.";
	
	@Override
	protected String getVersion() {
		return DEFAULT_VERSION;
	}
	@Override
	protected String getProvider() {
		return MAYO;
	}
	@Override
	protected String getDescription() {
		return DESCRIPTION;
	}

	public DirectoryResult<Summary> getResourceSummaries(
			String collectionPath, 
			String xpath,
			int start, int max) {
		
		return this.getResourceSummaries(null, collectionPath, xpath, start, max);
	}
	
	public DirectoryResult<Summary> getResourceSummaries(
			String changeSetUri,
			String collectionPath, 
			String xpath,
			int start, int max) {
		
		String changeSetDir = null;
		if(StringUtils.isNotBlank(changeSetUri)){
			changeSetDir = ExistServiceUtils.getTempChangeSetContentDirName(changeSetUri);
		}
		
		String queryString = this.getResourceInfo().getResourceXpath() + (StringUtils.isNotBlank(xpath) ? xpath : "");
		
		collectionPath = ExistServiceUtils.createPath(
				changeSetDir,
				this.getResourceInfo().getResourceBasePath(),
				collectionPath);

		ResourceSet collection = this.getExistResourceDao().query(
				collectionPath, queryString, start, max);


		return this.getResourceSummaries(collection, xpath, start, max,
				transform);
	}
	
	private DirectoryResult<Summary> getResourceSummaries(
			ResourceSet collection,
			String xpath,
			int start,
			int max,
			SummaryTransform<Summary, R> transform) {
		try {

			long size = collection.getSize();
			
			List<Summary> returnList = new ArrayList<Summary>();

		
			for(int i=start;i< start + max && i < size;i++) {
				Resource res = collection.getResource(i);
			
				@SuppressWarnings("unchecked")
				R entry = (R) this.resourceUnmarshaller.unmarshallResource(res);

				returnList.add(transform.transform(entry, res));
			}
		
			return new DirectoryResult<Summary>(returnList, size <= returnList.size(), (start + max) >= size);
		} catch (XMLDBException e) {
			throw new IllegalStateException(e);
		}
	}
	
	protected <E extends ResourceDescriptionDirectoryEntry, D extends ResourceDescription> E baseTransform(
			E summary, D resource) {

		summary.setAbout(resource.getAbout());
		summary.setFormalName(resource.getFormalName());
		summary.setResourceSynopsis(resource.getResourceSynopsis());

		return summary;
	}
	
	protected abstract Summary createSummary();

	protected abstract Summary doTransform(R resource, Summary summary,
			Resource eXistResource);

	private SummaryTransform<Summary, R> transform = new SummaryTransform<Summary, R>() {

		@Override
		public Summary createSummary() {
			return AbstractExistQueryService.this.createSummary();
		}

		@Override
		public Summary transform(R resource, Resource eXistResource) {
			return doTransform(resource,
					this.createSummary(), eXistResource);
		}
	};
	
	@Override
	protected List<MatchAlgorithmReference> getAvailableMatchAlgorithmReferences() {
		List<MatchAlgorithmReference> returnList = new ArrayList<MatchAlgorithmReference>();
		
		returnList.add(StandardMatchAlgorithmReference.CONTAINS.getMatchAlgorithmReference());
		returnList.add(StandardMatchAlgorithmReference.EXACT_MATCH.getMatchAlgorithmReference());
		returnList.add(StandardMatchAlgorithmReference.STARTS_WITH.getMatchAlgorithmReference());

		return returnList;
	}

	protected List<StateAdjustingModelAttributeReference<S>> getAvailableModelAttributeReferences() {
		ArrayList<StateAdjustingModelAttributeReference<S>> returnList = 
				new ArrayList<StateAdjustingModelAttributeReference<S>>();
		
		StateAdjustingModelAttributeReference<S> resourceName = 
			StateAdjustingModelAttributeReference.toModelAttributeReference(
					StandardModelAttributeReference.RESOURCE_NAME.getModelAttributeReference(), 
					getResourceNameStateUpdater());
		
		returnList.add(resourceName);
		
		return returnList;
	}
	
	protected abstract StateUpdater<S> getResourceNameStateUpdater();

	public void setUrlConstructor(UrlConstructor urlConstructor) {
		this.urlConstructor = urlConstructor;
	}

	public UrlConstructor getUrlConstructor() {
		return urlConstructor;
	}

	protected abstract ResourceInfo<R,?> getResourceInfo();

}
