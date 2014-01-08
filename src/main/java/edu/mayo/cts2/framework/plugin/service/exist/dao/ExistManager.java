package edu.mayo.cts2.framework.plugin.service.exist.dao;

import edu.mayo.cts2.framework.core.xml.Cts2Marshaller;
import edu.mayo.cts2.framework.model.exception.Cts2RuntimeException;
import edu.mayo.cts2.framework.plugin.service.exist.ExistServiceConstants;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exist.xmldb.DatabaseInstanceManager;
import org.exist.xmldb.IndexQueryService;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XQueryService;
import org.xmldb.api.modules.XUpdateQueryService;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class ExistManager implements InitializingBean, DisposableBean {
	
	protected final Log log = LogFactory.getLog(getClass().getName());
	
	private static final String DEFAULT_COLLECTION_ROOT = "/cts2resources";
	
	private String collectionRoot = DEFAULT_COLLECTION_ROOT;
	
	@javax.annotation.Resource
	private Cts2Marshaller cts2Marshaller;
	
	private String uri;
	private String existHome;
	private String userName;
	private String password;
    private boolean useChangeSets = true;
	
	private XQueryService xQueryService;
	private CollectionManagementService collectionManagementService;
	private DatabaseInstanceManager databaseInstanceManager;
	private IndexQueryService indexQueryService;
	private XUpdateQueryService xUpdateQueryService;
//	private ValidationService validationService;
	private Object syncLock = new Object();
	
	private Properties namespaceMappingProperties;

    @Override
    public void destroy() throws Exception {
        if(StringUtils.isNotBlank(this.existHome)){
            this.databaseInstanceManager.shutdown();
        }
    }

    @Override
	public void afterPropertiesSet() throws Exception {

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
		//This doesn't exist in existDB 2.1.  ValidationModule now maybe?  Not sure.  possibly an extension not loaded...
//		validationService = (ValidationService) root.getService("ValidationService", "1.0");	

		this.namespaceMappingProperties = this.cts2Marshaller.getNamespaceMappingProperties();
	
		this.xQueryService = this.createXQueryService("");
	}
	
	public void propertiesUpdated(Map<String,?> properties) throws Exception {
		log.warn("Properties Reloading: " + properties);
		
		String userName = (String) properties.get("userName");
		String uri = (String) properties.get("uri");
		String existHome = (String) properties.get("existHome");
		String password = (String) properties.get("password");
        boolean useChangeSets = BooleanUtils.toBoolean( (Boolean) properties.get("useChangeSets"));
		
		this.userName = userName;
		this.uri = uri;
		this.existHome = existHome;
		this.password = password;
        this.useChangeSets = useChangeSets;
		
		this.afterPropertiesSet();
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
			synchronized (syncLock)
			{
				col = DatabaseManager.getCollection( pathWithURI, userName, password);
				if (col != null)
				{
					return col;
				}
				String[] paths = path.split("/");
	
				String[] parentPath = (String[]) ArrayUtils.remove(paths, paths.length - 1);
				
				this.getOrCreateCollection(StringUtils.join(parentPath, '/'));
	
				CollectionManagementService mgt = this.getCollectionManagementService();
	
				col = mgt.createCollection(StringUtils.join(paths, '/'));

				//If we are creating a new root collection, we need to set up the indexing configuration for this collection, 
				//which is done by placing a collection.xconf file into the config path structure.
				if (paths.length == 1 && getCollectionRoot().endsWith(paths[0]))
				{
					log.info("Configuring Indexes for Collection " + paths[0]);
					//Add the index configuration file for our root config
					try
					{
                        String confFilePath = "/collection.xconf";
						String confFileXml = IOUtils.toString(this.getClass().getResourceAsStream(confFilePath), "UTF-8");
						Collection configCol = getOrCreateCollection("/system/config/db/" + paths[0]);
						org.xmldb.api.base.Resource r = configCol.createResource("collection.xconf", "XMLResource");
						r.setContent(confFileXml);
						configCol.storeResource(r);
						log.info("Wrote index configuration " + r.getParentCollection().getName() + "/"+ r.getId());
					}
					catch (Exception e)
					{
						log.error("Failure configuring indexes for collection " + paths[0] + " - indexes will not be available!", e);
					}
				}
				return col;
			}
		}
	}

	public XQueryService getXQueryService() {
		return xQueryService;
	}
	
	public XQueryService getXQueryService(String path) {
		return this.createXQueryService(path);
	}

	protected CollectionManagementService getCollectionManagementService() {
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
	
	public XUpdateQueryService getXupdateQueryService(String path) {
		return this.createXQueryUpdateService(path);
	}
	
	private XUpdateQueryService createXQueryUpdateService(String collectionPath){
		XUpdateQueryService service;
		Collection collection;
		try 
		{
			collection = this.getOrCreateCollection(collectionPath);
			service = (XUpdateQueryService)collection.getService("XUpdateQueryService", "1.0");
			service.setCollection(collection);
		} 
		catch (XMLDBException e) 
		{
			throw new Cts2RuntimeException(e);
		}
		return service;
	}


//	public ValidationService getValidationService() {
//		return validationService;
//	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getExistHome() {
		return existHome;
	}

	public void setExistHome(String existHome) {
		this.existHome = existHome;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getCollectionRoot() {
		return collectionRoot;
	}

	public void setCollectionRoot(String collectionRoot) {
		this.collectionRoot = collectionRoot;
	}

    public boolean isUseChangeSets() {
        return useChangeSets;
    }

    public void setUseChangeSets(boolean useChangeSets) {
        this.useChangeSets = useChangeSets;
    }

    protected String getBaseCollectionPath(){
		if(StringUtils.isNotBlank(this.collectionRoot)){
			return this.collectionRoot;
		} else {
			return DEFAULT_COLLECTION_ROOT;
		}
	}

}
