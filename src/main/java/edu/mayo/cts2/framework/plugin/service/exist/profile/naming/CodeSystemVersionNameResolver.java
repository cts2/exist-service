package edu.mayo.cts2.framework.plugin.service.exist.profile.naming;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.plugin.service.exist.dao.CodeSystemVersionExistDao;

@Component
public class CodeSystemVersionNameResolver {
	
	@Resource
	private CodeSystemVersionExistDao codeSystemVersionExistDao;
	
}
