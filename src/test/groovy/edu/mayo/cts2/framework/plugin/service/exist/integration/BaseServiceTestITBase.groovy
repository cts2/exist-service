package edu.mayo.cts2.framework.plugin.service.exist.integration;

import edu.mayo.cts2.framework.core.client.Cts2RestClient


class BaseServiceTestITBase {
	
	public Cts2RestClient client = new Cts2RestClient()
	
	public String server = "http://localhost:8080/webapp-rest/"
	

}
