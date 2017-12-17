package Messages;

import org.w3c.dom.Document;

/**
 * This Message is to tell the server that the client gave up his/her game.
 * <li>Communication: client --> server
 * 
 * @author Lukas
 */
public class GiveUp_Message extends Message {


	public GiveUp_Message(){
		super();
	}

	protected void addNodes(Document docIn){
		//nothing toDo here
	}


	protected void init(Document docIn){
		//nothing toDo here
	}
}//end GiveUp_Message