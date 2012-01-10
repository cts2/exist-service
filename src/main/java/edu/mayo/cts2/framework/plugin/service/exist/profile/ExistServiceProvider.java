/*
a * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
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

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.core.config.option.Option;
import edu.mayo.cts2.framework.core.config.option.StringOption;
import edu.mayo.cts2.framework.plugin.service.exist.ExistServiceConstants;
import edu.mayo.cts2.framework.util.spring.AbstractSpringServiceProvider;

/**
 * The Class BioportalServiceProvider.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Component("existServiceProvider")
public class ExistServiceProvider extends AbstractSpringServiceProvider {

	
	public Set<Option> getPluginOptions() {
		Option existHome = new StringOption(ExistServiceConstants.EXIST_HOME_PROP, "");
		Option password = new StringOption(ExistServiceConstants.PASSWORD_PROP, "");
		Option url = new StringOption(ExistServiceConstants.URL_PROP, "");
		Option userName = new StringOption(ExistServiceConstants.USER_NAME_PROP, "");
		
		Set<Option> options = new HashSet<Option>();
		
		options.add(existHome);
		options.add(password);
		options.add(url);
		options.add(userName);
		
		return options;
	}
}
