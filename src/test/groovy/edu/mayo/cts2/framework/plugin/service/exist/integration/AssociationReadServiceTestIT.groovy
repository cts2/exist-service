package edu.mayo.cts2.framework.plugin.service.exist.integration;

import static org.junit.Assert.*

import org.junit.Test

import edu.mayo.cts2.framework.model.association.Association
import edu.mayo.cts2.framework.model.association.AssociationMsg
import edu.mayo.cts2.framework.model.core.CodeSystemReference
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference
import edu.mayo.cts2.framework.model.core.PredicateReference
import edu.mayo.cts2.framework.model.core.StatementTarget
import edu.mayo.cts2.framework.model.core.URIAndEntityName


class AssociationReadServiceTestIT extends BaseServiceTestITBase {

	
	@Test void testGetAssociationByNameCycle(){
		
		def associationUri = "http://some/associ/1"

		def postResourceURI = "association"
		
		def entry = new Association(associationID:associationUri)
		entry.setSubject(new URIAndEntityName(name:"name", namespace:"ns"))
		
		entry.addTarget(new StatementTarget())
		entry.getTarget(0).setEntity(new URIAndEntityName(name:"target", namespace:"ns"))
		
		entry.setPredicate(new PredicateReference(name:"predicatename", namespace:"ns"))
		
		entry.setAssertedBy(new CodeSystemVersionReference(
			codeSystem: new CodeSystemReference(content:"TESTCS"),
			version: new NameAndMeaningReference(content:"TESTCSVERSION")))

		def getResourceURI = createResource(postResourceURI, entry)
		
		def msg = 
			client.getCts2Resource(server + getResourceURI.toString(), AssociationMsg.class)
			
		assertEquals entry.getAssociationID(), msg.getAssociation().getAssociationID()

	}
}
