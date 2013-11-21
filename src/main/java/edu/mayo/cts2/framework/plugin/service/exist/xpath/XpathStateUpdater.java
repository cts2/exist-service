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

import org.apache.commons.lang.StringUtils;
import edu.mayo.cts2.framework.filter.match.StateAdjustingComponentReference.StateUpdater;
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference;
import edu.mayo.cts2.framework.model.exception.UnspecifiedCts2Exception;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistQueryService;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder.XpathState;
import edu.mayo.cts2.framework.service.meta.StandardMatchAlgorithmReference;

/**
 * The Class XpathStateUpdater.
 *
 * @param <T> the generic type
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class XpathStateUpdater<T extends XpathState> implements StateUpdater<T> {

	private String queryPath;
	
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
			){
		super();
		this.queryPath = queryPath;
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
			
			if (this.queryPath.indexOf("@") >= 0)
			{
				//If it is an attribute search, we can simply do a contains search, as this will hit our attribute range index - 
				//which is likely (but untested) faster than a lucene leading wildcard search (which is known to be slow)
				sb.append("[fn:contains(" + this.queryPath  + ", '" + queryString + "')]");
			}
			else
			{
				//Otherwise, do a lucene leading-wildcard search
				//There are bugs in existdb - this is the only way to get it to parse the lucene query properly.
				sb.append("let $query := <query><wildcard>*" + queryString.toLowerCase() + "*</wildcard></query>\r\n");
				sb.append("return\r\n");
				//insert a comment for later processing... if there is an additional path, it should be placed here (instead of prefixed)
				sb.append(AbstractExistQueryService.REPLACE_INDICATOR);  
				sb.append("[ft:query(" + this.queryPath  + ", $query)]");
			}
		} else if(matchAlgorithm.equals(
				StandardMatchAlgorithmReference.
					STARTS_WITH.getMatchAlgorithmReference())){
			sb.append("[ft:query(" + this.queryPath  + ", '" + queryString.toLowerCase() + "*')]");
		} else if(matchAlgorithm.getContent().equals("lucene")){
			sb.append("[ft:query(" + this.queryPath  + ", '" + queryString.toLowerCase() + "')]");
		} else if(matchAlgorithm.equals(
				StandardMatchAlgorithmReference.
					EXACT_MATCH.getMatchAlgorithmReference())){
			sb.append("[" + this.queryPath  + " = '" + queryString + "']");
		} 
		else {
			throw new RuntimeException("Unsupported match algorithm '" + matchAlgorithm.toString() + "'"); 
		}
		
		//TODO - Dan notes that this implementation is actually quite broken per the current APIs, because the APIs would expect to call updateState
		//numerous times (once for each filter) but this implementation throws away any pre-existing state - and replaces it with the state for this 
		//exact query.  Which won't work if there is ever more than one restriction.  Another place this pops up is in the ExistValueSetDefinitionQueryService
		//which also needs to set up a state here.
		
		if (StringUtils.isNotBlank(currentState.getXpath()))
		{
			throw new UnspecifiedCts2Exception("Calling updateState multiple times is currently not supported");
		}
		currentState.setXpath(sb.toString());
		
		return currentState;
	}
}
