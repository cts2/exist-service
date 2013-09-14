package edu.mayo.cts2.framework.plugin.service.exist.profile.resolvedvalueset;

import static org.junit.Assert.*

import javax.annotation.Resource

import org.junit.Test

import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference
import edu.mayo.cts2.framework.model.core.ValueSetDefinitionReference
import edu.mayo.cts2.framework.model.core.ValueSetReference
import edu.mayo.cts2.framework.model.valuesetdefinition.ResolvedValueSet
import edu.mayo.cts2.framework.model.valuesetdefinition.ResolvedValueSetHeader
import edu.mayo.cts2.framework.plugin.service.exist.profile.BaseServiceDbCleaningBase
import edu.mayo.cts2.framework.service.profile.resolvedvalueset.ResolvedValueSetQuery
import edu.mayo.cts2.framework.service.profile.resolvedvalueset.ResolvedValueSetReference

class ExistResolvedValueSetQueryServiceIT extends BaseServiceDbCleaningBase {
	
	@Resource
	ExistResolvedValueSetQueryService query
	
	@Resource
	ExistResolvedValueSetLoaderService load
	
	@Test
	void testQuery(){
		def rvs = new ResolvedValueSet(
			resolutionInfo: new ResolvedValueSetHeader(
				resolutionOf: new ValueSetDefinitionReference(
					valueSetDefinition: new NameAndMeaningReference(content:"vsd"),
					valueSet: new ValueSetReference(content:"vs")
					)));
		
		ResolvedValueSetReference ref = load.load(rvs)
		
		def q = [
			getFilterComponent : { },
			getResolvedValueSetQueryServiceRestrictions : { },
			getQuery : { }
		] as ResolvedValueSetQuery
		
		def summaries = 
			query.getResourceSummaries(q, null, new Page())
			
		assertEquals 1, summaries.getEntries().size()

	}	
	
	@Test
	void testQueryHref(){
		def rvs = new ResolvedValueSet(
			resolutionInfo: new ResolvedValueSetHeader(
				resolutionOf: new ValueSetDefinitionReference(
					valueSetDefinition: new NameAndMeaningReference(content:"vsd"),
					valueSet: new ValueSetReference(content:"vs")
					)));
		
		ResolvedValueSetReference ref = load.load(rvs)
		
		def q = [
			getFilterComponent : { },
			getResolvedValueSetQueryServiceRestrictions : { },
			getQuery : { }
		] as ResolvedValueSetQuery
		
		def summaries =
			query.getResourceSummaries(q,null, new Page())
		
		assertNotNull summaries.getEntries().get(0).getHref()
		
		assertEquals "http://localhost:8080/webapp/valueset/vs/definition/vsd/resolution/" + ref.getLocalID(), summaries.getEntries().get(0).getHref()
	}

}
