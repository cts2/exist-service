package edu.mayo.cts2.framework.plugin.service.exist.profile;

import java.io.StringReader;

import javax.xml.transform.stream.StreamSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Component;
import org.xmldb.api.base.Resource;

@Component
public class ResourceUnmarshaller {

	@Autowired
	private Unmarshaller umarshaller;

	public Object unmarshallResource(Resource resource) {
		if(resource == null){
			return null;
		}
		try {
			String xml = (String) resource.getContent();

			Object obj = this.umarshaller.unmarshal(new StreamSource(
					new StringReader(xml)));

			return obj;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
