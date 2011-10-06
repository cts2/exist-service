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

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.mayo.cts2.framework.service.provider.AbstractSpringServiceProvider;

/**
 * The Class BioportalServiceProvider.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ExistServiceProvider extends AbstractSpringServiceProvider {

	/* (non-Javadoc)
	 * @see org.cts2.rest.service.provider.AbstractSpringServiceProvider#getApplicationContext()
	 */
	@Override
	protected ApplicationContext getApplicationContext(ApplicationContext parent) {
		ClassPathXmlApplicationContext ctx = 
				new ClassPathXmlApplicationContext(new String[]{"exist-context.xml"}, parent);

		return ctx;
	}

	@Override
	protected ApplicationContext getIntegrationTestApplicationContext(ApplicationContext parent) {
		ClassPathXmlApplicationContext ctx = 
				new ClassPathXmlApplicationContext(new String[]{"exist-validation-test-context.xml"}, parent);
		
		return ctx;
	}
}
