package edu.mayo.cts2.framework.plugin.service.exist.integration;

import org.junit.Before

import edu.mayo.cts2.framework.core.client.Cts2RestClient
import edu.mayo.cts2.framework.model.core.types.FinalizableState
import edu.mayo.cts2.framework.model.service.core.UpdateChangeSetMetadataRequest
import edu.mayo.cts2.framework.model.service.core.UpdatedState
import edu.mayo.cts2.framework.model.updates.ChangeSet


class BaseServiceTestITBase {
	
	public Cts2RestClient client = new Cts2RestClient()
	
	public String server = "http://localhost:5150/webapp-rest/"
	

	void createResource(url,resource){
		def uri = client.postCts2Resource(server + "changeset", null);
		
		def changeSet = client.getCts2Resource(server+uri, ChangeSet.class);
		
		def changeSetUri = changeSet.getChangeSetURI()
		
		client.postCts2Resource(server + url +"?changeseturi="+changeSetUri, resource);
		
		def update = new UpdateChangeSetMetadataRequest()
		update.setUpdatedState(new UpdatedState(state: FinalizableState.FINAL))
		
		client.postCts2Resource(server + "changeset/"+changeSetUri, update);
	}
	

}
