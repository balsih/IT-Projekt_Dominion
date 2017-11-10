package Messages;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:01:19
 */
public class PlayCard_Message extends Message {

	private static final String ELEMENT_CARD = "card";
	private static final String ATTR_INDEX = "index";
	private String card;
	private Integer index;


	public PlayCard_Message(){
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
		card.setAttribute(ATTR_INDEX, this.index.toString());
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
            this.index = Integer.parseInt(card.getAttribute(ATTR_INDEX));
        }
	}
	
	
	public Integer getIndex(){
		return this.index;
	}

	public String getCard(){
		return this.card;
	}
	
	public void setIndex(Integer index){
		this.index = index;
	}
	public void setCard(String card){
		this.card = card;
	}
}//end PlayCard_Message