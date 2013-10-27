package edu.mayo.cts2.framework.plugin.service.exist.profile.valuesetdefinition;

import static org.junit.Assert.*
import javax.annotation.Resource
import org.junit.Ignore;
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

@Ignore  //These tests don't make sense when the full ValueSetDefinitionResolution code is in place - it would only work with the previous hack code 
//that fell through to the storedValueSetResolution APIs
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
				null,null,null,null,new Page()
				)
		
		assertNotNull returned
	}
	
	@Test
	void testResolveWithTwo(){
		def rvs1 = new ResolvedValueSet(
			resolutionInfo: new ResolvedValueSetHeader(
				resolutionOf: new ValueSetDefinitionReference(
					valueSetDefinition: new NameAndMeaningReference(content:"vsd"),
					valueSet: new ValueSetReference(content:"vs")
					)));
		
		def rvs2 = new ResolvedValueSet(
			resolutionInfo: new ResolvedValueSetHeader(
				resolutionOf: new ValueSetDefinitionReference(
					valueSetDefinition: new NameAndMeaningReference(content:"vsd2"),
					valueSet: new ValueSetReference(content:"vs2")
					)));
		
		load.load(rvs1)
		load.load(rvs2)
		
		def valueSet = ModelUtils.nameOrUriFromName("vs");

		def returned =
			resolve.resolveDefinition(
				new ValueSetDefinitionReadId(
					"vsd",
					valueSet),
				null,null,null,null,new Page()
				)
		
		assertNotNull returned
	}
	
}
