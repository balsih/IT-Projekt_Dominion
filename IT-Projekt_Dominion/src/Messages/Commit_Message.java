package Messages;

import org.w3c.dom.Document;

/**
 * @author Lukas
 * @version 1.0
 * @created 01-Nov-2017 14:52:31
 */
public class Commit_Message extends Message {
	
	private static final String ELEMENT_NOTIFICATION = "notification";
	private static final String ATTR_MOVE = "move";
	private String notification;
	private String move;

	
	public Commit_Message(){
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
	 */
	@Override
	protected void init(Document docIn){

	}
	
}//end CommitTransmission_Message