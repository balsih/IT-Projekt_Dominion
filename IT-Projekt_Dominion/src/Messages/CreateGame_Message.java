package Messages;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import Cards.Card;
import Cards.CardName;
import Server_GameLogic.Phase;

/**
 * This Message is to create a new Game client-site.
 * <ul>
 * It includes all important data to the client to start a new Game:
 * <li>buyCards:		all cards <HashMap><CardName> and how many of them can be bought
 * <li>handCards:		the 5 hand-cards <LinkedList><Card> the client starts with
 * <li>handNumber:		number <Integer> of hand-cards (5)
 * <li>deckPile:		the 5 deck-cards <Stack><Card> the client starts with
 * <li>deckNumber:		the size <Integer> of the deck (5)
 * <li>startingPlayer:	the starting-player's name <String>
 * <li>opponent:		the opponent's name <String>
 * <li>phase:			the phase <Phase> to start with (Buy)
 * </ul>
 * <p><li>Communication: server --> client
 * 
 * @author Lukas
 */
public class CreateGame_Message extends Message {

	private static final String ELEMENT_BUYCARDS = "buyCards";
	private static final String ELEMENT_DECKPILE = "deckPile";
	private final static String ELEMENT_HANDCARDS = "handCards";
	private final static String ELEMENT_DECKCARD = "deckCard";
	private final static String ELEMENT_HANDCARD = "handCard";
	private static final String ELEMENT_BUYCARD = "buyCard";
	private static final String ATTR_BUYCARD_NUMBER = "buyCardNumber";
	private static final String ELEMENT_STARTING_PLAYER = "startingPlayer";
	private static final String ELEMENT_OPPONENT = "opponent";
	private static final String ATTR_DECK_NUMBER = "deckNumber";
	private static final String ATTR_HAND_NUMBER = "handNumber";
	private static final String ELEMENT_PHASE = "phase";
	
	private HashMap<CardName, Integer> buyCards;
	private Stack<Card> deckPile;
	private LinkedList<Card> handCards;
	private String startingPlayer;
	private String opponent;
	private Integer deckNumber;
	private Integer handNumber;
	private Phase phase;


	public CreateGame_Message(){
		super();
        this.deckPile = new Stack<Card>();
        this.buyCards = new HashMap<CardName, Integer>();
        this.handCards = new LinkedList<Card>();
	}

	/**
	 * Adds all data the client needs to start a new game to XML
	 * 
	 * @author Lukas
	 * @param docIn
	 * 				XML-Document
	 */
	@Override
	protected void addNodes(Document docIn){
        Element root = docIn.getDocumentElement();
		
        //insert all deckCards in the right order into the XML_Document
		Element deckPile = docIn.createElement(ELEMENT_DECKPILE);
		for(Card card: this.deckPile){
			Element deckCard = docIn.createElement(ELEMENT_DECKCARD);
			deckCard.setTextContent(card.getCardName().toString());
			deckPile.appendChild(deckCard);
		}
		root.appendChild(deckPile);
		
		//insert all buyCards (Cards that can be bought during the Game) created from Game into the XML_Document
		Element buyCards = docIn.createElement(ELEMENT_BUYCARDS);
		Set<CardName> cardSet = this.buyCards.keySet();
		for(CardName cardName: cardSet){
			Element buyCard = docIn.createElement(ELEMENT_BUYCARD);
			buyCard.setAttribute(ATTR_BUYCARD_NUMBER, Integer.toString(this.buyCards.get(cardName)));
			buyCard.setTextContent(cardName.toString());
			buyCards.appendChild(buyCard);
		}
		root.appendChild(buyCards);
		
		//insert all handCards in the right order into the XML_Document
		Element handCards = docIn.createElement(ELEMENT_HANDCARDS);
		for(Card card: this.handCards){
			Element handCard = docIn.createElement(ELEMENT_HANDCARD);
			handCard.setTextContent(card.getCardName().toString());
			handCards.appendChild(handCard);
		}
		root.appendChild(handCards);
		
		//insert opponents name, handcards and deckcards to show on client
		Element opponent = docIn.createElement(ELEMENT_OPPONENT);
		opponent.setTextContent(this.opponent);
		opponent.setAttribute(ATTR_HAND_NUMBER, Integer.toString(this.handNumber));
		opponent.setAttribute(ATTR_DECK_NUMBER, Integer.toString(this.deckNumber));
		root.appendChild(opponent);
		
		//insert startingPlayer
		Element startingPlayer = docIn.createElement(ELEMENT_STARTING_PLAYER);
		startingPlayer.setTextContent(this.startingPlayer);
		root.appendChild(startingPlayer);
		
		//insert Phase
		Element phase = docIn.createElement(ELEMENT_PHASE);
		phase.setTextContent(this.phase.toString());
		root.appendChild(phase);
	
	}
	
	/**
	 * Creates the objects from XML
	 * 
	 * @author Lukas
	 * @param docIn
	 * 				XML-Document
	 */
	@Override
	protected void init(Document docIn){
		Element root = docIn.getDocumentElement();
		
		//creates the deckPile from XML_Document with new CardObjects
		NodeList tmpElements = root.getElementsByTagName(ELEMENT_DECKPILE);
        if (tmpElements.getLength() > 0) {
            Element deckPile = (Element) tmpElements.item(0);
            NodeList deckElements = deckPile.getElementsByTagName(ELEMENT_DECKCARD);
            for(int i = deckElements.getLength() -1; i >= 0; i--){
            	Element deckCard = (Element) deckElements.item(i);
            	this.deckPile.push(Card.getCard(CardName.parseName(deckCard.getTextContent())));
            }
        }
        
        //creates all buyCards from XML_Document with new CardObjects
        tmpElements = root.getElementsByTagName(ELEMENT_BUYCARDS);
        if(tmpElements.getLength() > 0){
        	Element buyCards = (Element) tmpElements.item(0);
        	NodeList buyElements = buyCards.getElementsByTagName(ELEMENT_BUYCARD);
        	for(int i = buyElements.getLength() -1; i >= 0; i--){
        		Element buyCard = (Element) buyElements.item(i);
        		Integer numOfCards = Integer.parseInt(buyCard.getAttribute(ATTR_BUYCARD_NUMBER));
        		this.buyCards.put(CardName.parseName(buyCard.getTextContent()), numOfCards);
        	}
        }
        
        //creates all handCards from XML_Document with new CardObjects
        tmpElements = root.getElementsByTagName(ELEMENT_HANDCARDS);
        if(tmpElements.getLength() > 0){
        	Element handCards = (Element) tmpElements.item(0);
        	NodeList handElements = handCards.getElementsByTagName(ELEMENT_HANDCARD);
        	for(int i = handElements.getLength() -1; i >= 0; i--){
        		Element handCard = (Element) handElements.item(i);
        		this.handCards.add(Card.getCard(CardName.parseName(handCard.getTextContent())));
        	}
        }
        
        //sets the opponents name
        tmpElements = root.getElementsByTagName(ELEMENT_OPPONENT);
        if(tmpElements.getLength() > 0){
        	Element opponent = (Element) tmpElements.item(0);
        	this.opponent = opponent.getTextContent();
        	this.handNumber = Integer.parseInt(opponent.getAttribute(ATTR_HAND_NUMBER));
        	this.deckNumber = Integer.parseInt(opponent.getAttribute(ATTR_DECK_NUMBER));
        }
        
        //set the startingPlayer
        tmpElements = root.getElementsByTagName(ELEMENT_STARTING_PLAYER);
        if(tmpElements.getLength() > 0){
        	Element startingPlayer = (Element) tmpElements.item(0);
        	this.startingPlayer = startingPlayer.getTextContent();
        }
        
        //set the phase
        tmpElements = root.getElementsByTagName(ELEMENT_PHASE);
        if(tmpElements.getLength() > 0){
        	Element phase = (Element) tmpElements.item(0);
        	this.phase = Phase.parsePhase(phase.getTextContent());
        }
	}
	


	public HashMap<CardName, Integer> getBuyCards(){
		return this.buyCards;
	}


	public Stack<Card> getDeckPile(){
		return this.deckPile;
	}

	public String getOpponent(){
		return this.opponent;
	}
	

	
	public LinkedList<Card> getHandCards(){
		return this.handCards;
	}
	
	public Integer getDeckNumber() {
		return this.deckNumber;
	}
	

	public Integer getHandNumber() {
		return this.handNumber;
	}
	
	public String getStartingPlayer(){
		return this.startingPlayer;
	}
	
	public Phase getPhase(){
		return this.phase;
	}

	
	
	public void setHandCards(LinkedList<Card> handCards){
		this.handCards = handCards;
	}
	

	public void setBuyCards(HashMap<CardName, Integer> buyCards){
		this.buyCards = buyCards;
	}


	public void setDeckPile(Stack<Card> deckPile){
		this.deckPile = deckPile;
	}

	public void setOpponent(String opponent){
		this.opponent = opponent;
	}


	public void setDeckNumber(Integer deckNumber) {
		this.deckNumber = deckNumber;
	}

	public void setHandNumber(Integer handNumber) {
		this.handNumber = handNumber;
	}
	
	public void setStartingPlayer(String startingPlayer){
		this.startingPlayer = startingPlayer;
	}
	
	public void setPhase(Phase phase){
		this.phase = phase;
	}
}//end CreateGame_Message