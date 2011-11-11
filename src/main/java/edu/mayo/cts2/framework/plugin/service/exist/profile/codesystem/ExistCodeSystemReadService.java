package edu.mayo.cts2.framework.plugin.service.exist.profile.codesystem;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference;
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistReadService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.ResourceInfo;
import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemReadService;

@Component
public class ExistCodeSystemReadService
		extends
		AbstractExistReadService<CodeSystemCatalogEntry, NameOrURI, edu.mayo.cts2.framework.model.service.codesystem.CodeSystemReadService>
		implements CodeSystemReadService {

	@Resource
	private CodeSystemResourceInfo codeSystemResourceInfo;

	public CodeSystemCatalogEntry read(NameOrURI resourceIdentifier,
			ResolvedReadContext readContext) {
		CodeSystemCatalogEntry resource = super.read(resourceIdentifier,
				readContext);
		
		if(resource == null){
			return null;
		}
		
		resource.setVersions(this.getUrlConstructor().createVersionsOfCodeSystemUrl(resource.getCodeSystemName()));

		CodeSystemVersionReference currentVersion = resource
				.getCurrentVersion();

		if (currentVersion != null) {
			NameAndMeaningReference version = currentVersion.getVersion();
			if (version != null) {
				String versionName = version.getContent();
				if (StringUtils.isNotBlank(versionName)) {
					if (StringUtils.isBlank(version.getHref())) {
						version.setHref(this.getUrlConstructor()
								.createCodeSystemVersionUrl(
										resource.getCodeSystemName(),
										versionName));
					}
				}
			}
		}

		return resource;
	}

	@Override
	protected ResourceInfo<CodeSystemCatalogEntry, NameOrURI> getResourceInfo() {
		return this.codeSystemResourceInfo;
	}
}
