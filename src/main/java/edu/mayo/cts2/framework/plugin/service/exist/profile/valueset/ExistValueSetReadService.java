package edu.mayo.cts2.framework.plugin.service.exist.profile.valueset;

import java.util.Set;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference;
import edu.mayo.cts2.framework.model.core.ValueSetDefinitionReference;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntry;
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinitionDirectoryEntry;
import edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistDefaultReadService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.DefaultResourceInfo;
import edu.mayo.cts2.framework.plugin.service.exist.profile.valuesetdefinition.ExistValueSetDefinitionQueryService;
import edu.mayo.cts2.framework.service.command.restriction.ValueSetDefinitionQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.valueset.ValueSetReadService;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionQuery;

@Component
public class ExistValueSetReadService 
	extends AbstractExistDefaultReadService<
	ValueSetCatalogEntry,
	NameOrURI,
	edu.mayo.cts2.framework.model.service.valueset.ValueSetReadService>
	implements ValueSetReadService {
	
	@Resource
	private ValueSetResourceInfo valueSetResourceInfo;
	
	@Resource
	private ExistValueSetDefinitionQueryService valueSetDefinitionQueryService;

	@Override
	protected DefaultResourceInfo<ValueSetCatalogEntry, NameOrURI> getResourceInfo() {
		return this.valueSetResourceInfo;
	}

	@Override
	protected ValueSetCatalogEntry doRead(edu.mayo.cts2.framework.plugin.service.exist.profile.AbstractExistReadService.GetResourceCallback callback,
			final ResolvedReadContext readContext)
	{
		final ValueSetCatalogEntry vsce = super.doRead(callback, readContext);
		if (vsce != null)
		{
			//TODO - would there ever be a case where we wanted to leave this as the value that currently exists?  I think this is supposed to be 
			//server / service specific - but Kevin thought that there may be a case where a user wants to upload a ValueSet with definitions hosted on 
			// an external server.  This code sets the <definitions> value to the local URI, and sets the <currentDefinition" to an arbitrary, local definition.
			vsce.setDefinitions(getUrlConstructor().createValueSetUrl(vsce.getValueSetName() + "/definitions"));
	
			Page p = new Page();
			p.setMaxToReturn(1);
			ValueSetDefinitionQuery q = new ValueSetDefinitionQuery()
			{
				//TODO we need to filter for the CURRENT tag - but those don't exist in this service yet.
				@Override
				public ResolvedReadContext getReadContext()
				{
					return readContext;
				}
				
				@Override
				public Query getQuery()
				{
					return null;
				}
				
				@Override
				public Set<ResolvedFilter> getFilterComponent()
				{
					return null;
				}
				
				@Override
				public ValueSetDefinitionQueryServiceRestrictions getRestrictions()
				{
					ValueSetDefinitionQueryServiceRestrictions restrictions = new ValueSetDefinitionQueryServiceRestrictions();
					restrictions.setValueSet(ModelUtils.nameOrUriFromUri(vsce.getAbout()));
					return restrictions;
				}
			};
			DirectoryResult<ValueSetDefinitionDirectoryEntry> currentDef = valueSetDefinitionQueryService.getResourceSummaries(q, null, p);
			if (currentDef != null && currentDef.getEntries().size() > 0)
			{
				NameAndMeaningReference namr = new NameAndMeaningReference(currentDef.getEntries().get(0).getFormalName());
				namr.setHref(currentDef.getEntries().get(0).getHref());
				namr.setUri(currentDef.getEntries().get(0).getAbout());
				
				ValueSetDefinitionReference vsdr = vsce.getCurrentDefinition();
				if (vsdr == null)
				{
					vsdr = new ValueSetDefinitionReference();
					vsce.setCurrentDefinition(vsdr);
				}
				vsdr.setValueSetDefinition(namr);
			}
		}
		
		return vsce;
	}
}
