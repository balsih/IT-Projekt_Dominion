package Klassendiagramm.Messages;


/**
 * @author Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:01:13
 */
public class CreateGame_Message extends Message {

	private static final String ATTR_BUYCARDNUMBER;
	private static final String ATTR_DECKCARDNUMBER;
	private String buyCard;
	private int buyCardNumber;
	private String buyCards;
	private String deckCard;
	private int deckCardNumber;
	private String deckPile;
	private static final String ELEMENT_BUYCARD;
	private static final String ELEMENT_BUYCARDS;
	private static final String ELEMENT_DECKCARD;
	private static final String ELEMENT_DECKPILE;
	private static final String ELEMENT_OPPONENT;
	private String opponent;



	public void finalize() throws Throwable {
		super.finalize();
	}
	public CreateGame_Message(){

	}

	/**
	 * 
	 * @param docIn
	 */
	protected addNodes(Document docIn){

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
	protected init(Document docIn){

	}

	/**
	 * 
	 * @param buyCard
	 */
	public setBuyCard(String buyCard){

	}

	/**
	 * 
	 * @param buyCardNumber
	 */
	public setBuyCardNumber(int buyCardNumber){

	}

	/**
	 * 
	 * @param buyCards
	 */
	public setBuyCards(String buyCards){

	}

	/**
	 * 
	 * @param deckCard
	 */
	public setDeckCard(String deckCard){

	}

	/**
	 * 
	 * @param deckCardNumber
	 */
	public setDeckCardNumber(int deckCardNumber){

	}

	/**
	 * 
	 * @param deckPile
	 */
	public setDeckPile(String deckPile){

	}

	/**
	 * 
	 * @param opponent
	 */
	public setOpponent(String opponent){

	}
}//end CreateGame_Message