package edu.mayo.cts2.framework.plugin.service.exist.profile.conceptdomainbinding;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.conceptdomainbinding.ConceptDomainBinding;
import edu.mayo.cts2.framework.model.core.ChangeableElementGroup;
import edu.mayo.cts2.framework.model.core.VersionTagReference;
import edu.mayo.cts2.framework.model.extension.LocalIdConceptDomainBinding;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistLocalIdReadService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.LocalIdResourceInfo;
import edu.mayo.cts2.framework.service.profile.conceptdomainbinding.ConceptDomainBindingReadService;
import edu.mayo.cts2.framework.service.profile.conceptdomainbinding.name.ConceptDomainBindingReadId;

@Component
public class ExistConceptDomainBindingReadService 
	extends AbstractExistLocalIdReadService<
		ConceptDomainBinding,
		LocalIdConceptDomainBinding,
		ConceptDomainBindingReadId,
		edu.mayo.cts2.framework.model.service.conceptdomainbinding.ConceptDomainBindingReadService> 
	implements ConceptDomainBindingReadService {

	@Resource
	private ConceptDomainBindingResourceInfo conceptDomainBindingResourceInfo;


	@Override
	protected LocalIdResourceInfo<LocalIdConceptDomainBinding, ConceptDomainBindingReadId> getResourceInfo() {
		return this.conceptDomainBindingResourceInfo;
	}

	@Override
	protected ChangeableElementGroup getChangeableElementGroup(
			LocalIdConceptDomainBinding resource) {
		return resource.getResource().getChangeableElementGroup();
	}

	@Override
	protected LocalIdConceptDomainBinding createLocalIdResource(String id,
			ConceptDomainBinding resource) {
		return new LocalIdConceptDomainBinding(id, resource);
	}

	@Override
	public boolean exists(NameOrURI conceptDomain, NameOrURI valueSet,
			NameOrURI applicableContext, NameOrURI bindingQualifier) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ConceptDomainBinding read(NameOrURI conceptDomain,
			NameOrURI valueSet, NameOrURI applicableContext,
			NameOrURI bindingQualifier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VersionTagReference> getSupportedTag() {
		// TODO Auto-generated method stub
		return null;
	}
}
