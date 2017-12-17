package Messages;

import org.w3c.dom.Document;

/**
 * This Message's only purpose is to get a missing Message on server
 * 
 * @author Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:01:10
 */
public class Request_Message extends Message {

	public Request_Message(){
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
}//end Request_Message