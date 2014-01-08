package edu.mayo.cts2.framework.plugin.service.exist.profile;


import edu.mayo.cts2.framework.core.xml.Cts2Marshaller;
import edu.mayo.cts2.framework.model.core.ChangeDescription;
import edu.mayo.cts2.framework.model.exception.Cts2RuntimeException;
import edu.mayo.cts2.framework.model.updates.ChangeSet;
import edu.mayo.cts2.framework.model.updates.ChangeableResource;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistManager;
import edu.mayo.cts2.framework.plugin.service.exist.dao.ExistResourceDao;
import edu.mayo.cts2.framework.plugin.service.exist.profile.update.ChangeSetResourceInfo;
import edu.mayo.cts2.framework.plugin.service.exist.util.ExistServiceUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceSet;

import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class StateChangeCallback {

	@Autowired
	private ExistResourceDao existResourceDao;
	
	@javax.annotation.Resource
	private ExistManager existManager;
	
	@Autowired
	private ResourceUnmarshaller resourceUnmarshaller;
	
	@javax.annotation.Resource
	private Cts2Marshaller cts2Marshaller;
	
	@javax.annotation.Resource
	private ChangeSetResourceInfo changeSetResourceInfo;
	
	private HashMap<String, ArrayList<ChangeableResource>> bulkLoadData_ = new HashMap<String, ArrayList<ChangeableResource>>();
	
	private final Object syncLock = new Object();
	
	protected Log log = LogFactory.getLog(getClass());
	
	public void resourceAdded(ChangeableResource changeable) {
        if(this.existManager.isUseChangeSets()){
            ChangeDescription changeDescription =
                    changeable.getChangeableElementGroup().getChangeDescription();

            String changeSetUri = changeDescription.
                        getContainingChangeSet();

            this.addToChangeSet(changeSetUri, changeable);
        }
	}
	
	public void resourceUpdated(ChangeableResource changeable) {
        if(this.existManager.isUseChangeSets()){
            ChangeDescription changeDescription =
                    changeable.getChangeableElementGroup().getChangeDescription();

            String changeSetUri = changeDescription.
                        getContainingChangeSet();

            this.addToChangeSet(changeSetUri, changeable);
        }
	}
	
	public void resourceDeleted(ChangeableResource changeable, String changeSetUri) {
        if(this.existManager.isUseChangeSets()){
		    this.addToChangeSet(changeSetUri, changeable);
        }
	}
	
	private void addToChangeSet(String changeSetUri, ChangeableResource changeable){
		//We could only sync on something unique to the changeSetURI - but thats gets to be 
		//a sticky sync call to make in Java... not worth the difficulty.
		synchronized (syncLock)
		{
			log.debug("Updating ChangeSet document with Changeable");
			try
			{
				ArrayList<ChangeableResource> data = bulkLoadData_.get(changeSetUri);
				if (data == null)
				{
					String name = ExistServiceUtils.uriToExistName(changeSetUri);
					
					ResourceSet rs = existResourceDao.query(changeSetResourceInfo.getResourceBasePath(), 
							"doc('" + name + ".xml')/updates:ChangeSet/string(@entryCount)", 0, Integer.MAX_VALUE);

					long count = 0;
					
					if (rs.getResource(0) != null && ((String)rs.getResource(0).getContent()).length() > 0)
					{
						count = Long.parseLong((String)rs.getResource(0).getContent());
					}
					
					count++;
					changeable.setEntryOrder(count);
					
					
					//Get the XML for the new member to be added to the changeset.
					StringWriter sw = new StringWriter();
					StreamResult sr = new StreamResult(sw);
					cts2Marshaller.marshal(changeable, sr);
					StringBuffer changeableXML = sw.getBuffer();
	
					//Need to do a bit of mucking with the XML, as I just want a snippit that I'm going to insert into an already existing ChangeSet XML file.
					//Basically, turn <ns1:ChangeableResource ... entryOrder="x"> into <ns1:member entryOrder="x"> (and the corresponding closing tag)
					//TODO ask Kevin about this namespace stuff... might be brittle, seems hackish.
					Pattern entryOrderPattern = Pattern.compile("entryOrder=\"\\d*\"");
					Matcher m = entryOrderPattern.matcher(changeableXML);
					m.find();
					String entryOrderSnippit = changeableXML.substring(m.start(), m.end());
					
					Pattern snipFirstElement = Pattern.compile("<ns1:ChangeableResource.*?>", Pattern.DOTALL);
					m = snipFirstElement.matcher(changeableXML);
					m.find();
					int start = m.end() + 1;
					int end = changeableXML.indexOf("</ns1:ChangeableResource>");
					
					String xmlSnippit = "<updates:member " + entryOrderSnippit + ">" + System.getProperty("line.separator") + 
							changeableXML.substring(start, end) + "</updates:member>";
					
					//Fix the namespace  TODO why isn't the cts marshaller giving me this namespace name in the first place?
					xmlSnippit = xmlSnippit.replaceAll("<ns1", "<updates");
					xmlSnippit = xmlSnippit.replaceAll("</ns1", "</updates");
					
					//Build up a query that will append in the XML, and update the entryCount attribute at the same time.
					StringBuilder updateQuery = new StringBuilder();
					updateQuery.append("let $changeSetNode := doc('" + name + ".xml')/updates:ChangeSet\r\n");
					updateQuery.append("let $a := update insert " + xmlSnippit + " into $changeSetNode\r\n");
					
					//Update the ChangeSet count
					if (rs.getResource(0) == null || ((String)rs.getResource(0).getContent()).length() == 0)
					{
						updateQuery.append("return update insert attribute entryCount {" + count + "} into $changeSetNode"); 
					}
					else
					{
						updateQuery.append("return update value $changeSetNode/@entryCount with " + count);
					}
					
					//TODO performance issue - this is still horribly slow, as the document grows.
					//The most expensive part is updating the entryCount attribute, as it seems to trigger a complete re-index of the ChangeSet node
					//I've combined that operation with the append-xml process above, which helps slightly, as we only read the node once during execution
					//but this is still horribly, horribly slow.  For a fast alternative, see the bulk-load-mode, below.
					long startTime = System.currentTimeMillis();
					existResourceDao.query(changeSetResourceInfo.getResourceBasePath(), 
							updateQuery.toString(), 0, Integer.MAX_VALUE);
					
					log.info("Took " + (System.currentTimeMillis() - startTime) + " ms to update the stored changeset in-place");
				}
				else
				{
					//bulk load mode
					data.add(changeable);
				}
			}
			catch (Exception e)
			{
				throw new Cts2RuntimeException(e);
			}
			log.debug("ChangeSet update complete");
		}
	}
	
	public void beginBulkLoadMode(String changeSetURI)
	{
		synchronized (syncLock)
		{
			if (bulkLoadData_.containsKey(changeSetURI))
			{
				throw new IllegalArgumentException("Already in bulk load mode!");
			}
			log.info("Bulk Load mode begins for '" + changeSetURI + "'");
			bulkLoadData_.put(changeSetURI, new ArrayList<ChangeableResource>());
		}
	}
	
	public void endBulkLoadMode(String changeSetURI)
	{
		synchronized (syncLock)
		{
			try
			{
				ArrayList<ChangeableResource> data = bulkLoadData_.get(changeSetURI);
				if (data == null)
				{
					throw new IllegalArgumentException("Not in bulk load mode!");
				}
	
				log.info("Writing out Bulk Load data for '" + changeSetURI + "'");
				String name = ExistServiceUtils.uriToExistName(changeSetURI);
				
				Resource resource = existResourceDao.getResource(changeSetResourceInfo.getResourceBasePath(), name);
				
				ChangeSet changeSet = (ChangeSet) this.resourceUnmarshaller.unmarshallResource(resource);
				long count = (changeSet.getEntryCount() == null ? 0 : changeSet.getEntryCount());
				
				for (ChangeableResource d : data)
				{
					d.setEntryOrder(++count);
					changeSet.addMember(d);
				}
				changeSet.setEntryCount(count);
				existResourceDao.storeResource(changeSetResourceInfo.getResourceBasePath(), name, changeSet);
			}
			finally
			{
				bulkLoadData_.remove(changeSetURI);
				log.info("Bulk Load mode ends for '" + changeSetURI + "'");
			}
		}
	}
}
