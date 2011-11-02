package edu.mayo.cts2.framework.plugin.service.exist.integration;

import static org.junit.Assert.*

import org.junit.Ignore
import org.junit.Test

import edu.mayo.cts2.framework.model.core.CodeSystemReference
import edu.mayo.cts2.framework.model.core.SourceAndNotation
import edu.mayo.cts2.framework.model.core.ValueSetReference
import edu.mayo.cts2.framework.model.core.types.SetOperator
import edu.mayo.cts2.framework.model.valuesetdefinition.CompleteCodeSystemReference
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinition
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinitionEntry
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinitionMsg

class ValueSetDefinitionReadServiceTestIT extends BaseServiceTestITBase {
			
	@Test void testGetValueSetByNameCycle(){

		def getResourceURI = server +  "valueset/TESTVALUESET/definition/http://def/uri/test"
		def postResourceURI = "valuesetdefinition"
		
		def entry = new ValueSetDefinition(about:"testAbout", documentURI:"http://def/uri/test")
		entry.setSourceAndNotation(new SourceAndNotation())
		entry.setDefinedValueSet(new ValueSetReference())
		entry.addEntry(new ValueSetDefinitionEntry())
		entry.getEntry(0).setCompleteCodeSystem(new CompleteCodeSystemReference())
		entry.getEntry(0).getCompleteCodeSystem().setCodeSystem(new CodeSystemReference())
		entry.getEntry(0).setOperator(SetOperator.UNION)
		entry.getEntry(0).setEntryOrder(1l);
	
		this.createResource(postResourceURI,entry)
		
		def msg = 
			client.getCts2Resource(getResourceURI,ValueSetDefinitionMsg.class)
			
		assertEquals entry.getDocumentURI(), msg.getValueSetDefinition().getDocumentURI()
	}
}
