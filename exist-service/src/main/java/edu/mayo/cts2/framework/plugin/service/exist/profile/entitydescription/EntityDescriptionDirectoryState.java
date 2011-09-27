package edu.mayo.cts2.framework.plugin.service.exist.profile.entitydescription;

import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder.XpathState;

public class EntityDescriptionDirectoryState extends XpathState {

	private String codeSystemVersion;
	
	public String getCodeSystemVersion() {
		return codeSystemVersion;
	}
	public void setCodeSystemVersion(String codeSystemVersion) {
		this.codeSystemVersion = codeSystemVersion;
	}	
}