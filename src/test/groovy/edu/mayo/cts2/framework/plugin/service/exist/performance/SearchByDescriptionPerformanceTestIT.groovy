package edu.mayo.cts2.framework.plugin.service.exist.performance

import static org.junit.Assert.*

import org.junit.Ignore
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.model.command.ResolvedFilter
import edu.mayo.cts2.framework.model.core.ChangeDescription
import edu.mayo.cts2.framework.model.core.ChangeableElementGroup
import edu.mayo.cts2.framework.model.core.CodeSystemReference
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference
import edu.mayo.cts2.framework.model.core.ModelAttributeReference
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference
import edu.mayo.cts2.framework.model.core.ScopedEntityName
import edu.mayo.cts2.framework.model.core.TsAnyType
import edu.mayo.cts2.framework.model.core.types.ChangeType
import edu.mayo.cts2.framework.model.entity.Designation
import edu.mayo.cts2.framework.model.entity.EntityDescription
import edu.mayo.cts2.framework.model.entity.NamedEntityDescription
import edu.mayo.cts2.framework.model.mapversion.*
import edu.mayo.cts2.framework.plugin.service.exist.profile.BaseServiceDbCleaningBase
import edu.mayo.cts2.framework.plugin.service.exist.profile.entitydescription.ExistEntityDescriptionMaintenanceService
import edu.mayo.cts2.framework.plugin.service.exist.profile.entitydescription.ExistEntityDescriptionQueryService
import edu.mayo.cts2.framework.plugin.service.exist.profile.entitydescription.ExistEntityDescriptionReadService
import edu.mayo.cts2.framework.service.constant.ExternalCts2Constants
import edu.mayo.cts2.framework.service.meta.StandardMatchAlgorithmReference

@Ignore
class SearchByDescriptionPerformanceTestIT extends BaseServiceDbCleaningBase {

	@Autowired
	ExistEntityDescriptionReadService read

	@Autowired
	ExistEntityDescriptionQueryService query

	@Autowired
	ExistEntityDescriptionMaintenanceService maint

	@Test 
	void SearchEntityDescriptionsByName(){
		
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

		def i=0;
		def searchDescription;
		def total = 1000;
		while(i< total){
			
			if(i % 10 == 0){
				println i
			}
			
			def name = generateRandomName()
			def description = generateRandomSentence()
			
			if(i == total/2){
				searchDescription = description
			}
			maint.createResource(createEntity(name,changeSetUri,description))
			i++;
		}

		changeSetService.commitChangeSet(changeSetUri)
		
		def fc = new ResolvedFilter(
				matchAlgorithmReference:StandardMatchAlgorithmReference.CONTAINS.getMatchAlgorithmReference(),
				matchValue:searchDescription,
		        modelAttributeReference: new ModelAttributeReference(content:ExternalCts2Constants.MA_RESOURCE_SYNOPSIS_NAME),
				referenceType:TargetReferenceType.ATTRIBUTE)
		
		def time = System.currentTimeMillis()
		def summaries = query.getResourceSummaries(null, [fc] as Set, null, null, new Page())
		println "Time taken: " + (System.currentTimeMillis() - time)

		assertEquals 1, summaries.entries.size
	}
	
	String generateRandomName(){
		def seed = System.currentTimeMillis() + Runtime.runtime.freeMemory()
		def name = new Random(seed).nextInt()
		
		name
	}
	
	String generateRandomSentence(){
		new Random().nextFloat()
	}

	EntityDescription createEntity(name,changeSetUri,description){
		def entry = new NamedEntityDescription(about:"about")
		entry.setEntityID(new ScopedEntityName(name:name, namespace:"ns"))
		entry.setDescribingCodeSystemVersion(new CodeSystemVersionReference())
		entry.getDescribingCodeSystemVersion().setVersion(new NameAndMeaningReference())
		entry.getDescribingCodeSystemVersion().getVersion().setContent("TESTCSVERSION")
		entry.getDescribingCodeSystemVersion().setCodeSystem(new CodeSystemReference())
		entry.getDescribingCodeSystemVersion().getCodeSystem().setContent("TESTCS")
		if(description != null){
			entry.addDesignation(new Designation(value:new TsAnyType(content:description)))
		}

		//entry.addEntityType(new URIAndEntityName(name:"name", namespace:"ns"))

		entry.setChangeableElementGroup(new ChangeableElementGroup(
			changeDescription: new ChangeDescription(
				changeType: ChangeType.CREATE, 
				changeDate: new Date(),
				containingChangeSet: changeSetUri)))
		
		new EntityDescription(namedEntity:entry)
	}

}