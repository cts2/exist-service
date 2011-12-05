package edu.mayo.cts2.framework.plugin.service.exist.profile;


public interface ResourceInfo<I> extends PathInfo {

	public boolean isReadByUri(I identifier);

	public String createPath(I resourceIdentifier);

	public String getResourceUri(I resourceIdentifier);
	
	public String getExistResourceName(I resourceIdentifier);

}
