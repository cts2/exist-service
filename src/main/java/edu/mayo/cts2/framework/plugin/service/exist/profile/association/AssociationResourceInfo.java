package edu.mayo.cts2.framework.plugin.service.exist.profile.association;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry;
import edu.mayo.cts2.framework.model.extension.LocalIdAssociation;
import edu.mayo.cts2.framework.model.service.exception.UnknownCodeSystemVersion;
import edu.mayo.cts2.framework.plugin.service.exist.profile.LocalIdResourceInfo;
import edu.mayo.cts2.framework.plugin.service.exist.profile.codesystemversion.ExistCodeSystemVersionReadService;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;
import edu.mayo.cts2.framework.plugin.service.valueSetDefinitionResolutionServices.ctsUtility.queryBuilders.CodeSystemVersionQueryBuilder;
import edu.mayo.cts2.framework.service.profile.association.name.AssociationReadId;
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionQuery;

@Component
public class AssociationResourceInfo implements LocalIdResourceInfo<LocalIdAssociation,AssociationReadId> {

	private static final String ASSOCIATIONS_PATH = "/associations";
	
	@Autowired
	ExistCodeSystemVersionReadService ecsvrs;

	@Override
	public String getResourceBasePath(){
		return ASSOCIATIONS_PATH;	
	}

	@Override
	public String getResourceXpath() {
		return "/association:Association";
	}
	
	@Override
	public boolean isReadByUri(AssociationReadId identifier) {
		return !(identifier.getUri() == null);
	}

	@Override
	public String createPath(AssociationReadId id) {
		if (id.getCodeSystemVersion() != null)
		{
			if (StringUtils.isNotEmpty(id.getCodeSystemVersion().getName()))
			{
				return ExistServiceUtils.createPath(id.getCodeSystemVersion().getName());
			}
			else if (StringUtils.isNotEmpty(id.getCodeSystemVersion().getUri()))
			{
				//TODO move this builder to some proper shared code... (other buiders too, probably)
				CodeSystemVersionQuery query = CodeSystemVersionQueryBuilder.build(null);
				query.getRestrictions().setCodeSystem(id.getCodeSystemVersion());
				CodeSystemVersionCatalogEntry result = ecsvrs.read(id.getCodeSystemVersion(), null);
				
				if (result != null)
				{
					return ExistServiceUtils.createPath(result.getCodeSystemVersionName());
				}
				else
				{
					throw new UnknownCodeSystemVersion();
				}
			}
		}
		return "";
	}

	@Override
	public String createPathFromResource(LocalIdAssociation resource) {
		return ExistServiceUtils.createPath(
				resource.getResource().getAssertedBy().getVersion().getContent());
	}

	@Override
	public String getExistResourceName(AssociationReadId id) {
		return id.getName();
	}

	@Override
	public String getResourceUri(AssociationReadId id) {
		return id.getUri();
	}
	
	@Override
	public String getUriXpath() {
		return "@associationID";
	}

	@Override
	public String getResourceNameXpath() {
		return "@localID";
	}
	
}
