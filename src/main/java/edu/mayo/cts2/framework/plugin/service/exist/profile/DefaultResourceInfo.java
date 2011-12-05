package edu.mayo.cts2.framework.plugin.service.exist.profile;



public interface DefaultResourceInfo<R,I> extends ResourceInfo<I> {

	public String createPathFromResource(R resource);

	public String getExistResourceNameFromResource(R resource);

}
