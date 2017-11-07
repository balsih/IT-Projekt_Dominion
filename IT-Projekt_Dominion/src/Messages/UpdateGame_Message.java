package Messages;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
	private static final String ELEMENT_CHAT = "chat";
	private String chat;
	private String cardBuyed;
	private String currentPhase;
	private String currentPlayer;
	private String discardPile;
	private String handCard;
	private String handCards;
	private String log;
	private String playedCard;
	private String playedCards;
	private int actions;
	private int coins;
	
    private String[] stringElementNames = new String[]{ELEMENT_CARDBUYED, ELEMENT_CURRENTPHASE,ELEMENT_CURRENTPLAYER, ELEMENT_DISCARDPILE,
    		ELEMENT_HANDCARD, ELEMENT_HANDCARDS, ELEMENT_LOG, ELEMENT_PLAYEDCARD,ELEMENT_PLAYEDCARDS, ELEMENT_CHAT};
    private String[] intElementNames = new String[]{ELEMENT_ACTIONS, ELEMENT_COINS};

    private String[] stringElementContents = new String[]{this.cardBuyed, this.currentPhase, this.currentPlayer, this.discardPile,
    		this.handCard, this.handCards, this.log, this.playedCard, this.playedCards, this.chat};
    private int[] intElementContents = new int[]{this.actions, this.coins};
    
    

	public UpdateGame_Message(){
		super();
	}

	/**
	 * 
	 * @param docIn
	 */
	@Override
	protected void addNodes(Document docIn){
        Element root = docIn.getDocumentElement();
        
        for(int i = 0; i < this.stringElementNames.length; i++){
        	Element element = docIn.createElement(this.stringElementNames[i]);
        	element.setTextContent(this.stringElementContents[i]);
        	root.appendChild(element);
        }
        
        for(int i = 0; i < this.intElementNames.length; i++){
        	Element element = docIn.createElement(this.intElementNames[i]);
        	element.setTextContent(Integer.toString(this.intElementContents[i]));
        	root.appendChild(element);
        }
	}
	
	
	/**
	 * 
	 * @param docIn
	 */
	@Override
	protected void init(Document docIn){

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
	
	public String getChat(){
		return this.chat;
	}




	public void setChat(String chat){
		this.chat = chat;
	}
	
	public void setActions(int actions){

	}

	public String setCardBuyed(String cardBuyed){
		return "";
	}

	public void setCoins(int coins){

	}

	public void setCurrentPhase(String currentPhase){

	}

	public void setCurrentPlayer(String currentPlayer){

	}
	
	public void setDiscardPile(String discardPile){

	}

	public void setHandCard(String handCard){

	}

	public void setHandCards(String handCards){

	}

	public void setLog(String log){

	}

	public void setPlayedCard(String playedCard){

	}

	public void setPlayedCards(String playedCards){

	}
}//end UpdateGame_Message