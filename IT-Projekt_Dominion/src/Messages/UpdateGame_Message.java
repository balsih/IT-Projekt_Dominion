package Messages;

import org.w3c.dom.Document;

/**
 * @author Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:01:22
 */
public class UpdateGame_Message extends Message {

	
	private static final String ELEMENT_ACTIONS = "actions";
	private static final String ELEMENT_CARDBUYED = "cardBuyed";
	private static final String ELEMENT_COINS = "coins";
	private static final String ELEMENT_CURRENTPHASE = "currentPhase";
	private static final String ELEMENT_CURRENTPLAYER = "currentPlayer";
	private static final String ELEMENT_DISCARDPILE = "discardPile";
	private static final String ELEMENT_HANDCARD = "handCard";
	private static final String ELEMENT_HANDCARDS = "handCards";
	private static final String ELEMENT_LOG = "log";
	private static final String ELEMENT_PLAYEDCARD = "playedCard";
	private static final String ELEMENT_PLAYEDCARDS = "playedCards";
	private int actions;
	private String cardBuyed;
	private int coins;
	private String currentPhase;
	private String currentPlayer;
	private String discardPile;
	private String handCard;
	private String handCards;
	private String log;
	private String playedCard;
	private String playedCards;


	public UpdateGame_Message(){

	}

	/**
	 * 
	 * @param docIn
	 */
	@Override
	protected void addNodes(Document docIn){

	}

	public int getActions(){
		return 0;
	}

	public String getCardBuyed(){
		return "";
	}

	public int getCoins(){
		return 0;
	}

	public String getCurrentPhase(){
		return "";
	}

	public String getCurrentPlayer(){
		return "";
	}

	public String getDiscardPile(){
		return "";
	}

	public String getHandCard(){
		return "";
	}

	public String getHandCards(){
		return "";
	}

	public String getLog(){
		return "";
	}

	public String getPlayedCard(){
		return "";
	}

	public String getPlayedCards(){
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
	 * @param actions
	 */
	public void setActions(int actions){

	}

	/**
	 * 
	 * @param cardBuyed
	 */
	public String setCardBuyed(String cardBuyed){
		return "";
	}

	/**
	 * 
	 * @param coins
	 */
	public void setCoins(int coins){

	}

	/**
	 * 
	 * @param currentPhase
	 */
	public void setCurrentPhase(String currentPhase){

	}

	/**
	 * 
	 * @param currentPlayer
	 */
	public void setCurrentPlayer(String currentPlayer){

	}

	/**
	 * 
	 * @param discardPile
	 */
	public void setDiscardPile(String discardPile){

	}

	/**
	 * 
	 * @param handCard
	 */
	public void setHandCard(String handCard){

	}

	/**
	 * 
	 * @param handCards
	 */
	public void setHandCards(String handCards){

	}

	/**
	 * 
	 * @param log
	 */
	public void setLog(String log){

	}

	/**
	 * 
	 * @param playedCard
	 */
	public void setPlayedCard(String playedCard){

	}

	/**
	 * 
	 * @param playedCards
	 */
	public void setPlayedCards(String playedCards){

	}
}//end UpdateGame_Message