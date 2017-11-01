package Messages;

import org.w3c.dom.Document;

/**
 * @author Lukas
 * @version 1.0
 * @created 01-Nov-2017 14:52:31
 */
public class Commit_Message extends Message {
	
	private static final String ELEMENT_SUCCESS = "success";
	private String success;

	
	public Commit_Message(){

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
	
	public void setSuccess(String success){
		this.success = success;
	}
	
	public String getSuccess(){
		return this.success;
	}
}//end CommitTransmission_Message