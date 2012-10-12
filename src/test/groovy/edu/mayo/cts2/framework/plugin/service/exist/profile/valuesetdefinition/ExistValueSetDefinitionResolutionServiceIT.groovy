package edu.mayo.cts2.framework.plugin.service.exist.profile.valuesetdefinition;

import static org.junit.Assert.*

import javax.annotation.Resource

import org.junit.Test

import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference
import edu.mayo.cts2.framework.model.core.ValueSetDefinitionReference
import edu.mayo.cts2.framework.model.core.ValueSetReference
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.model.valuesetdefinition.ResolvedValueSet
import edu.mayo.cts2.framework.model.valuesetdefinition.ResolvedValueSetHeader
import edu.mayo.cts2.framework.plugin.service.exist.profile.BaseServiceDbCleaningBase
import edu.mayo.cts2.framework.plugin.service.exist.profile.resolvedvalueset.ExistResolvedValueSetLoaderService
import edu.mayo.cts2.framework.service.profile.resolvedvalueset.ResolvedValueSetReference
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.name.ValueSetDefinitionReadId

class ExistValueSetDefinitionResolutionServiceIT extends BaseServiceDbCleaningBase {
	
	@Resource
	ExistValueSetDefinitionResolutionService resolve
	
	@Resource
	ExistResolvedValueSetLoaderService load
	
	@Test
	void testResolve(){
		def rvs = new ResolvedValueSet(
			resolutionInfo: new ResolvedValueSetHeader(
				resolutionOf: new ValueSetDefinitionReference(
					valueSetDefinition: new NameAndMeaningReference(content:"vsd"),
					valueSet: new ValueSetReference(content:"vs")
					)));
		
		ResolvedValueSetReference ref = load.load(rvs)
		
		def valueSet = ModelUtils.nameOrUriFromName("vs");

		def returned = 
			resolve.resolveDefinition(
				new ValueSetDefinitionReadId(
					"vsd",
					valueSet),
				null,null,null,null,null,new Page()
				)
		
		assertNotNull returned
	}
	
}
