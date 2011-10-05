package edu.mayo.cts2.framework.plugin.service.exist.integration;

import org.junit.Test

import edu.mayo.cts2.framework.model.association.Association
import edu.mayo.cts2.framework.model.association.AssociationMsg
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference
import edu.mayo.cts2.framework.model.core.PredicateReference
import edu.mayo.cts2.framework.model.core.StatementTarget
import edu.mayo.cts2.framework.model.core.URIAndEntityName
import static org.junit.Assert.*


class AssociationReadServiceTestIT extends BaseServiceTestITBase {

	
	@Test void testGetAssociationByNameCycle(){

		def resourceURI = server +  "codesystem/TESTCS/version/TESTCSVERSION/association/associd"
		
		def entry = new Association(associationID:"associd")
		entry.setSubject(new URIAndEntityName(name:"name", namespace:"ns"))
		
		entry.addTarget(new StatementTarget())
		entry.getTarget(0).setEntity(new URIAndEntityName(name:"target", namespace:"ns"))
		
		entry.setPredicate(new PredicateReference(name:"predicatename", namespace:"ns"))
		
		entry.setAssertedBy(new CodeSystemVersionReference())
		entry.getAssertedBy().setVersion(new NameAndMeaningReference())
		
		client.putCts2Resource(resourceURI, entry)
		
		def msg = 
			client.getCts2Resource(resourceURI, AssociationMsg.class)
			
		assertEquals entry, msg.getAssociation()
	}
}
