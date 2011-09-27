package edu.mayo.cts2.sdk.plugin.service.exist.dao

import org.junit.Assert.assertEquals
import org.junit.Test
import org.scalatest.junit.AssertionsForJUnit

class EntityDescriptionExistDaoTest extends AssertionsForJUnit {
 
 
  @Test def testEntitiesPath() {
    var dao = new EntityDescriptionExistDao()
    
    assertEquals(dao.doGetResourceBasePath(), "/entities")
  }  
}