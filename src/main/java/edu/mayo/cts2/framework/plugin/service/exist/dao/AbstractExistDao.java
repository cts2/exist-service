package edu.mayo.cts2.framework.plugin.service.exist.dao;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Component;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XQueryService;

import edu.mayo.cts2.framework.core.url.UrlConstructor;
import edu.mayo.cts2.framework.model.core.Changeable;
import edu.mayo.cts2.framework.model.core.DirectoryEntry;
import edu.mayo.cts2.framework.model.core.ResourceDescription;
import edu.mayo.cts2.framework.model.core.ResourceDescriptionDirectoryEntry;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;

@Component
public abstract class AbstractExistDao {
	
	protected final Log log = LogFactory.getLog(getClass().getName());

	@Autowired
	private UrlConstructor urlConstructor;

	@Autowired
	private ExistManager existManager;

	private static final String XML_RESOURCE_TYPE = "XMLResource";

	protected static final String CTS2_RESOURCES_PATH = "/cts2resources";
	
	private static final String XML_SUFFIX = ".xml";

	@Autowired
	private Marshaller marshaller;

	@Autowired
	private Unmarshaller umarshaller;

	protected void createAndStoreResource(Object cts2Resource,
			Collection collection, String name) throws XMLDBException {
		Resource resource = collection.createResource(
				ExistServiceUtils.getExistResourceName(name) + XML_SUFFIX,
				XML_RESOURCE_TYPE);

		StringWriter sw = new StringWriter();
		StreamResult sr = new StreamResult(sw);

		try {

			this.marshaller.marshal(cts2Resource, sr);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		resource.setContent(sw.toString());

		collection.storeResource(resource);
	}


	protected Resource doGetResource(String name, String collection) {
		try {
			return existManager.getOrCreateCollection(collection)
					.getResource(ExistServiceUtils.getExistResourceName(name) + XML_SUFFIX);
		} catch (XMLDBException e) {
			throw new RuntimeException(e);
		}
	}
	
	public <S extends DirectoryEntry, R extends Changeable> DirectoryResult<S> getResourceSummaries(
			String collectionPath,
			int start,
			int max,
			SummaryTransform<S, R> transform) {
		return this.getResourceSummaries(collectionPath, null, start, max, transform);
	}

	public <S, R> DirectoryResult<S> getResourceSummaries(
			String collectionPath,
			String xpath,
			int start,
			int max,
			SummaryTransform<S, R> transform) {
		try {
	
			XQueryService xpqs =
				this.existManager.getXQueryService();

			String path = "collection('"
				+ collectionPath + "')";
			
			if(StringUtils.isNotBlank(xpath)){
				path = path  + xpath;
			}
			
			ResourceSet set = xpqs.query(path);
	
			long size = set.getSize();
			
			List<S> returnList = new ArrayList<S>();

		
			for(int i=start;i< start + max && i < size;i++) {
				Resource res = set.getResource(i);
			
				@SuppressWarnings("unchecked")
				R entry = (R) this.unmarshallResource(res);

				returnList.add(transform.transform(entry, res));
			}
		
			return new DirectoryResult<S>(returnList, size <= returnList.size(), (start + max) >= size);
		} catch (XMLDBException e) {
			throw new IllegalStateException(e);
		}
	}

	protected Object unmarshallResource(Resource resource) {
		try {
			String xml = (String) resource.getContent();

			Object obj = this.umarshaller.unmarshal(new StreamSource(
					new StringReader(xml)));

			return obj;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected <S extends ResourceDescriptionDirectoryEntry, R extends ResourceDescription> S baseTransform(
			S summary, R resource) {

		summary.setAbout(resource.getAbout());
		summary.setFormalName(resource.getFormalName());
		summary.setResourceSynopsis(resource.getResourceSynopsis());

		return summary;
	}

	protected ExistManager getExistManager() {
		return existManager;
	}

	protected void setExistManager(
			ExistManager existManager) {
		this.existManager = existManager;
	}

	public void setUrlConstructor(UrlConstructor urlConstructor) {
		this.urlConstructor = urlConstructor;
	}

	public UrlConstructor getUrlConstructor() {
		return urlConstructor;
	}
}
