package Messages;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * This Message sends a chat-message to all players in the current Game.
 *  Communication: client --> server
 *  
 *  @author Lukas
 */
public class Chat_Message extends Message {

	private static final String ELEMENT_CHAT = "chat";
	private String chat;


	public Chat_Message(){
		super();
	}

	/**
	 * Adds the chat-message to XML
	 * 
	 * @author Lukas
	 * @param docIn
	 * 				XML-Document
	 */
	@Override
	protected void addNodes(Document docIn){
        Element root = docIn.getDocumentElement();
		
		Element chat = docIn.createElement(ELEMENT_CHAT);
		chat.setTextContent(this.chat);
		root.appendChild(chat);
	}

	/**
	 * Creates the object chat (String) from XML
	 * 
	 * @author Lukas
	 * @param docIn
	 * 				XML-Document
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