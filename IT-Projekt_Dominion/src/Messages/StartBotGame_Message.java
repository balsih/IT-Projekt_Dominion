package Messages;

import org.w3c.dom.Document;

/**
 * This Message tells the server that the client is ready to start a singleplayer-game.
 * <li>Communication: client --> server
 * 
 * @author Lukas
 */
public class StartBotGame_Message extends Message {

	public StartBotGame_Message(){
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
}//end StartBotGame_Message