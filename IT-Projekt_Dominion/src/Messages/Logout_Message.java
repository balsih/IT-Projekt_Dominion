package Messages;

import org.w3c.dom.Document;

/**
 * This Message is to logout from the server.
 * <li>Communication: client --> server
 * 
 * @author Lukas
 */
public class Logout_Message extends Message {


	public Logout_Message(){
		super();
	}

	protected void addNodes(Document docIn){
		//nothing toDo here
	}

	protected void init(Document docIn){
		//nothing toDo here
	}
}//end Logout_Message