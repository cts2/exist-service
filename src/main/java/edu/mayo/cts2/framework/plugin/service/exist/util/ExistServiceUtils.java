package edu.mayo.cts2.framework.plugin.service.exist.util;

import org.apache.commons.lang.StringUtils;

import edu.mayo.cts2.framework.model.core.ScopedEntityName;
import edu.mayo.cts2.framework.model.entity.EntityDescriptionBase;

public class ExistServiceUtils {
		
	private static final String EXTERNAL_ENTITY_NAME_SEPERATOR = ":";

	private static final String EXIST_ENTITY_NAME_SEPERATOR = "__";
	
	public static final String XML_SUFFIX = ".xml";

	public static String getExistEntityName(ScopedEntityName name) {
		return name.getNamespace() + EXIST_ENTITY_NAME_SEPERATOR
				+ name.getName();
	}
	
	public static String getTempChangeSetContentDirName(String changeSetUri){
		return "tmpChangeSet-" + uriToExistName(changeSetUri);
	}
	
	public String getByUriXpath(String uriPath, String uri) {
		String expressionString = 
				 "[" + uriPath + "='" + uri + "']";
	
		return expressionString;
	}
	
	public static String createPath(String... path){
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
	public static String getExistEntityName(EntityDescriptionBase entity) {
		return entity.getEntityID().getNamespace() + EXIST_ENTITY_NAME_SEPERATOR
				+ entity.getEntityID().getName();
	}
	
	public static String getExistResourceName(String externalName){
		return externalName.replaceAll(":", "__");
	}
	
	public static String getExternalName(String existName){
		return existName.replaceAll("__", ":");
	}
	
	public static String getNameFromResourceName(String existName){
		return StringUtils.substringBefore(existName, XML_SUFFIX);
	}
	
	public static String getExternalEntityName(ScopedEntityName name, String codeSystemName) {
		if(StringUtils.equals(codeSystemName, name.getNamespace())){
			return name.getName();
		} else {
			return name.getNamespace() + EXTERNAL_ENTITY_NAME_SEPERATOR
					+ name.getName();
		}
	}

	public static String uriToExistName(String id) {
		return Integer.toString(id.hashCode()).replaceFirst("-", "NEG");
	}
}
