package edu.mayo.cts2.sdk.plugin.service.exist.dao

import org.junit.Assert.assertEquals
import org.junit.Test
import org.scalatest.junit.AssertionsForJUnit

class CodeSystemExistDaoTest extends AssertionsForJUnit {
 
 
  @Test def testCodeSystemPath() {
    var dao = new CodeSystemExistDao()
    
    assertEquals(dao.doGetResourceBasePath(), "/codesystems")
  }

  
}