package edu.mayo.cts2.framework.plugin.service.exist.profile;

import edu.mayo.cts2.framework.model.core.IsChangeable;
import edu.mayo.cts2.framework.model.service.core.BaseReadService;

public abstract class AbstractExistDefaultReadService<
	R extends IsChangeable,
	I,
	T extends BaseReadService> 
	extends AbstractExistReadService<R,R,I,T> {



}
