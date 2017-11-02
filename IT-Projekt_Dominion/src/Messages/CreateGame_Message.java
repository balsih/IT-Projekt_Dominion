package Messages;

import java.util.HashMap;
import java.util.Stack;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import Cards.Card;

/**
 * @author Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:01:13
 */
public class CreateGame_Message extends Message {

	private static final String ATTR_BUYCARDNUMBER = "buyCardNumber";
	private static final String ELEMENT_BUYCARD = "buyCard";
	private static final String ELEMENT_BUYCARDS = "buyCards";
	private static final String ELEMENT_DECKCARD = "deckCard";
	private static final String ELEMENT_DECKPILE = "deckPile";
	private static final String ELEMENT_OPPONENT = "opponent";
	private static final String ELEMENT_DECKCARDNUMBER = "deckCardNumber";
	private int buyCardNumber;
	private int deckCardNumber;
	private String buyCards;
	private String deckPile;
	private String opponent;
	private Stack<Card> deckCard;
	private Stack<Card> buyCard;
	private HashMap<Card, Integer> fieldCards;


	public CreateGame_Message(){
		super();
		this.deckCard = new Stack<Card>();
		this.buyCard = new Stack<Card>();
	}

	/**
	 * 
	 * @param docIn
	 */
	@Override
	protected void addNodes(Document docIn){
        Element root = docIn.getDocumentElement();
		
		Element deckPile = docIn.createElement(ELEMENT_DECKPILE);
		deckPile.setTextContent(this.deckPile);
		root.appendChild(deckPile);
		
		Element deckCard = docIn.createElement(ELEMENT_DECKCARD);
	}
	
	/**
	 * 
	 * @param docIn
	 */
	@Override
	protected void init(Document docIn){

	}
	

	public Stack<Card> getBuyCard(){
		return this.buyCard;
	}

	public int getBuyCardNumber(){
		return this.buyCardNumber;
	}

	public String getBuyCards(){
		return this.buyCards;
	}

	public Stack<Card> getDeckCard(){
		return this.deckCard;
	}


	public String getDeckPile(){
		return this.deckPile;
	}

	public String getOpponent(){
		return this.opponent;
	}
	
	public int getDeckCardNumber(){
		return this.deckCardNumber;
	}
	
	public HashMap<Card, Integer> getFieldCards(){
		return this.fieldCards;
	}

	
	public void setFieldCards(HashMap<Card, Integer> fieldCards){
		this.fieldCards = fieldCards;
	}

	public void setBuyCard(Stack<Card> buyCard){
		this.buyCard = buyCard;
	}

	public void setBuyCardNumber(int buyCardNumber){
		this.buyCardNumber = buyCardNumber;
	}
	
	public void setDeckCardNumber(int deckCardNumber){
		this.deckCardNumber = deckCardNumber;
	}

	public void setBuyCards(String buyCards){
		this.buyCards = buyCards;
	}

	public void setDeckCard(Stack<Card> deckCard){
		this.deckCard = deckCard;
	}

	public void setDeckPile(String deckPile){
		this.deckPile = deckPile;
	}

	public void setOpponent(String opponent){
		this.opponent = opponent;
	}
}//end CreateGame_Message