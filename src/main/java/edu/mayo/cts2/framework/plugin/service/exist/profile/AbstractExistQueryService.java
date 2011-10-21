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

import edu.mayo.cts2.framework.filter.match.StateAdjustingModelAttributeReference;
import edu.mayo.cts2.framework.filter.match.StateAdjustingModelAttributeReference.StateUpdater;
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference;
import edu.mayo.cts2.framework.model.service.core.BaseQueryService;
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
	<T extends BaseQueryService,S extends XpathState> 
	extends AbstractQueryService<T> {
	
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
	
	protected String createPath(String... path){
		if(path == null){
			return "";
		}
		
		StringBuffer sb = new StringBuffer();
		for(String st : path){
			if(StringUtils.isNotBlank(st)){
				sb.append(st);
				sb.append("/");
			}
		}
		
		return ExistServiceUtils.getExistResourceName(sb.toString());
	}
	
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
}
