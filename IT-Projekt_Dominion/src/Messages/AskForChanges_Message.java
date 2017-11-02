package Messages;

import org.w3c.dom.Document;

/**
 * The client asks the server if something has changed in the game.
 * If yes, the messages will be waiting in a Queue
 * 
 * @author Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:01:10
 */
public class AskForChanges_Message extends Message {

	public AskForChanges_Message(){
		super();
	}

	/**
	 * 
	 * @param docIn
	 */
	@Override
	protected void addNodes(Document docIn){

	}

	/**
	 * 
	 * @param docIn
	 * @return 
	 */
	@Override
	protected void init(Document docIn){

	}
}//end AksForChanges_Message