package Messages;

import com.sun.xml.internal.txw2.Document;

/**
 * @author Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:01:13
 */
public class CreateGame_Message extends Message {

	private static final String ATTR_BUYCARDNUMBER = "buyCardNumber";
	private static final String ATTR_DECKCARDNUMBER = "deckCardNumber";
	private static final String ELEMENT_BUYCARD = "buyCard";
	private static final String ELEMENT_BUYCARDS = "buyCards";
	private static final String ELEMENT_DECKCARD = "deckCard";
	private static final String ELEMENT_DECKPILE = "deckPile";
	private static final String ELEMENT_OPPONENT = "opponent";
	private String buyCard;
	private int buyCardNumber;
	private String buyCards;
	private String deckCard;
	private int deckCardNumber;
	private String deckPile;
	private String opponent;


	public CreateGame_Message(){

	}

	/**
	 * 
	 * @param docIn
	 */
	@Override
	protected void addNodes(Document docIn){

	}

	public String getBuyCard(){
		return "";
	}

	public int getBuyCardNumber(){
		return 0;
	}

	public String getBuyCards(){
		return "";
	}

	public String getDeckCard(){
		return "";
	}

	public int getDeckCardNumber(){
		return 0;
	}

	public String getDeckPile(){
		return "";
	}

	public String getOpponent(){
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
	 * @param buyCard
	 */
	public void setBuyCard(String buyCard){

	}

	/**
	 * 
	 * @param buyCardNumber
	 */
	public void setBuyCardNumber(int buyCardNumber){

	}

	/**
	 * 
	 * @param buyCards
	 */
	public void setBuyCards(String buyCards){

	}

	/**
	 * 
	 * @param deckCard
	 */
	public void setDeckCard(String deckCard){

	}

	/**
	 * 
	 * @param deckCardNumber
	 */
	public void setDeckCardNumber(int deckCardNumber){

	}

	/**
	 * 
	 * @param deckPile
	 */
	public void setDeckPile(String deckPile){

	}

	/**
	 * 
	 * @param opponent
	 */
	public void setOpponent(String opponent){

	}
}//end CreateGame_Message