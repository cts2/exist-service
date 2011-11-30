package edu.mayo.cts2.framework.plugin.service.exist.integration;

import static org.junit.Assert.*

import org.apache.commons.lang.StringUtils

import edu.mayo.cts2.framework.model.association.Association
import edu.mayo.cts2.framework.model.association.AssociationMsg
import edu.mayo.cts2.framework.model.core.CodeSystemReference
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference
import edu.mayo.cts2.framework.model.core.PredicateReference
import edu.mayo.cts2.framework.model.core.StatementTarget
import edu.mayo.cts2.framework.model.core.URIAndEntityName


class AssociationReadServiceTestIT extends BaseReadServiceTestITBase {

	def associationUri = "http://some/associ/1"

	@Override
	public Object getResourceClass() {
		AssociationMsg.class
	}

	@Override
	public Object getReadByNameUrl() {
		StringUtils.substringBefore(currentResourceUrl.toString(), "?")
	}

	@Override
	public Object getReadByUriUrl() {
		"associationbyuri?uri="+associationUri
	}

	@Override
	public Object getCreateUrl() {
		"association"
	}

	@Override
	public Object getResource() {
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

	@Override
	resourcesEqual(Object resource) {
		true
	}
	
	
}
