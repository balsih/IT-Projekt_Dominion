package Messages;

import org.w3c.dom.Document;

/**
 * @author Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:01:12
 */
public class Chat_Message extends Message {

	private String chat;
	private static final String ELEMENT_CHAT = "chat";


	public Chat_Message(){

	}

	/**
	 * 
	 * @param docIn
	 */
	@Override
	protected void addNodes(Document docIn){

	}

	public String getChat(){
		return "";
	}

	/**
	 * 
	 * @param docIn
	 */
	@Override
	protected void init(Document docIn){

	}

	/**
	 * 
	 * @param chat
	 */
	public void setChat(String chat){

	}
}//end Chat_Message