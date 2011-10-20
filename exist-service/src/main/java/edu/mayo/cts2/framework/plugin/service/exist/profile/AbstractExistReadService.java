package edu.mayo.cts2.framework.plugin.service.exist.profile;

import edu.mayo.cts2.framework.model.service.core.BaseReadService;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistDao;
import edu.mayo.cts2.framework.service.profile.ReadService;

public abstract class AbstractExistReadService<R,I,T extends BaseReadService > extends AbstractExistService<T> 
	implements ReadService<R,I> {

	@Override
	public R read(I resourceIdentifier) {
		if(! this.isReadByUri(resourceIdentifier)){
			return this.getExistDao().getResource(this.createPath(resourceIdentifier),
					this.getResourceName(resourceIdentifier));
		} else {
			return this.getExistDao().getResourceByUri(this.createPath(resourceIdentifier), 
					this.getResourceUri(resourceIdentifier));
		}
	}
	
	protected abstract boolean isReadByUri(I identifier);

	@Override
	public boolean exists(I identifier) {
		throw new UnsupportedOperationException();
	}

	protected abstract String createPath(I resourceIdentifier);
	
	protected abstract String getResourceName(I resourceIdentifier);
	
	protected abstract String getResourceUri(I resourceIdentifier);

	protected abstract ExistDao<?,R> getExistDao();

}
