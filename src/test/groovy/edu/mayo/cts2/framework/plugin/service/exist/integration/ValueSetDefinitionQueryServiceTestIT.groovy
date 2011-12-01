package edu.mayo.cts2.framework.plugin.service.exist.integration;

import static org.junit.Assert.*
import edu.mayo.cts2.framework.model.core.CodeSystemReference
import edu.mayo.cts2.framework.model.core.SourceAndNotation
import edu.mayo.cts2.framework.model.core.ValueSetReference
import edu.mayo.cts2.framework.model.core.types.SetOperator
import edu.mayo.cts2.framework.model.valuesetdefinition.CompleteCodeSystemReference
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinition
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinitionDirectory
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinitionEntry

class ValueSetDefinitionQueryServiceTestIT extends BaseQueryServiceTestITBase {

	@Override
	getResourceClass() {
		ValueSetDefinitionDirectory.class
	}

	@Override
	getQueryUrl() {
		"valuesetdefinitions"
	}
	
	@Override
	getCreateUrl(){
		"valuesetdefinition"
	}

	@Override
	List getResources() {
		[createEntry("http://testAbout1"),
			createEntry("http://testAbout2"),
			createEntry("http://testAbout3")]
	}

	def createEntry(uri) {
		def entry = new ValueSetDefinition(about:"http://testAbout", documentURI:uri)
		entry.setSourceAndNotation(new SourceAndNotation())
		entry.setDefinedValueSet(new ValueSetReference(content:"testvs"))
		entry.addEntry(new ValueSetDefinitionEntry())
		entry.getEntry(0).setCompleteCodeSystem(new CompleteCodeSystemReference())
		entry.getEntry(0).getCompleteCodeSystem().setCodeSystem(new CodeSystemReference())
		entry.getEntry(0).setOperator(SetOperator.UNION)
		entry.getEntry(0).setEntryOrder(1l);
		
		entry
	}
	
}
