package Klassendiagramm.Messages;


/**
 * @author Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:01:22
 */
public class UpdateGame_Message extends Message {

	private int actions;
	private String cardBuyed;
	private int coins;
	private String currentPhase;
	private String currentPlayer;
	private String discardPile;
	private static final String ELEMENT_ACTIONS;
	private static final String ELEMENT_CARDBUYED;
	private static final String ELEMENT_COINS;
	private static final String ELEMENT_CURRENTPHASE;
	private static final String ELEMENT_CURRENTPLAYER;
	private static final String ELEMENT_DISCARDPILE;
	private static final String ELEMENT_HANDCARD;
	private static final String ELEMENT_HANDCARDS;
	private static final String ELEMENT_LOG;
	private static final String ELEMENT_PLAYEDCARD;
	private static final String ELEMENT_PLAYEDCARDS;
	private String handCard;
	private String handCards;
	private String log;
	private String playedCard;
	private String playedCards;



	public void finalize() throws Throwable {
		super.finalize();
	}
	public UpdateGame_Message(){

	}

	/**
	 * 
	 * @param docIn
	 */
	protected addNodes(Document docIn){

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
	protected init(Document docIn){

	}

	/**
	 * 
	 * @param actions
	 */
	public setActions(int actions){

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
	public setCoins(int coins){

	}

	/**
	 * 
	 * @param currentPhase
	 */
	public setCurrentPhase(String currentPhase){

	}

	/**
	 * 
	 * @param currentPlayer
	 */
	public setCurrentPlayer(String currentPlayer){

	}

	/**
	 * 
	 * @param discardPile
	 */
	public setDiscardPile(String discardPile){

	}

	/**
	 * 
	 * @param handCard
	 */
	public setHandCard(String handCard){

	}

	/**
	 * 
	 * @param handCards
	 */
	public setHandCards(String handCards){

	}

	/**
	 * 
	 * @param log
	 */
	public setLog(String log){

	}

	/**
	 * 
	 * @param playedCard
	 */
	public setPlayedCard(String playedCard){

	}

	/**
	 * 
	 * @param playedCards
	 */
	public setPlayedCards(String playedCards){

	}
}//end UpdateGame_Message