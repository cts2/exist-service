package edu.mayo.cts2.framework.plugin.service.exist.integration;

import static org.junit.Assert.*
import edu.mayo.cts2.framework.model.association.Association
import edu.mayo.cts2.framework.model.association.AssociationDirectory
import edu.mayo.cts2.framework.model.core.CodeSystemReference
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference
import edu.mayo.cts2.framework.model.core.PredicateReference
import edu.mayo.cts2.framework.model.core.StatementTarget
import edu.mayo.cts2.framework.model.core.URIAndEntityName

class AssociationQueryServiceTestIT extends BaseQueryServiceTestITBase {

	@Override
	getResourceClass() {
		AssociationDirectory.class
	}

	@Override
	getQueryUrl() {
		"associations"
	}
	
	@Override
	getCreateUrl(){
		"association"
	}

	@Override
	List getResources() {
		[createAssociation("testAbout1"),
			createAssociation("testAbout2"),
			createAssociation("testAbout3")]
	}

	def createAssociation(associationUri) {
		def entry = new Association(associationID:associationUri)
		entry.setSubject(new URIAndEntityName(name:"name", namespace:"ns"))
		
		entry.addTarget(new StatementTarget())
		entry.getTarget(0).setEntity(new URIAndEntityName(name:"target", namespace:"ns"))
		
		entry.setPredicate(new PredicateReference(name:"predicatename", namespace:"ns"))
		
		entry.setAssertedBy(new CodeSystemVersionReference(
			codeSystem: new CodeSystemReference(content:"TESTCS"),
			version: new NameAndMeaningReference(content:"TESTAssocCSVERSION")))
		
		entry
	}
	
}
