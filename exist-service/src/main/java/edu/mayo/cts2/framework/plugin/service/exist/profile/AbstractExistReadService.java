package edu.mayo.cts2.framework.plugin.service.exist.profile;

import edu.mayo.cts2.framework.model.service.core.BaseReadService;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistDao;
import edu.mayo.cts2.framework.service.name.ResourceIdentifier;
import edu.mayo.cts2.framework.service.profile.UriResolvable;

public abstract class AbstractExistReadService<R,I extends ResourceIdentifier<?>, T extends BaseReadService > extends AbstractExistService<T> 
	implements UriResolvable<R,I> {

	@Override
	public R readByUri(String uri) {
		return this.getExistDao().getResourceByUri(uri);
	}

	protected abstract ExistDao<?,R> getExistDao();

}
