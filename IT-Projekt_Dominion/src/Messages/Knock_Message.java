package Messages;

import org.w3c.dom.Document;

/**
 * Knocks to server to know if this port with this ip is open
 * 
 * @author Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:01:10
 */
public class Knock_Message extends Message {

	public Knock_Message(){
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
}//end Knock_Message