package Messages;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import Cards.CardName;

/**
 * @author Lukas
 * This Message tries to buy the chosen card
 * client --> server
 */
public class BuyCard_Message extends Message {

	private static final String ELEMENT_CARD = "card";
	private CardName cardName;


	public BuyCard_Message(){
		super();
	}

	/**
	 * @author Lukas
	 * Adds the chosen card to XML
	 * 
	 * @param docIn
	 * 				XML-Document
	 */
	@Override
	protected void addNodes(Document docIn){
        Element root = docIn.getDocumentElement();
		
		Element card = docIn.createElement(ELEMENT_CARD);
		card.setTextContent(this.cardName.toString());
		root.appendChild(card);
	}


	/**
	 * @author Lukas
	 * Creates the object cardName (CardName) from XML
	 * 
	 * @param docIn
	 * 				XML-Document
	 */
	@Override
	protected void init(Document docIn){
		Element root = docIn.getDocumentElement();
		
		NodeList tmpElements = root.getElementsByTagName(ELEMENT_CARD);
        if (tmpElements.getLength() > 0) {
            Element card = (Element) tmpElements.item(0);
            this.cardName = CardName.parseName(card.getTextContent());
            }
	}

	public CardName getCard(){
		return this.cardName;
	}
	
	public void setCard(CardName cardName){
		this.cardName = cardName;
	}
}//end BuyCard_Message