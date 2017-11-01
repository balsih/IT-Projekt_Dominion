package Messages;

import org.w3c.dom.Document;

/**
 * @author Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:01:14
 */
public class GameMode_Message extends Message {

	private static final String ELEMENT_MODE = "mode";
	private String mode;


	public GameMode_Message(){

	}

	/**
	 * 
	 * @param docIn
	 */
	@Override
	protected void addNodes(Document docIn){

	}

	public String getMode(){
		return "";
	}

	/**
	 * 
	 * @param docIn
	 */
	@Override
	protected void init(Document docIn){

	}

	/**
	 * 
	 * @param mode
	 */
	public void setMode(String mode){

	}
}//end GameMode_Message