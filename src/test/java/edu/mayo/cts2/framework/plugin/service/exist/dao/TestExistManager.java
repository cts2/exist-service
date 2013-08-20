package edu.mayo.cts2.framework.plugin.service.exist.dao;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.SystemUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.StringWriter;
import java.util.UUID;


public class TestExistManager extends ExistManager {

	
	@Override
	public void afterPropertiesSet() {
		try {
			File tmpDir = SystemUtils.getJavaIoTmpDir();

			File file = new File(tmpDir.getAbsolutePath() + "/"+ UUID.randomUUID().toString() + "/");
			
			if(!file.exists()){
				file.mkdir();
			}

			FileUtils.cleanDirectory(file);
			
			System.setProperty("exist.home", file.getAbsolutePath());
			
			Resource confXml = new ClassPathResource("conf.xml");
			
			File confXmlFile = new File(file.getAbsolutePath() + "/conf.xml");
			confXmlFile.createNewFile();
			
			StringWriter writer = new StringWriter();
			IOUtils.copy(confXml.getInputStream(), writer);
			
			FileUtils.writeStringToFile(confXmlFile, writer.toString());

			this.setUri("xmldb:exist:///db/");
			System.setProperty("exist.initdb", "true");

			FileUtils.forceDeleteOnExit(file);
			
			this.setUserName("admin");
			this.setPassword("");
			
			super.afterPropertiesSet();
		
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
