package edu.mayo.cts2.framework.plugin.service.exist.profile.entitydescription

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
import edu.mayo.cts2.framework.model.core.URIAndEntityName
import edu.mayo.cts2.framework.model.core.types.ChangeType
import edu.mayo.cts2.framework.model.core.types.TargetReferenceType
import edu.mayo.cts2.framework.model.entity.EntityDescription
import edu.mayo.cts2.framework.model.entity.NamedEntityDescription
import edu.mayo.cts2.framework.model.service.core.EntityNameOrURI
import edu.mayo.cts2.framework.plugin.service.exist.profile.BaseServiceDbCleaningBase
import edu.mayo.cts2.framework.service.command.restriction.EntityDescriptionQueryServiceRestrictions
import edu.mayo.cts2.framework.service.constant.ExternalCts2Constants
import edu.mayo.cts2.framework.service.meta.StandardMatchAlgorithmReference

import static org.junit.Assert.*

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
import edu.mayo.cts2.framework.model.core.URIAndEntityName
import edu.mayo.cts2.framework.model.core.types.ChangeType
import edu.mayo.cts2.framework.model.core.types.TargetReferenceType
import edu.mayo.cts2.framework.model.entity.Designation
import edu.mayo.cts2.framework.model.entity.EntityDescription
import edu.mayo.cts2.framework.model.entity.NamedEntityDescription
import edu.mayo.cts2.framework.model.service.core.EntityNameOrURI
import edu.mayo.cts2.framework.plugin.service.exist.profile.BaseServiceDbCleaningBase
import edu.mayo.cts2.framework.service.command.restriction.EntityDescriptionQueryServiceRestrictions
import edu.mayo.cts2.framework.service.constant.ExternalCts2Constants
import edu.mayo.cts2.framework.service.meta.StandardMatchAlgorithmReference
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionQuery

class ExistEntityDescriptionServiceGroovyTestIT extends BaseServiceDbCleaningBase {

	@Autowired
	ExistEntityDescriptionReadService read

	@Autowired
	ExistEntityDescriptionQueryService query

	@Autowired
	ExistEntityDescriptionMaintenanceService maint

	@Test
	void Get_Entity_Description_Summaries_With_Page_Limit(){
		
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

		maint.createResource(createEntity("something",changeSetUri))
		maint.createResource(createEntity("name",changeSetUri))

		changeSetService.commitChangeSet(changeSetUri)
		
		def q = [
			getFilterComponent : { },
			getReadContext : { },
			getQuery : { },
			getRestrictions : { }
		] as EntityDescriptionQuery
		
		def summaries = query.getResourceSummaries(q,null, new Page(maxtoreturn:1))

		assertEquals 1, summaries.entries.size
	}
	
	@Test 
	void Get_Entity_Description_Summaries_With_Contains_ResourceNameOrUri_Restriction(){
		
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

		maint.createResource(createEntity("something",changeSetUri))
		maint.createResource(createEntity("name",changeSetUri))

		changeSetService.commitChangeSet(changeSetUri)
		
		def fc = new ResolvedFilter(
				matchAlgorithmReference:StandardMatchAlgorithmReference.CONTAINS.getMatchAlgorithmReference(),
				matchValue:"name",
		        modelAttributeReference: new ModelAttributeReference(content:ExternalCts2Constants.MA_RESOURCE_NAME_NAME),
				referenceType:TargetReferenceType.ATTRIBUTE)
		
		def q = [
			getFilterComponent : { [fc] as Set },
			getReadContext : { },
			getQuery : { },
			getRestrictions : { }
		] as EntityDescriptionQuery
		
		def summaries = query.getResourceSummaries(q, null, new Page())

		assertEquals 1, summaries.entries.size
	}
	
	@Test
	void Get_Entity_Description_Summaries_With_Contains_ResourceSynopsis_Restriction(){
		
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

		maint.createResource(createEntity("something",changeSetUri,"aname"))

		changeSetService.commitChangeSet(changeSetUri)
		
		def fc = new ResolvedFilter(
				matchAlgorithmReference:StandardMatchAlgorithmReference.EXACT_MATCH.getMatchAlgorithmReference(),
				matchValue:"aname",
				modelAttributeReference: new ModelAttributeReference(content:ExternalCts2Constants.MA_RESOURCE_SYNOPSIS_NAME),
				referenceType:TargetReferenceType.ATTRIBUTE)
		
		def q = [
			getFilterComponent : { [fc] as Set },
			getReadContext : { },
			getQuery : { },
			getRestrictions : { }
		] as EntityDescriptionQuery
		
		def summaries = query.getResourceSummaries(q, null, new Page())

		assertEquals 1, summaries.entries.size
	}

	@Test void GetEntityDescriptionSummariesWithContainsWrongResourceNameOrUriRestriction(){
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

		maint.createResource( createEntity("something",changeSetUri))
		maint.createResource( createEntity("name",changeSetUri))

		changeSetService.commitChangeSet(changeSetUri)
		
		def fc = new ResolvedFilter(
				matchAlgorithmReference:StandardMatchAlgorithmReference.CONTAINS.getMatchAlgorithmReference(),
				matchValue:"asdfasdf",
				modelAttributeReference: new ModelAttributeReference(content:ExternalCts2Constants.MA_RESOURCE_NAME_NAME),
				referenceType:TargetReferenceType.ATTRIBUTE)
		
		def set = new HashSet()
		set.add(fc)
		
		def q = [
			getFilterComponent : { [fc] as Set },
			getReadContext : { },
			getQuery : { },
			getRestrictions : { }
		] as EntityDescriptionQuery
		
		def summaries = query.getResourceSummaries(q, null, new Page())

		assertEquals 0, summaries.entries.size
	}

	@Test void GetEntityDescriptionSummariesWithExactMatchResourceNameOrUriRestriction(){
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

		maint.createResource( createEntity("something",changeSetUri))
		maint.createResource( createEntity("name",changeSetUri))
		
		changeSetService.commitChangeSet(changeSetUri)

		def fc = new ResolvedFilter(
				matchAlgorithmReference:StandardMatchAlgorithmReference.EXACT_MATCH.getMatchAlgorithmReference(),
				matchValue:"something",
				modelAttributeReference: new ModelAttributeReference(content:ExternalCts2Constants.MA_RESOURCE_NAME_NAME),
				referenceType:TargetReferenceType.ATTRIBUTE)
		
		def q = [
			getFilterComponent : { [fc] as Set },
			getReadContext : { },
			getQuery : { },
			getRestrictions : { }
		] as EntityDescriptionQuery

		def summaries = query.getResourceSummaries(q, null, new Page())

		assertEquals 1, summaries.entries.size
	}
	
	@Test void GetEntityDescriptionSummariesWithExactMatchWrongResourceNameOrUriRestriction(){
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

		maint.createResource( createEntity("something",changeSetUri))
		maint.createResource( createEntity("name",changeSetUri))
		
		changeSetService.commitChangeSet(changeSetUri)

		def fc = new ResolvedFilter(
				matchAlgorithmReference:StandardMatchAlgorithmReference.EXACT_MATCH.getMatchAlgorithmReference(),
				matchValue:"somethin",
				modelAttributeReference: new ModelAttributeReference(content:ExternalCts2Constants.MA_RESOURCE_NAME_NAME),
				referenceType:TargetReferenceType.ATTRIBUTE)
		
		def q = [
			getFilterComponent : { [fc] as Set },
			getReadContext : { },
			getQuery : { },
			getRestrictions : { }
		] as EntityDescriptionQuery
	
		def summaries = query.getResourceSummaries(q, null, new Page())

		assertEquals 0, summaries.entries.size
	}

	@Test void GetEntityDescriptionSummariesWithStartsWithResourceNameOrUriRestriction(){
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

		maint.createResource( createEntity("something",changeSetUri))
		maint.createResource( createEntity("name",changeSetUri))
		
		changeSetService.commitChangeSet(changeSetUri)

		def fc = new ResolvedFilter(
				matchAlgorithmReference:StandardMatchAlgorithmReference.STARTS_WITH.getMatchAlgorithmReference(),
				matchValue:"someth",
				modelAttributeReference: new ModelAttributeReference(content:ExternalCts2Constants.MA_RESOURCE_NAME_NAME),
				referenceType:TargetReferenceType.ATTRIBUTE)
		
		def q = [
			getFilterComponent : { [fc] as Set },
			getReadContext : { },
			getQuery : { },
			getRestrictions : { }
		] as EntityDescriptionQuery
	
		def summaries = query.getResourceSummaries(q, null, new Page())

		assertEquals 1, summaries.entries.size
	}
	
	@Test void GetEntityDescriptionSummariesWithStartsWithWrongResourceNameOrUriRestriction(){
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

		maint.createResource( createEntity("something",changeSetUri))
		maint.createResource( createEntity("name",changeSetUri))
		
		changeSetService.commitChangeSet(changeSetUri)

		def fc = new ResolvedFilter(
				matchAlgorithmReference:StandardMatchAlgorithmReference.STARTS_WITH.getMatchAlgorithmReference(),
				matchValue:"thing",
				modelAttributeReference: new ModelAttributeReference(content:ExternalCts2Constants.MA_RESOURCE_NAME_NAME),
				referenceType:TargetReferenceType.ATTRIBUTE)

		def q = [
			getFilterComponent : { [fc] as Set },
			getReadContext : { },
			getQuery : { },
			getRestrictions : { }
		] as EntityDescriptionQuery
	
		def summaries = query.getResourceSummaries(q, null, new Page())

		assertEquals 0, summaries.entries.size
	}
	
	@Test void GetEntityDescriptionSummariesWithEntityRestriction(){
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

		maint.createResource( createEntity("something",changeSetUri))
		maint.createResource( createEntity("name",changeSetUri))
		
		changeSetService.commitChangeSet(changeSetUri)

		def restrictions = new EntityDescriptionQueryServiceRestrictions(entities:[new EntityNameOrURI(entityName:new ScopedEntityName(name:"something"))])
		
		def q = [
			getFilterComponent : { },
			getReadContext : { },
			getQuery : { },
			getRestrictions : { restrictions }
		] as EntityDescriptionQuery
	
		def summaries = query.getResourceSummaries(q, null, new Page())

		assertEquals 1, summaries.entries.size
	}
	
	@Test void GetEntityDescriptionSummariesWithTwoEntityRestrictions(){
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

		maint.createResource( createEntity("something",changeSetUri))
		maint.createResource( createEntity("name",changeSetUri))
		
		changeSetService.commitChangeSet(changeSetUri)

		def restrictions = new EntityDescriptionQueryServiceRestrictions(
			entities:[
				new EntityNameOrURI(entityName:new ScopedEntityName(name:"something")),
				new EntityNameOrURI(entityName:new ScopedEntityName(name:"name"))])
		
		def q = [
			getFilterComponent : { },
			getReadContext : { },
			getQuery : { },
			getRestrictions : { restrictions }
		] as EntityDescriptionQuery

		def summaries = query.getResourceSummaries(q, null, new Page())

		assertEquals 2, summaries.entries.size
	}

	@Test void GetEntityDescriptionSummariesWithWrongEntityRestriction(){
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

		maint.createResource( createEntity("something",changeSetUri))
		maint.createResource( createEntity("name",changeSetUri))
		
		changeSetService.commitChangeSet(changeSetUri)

		def restrictions = new EntityDescriptionQueryServiceRestrictions(entities:[new EntityNameOrURI(entityName:new ScopedEntityName(name:"INVALID"))])

		def q = [
			getFilterComponent : { },
			getReadContext : { },
			getQuery : { },
			getRestrictions : { restrictions }
		] as EntityDescriptionQuery
	
		def summaries = query.getResourceSummaries(q, null, new Page())

		assertEquals 0, summaries.entries.size
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

		entry.addEntityType(new URIAndEntityName(name:"name", namespace:"ns"))

		entry.setChangeableElementGroup(new ChangeableElementGroup(
			changeDescription: new ChangeDescription(
				changeType: ChangeType.CREATE, 
				changeDate: new Date(),
				containingChangeSet: changeSetUri)))
		
		new EntityDescription(namedEntity:entry)
	}

	EntityDescription createEntity(name,changeSetUri){
		createEntity(name,changeSetUri,null)
	}
}
