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
package edu.mayo.cts2.framework.plugin.service.exist.restrict.directory;

import edu.mayo.cts2.framework.filter.directory.AbstractStateBuildingDirectoryBuilder;
import edu.mayo.cts2.framework.filter.match.StateAdjustingComponentReference;
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder.XpathState;

import java.util.Set;

/**
 * The Class EntityDirectoryBuilder.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class XpathDirectoryBuilder<S extends XpathState, T> extends
		AbstractStateBuildingDirectoryBuilder<S, T> {

	private static final String EMPTY_STRING = "";

	/**
	 * Instantiates a new entity directory builder.
	 * 
	 * @param callback
	 *            the callback
	 */
	@Deprecated
	public XpathDirectoryBuilder(S state, Callback<S, T> callback) {
		super(state, callback);
	}

	/**
	 * Instantiates a new entity directory builder.
	 * 
	 * @param callback
	 *            the callback
	 * @param matchAlgorithmReferences
	 *            the match algorithm references
	 */
	public XpathDirectoryBuilder(
			S state,
			Callback<S, T> callback,
			Set<MatchAlgorithmReference> matchAlgorithmReferences,
			Set<StateAdjustingComponentReference<S>> modelAttributeReferences) {
		super(state, 
				callback,
				matchAlgorithmReferences,
				modelAttributeReferences);
	}

	public static class XpathState {

		private String xpath = EMPTY_STRING;

		public String getXpath() {
			return xpath;
		}

		public void setXpath(String xpath) {
			this.xpath = xpath;
		}
	}
}
