package Messages;

import org.w3c.dom.Document;

/**
 * @author Lukas
 * This Message asks the server if something has changed in the game
 * client --> server
 */
public class AskForChanges_Message extends Message {

	public AskForChanges_Message(){
		super();
	}

	@Override
	protected void addNodes(Document docIn){
		//nothing toDo here
	}

	/**
	 * 
	 * @param docIn
	 * @return 
	 */
	@Override
	protected void init(Document docIn){
		//nothing toDo here
	}
}//end AksForChanges_Message