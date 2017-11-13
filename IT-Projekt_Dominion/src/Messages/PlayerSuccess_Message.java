package Messages;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:01:20
 */
public class PlayerSuccess_Message extends Message {

	private static final String ELEMENT_SUCCESS = "success";
	private Content success;


	public PlayerSuccess_Message(){
		super();
	}

	/**
	 * 
	 * @param docIn
	 */
	@Override
	protected void addNodes(Document docIn){
        Element root = docIn.getDocumentElement();
		
		Element success = docIn.createElement(ELEMENT_SUCCESS);
		success.setTextContent(this.success.toString());
		root.appendChild(success);
	}


	/**
	 * 
	 * @param docIn
	 */
	@Override
	protected void init(Document docIn){
		Element root = docIn.getDocumentElement();
		
		NodeList tmpElements = root.getElementsByTagName(ELEMENT_SUCCESS);
        if (tmpElements.getLength() > 0) {
            Element success = (Element) tmpElements.item(0);
            this.success = Content.parseContent(success.getTextContent());
        }
	}

	public Content getSuccess(){
		return this.success;
	}
	
	public void setSuccess(Content success){
		this.success = success;
	}
}//end PlayerSuccess_Message