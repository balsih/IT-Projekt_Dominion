package Messages;

import com.sun.xml.internal.txw2.Document;

/**
 * @author Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:01:20
 */
public class PlayerSuccess_Message extends Message {

	private static final String ELEMENT_SUCCESS = "success";
	private String success;


	public PlayerSuccess_Message(){

	}

	/**
	 * 
	 * @param docIn
	 */
	@Override
	protected void addNodes(Document docIn){

	}

	public String getSuccess(){
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
	 * @param success
	 */
	public void setSuccess(String success){

	}
}//end PlayerSuccess_Message