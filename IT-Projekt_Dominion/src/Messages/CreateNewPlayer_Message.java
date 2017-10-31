package Messages;

import com.sun.xml.internal.txw2.Document;

/**
 * @author Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:01:14
 */
public class CreateNewPlayer_Message extends Message {

	private static final String ELEMENT_PASSWORD = "password";
	private String password;


	public CreateNewPlayer_Message(){

	}

	/**
	 * 
	 * @param docIn
	 */
	@Override
	protected void addNodes(Document docIn){

	}

	public String getPassword(){
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
	 * @param password
	 */
	public void setPassword(String password){

	}
}//end CreateNewPlayer_Message