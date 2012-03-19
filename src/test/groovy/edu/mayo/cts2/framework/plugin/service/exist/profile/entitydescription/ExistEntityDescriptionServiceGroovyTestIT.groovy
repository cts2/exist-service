package edu.mayo.cts2.framework.plugin.service.exist.profile.entitydescription

import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.model.command.ResolvedFilter
import edu.mayo.cts2.framework.model.core.ChangeDescription
import edu.mayo.cts2.framework.model.core.ChangeableElementGroup
import edu.mayo.cts2.framework.model.core.CodeSystemReference
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference
import edu.mayo.cts2.framework.model.core.ScopedEntityName
import edu.mayo.cts2.framework.model.core.URIAndEntityName
import edu.mayo.cts2.framework.model.core.types.ChangeType
import edu.mayo.cts2.framework.model.entity.EntityDescription
import edu.mayo.cts2.framework.model.entity.NamedEntityDescription
import edu.mayo.cts2.framework.model.service.core.EntityNameOrURI
import edu.mayo.cts2.framework.plugin.service.exist.profile.BaseServiceDbCleaningBase
import edu.mayo.cts2.framework.service.command.restriction.EntityDescriptionQueryServiceRestrictions
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
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference
import edu.mayo.cts2.framework.model.core.ScopedEntityName
import edu.mayo.cts2.framework.model.core.TsAnyType
import edu.mayo.cts2.framework.model.core.URIAndEntityName
import edu.mayo.cts2.framework.model.core.types.ChangeType
import edu.mayo.cts2.framework.model.entity.Designation
import edu.mayo.cts2.framework.model.entity.EntityDescription
import edu.mayo.cts2.framework.model.entity.NamedEntityDescription
import edu.mayo.cts2.framework.model.service.core.EntityNameOrURI
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.plugin.service.exist.profile.BaseServiceDbCleaningBase
import edu.mayo.cts2.framework.service.command.restriction.EntityDescriptionQueryServiceRestrictions
import edu.mayo.cts2.framework.service.meta.StandardMatchAlgorithmReference
import edu.mayo.cts2.framework.service.meta.StandardModelAttributeReference
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionQuery
import edu.mayo.cts2.framework.service.profile.entitydescription.name.EntityDescriptionReadId

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
		        propertyReference: StandardModelAttributeReference.RESOURCE_NAME.propertyReference)
		
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
				matchAlgorithmReference:StandardMatchAlgorithmReference.CONTAINS.getMatchAlgorithmReference(),
				matchValue:"aname",
				propertyReference: StandardModelAttributeReference.RESOURCE_SYNOPSIS.propertyReference)
		
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
				propertyReference: StandardModelAttributeReference.RESOURCE_NAME.propertyReference)
		
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
				propertyReference: StandardModelAttributeReference.RESOURCE_NAME.propertyReference)
		
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
				propertyReference: StandardModelAttributeReference.RESOURCE_NAME.propertyReference)
		
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
				propertyReference: StandardModelAttributeReference.RESOURCE_NAME.propertyReference)
		
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
				propertyReference: StandardModelAttributeReference.RESOURCE_NAME.propertyReference)

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
	
	@Test void GetEntityDescriptionByUriWithCodeSystemVersion(){
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

		maint.createResource( createEntity("something",changeSetUri))
		
		changeSetService.commitChangeSet(changeSetUri)
	
		def entry = read.read(
			new EntityDescriptionReadId("about", ModelUtils.nameOrUriFromName("TESTCSVERSION")), null)

		assertNotNull entry
	}
	
	@Test void GetEntityDescriptionByUriWithWrongCodeSystemVersion(){
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

		maint.createResource( createEntity("something",changeSetUri))
		
		changeSetService.commitChangeSet(changeSetUri)
	
		def entry = read.read(
			new EntityDescriptionReadId("about", ModelUtils.nameOrUriFromName("__WRONG__VERSION__")), null)

		assertNull entry
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
