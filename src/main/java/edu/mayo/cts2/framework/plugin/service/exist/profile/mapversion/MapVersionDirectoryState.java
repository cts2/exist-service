package edu.mayo.cts2.framework.plugin.service.exist.profile.mapversion;

import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder.XpathState;

public class MapVersionDirectoryState extends XpathState {
	
	private String map;
	
	public String getMap() {
		return map;
	}
	public void setMap(String map) {
		this.map = map;
	}
}