package edu.mayo.cts2.framework.plugin.service.exist.dao

import org.junit.Assert.assertEquals
import org.junit.Test
import org.scalatest.junit.AssertionsForJUnit

class MapExistDaoTest extends AssertionsForJUnit {
 
 
  @Test def testMapPath() {
    var dao = new MapExistDao()
    
    assertEquals(dao.doGetResourceBasePath(), "/maps")
  }

  
}