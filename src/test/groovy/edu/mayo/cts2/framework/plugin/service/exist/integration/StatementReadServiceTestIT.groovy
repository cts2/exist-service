package edu.mayo.cts2.framework.plugin.service.exist.integration;

import static org.junit.Assert.*

import org.junit.Ignore
import org.junit.Test

import edu.mayo.cts2.framework.model.core.CodeSystemReference
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference
import edu.mayo.cts2.framework.model.core.StatementTarget
import edu.mayo.cts2.framework.model.core.URIAndEntityName
import edu.mayo.cts2.framework.model.statement.Statement
import edu.mayo.cts2.framework.model.statement.StatementMsg
import edu.mayo.cts2.framework.model.statement.StatementSubject

class StatementReadServiceTestIT extends BaseServiceTestITBase {
			
	@Test void testGetStatementByURICycle(){

		def getResourceURI = server +  "statement/http://some/stmnt1"
		def postResourceURI = "statement"
		
		def entry = new Statement(statementURI:"http://some/stmnt1")
		entry.setSubject(new StatementSubject())
		entry.setPredicate(new URIAndEntityName(name:"name",namespace:"namespace"))
		
		def target = (new URIAndEntityName(name:"entityname",namespace:"namespace"))
		entry.addTarget(new StatementTarget(entity:target))
	
		entry.setAssertedBy(new CodeSystemVersionReference())
		entry.getAssertedBy().setCodeSystem(new CodeSystemReference())
		entry.getAssertedBy().setVersion(new NameAndMeaningReference())
		
		this.createResource(postResourceURI,entry)
		
		def msg = 
			client.getCts2Resource(getResourceURI,StatementMsg.class)
			
		assertEquals entry, msg.getStatement()
	}
}
