package Messages;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import Cards.Card;
import Cards.CardName;

/**
 * This Message is to play a card from the client's hand
 * <li>Communication: client --> server
 * 
 * @author Lukas
 */
public class PlayCard_Message extends Message {

	private static final String ELEMENT_CARD = "card";
	private Card card;


	public PlayCard_Message(){
		super();
	}

	/**
	 * Adds the chosen card to XML
	 * 
	 * @author Lukas
	 * @param docIn
	 * 				XML-Document
	 */
	@Override
	protected void addNodes(Document docIn){
        Element root = docIn.getDocumentElement();
		
		Element card = docIn.createElement(ELEMENT_CARD);
		card.setTextContent(this.card.toString());
		root.appendChild(card);
	}

	/**
	 * Creates the object card (Card) from XML
	 * 
	 * @author Lukas
	 * @param docIn
	 * 				XML-Document
	 */
	@Override
	protected void init(Document docIn){
		Element root = docIn.getDocumentElement();
		
		NodeList tmpElements = root.getElementsByTagName(ELEMENT_CARD);
        if (tmpElements.getLength() > 0) {
            Element card = (Element) tmpElements.item(0);
            this.card = Card.getCard(CardName.parseName(card.getTextContent()));
        }
	}
	

	public Card getCard(){
		return this.card;
	}
	
	public void setCard(Card card){
		this.card = card;
	}
}//end PlayCard_Message