package edu.mayo.cts2.framework.plugin.service.exist.dao;

import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.exist.validation.service.ValidationService;
import org.exist.xmldb.DatabaseInstanceManager;
import org.exist.xmldb.IndexQueryService;
import org.springframework.beans.factory.InitializingBean;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XQueryService;
import org.xmldb.api.modules.XUpdateQueryService;

import edu.mayo.cts2.framework.core.plugin.PluginConfig;
import edu.mayo.cts2.framework.core.xml.Cts2Marshaller;
import edu.mayo.cts2.framework.model.exception.Cts2RuntimeException;
import edu.mayo.cts2.framework.plugin.service.exist.ExistServiceConstants;

public class ExistManager implements InitializingBean {	
	
	private static final String EXIST_SERVICE_CONFIG_NAMESPACE = "exist-service";
	
	@javax.annotation.Resource
	private edu.mayo.cts2.framework.core.plugin.PluginConfigManager pluginConfigManager;
	
	@javax.annotation.Resource
	private Cts2Marshaller cts2Marshaller;

	private String uri;
	private String existHome;
	private String userName;
	private String password;
	
	private XQueryService xQueryService;
	private CollectionManagementService collectionManagementService;
	private DatabaseInstanceManager databaseInstanceManager;
	private IndexQueryService indexQueryService;
	private XUpdateQueryService xUpdateQueryService;
	private ValidationService validationService;
	
	private Properties namespaceMappingProperties;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		setPropertiesFromConfig();
		
		if(StringUtils.isNotBlank(this.existHome)){
			System.setProperty(ExistServiceConstants.EXIST_HOME_PROP, this.existHome);
			System.setProperty("exist.initdb", "true");
		}

		final String driver = "org.exist.xmldb.DatabaseImpl";

		// initialize database driver
		Class<?> cl = Class.forName(driver);
		Database database = (Database) cl.newInstance();
		database.setProperty("create-database", "true");
		DatabaseManager.registerDatabase(database);
		
		Collection root = this.getOrCreateCollection("");
		
		xQueryService = (XQueryService) root.getService("XPathQueryService", "1.0");
		collectionManagementService = (CollectionManagementService) root.getService("CollectionManagementService", "1.0");
		databaseInstanceManager = (DatabaseInstanceManager) root.getService("DatabaseInstanceManager", "1.0");
		indexQueryService = (IndexQueryService) root.getService("IndexQueryService", "1.0");
		xUpdateQueryService = (XUpdateQueryService) root.getService("XUpdateQueryService", "1.0");
		validationService = (ValidationService) root.getService("ValidationService", "1.0");	

		this.namespaceMappingProperties = this.cts2Marshaller.getNamespaceMappingProperties();
	
		this.xQueryService = this.createXQueryService("");
	}
	
	private XQueryService createXQueryService(String collectionPath){
		XQueryService service;
		Collection collection;
		try {
			collection = this.getOrCreateCollection(collectionPath);
	
			service = (XQueryService) 
					collection.getService("XPathQueryService", "1.0");
			
			service.setCollection(collection);
		} catch (XMLDBException e) {
			throw new Cts2RuntimeException(e);
		}
		
		try {
			for(Entry<Object, Object> entry : namespaceMappingProperties.entrySet()){
				service.setNamespace((String)entry.getKey(), (String)entry.getValue());
			}
		} catch (XMLDBException e) {
			throw new Cts2RuntimeException(e);
		}
		
		return service;
	}
	
	protected void setPropertiesFromConfig() {
		PluginConfig pluginConfig = this.pluginConfigManager.getPluginConfig(EXIST_SERVICE_CONFIG_NAMESPACE);
		
		this.uri = pluginConfig.getStringOption(ExistServiceConstants.URL_PROP).getOptionValue();
		this.existHome = pluginConfig.getStringOption(ExistServiceConstants.EXIST_HOME_PROP).getOptionValue();
		this.userName = pluginConfig.getStringOption(ExistServiceConstants.USER_NAME_PROP).getOptionValue();
		this.password = pluginConfig.getStringOption(ExistServiceConstants.PASSWORD_PROP).getOptionValue();
	}

	public Collection getOrCreateCollection(String path) throws XMLDBException {
		path = StringUtils.removeStart(path, "/");
		path = StringUtils.removeEnd(path, "/");
		path = path.replaceAll("[/]+", "/");
		
		String pathWithURI = uri + path;

		Collection col = DatabaseManager.getCollection( pathWithURI, userName,
				password);

		if (col != null) {
			return col;
		} else {
			String[] paths = path.split("/");

			String[] parentPath = (String[]) ArrayUtils.remove(paths, paths.length - 1);
			
			this.getOrCreateCollection(StringUtils.join(parentPath, '/'));

			CollectionManagementService mgt = this.getCollectionManagementService();

			return mgt.createCollection(StringUtils.join(paths, '/'));
		}
	}

	public XQueryService getXQueryService() {
		return xQueryService;
	}
	
	public XQueryService getXQueryService(String path) {
		return this.createXQueryService(path);
	}

	public CollectionManagementService getCollectionManagementService() {
		return collectionManagementService;
	}
	
	public DatabaseInstanceManager getDatabaseInstanceManager() {
		return databaseInstanceManager;
	}

	public IndexQueryService getIndexQueryService() {
		return indexQueryService;
	}

	public XUpdateQueryService getXupdateQueryService() {
		return xUpdateQueryService;
	}


	public ValidationService getValidationService() {
		return validationService;
	}

	protected String getUri() {
		return uri;
	}

	protected void setUri(String uri) {
		this.uri = uri;
	}

	protected void setUserName(String userName) {
		this.userName = userName;
	}

	protected void setPassword(String password) {
		this.password = password;
	}
}
