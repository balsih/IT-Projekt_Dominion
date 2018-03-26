package Messages;

import org.w3c.dom.Document;

/**
 * This Message is to asks the server if something has changed in the game.
 * <li>Communication: client --> server
 * 
 * @author Lukas
 */
public class AskForChanges_Message extends Message {

	public AskForChanges_Message(){
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
}//end AksForChanges_Message