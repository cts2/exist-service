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

import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import edu.mayo.cts2.framework.model.core.OpaqueData;
import edu.mayo.cts2.framework.model.core.SourceReference;
import edu.mayo.cts2.framework.model.service.core.DocumentedNamespaceReference;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;
import edu.mayo.cts2.framework.service.profile.BaseService;

/**
 * The Class AbstractService.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractExistService implements BaseService {
	
	private static final String MAYO = "Mayo Clinic";
	
	@Value("#{existBuildProperties.buildversion}") 
	protected String buildVersion;

	@Value("#{existBuildProperties.name}") 
	protected String buildName;

	@Value("#{existBuildProperties.description}") 
	protected String buildDescription;
	
	@Override
	public String getServiceVersion() {
		return buildVersion;
	}
	
	@Override
	public SourceReference getServiceProvider() {
		SourceReference ref = new SourceReference();
		ref.setContent(MAYO);
		
		return ref;
	}
	
	@Override
	public OpaqueData getServiceDescription() {
		return ModelUtils.createOpaqueData(buildDescription);
	}

	@Override
	public String getServiceName() {
		return this.getClass().getSimpleName() + " - " + buildName;
	}

	@Override
	public List<DocumentedNamespaceReference> getKnownNamespaceList() {
		return null;
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
}
