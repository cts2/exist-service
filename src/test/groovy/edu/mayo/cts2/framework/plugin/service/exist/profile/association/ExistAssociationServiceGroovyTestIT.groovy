package edu.mayo.cts2.framework.plugin.service.exist.profile.association
import edu.mayo.cts2.framework.model.association.Association
import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.model.core.*
import edu.mayo.cts2.framework.model.core.types.ChangeType
import edu.mayo.cts2.framework.model.service.core.EntityNameOrURI
import edu.mayo.cts2.framework.model.service.core.NameOrURI
import edu.mayo.cts2.framework.plugin.service.exist.profile.BaseServiceDbCleaningBase
import edu.mayo.cts2.framework.service.command.restriction.AssociationQueryServiceRestrictions
import edu.mayo.cts2.framework.service.profile.association.AssociationQuery
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

class ExistAssociationServiceGroovyTestIT extends BaseServiceDbCleaningBase {

	@Autowired
	ExistAssociationQueryService query


    @Autowired
    ExistAssociationMaintenanceService maint

    @Test
    void TestCodeSystemVersionBadQuery(){

        def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

        def assoc1 = new Association(associationID:"http://someAssoc")
        assoc1.setSubject(new URIAndEntityName(name:"p", namespace:"ns", uri:"http://p"))

        assoc1.addTarget(new StatementTarget())
        assoc1.getTarget(0).setEntity(new URIAndEntityName(name:"c", namespace:"ns", uri:"http://c"))

        assoc1.setPredicate(new PredicateReference(name:"predicatename", namespace:"ns", uri:"uri"))

        assoc1.setAssertedBy(new CodeSystemVersionReference(
                codeSystem: new CodeSystemReference(content:"TESTCS"),
                version: new NameAndMeaningReference(content:"TESTCSVERSION")))

        assoc1.setChangeableElementGroup(new ChangeableElementGroup(
                changeDescription: new ChangeDescription(
                        changeType: ChangeType.CREATE,
                        changeDate: new Date(),
                        containingChangeSet: changeSetUri)))

        maint.createResource(assoc1)

        changeSetService.commitChangeSet(changeSetUri)

        def ed = query.getResourceSummaries([
                getRestrictions: {
                    new AssociationQueryServiceRestrictions(
                            codeSystemVersion: new NameOrURI(name: "__INVALID__"))
                },
                getReadContext: {null},
                getFilterComponent: {null}
        ] as AssociationQuery, null, new Page()
        )

        assertNotNull ed
        assertEquals 0, ed.entries.size()
    }

    @Test
    void TestCodeSystemVersionGoodQuery(){

        def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

        def assoc1 = new Association(associationID:"http://someAssoc")
        assoc1.setSubject(new URIAndEntityName(name:"p", namespace:"ns", uri:"http://p"))

        assoc1.addTarget(new StatementTarget())
        assoc1.getTarget(0).setEntity(new URIAndEntityName(name:"c", namespace:"ns", uri:"http://c"))

        assoc1.setPredicate(new PredicateReference(name:"predicatename", namespace:"ns", uri:"uri"))

        assoc1.setAssertedBy(new CodeSystemVersionReference(
                codeSystem: new CodeSystemReference(content:"TESTCS"),
                version: new NameAndMeaningReference(content:"TESTCSVERSION")))

        assoc1.setChangeableElementGroup(new ChangeableElementGroup(
                changeDescription: new ChangeDescription(
                        changeType: ChangeType.CREATE,
                        changeDate: new Date(),
                        containingChangeSet: changeSetUri)))

        maint.createResource(assoc1)

        changeSetService.commitChangeSet(changeSetUri)

        def ed = query.getResourceSummaries([
                getRestrictions: {
                    new AssociationQueryServiceRestrictions(
                            codeSystemVersion: new NameOrURI(name: "TESTCSVERSION"))
                },
                getReadContext: {null},
                getFilterComponent: {null}
        ] as AssociationQuery, null, new Page()
        )

        assertNotNull ed
        assertEquals 1, ed.entries.size()
    }

    @Test
    void TestSourceOfQuery(){

        def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

        def assoc1 = new Association(associationID:"http://someAssoc")
        assoc1.setSubject(new URIAndEntityName(name:"p", namespace:"ns", uri:"http://p"))

        assoc1.addTarget(new StatementTarget())
        assoc1.getTarget(0).setEntity(new URIAndEntityName(name:"c", namespace:"ns", uri:"http://c"))

        assoc1.setPredicate(new PredicateReference(name:"predicatename", namespace:"ns", uri:"uri"))

        assoc1.setAssertedBy(new CodeSystemVersionReference(
                codeSystem: new CodeSystemReference(content:"TESTCS"),
                version: new NameAndMeaningReference(content:"TESTCSVERSION")))

        assoc1.setChangeableElementGroup(new ChangeableElementGroup(
                changeDescription: new ChangeDescription(
                        changeType: ChangeType.CREATE,
                        changeDate: new Date(),
                        containingChangeSet: changeSetUri)))

        maint.createResource(assoc1)

        changeSetService.commitChangeSet(changeSetUri)

        def ed = query.getResourceSummaries([
                getRestrictions: {
                    new AssociationQueryServiceRestrictions(sourceEntity: new EntityNameOrURI(entityName: new ScopedEntityName(name: "p")) )
                },
                getReadContext: {null},
                getFilterComponent: {null}
        ] as AssociationQuery, null, new Page()
        )

        assertNotNull ed
        assertEquals 1, ed.entries.size()
    }

    @Test
    void TestSourceOfQueryDifferentCodeSystemVersion(){

        def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

        def assoc1 = new Association(associationID:"http://someAssoc")
        assoc1.setSubject(new URIAndEntityName(name:"p", namespace:"ns", uri:"http://p"))

        assoc1.addTarget(new StatementTarget())
        assoc1.getTarget(0).setEntity(new URIAndEntityName(name:"c", namespace:"ns", uri:"http://c"))

        assoc1.setPredicate(new PredicateReference(name:"predicatename", namespace:"ns", uri:"uri"))

        assoc1.setAssertedBy(new CodeSystemVersionReference(
                codeSystem: new CodeSystemReference(content:"TESTCS-1"),
                version: new NameAndMeaningReference(content:"TESTCSVERSION-1")))

        assoc1.setChangeableElementGroup(new ChangeableElementGroup(
                changeDescription: new ChangeDescription(
                        changeType: ChangeType.CREATE,
                        changeDate: new Date(),
                        containingChangeSet: changeSetUri)))

        maint.createResource(assoc1)

        changeSetService.commitChangeSet(changeSetUri)

        def ed = query.getResourceSummaries([
                getRestrictions: {
                    new AssociationQueryServiceRestrictions(
                            codeSystemVersion: new NameOrURI(name:"TESTCSVERSION-2"),
                            sourceEntity: new EntityNameOrURI(entityName: new ScopedEntityName(name: "p")) )
                },
                getReadContext: {null},
                getFilterComponent: {null}
        ] as AssociationQuery, null, new Page()
        )

        assertNotNull ed
        assertEquals 1, ed.entries.size()
    }

    @Test
    void TestSourceOfQueryWithCsv(){

        def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

        def assoc1 = new Association(associationID:"http://someAssoc")
        assoc1.setSubject(new URIAndEntityName(name:"p", namespace:"ns", uri:"http://p"))

        assoc1.addTarget(new StatementTarget())
        assoc1.getTarget(0).setEntity(new URIAndEntityName(name:"c", namespace:"ns", uri:"http://c"))

        assoc1.setPredicate(new PredicateReference(name:"predicatename", namespace:"ns", uri:"uri"))

        assoc1.setAssertedBy(new CodeSystemVersionReference(
                codeSystem: new CodeSystemReference(content:"TESTCS"),
                version: new NameAndMeaningReference(content:"TESTCSVERSION")))

        assoc1.setChangeableElementGroup(new ChangeableElementGroup(
                changeDescription: new ChangeDescription(
                        changeType: ChangeType.CREATE,
                        changeDate: new Date(),
                        containingChangeSet: changeSetUri)))

        maint.createResource(assoc1)

        changeSetService.commitChangeSet(changeSetUri)

        def ed = query.getResourceSummaries([
                getRestrictions: {
                    new AssociationQueryServiceRestrictions(
                            sourceEntity: new EntityNameOrURI(entityName: new ScopedEntityName(name: "p")),
                            codeSystemVersion: new NameOrURI(name: "TESTCSVERSION")
                    )
                },
                getReadContext: {null},
                getFilterComponent: {null}
        ] as AssociationQuery, null, new Page()
        )

        assertNotNull ed
        assertEquals 1, ed.entries.size()
    }

    @Test
    void TestSourceOfQueryInvalid(){

        def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

        def assoc1 = new Association(associationID:"http://someAssoc")
        assoc1.setSubject(new URIAndEntityName(name:"p", namespace:"ns", uri:"http://p"))

        assoc1.addTarget(new StatementTarget())
        assoc1.getTarget(0).setEntity(new URIAndEntityName(name:"c", namespace:"ns", uri:"http://c"))

        assoc1.setPredicate(new PredicateReference(name:"predicatename", namespace:"ns", uri:"uri"))

        assoc1.setAssertedBy(new CodeSystemVersionReference(
                codeSystem: new CodeSystemReference(content:"TESTCS"),
                version: new NameAndMeaningReference(content:"TESTCSVERSION")))

        assoc1.setChangeableElementGroup(new ChangeableElementGroup(
                changeDescription: new ChangeDescription(
                        changeType: ChangeType.CREATE,
                        changeDate: new Date(),
                        containingChangeSet: changeSetUri)))

        maint.createResource(assoc1)

        changeSetService.commitChangeSet(changeSetUri)

        def ed = query.getResourceSummaries([
                getRestrictions: {
                    new AssociationQueryServiceRestrictions(sourceEntity: new EntityNameOrURI(entityName: new ScopedEntityName(name: "__INVALID__")) )
                },
                getReadContext: {null},
                getFilterComponent: {null}
        ] as AssociationQuery, null, new Page()
        )

        assertNotNull ed
        assertEquals 0, ed.entries.size()
    }

    @Test
    void TestTargetofQuery(){

        def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

        def assoc1 = new Association(associationID:"http://someAssoc")
        assoc1.setSubject(new URIAndEntityName(name:"p", namespace:"ns", uri:"http://p"))

        assoc1.addTarget(new StatementTarget())
        assoc1.getTarget(0).setEntity(new URIAndEntityName(name:"c", namespace:"ns", uri:"http://c"))

        assoc1.setPredicate(new PredicateReference(name:"predicatename", namespace:"ns", uri:"uri"))

        assoc1.setAssertedBy(new CodeSystemVersionReference(
                codeSystem: new CodeSystemReference(content:"TESTCS"),
                version: new NameAndMeaningReference(content:"TESTCSVERSION")))

        assoc1.setChangeableElementGroup(new ChangeableElementGroup(
                changeDescription: new ChangeDescription(
                        changeType: ChangeType.CREATE,
                        changeDate: new Date(),
                        containingChangeSet: changeSetUri)))

        maint.createResource(assoc1)

        changeSetService.commitChangeSet(changeSetUri)

        def ed = query.getResourceSummaries([
                getRestrictions: {
                    new AssociationQueryServiceRestrictions(targetEntity: new EntityNameOrURI(entityName: new ScopedEntityName(name: "c")) )
                },
                getReadContext: {null},
                getFilterComponent: {null}
        ] as AssociationQuery, null, new Page()
        )

        assertNotNull ed
        assertEquals 1, ed.entries.size()
    }

    @Test
    void TestTargetOfQueryInvalid(){

        def changeSetUri = changeSetService.createChangeSet().getChangeSetURI()

        def assoc1 = new Association(associationID:"http://someAssoc")
        assoc1.setSubject(new URIAndEntityName(name:"p", namespace:"ns", uri:"http://p"))

        assoc1.addTarget(new StatementTarget())
        assoc1.getTarget(0).setEntity(new URIAndEntityName(name:"c", namespace:"ns", uri:"http://c"))

        assoc1.setPredicate(new PredicateReference(name:"predicatename", namespace:"ns", uri:"uri"))

        assoc1.setAssertedBy(new CodeSystemVersionReference(
                codeSystem: new CodeSystemReference(content:"TESTCS"),
                version: new NameAndMeaningReference(content:"TESTCSVERSION")))

        assoc1.setChangeableElementGroup(new ChangeableElementGroup(
                changeDescription: new ChangeDescription(
                        changeType: ChangeType.CREATE,
                        changeDate: new Date(),
                        containingChangeSet: changeSetUri)))

        maint.createResource(assoc1)

        changeSetService.commitChangeSet(changeSetUri)

        def ed = query.getResourceSummaries([
                getRestrictions: {
                    new AssociationQueryServiceRestrictions(targetEntity: new EntityNameOrURI(entityName: new ScopedEntityName(name: "__INVALID__")) )
                },
                getReadContext: {null},
                getFilterComponent: {null}
        ] as AssociationQuery, null, new Page()
        )

        assertNotNull ed
        assertEquals 0, ed.entries.size()
    }

}
