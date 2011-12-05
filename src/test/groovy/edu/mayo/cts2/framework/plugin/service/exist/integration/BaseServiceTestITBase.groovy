package edu.mayo.cts2.framework.plugin.service.exist.integration;

import org.junit.After
import org.junit.Before

import edu.mayo.cts2.framework.core.client.Cts2RestClient
import edu.mayo.cts2.framework.model.core.types.FinalizableState
import edu.mayo.cts2.framework.model.service.core.UpdateChangeSetMetadataRequest
import edu.mayo.cts2.framework.model.service.core.UpdatedState
import edu.mayo.cts2.framework.model.updates.ChangeSet


class BaseServiceTestITBase {
	
	public Cts2RestClient client = new Cts2RestClient()
	
	public String server = "http://localhost:5150/webapp-rest/"
	
	def currentResourceUrl
	
	String changeSetUri
	
	@Before void createChangeSet(){
		def uri = client.postCts2Resource(server + "changeset", null);
		
		def changeSet = client.getCts2Resource(server+uri, ChangeSet.class);
		
		changeSetUri = changeSet.getChangeSetURI()
	}
	
	@After void rollbackChangeSet(){
		if(changeSetUri != null){
			client.deleteCts2Resource(server + "changeset/"+changeSetUri,)
		}
	}
	
	
	void commitChangeSet(){
		def updateRequest = new UpdateChangeSetMetadataRequest()
		updateRequest.setUpdatedState(new UpdatedState(state:FinalizableState.FINAL))
		
		client.postCts2Resource(server + "changeset/"+changeSetUri, updateRequest);
		
		changeSetUri = null
	}

	void deleteResource(url){
		url = url +"?changesetcontext="+changeSetUri;
		client.deleteCts2Resource(server + url);
	}
	
	def read(url,clazz){
		def paramMark = url.contains("?")?"&":"?"

		client.getCts2Resource(server+url+paramMark+"changesetcontext="+changeSetUri, clazz);

	}
	
	def readCurrent(url,clazz){
		client.getCts2Resource(server+url, clazz);
	}

	URI createResource(url,resource){
		def returnuri = client.postCts2Resource(server + url +"?changesetcontext="+changeSetUri, resource);
		
		currentResourceUrl = returnuri
		
		returnuri
	}
	

}
