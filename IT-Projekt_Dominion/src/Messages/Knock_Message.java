package Messages;

import org.w3c.dom.Document;

/**
 * This Message is to knock on the server to know if the socket's port is open.
 * <li>Communication: client --> server
 * 
 * @author Lukas
 */
public class Knock_Message extends Message {

	public Knock_Message(){
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
}//end Knock_Message