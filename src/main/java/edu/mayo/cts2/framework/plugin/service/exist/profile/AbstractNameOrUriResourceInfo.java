package edu.mayo.cts2.framework.plugin.service.exist.profile;

import org.apache.commons.lang.StringUtils;

import edu.mayo.cts2.framework.model.service.core.NameOrURI;

public abstract class AbstractNameOrUriResourceInfo<R,I extends NameOrURI> 
	implements DefaultResourceInfo<R,I> {

	@Override
	public boolean isReadByUri(I id) {
		return StringUtils.isNotBlank(id.getUri());
	}

	@Override
	public String getExistResourceName(I id) {
		return id.getName();
	}

	@Override
	public String getResourceUri(I id) {
		return id.getUri();
	}
}
