package edu.mayo.cts2.framework.plugin.service.exist.profile.entitydescription

import static org.junit.Assert.*

import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import edu.mayo.cts2.framework.plugin.service.exist.profile.entitydescription.ExistEntityDescriptionMaintenanceService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.entitydescription.ExistEntityDescriptionQueryService;
import edu.mayo.cts2.framework.plugin.service.exist.profile.entitydescription.ExistEntityDescriptionReadService;
import edu.mayo.cts2.framework.model.core.CodeSystemReference
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference
import edu.mayo.cts2.framework.model.core.FilterComponent
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference
import edu.mayo.cts2.framework.model.core.ScopedEntityName
import edu.mayo.cts2.framework.model.core.URIAndEntityName
import edu.mayo.cts2.framework.model.core.types.TargetReferenceType
import edu.mayo.cts2.framework.model.entity.EntityDescription
import edu.mayo.cts2.framework.model.entity.NamedEntityDescription
import edu.mayo.cts2.framework.plugin.service.exist.profile.BaseServiceTestGroovy
import edu.mayo.cts2.framework.service.command.Page
import edu.mayo.cts2.framework.service.command.restriction.EntityDescriptionQueryServiceRestrictions
import edu.mayo.cts2.framework.service.constant.ExternalCts2Constants
import edu.mayo.cts2.framework.service.meta.StandardMatchAlgorithmReference

class ExistEntityDescriptionServiceGroovyTestIT extends BaseServiceTestGroovy {

	@Autowired
	ExistEntityDescriptionReadService read

	@Autowired
	ExistEntityDescriptionQueryService query

	@Autowired
	ExistEntityDescriptionMaintenanceService maint

	@Test void "Get Entity Description Summaries With Contains Name Restriction"(){

		maint.createResource("", createEntity("something"))
		maint.createResource("", createEntity("name"))

		def fc = new FilterComponent(
				matchAlgorithm:StandardMatchAlgorithmReference.CONTAINS.getMatchAlgorithmReference(),
				matchValue:"name",
				referenceTarget: new URIAndEntityName(name:ExternalCts2Constants.MA_RESOURCE_NAME_NAME),
				referenceType:TargetReferenceType.ATTRIBUTE)
		def summaries = query.getResourceSummaries(null, fc, null, new Page())

		assertEquals 1, summaries.entries.size
	}

	@Test void "Get Entity Description Summaries With Contains Wrong Name Restriction"(){

		maint.createResource("", createEntity("something"))
		maint.createResource("", createEntity("name"))

		def fc = new FilterComponent(
				matchAlgorithm:StandardMatchAlgorithmReference.CONTAINS.getMatchAlgorithmReference(),
				matchValue:"asdfasdf",
				referenceTarget: new URIAndEntityName(name:ExternalCts2Constants.MA_RESOURCE_NAME_NAME),
				referenceType:TargetReferenceType.ATTRIBUTE)
		def summaries = query.getResourceSummaries(null, fc, null, new Page())

		assertEquals 0, summaries.entries.size
	}

	@Test void "Get Entity Description Summaries With ExactMatch Name Restriction"(){

		maint.createResource("", createEntity("something"))
		maint.createResource("", createEntity("name"))

		def fc = new FilterComponent(
				matchAlgorithm:StandardMatchAlgorithmReference.EXACT_MATCH.getMatchAlgorithmReference(),
				matchValue:"something",
				referenceTarget: new URIAndEntityName(name:ExternalCts2Constants.MA_RESOURCE_NAME_NAME),
				referenceType:TargetReferenceType.ATTRIBUTE)
		def summaries = query.getResourceSummaries(null, fc, null, new Page())

		assertEquals 1, summaries.entries.size
	}
	
	@Test void "Get Entity Description Summaries With ExactMatch Wrong Name Restriction"(){

		maint.createResource("", createEntity("something"))
		maint.createResource("", createEntity("name"))

		def fc = new FilterComponent(
				matchAlgorithm:StandardMatchAlgorithmReference.EXACT_MATCH.getMatchAlgorithmReference(),
				matchValue:"somethin",
				referenceTarget: new URIAndEntityName(name:ExternalCts2Constants.MA_RESOURCE_NAME_NAME),
				referenceType:TargetReferenceType.ATTRIBUTE)
		def summaries = query.getResourceSummaries(null, fc, null, new Page())

		assertEquals 0, summaries.entries.size
	}

	@Test void "Get Entity Description Summaries With StartsWith Name Restriction"(){

		maint.createResource("", createEntity("something"))
		maint.createResource("", createEntity("name"))

		def fc = new FilterComponent(
				matchAlgorithm:StandardMatchAlgorithmReference.STARTS_WITH.getMatchAlgorithmReference(),
				matchValue:"someth",
				referenceTarget: new URIAndEntityName(name:ExternalCts2Constants.MA_RESOURCE_NAME_NAME),
				referenceType:TargetReferenceType.ATTRIBUTE)
		def summaries = query.getResourceSummaries(null, fc, null, new Page())

		assertEquals 1, summaries.entries.size
	}
	
	@Test void "Get Entity Description Summaries With StartsWith Wrong Name Restriction"(){

		maint.createResource("", createEntity("something"))
		maint.createResource("", createEntity("name"))

		def fc = new FilterComponent(
				matchAlgorithm:StandardMatchAlgorithmReference.STARTS_WITH.getMatchAlgorithmReference(),
				matchValue:"thing",
				referenceTarget: new URIAndEntityName(name:ExternalCts2Constants.MA_RESOURCE_NAME_NAME),
				referenceType:TargetReferenceType.ATTRIBUTE)
		def summaries = query.getResourceSummaries(null, fc, null, new Page())

		assertEquals 0, summaries.entries.size
	}
	
	@Test void "Get Entity Description Summaries With Entity Restriction"(){

		maint.createResource("", createEntity("something"))
		maint.createResource("", createEntity("name"))

		def restrictions = new EntityDescriptionQueryServiceRestrictions(entity:["something"])
		
		def summaries = query.getResourceSummaries(null, null, restrictions, new Page())

		assertEquals 1, summaries.entries.size
	}
	
	@Test void "Get Entity Description Summaries With Two Entity Restrictions"(){

		maint.createResource("", createEntity("something"))
		maint.createResource("", createEntity("name"))

		def restrictions = new EntityDescriptionQueryServiceRestrictions(entity:["something", "name"])

		def summaries = query.getResourceSummaries(null, null, restrictions, new Page())

		assertEquals 2, summaries.entries.size
	}

	@Test void "Get Entity Description Summaries With Wrong Entity Restriction"(){

		maint.createResource("", createEntity("something"))
		maint.createResource("", createEntity("name"))

		def restrictions = new EntityDescriptionQueryServiceRestrictions(entity:["INVALID"])

		def summaries = query.getResourceSummaries(null, null, restrictions, new Page())

		assertEquals 0, summaries.entries.size
	}

	EntityDescription createEntity(String name){
		def entry = new NamedEntityDescription(about:"about")
		entry.setEntityID(new ScopedEntityName(name:name, namespace:"ns"))
		entry.setDescribingCodeSystemVersion(new CodeSystemVersionReference())
		entry.getDescribingCodeSystemVersion().setVersion(new NameAndMeaningReference())
		entry.getDescribingCodeSystemVersion().getVersion().setContent("TESTCSVERSION")
		entry.getDescribingCodeSystemVersion().setCodeSystem(new CodeSystemReference())
		entry.getDescribingCodeSystemVersion().getCodeSystem().setContent("TESTCS")

		entry.addEntityType(new URIAndEntityName(name:"name", namespace:"ns"))

		new EntityDescription(namedEntity:entry)
	}
}
