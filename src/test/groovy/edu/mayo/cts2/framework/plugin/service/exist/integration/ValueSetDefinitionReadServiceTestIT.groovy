package edu.mayo.cts2.framework.plugin.service.exist.integration
import edu.mayo.cts2.framework.model.core.CodeSystemReference
import edu.mayo.cts2.framework.model.core.SourceAndNotation
import edu.mayo.cts2.framework.model.core.ValueSetReference
import edu.mayo.cts2.framework.model.core.types.SetOperator
import edu.mayo.cts2.framework.model.valuesetdefinition.CompleteCodeSystemReference
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinition
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinitionEntry
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinitionMsg
import org.junit.Ignore

class ValueSetDefinitionReadServiceTestIT extends BaseReadServiceTestITBase {

	@Override
	public Object getResourceClass() {
		ValueSetDefinitionMsg.class
	}

	@Override
	public Object getReadByNameUrl() {
		null
	}

	@Override
	public Object getReadByUriUrl() {
		"valuesetdefinitionbyuri?uri=http://def/uri/testByURI"
	}

	@Override
	public Object getCreateUrl() {
		"valuesetdefinition"
	}

	@Override
	public Object getResource() {
		def entry = new ValueSetDefinition(about:"http://def/uri/testByURI")
		entry.setSourceAndNotation(new SourceAndNotation())
		entry.setDefinedValueSet(new ValueSetReference("vsname"))
		entry.setDocumentURI("http://def/uri/testByURI")
		entry.addEntry(new ValueSetDefinitionEntry())
		entry.getEntry(0).setCompleteCodeSystem(new CompleteCodeSystemReference())
		entry.getEntry(0).getCompleteCodeSystem().setCodeSystem(new CodeSystemReference())
		entry.getEntry(0).setOperator(SetOperator.UNION)
		entry.getEntry(0).setEntryOrder(1l);
		
		entry
	}

	@Override
	public Object resourcesEqual(msg) {
		msg.getValueSetDefinition().getDocumentURI().equals("http://def/uri/testByURI")
	}

}
