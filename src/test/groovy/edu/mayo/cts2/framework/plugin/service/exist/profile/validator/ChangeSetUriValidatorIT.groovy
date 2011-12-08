package edu.mayo.cts2.framework.plugin.service.exist.profile.validator;

import javax.annotation.Resource

import org.junit.Test

import edu.mayo.cts2.framework.model.exception.changeset.ChangeSetIsNotOpenException
import edu.mayo.cts2.framework.model.exception.changeset.UnknownChangeSetException
import edu.mayo.cts2.framework.plugin.service.exist.profile.BaseServiceDbCleaningBase
import edu.mayo.cts2.framework.plugin.service.exist.profile.update.ExistChangeSetService

public class ChangeSetUriValidatorIT extends BaseServiceDbCleaningBase {
	
	@Resource
	ExistChangeSetService existChangeSetService
	
	@Resource
	ChangeSetUriValidator changeSetUriValidator
	
	@Test
	void TestValidChangeSet(){
		def changeSetUri = existChangeSetService.createChangeSet().getChangeSetURI();

		changeSetUriValidator.validateChangeSet(changeSetUri)
	}
	
	@Test(expected=ChangeSetIsNotOpenException)
	void TestIsNotOpenException(){
		def changeSetUri = existChangeSetService.createChangeSet().getChangeSetURI();
		
		existChangeSetService.commitChangeSet(changeSetUri)
		
		changeSetUriValidator.validateChangeSet(changeSetUri)
	}
	
	@Test(expected=UnknownChangeSetException)
	void TestUnknownChangeSetException(){
		def changeSetUri = existChangeSetService.createChangeSet().getChangeSetURI();
		
		changeSetUriValidator.validateChangeSet("__INVALID__")
	}

}
