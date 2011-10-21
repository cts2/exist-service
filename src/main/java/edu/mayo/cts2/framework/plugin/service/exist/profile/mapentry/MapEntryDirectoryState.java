package edu.mayo.cts2.framework.plugin.service.exist.profile.mapentry;

import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder.XpathState;

public class MapEntryDirectoryState extends XpathState {
	
	private String mapVersion;

	public String getMapVersion() {
		return mapVersion;
	}

	public void setMapVersion(String mapVersion) {
		this.mapVersion = mapVersion;
	}
}
