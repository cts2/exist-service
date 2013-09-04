package edu.mayo.cts2.framework.plugin.service.exist.util;

import edu.mayo.cts2.framework.model.core.ScopedEntityName;
import edu.mayo.cts2.framework.model.core.URIAndEntityName;
import edu.mayo.cts2.framework.model.entity.EntityDescriptionBase;
import org.apache.commons.lang.StringUtils;

public class ExistServiceUtils {
		
	private static final String EXTERNAL_ENTITY_NAME_SEPERATOR = ":";

	private static final String EXIST_ENTITY_NAME_SEPERATOR = "__";
	
	public static final String XML_SUFFIX = ".xml";

    public static String getExistEntityName(EntityDescriptionBase entity) {
        return getExistEntityName(entity.getEntityID());
    }

	public static String getExistEntityName(URIAndEntityName name) {
        return getExistEntityName(name.getNamespace(), name.getName());
    }
	
	public static String getExistEntityName(ScopedEntityName name) {
		return getExistEntityName(name.getNamespace(), name.getName());
	}

    private static String getExistEntityName(String namespace, String name) {
        String encodedName = getExistResourceName(namespace + EXIST_ENTITY_NAME_SEPERATOR + name);

        return encodedName + "_" + Integer.toHexString(encodedName.hashCode());
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
		
		return sb.toString();
	}
	
	public static String getExistResourceName(String externalName){
		String name = externalName.replaceAll("[^a-zA-Z0-9.\\-/]", "_");

        return name;
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
