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

import org.apache.commons.lang.StringUtils;

import edu.mayo.cts2.framework.filter.directory.AbstractStateBuildingDirectoryBuilder.StateBuildingRestriction;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder.XpathState;

/**
 * The Class XpathStateUpdater.
 *
 * @param <T> the generic type
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class XpathStateBuildingRestriction<T extends XpathState> implements StateBuildingRestriction<T> {
	
	//TODO removed ALL - ask Kevin.  I don't think it makes any sense here...
	//This only does exact matches, and on one attribute of a document.  There is no way for ALL to ever work, because 
	//uri, for example, can't contain two different URI values in the same document.  
	//There were also no use cases.  Perhaps if we end up with a use case, we would add it back.. but the query would
	//have to be written differently than it was written (simply changing or to and wont work)
	public enum AllOrAny {ANY}

	private String queryPath;
	private String queryAttributeOrPeriod;
	private AllOrAny allOrAny;
	private Iterable<String> restrictions;
	
	/**
	 * Instantiates a new xpath state updater.
	 *
	 * @param elementPath the element path
	 *  Example: /entity:EntityDescription
	 * @param queryPath the query path
	 * 	Example: .//entity:entityID/core:name
	 * @param queryAttributeOrPeriod the query attribute or period (for node)
	 *  Example: text()
	 */
	public XpathStateBuildingRestriction(
			String queryPath,
			String queryAttributeOrPeriod,
			AllOrAny allOrAny,
			Iterable<String> restrictions){
		super();
		this.queryPath = queryPath;
		this.queryAttributeOrPeriod = queryAttributeOrPeriod;
		this.allOrAny = allOrAny;
		this.restrictions = restrictions;
	}
	
	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.filter.match.StateAdjustingModelAttributeReference.StateUpdater#updateState(java.lang.Object, edu.mayo.cts2.framework.model.core.MatchAlgorithmReference, java.lang.String)
	 */
	@Override
	public T restrict(
			T currentState) {
		String algorithm;
		
		switch(this.allOrAny){
//			case ALL : {
//				algorithm = "and";
//				break;
//			}
			case ANY : {
				algorithm = "or";
				break;
			}
			default : throw new IllegalStateException();
		}
		
		StringBuffer sb = new StringBuffer();

		if(StringUtils.isNotBlank(currentState.getXpath())){
			sb.append(currentState.getXpath());
			sb.append(" | ");
		}
		
		sb.append("[" + this.queryPath + "[");
		Iterator<String> itr = this.restrictions.iterator();
		
		while(itr.hasNext()){
			sb.append(this.queryAttributeOrPeriod + " = '" +itr.next()+"'");
			if(itr.hasNext()){
				sb.append(" " + algorithm + " ");
			}
		}
		sb.append("]]");
		
		currentState.setXpath(sb.toString());
		
		return currentState;
	}
}
