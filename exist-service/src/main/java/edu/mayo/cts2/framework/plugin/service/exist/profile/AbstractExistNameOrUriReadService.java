package edu.mayo.cts2.framework.plugin.service.exist.profile;

import org.apache.commons.lang.StringUtils;

import edu.mayo.cts2.framework.model.service.core.BaseReadService;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;
import edu.mayo.cts2.framework.service.profile.ReadService;

public abstract class AbstractExistNameOrUriReadService<
	R,I extends NameOrURI, T extends BaseReadService > 
	extends AbstractExistReadService<R,I,T> 
	implements ReadService<R,I> {

	@Override
	public boolean isReadByUri(I id) {
		return StringUtils.isNotBlank(id.getUri());
	}

	@Override
	protected String getResourceName(I id) {
		return id.getName();
	}

	@Override
	protected String getResourceUri(I id) {
		return id.getUri();
	}


}
