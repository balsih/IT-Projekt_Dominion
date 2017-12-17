package Messages;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * This Message is to commit some actions server-site.
 * So the client knows his/her action succeeded.
 * It is possible to attach a notification for the client.
 * <li>Communication: server --> client
 * 
 * @author Lukas
 */
public class Commit_Message extends Message {
	
	private static final String ELEMENT_NOTIFICATION = "notification";
	private String notification;

	
	public Commit_Message(){
		super();
	}

	/**
	 * Adds the notification to XML (if set)
	 *
	 *@author Lukas
	 * @param docIn
	 * 				XML-Document
	 */
	@Override
	protected void addNodes(Document docIn){
        Element root = docIn.getDocumentElement();
		
        if(this.notification != null){
    		Element notification = docIn.createElement(ELEMENT_NOTIFICATION);
    		notification.setTextContent(this.notification);
    		root.appendChild(notification);
        }
	}

	/**
	 * Creates the object notification (String) from XML
	 * 
	 * @author Lukas
	 * @param docIn
	 * 				XML-Document
	 */
	@Override
	protected void init(Document docIn){
		Element root = docIn.getDocumentElement();
		
		NodeList tmpElements = root.getElementsByTagName(ELEMENT_NOTIFICATION);
        if (tmpElements.getLength() > 0) {
            Element notification = (Element) tmpElements.item(0);
            this.notification = notification.getTextContent();
        }
	}

	
	
	public String getNotification() {
		return notification;
	}
	

	public void setNotification(String notification) {
		this.notification = notification;
	}
	
}//end Commit_Message