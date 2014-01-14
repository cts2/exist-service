package edu.mayo.cts2.framework.plugin.service.exist.profile.entitydescription

import edu.mayo.cts2.framework.model.association.Association
import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.model.command.ResolvedFilter
import edu.mayo.cts2.framework.model.core.*
import edu.mayo.cts2.framework.model.core.types.ChangeType
import edu.mayo.cts2.framework.model.entity.Designation
import edu.mayo.cts2.framework.model.entity.EntityDescription
import edu.mayo.cts2.framework.model.entity.NamedEntityDescription
import edu.mayo.cts2.framework.model.service.core.EntityNameOrURI
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.plugin.service.exist.profile.BaseServiceDbCleaningBase
import edu.mayo.cts2.framework.plugin.service.exist.profile.association.ExistAssociationMaintenanceService
import edu.mayo.cts2.framework.service.command.restriction.EntityDescriptionQueryServiceRestrictions
import edu.mayo.cts2.framework.service.command.restriction.EntityDescriptionQueryServiceRestrictions.HierarchyRestriction
import edu.mayo.cts2.framework.service.command.restriction.EntityDescriptionQueryServiceRestrictions.HierarchyRestriction.HierarchyType
import edu.mayo.cts2.framework.service.meta.StandardMatchAlgorithmReference
import edu.mayo.cts2.framework.service.meta.StandardModelAttributeReference
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionQuery
import edu.mayo.cts2.framework.service.profile.entitydescription.name.EntityDescriptionReadId
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import static org.junit.Assert.*

class ExistEntityDescriptionServiceGroovyTestIT extends BaseServiceDbCleaningBase {

	@Autowired
	ExistEntityDescriptionReadService read

	@Autowired
	ExistEntityDescriptionQueryService query

	@Autowired
	ExistEntityDescriptionMaintenanceService maint

    @Autowired
    ExistAssociationMaintenanceService assocMaint

    @Test
    void TestCount(){

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

        def count = query.count(q)

        assertEquals 2, count
    }

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
		        componentReference: StandardModelAttributeReference.RESOURCE_NAME.componentReference)
		
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
    void Test_Count_With_Contains_ResourceNameOrUri_Restriction(){

        def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

        maint.createResource(createEntity("something",changeSetUri))
        maint.createResource(createEntity("name",changeSetUri))

        changeSetService.commitChangeSet(changeSetUri)

        def fc = new ResolvedFilter(
                matchAlgorithmReference:StandardMatchAlgorithmReference.CONTAINS.getMatchAlgorithmReference(),
                matchValue:"name",
                componentReference: StandardModelAttributeReference.RESOURCE_NAME.componentReference)

        def q = [
                getFilterComponent : { [fc] as Set },
                getReadContext : { },
                getQuery : { },
                getRestrictions : { }
        ] as EntityDescriptionQuery

        def count = query.count(q)

        assertEquals 1, count
    }

	@Test
	void Get_Entity_Description_Summaries_With_Contains_ResourceSynopsis_Restriction(){
		
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

		maint.createResource(createEntity("something",changeSetUri,"aname"))

		changeSetService.commitChangeSet(changeSetUri)
		
		def fc = new ResolvedFilter(
				matchAlgorithmReference:StandardMatchAlgorithmReference.CONTAINS.getMatchAlgorithmReference(),
				matchValue:"aname",
				componentReference: StandardModelAttributeReference.RESOURCE_SYNOPSIS.componentReference)
		
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
    void Get_Entity_Description_Summaries_With_Contains_ResourceSynopsis_Restriction_Case_Insensitive(){

        def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

        maint.createResource(createEntity("something",changeSetUri,"aname"))

        changeSetService.commitChangeSet(changeSetUri)

        def fc = new ResolvedFilter(
                matchAlgorithmReference:StandardMatchAlgorithmReference.CONTAINS.getMatchAlgorithmReference(),
                matchValue:"AName",
                componentReference: StandardModelAttributeReference.RESOURCE_SYNOPSIS.componentReference)

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
				componentReference: StandardModelAttributeReference.RESOURCE_NAME.componentReference)
		
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
				componentReference: StandardModelAttributeReference.RESOURCE_NAME.componentReference)
		
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
				componentReference: StandardModelAttributeReference.RESOURCE_NAME.componentReference)
		
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
				componentReference: StandardModelAttributeReference.RESOURCE_NAME.componentReference)
		
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
				componentReference: StandardModelAttributeReference.RESOURCE_NAME.componentReference)

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

    @Test void GetAvailableDescriptionsName(){
        def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

        maint.createResource( createEntity("something",changeSetUri))

        changeSetService.commitChangeSet(changeSetUri)

        def nameOrUri = new EntityNameOrURI(entityName: new ScopedEntityName(name: "something", namespace: "ns"))
        def entry = read.availableDescriptions(nameOrUri, null)

        assertNotNull entry
    }

    @Test void GetAvailableDescriptionsNameWrongNamespace(){
        def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

        maint.createResource( createEntity("something",changeSetUri))

        changeSetService.commitChangeSet(changeSetUri)

        def nameOrUri = new EntityNameOrURI(entityName: new ScopedEntityName(name: "something", namespace: "__INVALID__"))
        def entry = read.availableDescriptions(nameOrUri, null)

        assertNull entry
    }

    @Test void GetAvailableDescriptionsNameNoNamespace(){
        def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

        maint.createResource( createEntity("something",changeSetUri))

        changeSetService.commitChangeSet(changeSetUri)

        def nameOrUri = new EntityNameOrURI(entityName: new ScopedEntityName(name: "something"))
        def entry = read.availableDescriptions(nameOrUri, null)

        assertNotNull entry
    }

    @Test void GetAvailableDescriptionsNameInvalid(){
        def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

        maint.createResource( createEntity("something",changeSetUri))

        changeSetService.commitChangeSet(changeSetUri)

        def nameOrUri = new EntityNameOrURI(entityName: new ScopedEntityName(name: "__INVALID__", namespace: "__INVALID__"))
        def entry = read.availableDescriptions(nameOrUri, null)

        assertNull entry
    }

    @Test void GetAvailableDescriptionsUri(){
        def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

        maint.createResource( createEntity("something",changeSetUri))

        changeSetService.commitChangeSet(changeSetUri)

        def nameOrUri = new EntityNameOrURI(uri: "about")
        def entry = read.availableDescriptions(nameOrUri, null)

        assertNotNull entry
    }

    @Test void GetAvailableDescriptionsUriInvalid(){
        def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

        maint.createResource( createEntity("something",changeSetUri))

        changeSetService.commitChangeSet(changeSetUri)

        def nameOrUri = new EntityNameOrURI(uri: "__INVALID__")
        def entry = read.availableDescriptions(nameOrUri, null)

        assertNull entry
    }
	
	@Test
	void Get_Entity_Description_Children_Name(){
		
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

		def parent = createEntity("p",changeSetUri,"desc")
		parent.namedEntity.about = "http://parent"
		
		def child = createEntity("c",changeSetUri,"desc")
		child.namedEntity.parent = new URIAndEntityName(
			name:"p",
			namespace:"ns",
			uri:"http://parent")
		
		def other = createEntity("z",changeSetUri,"desc")
		
		maint.createResource(parent)
		maint.createResource(child)
		maint.createResource(other)
		
		changeSetService.commitChangeSet(changeSetUri)
			
		def q = [
			getFilterComponent : { },
			getReadContext : { },
			getQuery : { },
			getRestrictions : {
				new EntityDescriptionQueryServiceRestrictions(
					hierarchyRestriction: new HierarchyRestriction(
						hierarchyType: HierarchyType.CHILDREN,
						entity: new EntityNameOrURI(
							entityName : new ScopedEntityName(name:"p"))
					)
				)
			}
		] as EntityDescriptionQuery
		
		def summaries = query.getResourceSummaries(q, null, new Page())

		assertEquals 1, summaries.entries.size
		assertEquals "c", summaries.entries.get(0).name.name
		
	}

    @Test
    void Get_Entity_Description_Has_Subject_Of_Href(){

        def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

        def parent = createEntity("p",changeSetUri,"desc")
        parent.namedEntity.about = "http://p"
        parent.namedEntity.setDescribingCodeSystemVersion(new CodeSystemVersionReference())
        parent.namedEntity.getDescribingCodeSystemVersion().setVersion(new NameAndMeaningReference())
        parent.namedEntity.getDescribingCodeSystemVersion().getVersion().setContent("TESTCSVERSION")
        parent.namedEntity.getDescribingCodeSystemVersion().setCodeSystem(new CodeSystemReference())
        parent.namedEntity.getDescribingCodeSystemVersion().getCodeSystem().setContent("TESTCS")

        def child = createEntity("c",changeSetUri,"desc")
        child.namedEntity.about = "http://c"
        child.namedEntity.setDescribingCodeSystemVersion(new CodeSystemVersionReference())
        child.namedEntity.getDescribingCodeSystemVersion().setVersion(new NameAndMeaningReference())
        child.namedEntity.getDescribingCodeSystemVersion().getVersion().setContent("TESTCSVERSION")
        child.namedEntity.getDescribingCodeSystemVersion().setCodeSystem(new CodeSystemReference())
        child.namedEntity.getDescribingCodeSystemVersion().getCodeSystem().setContent("TESTCS")

        def assoc = new Association(associationID:"http://someAssoc")
        assoc.setSubject(new URIAndEntityName(name:"p", namespace:"ns", uri:"http://p"))

        assoc.addTarget(new StatementTarget())
        assoc.getTarget(0).setEntity(new URIAndEntityName(name:"c", namespace:"ns", uri:"http://c"))

        assoc.setPredicate(new PredicateReference(name:"predicatename", namespace:"ns", uri:"uri"))

        assoc.setAssertedBy(new CodeSystemVersionReference(
                codeSystem: new CodeSystemReference(content:"TESTCS"),
                version: new NameAndMeaningReference(content:"TESTCSVERSION")))

        assoc.setChangeableElementGroup(new ChangeableElementGroup(
                changeDescription: new ChangeDescription(
                        changeType: ChangeType.CREATE,
                        changeDate: new Date(),
                        containingChangeSet: changeSetUri)))

        maint.createResource(parent)
        maint.createResource(child)
        assocMaint.createResource(assoc)

        changeSetService.commitChangeSet(changeSetUri)

        def ed = read.read(new EntityDescriptionReadId("http://p", ModelUtils.nameOrUriFromName("TESTCSVERSION")), null)

        assertNotNull ed.namedEntity.subjectOf
    }

    @Test
    void Get_Entity_Description_Has_Subject_Of_Href_Invalid(){

        def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

        def parent = createEntity("p",changeSetUri,"desc")
        parent.namedEntity.about = "http://p"
        parent.namedEntity.setDescribingCodeSystemVersion(new CodeSystemVersionReference())
        parent.namedEntity.getDescribingCodeSystemVersion().setVersion(new NameAndMeaningReference())
        parent.namedEntity.getDescribingCodeSystemVersion().getVersion().setContent("TESTCSVERSION")
        parent.namedEntity.getDescribingCodeSystemVersion().setCodeSystem(new CodeSystemReference())
        parent.namedEntity.getDescribingCodeSystemVersion().getCodeSystem().setContent("TESTCS")

        def child = createEntity("c",changeSetUri,"desc")
        child.namedEntity.about = "http://c"
        child.namedEntity.setDescribingCodeSystemVersion(new CodeSystemVersionReference())
        child.namedEntity.getDescribingCodeSystemVersion().setVersion(new NameAndMeaningReference())
        child.namedEntity.getDescribingCodeSystemVersion().getVersion().setContent("TESTCSVERSION")
        child.namedEntity.getDescribingCodeSystemVersion().setCodeSystem(new CodeSystemReference())
        child.namedEntity.getDescribingCodeSystemVersion().getCodeSystem().setContent("TESTCS")

        def assoc = new Association(associationID:"http://someAssoc")
        assoc.setSubject(new URIAndEntityName(name:"__INVALID__", namespace:"__INVALID__", uri:"__INVALID__"))

        assoc.addTarget(new StatementTarget())
        assoc.getTarget(0).setEntity(new URIAndEntityName(name:"c", namespace:"ns", uri:"http://c"))

        assoc.setPredicate(new PredicateReference(name:"predicatename", namespace:"ns", uri:"uri"))

        assoc.setAssertedBy(new CodeSystemVersionReference(
                codeSystem: new CodeSystemReference(content:"TESTCS"),
                version: new NameAndMeaningReference(content:"TESTCSVERSION")))

        assoc.setChangeableElementGroup(new ChangeableElementGroup(
                changeDescription: new ChangeDescription(
                        changeType: ChangeType.CREATE,
                        changeDate: new Date(),
                        containingChangeSet: changeSetUri)))

        maint.createResource(parent)
        maint.createResource(child)
        assocMaint.createResource(assoc)

        changeSetService.commitChangeSet(changeSetUri)

        def ed = read.read(new EntityDescriptionReadId("http://p", ModelUtils.nameOrUriFromName("TESTCSVERSION")), null)

        assertNull ed.namedEntity.subjectOf
    }

    @Test
    void Get_Entity_Description_Has_Children_Href(){

        def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

        def parent = createEntity("p",changeSetUri,"desc")
        parent.namedEntity.about = "http://parent"
        parent.namedEntity.setDescribingCodeSystemVersion(new CodeSystemVersionReference())
        parent.namedEntity.getDescribingCodeSystemVersion().setVersion(new NameAndMeaningReference())
        parent.namedEntity.getDescribingCodeSystemVersion().getVersion().setContent("TESTCSVERSION")
        parent.namedEntity.getDescribingCodeSystemVersion().setCodeSystem(new CodeSystemReference())
        parent.namedEntity.getDescribingCodeSystemVersion().getCodeSystem().setContent("TESTCS")

        def child = createEntity("c",changeSetUri,"desc")
        child.namedEntity.about = "http://child"
        child.namedEntity.setDescribingCodeSystemVersion(new CodeSystemVersionReference())
        child.namedEntity.getDescribingCodeSystemVersion().setVersion(new NameAndMeaningReference())
        child.namedEntity.getDescribingCodeSystemVersion().getVersion().setContent("TESTCSVERSION")
        child.namedEntity.getDescribingCodeSystemVersion().setCodeSystem(new CodeSystemReference())
        child.namedEntity.getDescribingCodeSystemVersion().getCodeSystem().setContent("TESTCS")
        child.namedEntity.parent = new URIAndEntityName(
                name:"p",
                namespace:"ns",
                uri:"http://parent")

        maint.createResource(parent)
        maint.createResource(child)

        changeSetService.commitChangeSet(changeSetUri)

        def ed = read.read(new EntityDescriptionReadId("http://parent", ModelUtils.nameOrUriFromName("TESTCSVERSION")), null)

        assertNotNull ed.namedEntity.children
    }

    @Test
    void Get_Entity_Description_Has_Children_No_Href(){

        def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

        def parent = createEntity("p",changeSetUri,"desc")
        parent.namedEntity.about = "http://parent"
        parent.namedEntity.setDescribingCodeSystemVersion(new CodeSystemVersionReference())
        parent.namedEntity.getDescribingCodeSystemVersion().setVersion(new NameAndMeaningReference())
        parent.namedEntity.getDescribingCodeSystemVersion().getVersion().setContent("TESTCSVERSION")
        parent.namedEntity.getDescribingCodeSystemVersion().setCodeSystem(new CodeSystemReference())
        parent.namedEntity.getDescribingCodeSystemVersion().getCodeSystem().setContent("TESTCS")

        def child = createEntity("c",changeSetUri,"desc")
        child.namedEntity.about = "http://child"
        child.namedEntity.setDescribingCodeSystemVersion(new CodeSystemVersionReference())
        child.namedEntity.getDescribingCodeSystemVersion().setVersion(new NameAndMeaningReference())
        child.namedEntity.getDescribingCodeSystemVersion().getVersion().setContent("TESTCSVERSION")
        child.namedEntity.getDescribingCodeSystemVersion().setCodeSystem(new CodeSystemReference())
        child.namedEntity.getDescribingCodeSystemVersion().getCodeSystem().setContent("TESTCS")
        child.namedEntity.parent = new URIAndEntityName(
                name:"p",
                namespace:"ns",
                uri:"http://parent")

        maint.createResource(parent)
        maint.createResource(child)

        changeSetService.commitChangeSet(changeSetUri)

        def ed = read.read(new EntityDescriptionReadId("http://child", ModelUtils.nameOrUriFromName("TESTCSVERSION")), null)

        assertNull ed.namedEntity.children
    }

	@Test
	void Get_Entity_Description_Children_Uri(){
		
		def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

		def parent = createEntity("p",changeSetUri,"desc")
		parent.namedEntity.about = "http://parent"
		
		def child = createEntity("c",changeSetUri,"desc")
		child.namedEntity.parent = new URIAndEntityName(
			name:"p",
			namespace:"ns",
			uri:"http://parent")
		
		def other = createEntity("z",changeSetUri,"desc")
		
		maint.createResource(parent)
		maint.createResource(child)
		maint.createResource(other)
		
		changeSetService.commitChangeSet(changeSetUri)
			
		def q = [
			getFilterComponent : { },
			getReadContext : { },
			getQuery : { },
			getRestrictions : {
				new EntityDescriptionQueryServiceRestrictions(
					hierarchyRestriction: new HierarchyRestriction(
						hierarchyType: HierarchyType.CHILDREN,
						entity: new EntityNameOrURI(uri:"http://parent")
					)
				)
			}
		] as EntityDescriptionQuery
		
		def summaries = query.getResourceSummaries(q, null, new Page())

		assertEquals 1, summaries.entries.size
		assertEquals "c", summaries.entries.get(0).name.name
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

		entry.addEntityType(new URIAndEntityName(name:"name", namespace:"ns", uri:"uri"))

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
