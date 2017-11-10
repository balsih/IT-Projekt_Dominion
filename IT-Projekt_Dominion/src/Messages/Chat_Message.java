package Messages;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:01:12
 */
public class Chat_Message extends Message {

	private String chat;
	private static final String ELEMENT_CHAT = "chat";


	public Chat_Message(){
		super();
	}

	/**
	 * 
	 * @param docIn
	 */
	@Override
	protected void addNodes(Document docIn){
        Element root = docIn.getDocumentElement();
		
		Element chat = docIn.createElement(ELEMENT_CHAT);
		chat.setTextContent(this.chat);
		root.appendChild(chat);
	}

	/**
	 * 
	 * @param docIn
	 */
	@Override
	protected void init(Document docIn){
		Element root = docIn.getDocumentElement();
		
		NodeList tmpElements = root.getElementsByTagName(ELEMENT_CHAT);
        if (tmpElements.getLength() > 0) {
            Element chat = (Element) tmpElements.item(0);
            this.chat = chat.getTextContent();
        }
	}

	public String getChat(){
		return this.chat;
	}

	public void setChat(String chat){
		this.chat = chat;
	}
}//end Chat_Message