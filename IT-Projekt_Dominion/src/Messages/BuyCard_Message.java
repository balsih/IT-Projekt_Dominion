package Messages;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 
 * @author Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:01:11
 */
public class BuyCard_Message extends Message {

	private static final String ELEMENT_CARD = "card";
	private String card;


	public BuyCard_Message(){
		super();
	}

	/**
	 * 
	 * @param docIn
	 */
	@Override
	protected void addNodes(Document docIn){
        Element root = docIn.getDocumentElement();
		
		Element card = docIn.createElement(ELEMENT_CARD);
		card.setTextContent(this.card);
		root.appendChild(card);
	}


	/**
	 * 
	 * @param docIn
	 */
	@Override
	protected void init(Document docIn){
		Element root = docIn.getDocumentElement();
		
		NodeList tmpElements = root.getElementsByTagName(ELEMENT_CARD);
        if (tmpElements.getLength() > 0) {
            Element card = (Element) tmpElements.item(0);
            this.card = card.getTextContent();
            }
	}

	public String getCard(){
		return this.card;
	}
	
	public void setCard(String card){
		this.card = card;
	}
}//end BuyCard_Message