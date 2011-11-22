package edu.mayo.cts2.framework.plugin.service.exist.profile;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Marshaller;
import org.springframework.stereotype.Component;

@Component
public class ResourceMarshaller {

	@Autowired
	private Marshaller marshaller;

	public String marshallResource(Object resource) {
		if(resource == null){
			return null;
		}
		StringWriter sw = new StringWriter();
		StreamResult sr = new StreamResult(sw);

		try {

			this.marshaller.marshal(resource, sr);
			
			return sw.toString();

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
