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

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder.XpathState;
import edu.mayo.cts2.sdk.filter.directory.AbstractStateBuildingDirectoryBuilder.StateBuildingRestriction;

/**
 * The Class XpathStateUpdater.
 *
 * @param <T> the generic type
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class XpathStateBuildingRestriction<T extends XpathState> implements StateBuildingRestriction<T> {
	
	public enum AllOrAny {ALL, ANY}

	private String elementPath;
	private String queryPath;
	private String queryAttributeOrText;
	private AllOrAny allOrAny;
	private List<String> restrictions;
	
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
	public XpathStateBuildingRestriction(
			String elementPath, 
			String queryPath,
			String queryAttributeOrText,
			AllOrAny allOrAny,
			List<String> restrictions){
		super();
		this.elementPath = elementPath;
		this.queryPath = queryPath;
		this.queryAttributeOrText = queryAttributeOrText;
		this.allOrAny = allOrAny;
		this.restrictions = restrictions;
	}
	
	/* (non-Javadoc)
	 * @see edu.mayo.cts2.sdk.filter.match.StateAdjustingModelAttributeReference.StateUpdater#updateState(java.lang.Object, edu.mayo.cts2.sdk.model.core.MatchAlgorithmReference, java.lang.String)
	 */
	@Override
	public T restrict(
			T currentState) {

		
		StringBuffer queryString = new StringBuffer();
		
		Iterator<String> itr = this.restrictions.iterator();
		
		while(itr.hasNext()){
			queryString.append('\'');
			queryString.append(itr.next());
			queryString.append('\'');
			if(itr.hasNext()){
				queryString.append(',');
			}
		}
		
		String algorithm;
		
		switch(this.allOrAny){
			case ALL : {
				algorithm = "&=";
				break;
			}
			case ANY : {
				algorithm = "|=";
				break;
			}
			default : throw new IllegalStateException();
		}
		
		StringBuffer sb = new StringBuffer();

		if(StringUtils.isNotBlank(currentState.getXpath())){
			sb.append(currentState.getXpath());
			sb.append(" | ");
		}
		
		sb.append(this.elementPath + "["+ this.queryPath + "["+this.queryAttributeOrText+" " + algorithm + " (\""+queryString.toString()+"\")]]");
		
		currentState.setXpath(sb.toString());
		
		return currentState;
	}
}
