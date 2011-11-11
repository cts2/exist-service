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
package edu.mayo.cts2.framework.plugin.service.exist.xpath;

import edu.mayo.cts2.framework.filter.match.StateAdjustingModelAttributeReference.StateUpdater;
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder.XpathState;
import edu.mayo.cts2.framework.service.meta.StandardMatchAlgorithmReference;

/**
 * The Class XpathStateUpdater.
 *
 * @param <T> the generic type
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class XpathStateUpdater<T extends XpathState> implements StateUpdater<T> {

	//private String elementPath;
	private String queryPath;
	//private String queryAttributeOrText;
	
	/**
	 * Instantiates a new xpath state updater.
	 *
	 * @param elementPath the element path
	 *  Example: /entity:EntityDescription
	 * @param queryPath the query path
	 * 	Example: .//entity:entityID/core:name
	 * @param queryAttributeOrText the query attribute or text
	 *  Example: text()
	 */
	public XpathStateUpdater(
			String queryPath
			//String queryAttributeOrText
			){
		super();
		//this.elementPath = elementPath;
		this.queryPath = queryPath;
		//this.queryAttributeOrText = queryAttributeOrText;
	}
	
	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.filter.match.StateAdjustingModelAttributeReference.StateUpdater#updateState(java.lang.Object, edu.mayo.cts2.framework.model.core.MatchAlgorithmReference, java.lang.String)
	 */
	@Override
	public T updateState(
			T currentState,
			MatchAlgorithmReference matchAlgorithm, 
			String queryString) {
		StringBuffer sb = new StringBuffer();

		if(matchAlgorithm.equals(
				StandardMatchAlgorithmReference.
					CONTAINS.getMatchAlgorithmReference())){
			queryString =  "*"+queryString+"*";
		} else if(matchAlgorithm.equals(
				StandardMatchAlgorithmReference.
					STARTS_WITH.getMatchAlgorithmReference())){
			queryString = queryString+"*";
		} else if(matchAlgorithm.equals(
				StandardMatchAlgorithmReference.
					EXACT_MATCH.getMatchAlgorithmReference())){
			// nop for exact match
		} 
			
		sb.append("["+ this.queryPath +" &= '"+queryString+"']");
		
		currentState.setXpath(sb.toString());
		
		return currentState;
	}
}
