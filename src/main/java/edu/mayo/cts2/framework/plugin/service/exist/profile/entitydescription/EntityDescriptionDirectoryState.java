package edu.mayo.cts2.framework.plugin.service.exist.profile.entitydescription;

import java.util.Set;

import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder.XpathState;

public class EntityDescriptionDirectoryState extends XpathState {

	private Set<NameOrURI> codeSystemVersions;

	public Set<NameOrURI> getCodeSystemVersions() {
		return codeSystemVersions;
	}

	public void setCodeSystemVersions(Set<NameOrURI> codeSystemVersions) {
		this.codeSystemVersions = codeSystemVersions;
	}

}