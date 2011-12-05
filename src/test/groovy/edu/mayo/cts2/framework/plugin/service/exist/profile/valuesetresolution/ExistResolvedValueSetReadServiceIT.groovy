package edu.mayo.cts2.framework.plugin.service.exist.profile.valuesetresolution;

import static org.junit.Assert.*

import javax.annotation.Resource

import org.junit.Test

import edu.mayo.cts2.framework.model.core.NameAndMeaningReference
import edu.mayo.cts2.framework.model.core.ValueSetDefinitionReference
import edu.mayo.cts2.framework.model.core.ValueSetReference
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.model.valuesetdefinition.ResolvedValueSet
import edu.mayo.cts2.framework.model.valuesetdefinition.ResolvedValueSetHeader
import edu.mayo.cts2.framework.plugin.service.exist.profile.BaseServiceDbCleaningBase
import edu.mayo.cts2.framework.service.profile.valuesetresolution.ResolvedValueSetReference
import edu.mayo.cts2.framework.service.profile.valuesetresolution.name.ResolvedValueSetReadId

class ExistResolvedValueSetReadServiceIT extends BaseServiceDbCleaningBase {
	
	@Resource
	ExistResolvedValueSetReadService read
	
	@Resource
	ExistResolvedValueSetLoaderService load
	
	@Test
	void testLoadAndReadByLocalId(){
		def rvs = new ResolvedValueSet(
			resolutionInfo: new ResolvedValueSetHeader(
				resolutionOf: new ValueSetDefinitionReference(
					valueSetDefinition: new NameAndMeaningReference(content:"vsd"),
					valueSet: new ValueSetReference(content:"vs")
					)));
		
		ResolvedValueSetReference ref = load.load(rvs)
		
		def valueSetDefinition = ModelUtils.nameOrUriFromName(
			ref.getValueSetDefinitionReference().
				getValueSetDefinition().
					getContent());
				
		def valueSet = ModelUtils.nameOrUriFromName(
					ref.getValueSetDefinitionReference().
						getValueSet().
							getContent())
		def returned = 
			read.read(
				new ResolvedValueSetReadId(
					ref.getLocalID(),
					valueSet,
					valueSetDefinition)
				)
		
		assertNotNull returned
	}
	
	@Test
	void testDelete(){
		def rvs = new ResolvedValueSet(
			resolutionInfo: new ResolvedValueSetHeader(
				resolutionOf: new ValueSetDefinitionReference(
					valueSetDefinition: new NameAndMeaningReference(content:"vsd"),
					valueSet: new ValueSetReference(content:"vs")
					)));
		
		ResolvedValueSetReference ref = load.load(rvs)
		
		def valueSetDefinition = ModelUtils.nameOrUriFromName(
			ref.getValueSetDefinitionReference().
				getValueSetDefinition().
					getContent());
				
		def valueSet = ModelUtils.nameOrUriFromName(
					ref.getValueSetDefinitionReference().
						getValueSet().
							getContent())
		
		def id = new ResolvedValueSetReadId(
			ref.getLocalID(),
			valueSet,
			valueSetDefinition);
		
		def returned =
			read.read(id)
		
		assertNotNull returned
		
		this.load.delete(id)
		
		returned = read.read(id)
		
		assertNull returned
	}

}
