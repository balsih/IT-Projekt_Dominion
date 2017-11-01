package Messages;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author default: Bradley Richards
 * @version 1.0
 * @created 01-Nov-2017 10:54:39
 */

public class Error_Message extends Message {
	private static final String ELEMENT_INFO = "info";
	
	private String info;
	
    public Error_Message() {
    	super();
    }

    /**
     * Given a document from SuperClass Message when a Message received,
     * fills in the content of the Message for further tasks
     * 
     * @param docIn, XML_Document from superClass Message
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
	 * Adds nodes to XML_Document in addition to superClass Message
	 * 
	 * @param docIn, XML_Document from superClass Message
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