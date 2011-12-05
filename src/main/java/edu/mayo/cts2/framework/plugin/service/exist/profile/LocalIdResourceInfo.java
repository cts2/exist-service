package edu.mayo.cts2.framework.plugin.service.exist.profile;



public interface LocalIdResourceInfo<R,I> extends ResourceInfo<I> {

	public String createPathFromResource(R resource);

}
