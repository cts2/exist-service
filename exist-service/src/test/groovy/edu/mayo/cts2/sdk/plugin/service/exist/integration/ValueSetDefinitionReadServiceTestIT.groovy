package edu.mayo.cts2.sdk.plugin.service.exist.integration;

import static org.junit.Assert.*

import org.junit.Test

import edu.mayo.cts2.sdk.model.core.CodeSystemReference
import edu.mayo.cts2.sdk.model.core.SourceAndNotation
import edu.mayo.cts2.sdk.model.core.ValueSetReference
import edu.mayo.cts2.sdk.model.core.types.SetOperator
import edu.mayo.cts2.sdk.model.valuesetdefinition.CompleteCodeSystemReference
import edu.mayo.cts2.sdk.model.valuesetdefinition.ValueSetDefinition
import edu.mayo.cts2.sdk.model.valuesetdefinition.ValueSetDefinitionEntry
import edu.mayo.cts2.sdk.model.valuesetdefinition.ValueSetDefinitionMsg

class ValueSetDefinitionReadServiceTestIT extends BaseServiceTestITBase {
			
	@Test void testGetValueSetByNameCycle(){

		def resourceURI = server +  "valueset/TESTVALUESET/definition/TESTDEFURI"
		
		def entry = new ValueSetDefinition(about:"testAbout", documentURI:"TESTDEFURI")
		entry.setSourceAndNotation(new SourceAndNotation())
		entry.setDefinedValueSet(new ValueSetReference())
		entry.addEntry(new ValueSetDefinitionEntry())
		entry.getEntry(0).setCompleteCodeSystem(new CompleteCodeSystemReference())
		entry.getEntry(0).getCompleteCodeSystem().setCodeSystem(new CodeSystemReference())
		entry.getEntry(0).setOperator(SetOperator.UNION)
		entry.getEntry(0).setEntryOrder(1l);
	
		client.putCts2Resource(resourceURI, entry)
		
		def msg = 
			client.getCts2Resource(resourceURI,ValueSetDefinitionMsg.class)
			
		assertEquals entry, msg.getValueSetDefinition()
	}
}
