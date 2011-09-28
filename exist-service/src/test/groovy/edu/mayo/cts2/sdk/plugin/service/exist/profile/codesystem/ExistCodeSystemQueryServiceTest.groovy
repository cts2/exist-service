package edu.mayo.cts2.framework.plugin.service.exist.profile.codesystem;

import static org.junit.Assert.*

import org.junit.Test

import edu.mayo.cts2.framework.plugin.service.exist.profile.codesystem.ExistCodeSystemQueryService;
import edu.mayo.cts2.framework.plugin.service.exist.restrict.directory.XpathDirectoryBuilder.XpathState

class ExistCodeSystemQueryServiceTest {

	ExistCodeSystemQueryService service = new ExistCodeSystemQueryService()
	
	@Test
	void stateBuilderOneRestriction(){
		def builder = new ExistCodeSystemQueryService$CodeSystemNameStateUpdater()
		
		def state = new XpathState()
		
		def updatedState = builder.updateState(state, null, "testCsName")
		
		assertEquals "//codesystem:CodeSystemCatalogEntry[@codeSystemName = 'testCsName']", updatedState.getXpath()
	}
	
	@Test
	void stateBuilderTwoRestrictions(){
		def builder = new ExistCodeSystemQueryService$CodeSystemNameStateUpdater()
		
		def state = new XpathState(xpath:"test:test[@someOtherThing = 'test']")
		
		def updatedState = builder.updateState(state, null, "testCsName")
		
		assertEquals "test:test[@someOtherThing = 'test'] //codesystem:CodeSystemCatalogEntry[@codeSystemName = 'testCsName']", 
			updatedState.getXpath()
	}
	
	
}
