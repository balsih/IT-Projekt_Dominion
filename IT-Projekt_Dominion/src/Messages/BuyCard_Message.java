package Messages;

import org.w3c.dom.Document;

/**
 * 
 * @author Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:01:11
 */
public class BuyCard_Message extends Message {

	private String card;
	private static final String ELEMENT_CARD = "card";


	public BuyCard_Message(){

	}

	/**
	 * 
	 * @param docIn
	 */
	@Override
	protected void addNodes(Document docIn){

	}

	public String getCard(){
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
	 * @param card
	 */
	public void setCard(String card){

	}
}//end BuyCard_Message