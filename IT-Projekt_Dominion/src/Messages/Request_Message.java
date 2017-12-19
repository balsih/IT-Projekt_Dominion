package Messages;

import org.w3c.dom.Document;

/**
 * This Message is if the server wasn't able to send back a certain Message, i.e. caused by a socketException.
 * <li>Communication: client --> server
 * <li>Communication: server --> client
 * 
 * @author Lukas
 */
public class Request_Message extends Message {

	public Request_Message(){
		super();
	}

	@Override
	protected void addNodes(Document docIn){
		//nothing toDo here
	}


	@Override
	protected void init(Document docIn){
		//nothing toDo here
	}
}//end Request_Message