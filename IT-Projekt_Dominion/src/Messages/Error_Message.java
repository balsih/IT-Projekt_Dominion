package Messages;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * This Message's purpose is it to be created if there's a failure in the communication.
 * 
 * @author Bradley Richards
 */

public class Error_Message extends Message {
	private static final String ELEMENT_INFO = "info";
	
	private String info;
	
    public Error_Message() {
    	super();
    }

    /**
     * Creates the object info (String) from XML
     * 
     * @author Bradley Richards
     * @param docIn
     * 			XML-Document
     */
	@Override
	protected void init(Document docIn) {
        Element root = docIn.getDocumentElement();
		
		NodeList tmpElements = root.getElementsByTagName(ELEMENT_INFO);
        if (tmpElements.getLength() > 0) {
            Element info = (Element) tmpElements.item(0);
            this.info = info.getTextContent();
        }
	}    
	
	/**
	 * Adds the info to XML
	 * 
	 * @author Bradley Richards
	 * @param docIn
	 * 			XML-Document
	 */
	@Override
	protected void addNodes(Document docIn) {
        Element root = docIn.getDocumentElement();
		
		Element info = docIn.createElement(ELEMENT_INFO);
		info.setTextContent(this.info);
		root.appendChild(info);
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
}//end Error_Message