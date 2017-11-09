package Messages;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import Cards.Card;
import Client_Services.ServiceLocator;
import Client_Services.Translator;

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
	private static final String ELEMENT_DECKPILE = "deckPile";
	private static final String ELEMENT_HANDCARD = "handCard";
	private static final String ELEMENT_HANDCARDS = "handCards";
	private static final String ELEMENT_LOG = "log";
	private static final String ELEMENT_PLAYEDCARD = "playedCard";
	private static final String ELEMENT_PLAYEDCARDS = "playedCards";
	private static final String ELEMENT_CHAT = "chat";
	private static final String ELEMENT_BUYS = "buys";
	private static final String ATTR_DECKPILECARDNUMBER = "deckPileCardNumber";
	private static final String ATTR_DISCARDPILECARDNUMBER = "discardPileCardNumber";
	private String chat = null;
	private String cardBuyed = null;
	private String currentPhase = null;
	private String currentPlayer = null;
	private String discardPile = null;
	private String log = null;
	private Integer deckPileCardNumber = null;
	private Integer discardPileCardNumber = null;
	private Integer buys = null;
	private Integer actions = null;
	private Integer coins = null;
	private LinkedList<Card> handCards = null;
	private LinkedList<Card> playedCards = null;
	
    private LinkedList<String> stringElementNames;
    private LinkedList<String> intElementNames;
    private LinkedList<String> handCardElementNames;
    private LinkedList<String> playedCardElementNames;

    private LinkedList<String> stringElementContents;
    private LinkedList<Integer> intElementContents;
    
    private HashMap<String, String> attrElements;
    private HashMap<String, Integer> attrValues;
    
    
    /**
     * Constructor fills the Top-Level XML List for simpler adding
     */
	public UpdateGame_Message(){
		super();
		//Top_Level String Elements
		//if the content is null, the Element just has no content, but is necessary keep the List compatible for addContentElement method
		this.stringElementNames = new LinkedList<String>();
		this.stringElementContents = new LinkedList<String>();
		this.stringElementNames.addAll(Arrays.asList(ELEMENT_CARDBUYED, ELEMENT_CURRENTPHASE,ELEMENT_CURRENTPLAYER, 
	    	ELEMENT_LOG, ELEMENT_CHAT, ELEMENT_DISCARDPILE, ELEMENT_DECKPILE));
		
		//Top-Level Integer Elements
		this.intElementNames = new LinkedList<String>();
		this.intElementContents = new LinkedList<Integer>();
		this.intElementNames.addAll(Arrays.asList(ELEMENT_ACTIONS, ELEMENT_COINS, ELEMENT_BUYS));
		
		//This Elements will be filled in the appropriate setterMethods. The size depends on how many Cards are in use
		this.handCardElementNames = new LinkedList<String>();
		this.playedCardElementNames = new LinkedList<String>();
		
		//Link the Elements with the Attributes
		this.attrValues = new HashMap<String, Integer>();
		this.attrElements = new HashMap<String, String>();
		this.attrElements.put(ELEMENT_DECKPILE, ATTR_DECKPILECARDNUMBER);
		this.attrElements.put(ELEMENT_DISCARDPILE, ATTR_DISCARDPILECARDNUMBER);
		}

	/**
	 * 
	 * @param docIn
	 */
	@Override
	protected void addNodes(Document docIn){
        Element root = docIn.getDocumentElement();
        
        //stores the content of the variables into the appropriate structure
		this.stringElementContents.addAll(Arrays.asList(this.cardBuyed, this.currentPhase, this.currentPlayer, 
				this.log, this.chat,this.discardPile, null));
		this.intElementContents.addAll(Arrays.asList(this.actions, this.coins, this.buys));
		this.attrValues.put(ATTR_DECKPILECARDNUMBER, this.deckPileCardNumber);
		this.attrValues.put(ATTR_DISCARDPILECARDNUMBER, this.discardPileCardNumber);
        
        //adds the Top-Level Elements to XML
        this.addContentElements(docIn, root, this.stringElementNames, this.stringElementContents);
        this.addContentElements(docIn, root, this.intElementNames, this.intElementContents);
        
        //adds the Top-Level Element HANDCARDS to XML and the subElements HANDCARD
        Element handCardsElement = docIn.createElement(ELEMENT_HANDCARDS);
        this.addContentElements(docIn, handCardsElement, this.handCardElementNames, this.handCards);
        root.appendChild(handCardsElement);
        
        //adds the Top-Level Element PLAYEDCARDS to XML and the subElements PLAYEDCARD
        Element playedCardsElement = docIn.createElement(ELEMENT_PLAYEDCARDS);
        this.addContentElements(docIn, playedCardsElement, this.playedCardElementNames, this.playedCards);
        root.appendChild(playedCardsElement);
	}
	
	/**
	 * Helps the addNodes method to add elements in the given root
	 * If an element intends to have an attribute, it will be set
	 * 
	 * The elementNames and elementContents have to be suitable to each other (correct number and order)
	 * 
	 * @param docIn, to create further elements
	 * @param root, the current root to add elements
	 * @param elementNames, list of Element names, dependence on elementContents
	 * @param elementContents, generic list of the content, dependence on elementNames
	 */
	private <T> void addContentElements(Document docIn, Element root, LinkedList<String> elementNames, LinkedList<T> elementContents){
        for(int i = 0; i < elementNames.size(); i++){
        	Element element = docIn.createElement(elementNames.get(i));
        	if(this.attrElements.containsKey(elementNames.get(i))){//if an element contains an attribute, it will be set
        		element.setAttribute
       		(this.attrElements.get(elementNames.get(i)), this.attrValues.get(this.attrElements.get(elementNames.get(i))).toString());
        	}
        	if(elementContents.get(i) != null && elementContents.get(i).toString().length() > 0)//if content has changed, it will be set
        		element.setTextContent(elementContents.get(i).toString());
        	root.appendChild(element);
        }
	}
	
	
	/**
	 * 
	 * @param docIn
	 */
	@Override
	protected void init(Document docIn){
		ServiceLocator sl = ServiceLocator.getServiceLocator();
		Translator t = sl.getTranslator();
		Element root = docIn.getDocumentElement();
		
        NodeList tmpElements = root.getElementsByTagName(ELEMENT_CHAT);
        if(tmpElements.getLength() > 0){
        	Element chat = (Element) tmpElements.item(0);
        	this.chat = chat.getTextContent();
        }
		
//		NodeList tmpElements = root.getElementsByTagName(ELEMENT_DECKPILE);
//        if (tmpElements.getLength() > 0) {
//            Element deckPile = (Element) tmpElements.item(0);
//            NodeList deckElements = deckPile.getElementsByTagName(ELEMENT_DECKCARD);
//            for(int i = deckElements.getLength() -1; i >= 0; i--){
//            	Element deckCard = (Element) deckElements.item(i);
//            	this.deckPile.push(Card.getCard(deckCard.getTextContent()));
//            }
//        }
	}
	
	private <T> void parseContentFromElement(Element root, LinkedList<String> elementNames, LinkedList<T> elementContents){
		for(int i = 0; i < elementNames.size(); i++){
			NodeList tmpElements = root.getElementsByTagName(elementNames.get(i));
			if(tmpElements.getLength() > 0){
				Element element = (Element) tmpElements.item(0);
				if(elementContents.get(0) instanceof String){
					
				}else if(elementContents.get(0) instanceof Integer){
					
				}else if(elementContents.get(0) instanceof Card){
					
				}
			}
		}
	}
	

	public Integer getActions(){
		return this.actions;
	}

	public String getCardBuyed(){
		return this.cardBuyed;
	}

	public Integer getCoins(){
		return this.coins;
	}

	public String getCurrentPhase(){
		return this.currentPhase;
	}

	public String getCurrentPlayer(){
		return this.currentPlayer;
	}

	public String getDiscardPile(){
		return this.discardPile;
	}


	public LinkedList<Card> getHandCards(){
		return this.handCards;
	}

	public String getLog(){
		return this.log;
	}


	public LinkedList<Card> getPlayedCards(){
		return this.playedCards;
	}
	
	public String getChat(){
		return this.chat;
	}
	
	public Integer getBuys(){
		return this.buys;
	}
	
	public Integer getPileCardNumber(){
		return this.deckPileCardNumber;
	}
	
	public Integer getDiscardPileCardNumber(){
		return this.discardPileCardNumber;
	}



	public void setDeckPileCardNumber(Integer deckPileCardNumber){
		this.deckPileCardNumber = deckPileCardNumber;
	}
	
	public void setDiscardPileCardNumber(Integer discardPileCardNumber){
		this.discardPileCardNumber = discardPileCardNumber;
	}
	
	public void setBuys(Integer buys){
		this.buys = buys;
	}

	public void setChat(String chat){
		this.chat = chat;
	}
	
	public void setActions(Integer actions){
		this.actions = actions;
	}

	public String setCardBuyed(String cardBuyed){
		return this.cardBuyed = cardBuyed;
	}

	public void setCoins(Integer coins){
		this.coins = coins;
	}

	public void setCurrentPhase(String currentPhase){
		this.currentPhase = currentPhase;
	}

	public void setCurrentPlayer(String currentPlayer){
		this.currentPlayer = currentPlayer;
	}
	
	public void setDiscardPile(String discardPile){
		this.discardPile = discardPile;
	}
	
	public void setLog(String log){
		this.log = log;
	}

	//fill the handCardElementNames to use addContentElements
	public void setHandCards(LinkedList<Card> handCards){
		this.handCards = handCards;
		for(int i = 0; i < this.handCards.size(); i++)
			this.handCardElementNames.add(ELEMENT_HANDCARD);		
	}

	//fill the playedCardElementNames to use addContentElements
	public void setPlayedCards(LinkedList<Card> playedCards){
		this.playedCards = playedCards;
		for(int i = 0; i < this.playedCards.size(); i++)
			this.playedCardElementNames.add(ELEMENT_PLAYEDCARD);
	}
}//end UpdateGame_Message