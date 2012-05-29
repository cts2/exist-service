package edu.mayo.cts2.framework.plugin.service.exist.integration;

import static org.junit.Assert.*
import edu.mayo.cts2.framework.model.core.CodeSystemReference
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference
import edu.mayo.cts2.framework.model.core.StatementTarget
import edu.mayo.cts2.framework.model.core.URIAndEntityName
import edu.mayo.cts2.framework.model.statement.Statement
import edu.mayo.cts2.framework.model.statement.StatementDirectory
import edu.mayo.cts2.framework.model.statement.StatementSubject

class StatementQueryServiceTestIT extends BaseQueryServiceTestITBase {

	@Override
	getResourceClass() {
		StatementDirectory.class
	}

	@Override
	getQueryUrl() {
		"statements"
	}
	
	@Override
	getCreateUrl(){
		"statement"
	}

	@Override
	List getResources() {
		[createEntry("http://testAbout1"),
			createEntry("http://testAbout2"),
			createEntry("http://testAbout3")]
	}

	def createEntry(uri) {
		def entry = new Statement(statementURI:uri)
		entry.setSubject(new StatementSubject())
		entry.setPredicate(new URIAndEntityName(name:"name",namespace:"namespace", uri:"uri"))
		
		def target = (new URIAndEntityName(name:"entityname",namespace:"namespace", uri:"uri"))
		entry.addTarget(new StatementTarget(entity:target))
	
		entry.setAssertedBy(new CodeSystemVersionReference())
		entry.getAssertedBy().setCodeSystem(new CodeSystemReference(content:"statementtestcs"))
		entry.getAssertedBy().setVersion(new NameAndMeaningReference(content:"statementtestcsversion"))
		
		entry
	}
	
}
