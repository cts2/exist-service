package edu.mayo.cts2.framework.plugin.service.exist.dao;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Marshaller;
import org.springframework.stereotype.Component;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.CompiledExpression;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XQueryService;

import edu.mayo.cts2.framework.model.exception.UnspecifiedCts2RuntimeException;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;

@Component
public class ExistDaoImpl implements ExistResourceDao {
	
	protected final Log log = LogFactory.getLog(getClass().getName());

	@Autowired
	private ExistManager existManager;

	private static final String XML_RESOURCE_TYPE = "XMLResource";

	protected static final String CTS2_RESOURCES_PATH = "/cts2resources";
	
	private static final String XML_SUFFIX = ".xml";

	@Autowired
	private Marshaller marshaller;

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
	
	public void storeResource(String path, String name, Object entry) {
		Collection collection = null;
		try {
			collection = this.getExistManager()
					.getOrCreateCollection(this.getResourcePath(path));

			this.createAndStoreResource(entry, collection, name );

		} catch (XMLDBException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if(collection != null){
					collection.close();
				}
			} catch (XMLDBException e) {
				this.log.warn(e);
			}
		}
	}

	public Resource getResource(String path, String name) {
		Resource resource = this.doGetResource(name, this.getResourcePath(path));

		return resource;
	}
	
	public void deleteResource(String path, String name) {
		Resource resource = this.doGetResource(name, this.getResourcePath(path));
		
		try {
			resource.getParentCollection().removeResource(resource);
		} catch (XMLDBException e) {
			throw new UnspecifiedCts2RuntimeException(e);
		}

	}
	
	
	public ResourceSet query(
			String collectionPath, 
			String queryString, 
			int start, 
			int max){
		try {
			XQueryService xqueryService = this.getExistManager().getXQueryService(collectionPath);

			CompiledExpression expression = 
					xqueryService.compile(queryString);
			
			return xqueryService.execute(expression);

		} catch (XMLDBException e) {
			throw new UnspecifiedCts2RuntimeException(e);
		}
	}
	
	@Override
	public Resource getResourceByXpath(String collectionPath, String xpathQuery) {
		Resource resource;
		try {
		
			ResourceSet resourceSet = this.query(collectionPath, xpathQuery, 0, 1);

			long size = resourceSet.getSize();
			
			if (size == 0) {
				return null;
			}
			
			//this should be caught during insert. If we get this far, the service is in an
			//illegal state.
			if (size > 1) {
				throw new IllegalStateException("Duplicate Entries found.");
			}

			resource = resourceSet.getResource(0);
			
			return resource;
			
		} catch (XMLDBException e) {
			throw new UnspecifiedCts2RuntimeException(e);
		}
	}

	protected String getResourcePath(String path) {
		return CTS2_RESOURCES_PATH + "/" + path;
	}

	protected Resource doGetResource(String name, String collection) {
		try {
			return existManager.getOrCreateCollection(collection)
					.getResource(ExistServiceUtils.getExistResourceName(name) + XML_SUFFIX);
		} catch (XMLDBException e) {
			throw new RuntimeException(e);
		}
	}

	protected ExistManager getExistManager() {
		return existManager;
	}

	protected void setExistManager(
			ExistManager existManager) {
		this.existManager = existManager;
	}
}
