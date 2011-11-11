package edu.mayo.cts2.framework.plugin.service.exist.profile;

public interface ResourceInfo<R,I> {

	public boolean isReadByUri(I identifier);

	public String createPath(I resourceIdentifier);
	
	public String createPathFromResource(R resource);
	
	public String getExistResourceName(I resourceIdentifier);
	
	public String getExistResourceNameFromResource(R resource);
	
	public String getResourceUri(I resourceIdentifier);
	
	public String getUriXpath();
	
	public String getResourceNameXpath();
	
	public String getResourceXpath();
	
	public String getResourceBasePath();

}
