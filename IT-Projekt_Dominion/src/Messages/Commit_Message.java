package Messages;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author Lukas
 * @version 1.0
 * @created 01-Nov-2017 14:52:31
 */
public class Commit_Message extends Message {
	
	private static final String ELEMENT_NOTIFICATION = "notification";
	private static final String ATTR_MOVE = "move";
	private String notification;
	private Content move;

	
	public Commit_Message(){
		super();
	}

	/**
	 * 
	 * @param docIn
	 */
	@Override
	protected void addNodes(Document docIn){
        Element root = docIn.getDocumentElement();
		
		Element notification = docIn.createElement(ELEMENT_NOTIFICATION);
		notification.setTextContent(this.notification);
		notification.setAttribute(ATTR_MOVE, this.move.toString());
		root.appendChild(notification);
	}

	/**
	 * 
	 * @param docIn
	 */
	@Override
	protected void init(Document docIn){
		Element root = docIn.getDocumentElement();
		
		NodeList tmpElements = root.getElementsByTagName(ELEMENT_NOTIFICATION);
        if (tmpElements.getLength() > 0) {
            Element notification = (Element) tmpElements.item(0);
            this.notification = notification.getTextContent();
            this.move = Content.parseContent(notification.getAttribute(ATTR_MOVE));
        }
	}

	
	
	public String getNotification() {
		return notification;
	}
	

	public Content getMove() {
		return move;
	}
	

	public void setNotification(String notification) {
		this.notification = notification;
	}


	public void setMove(Content move) {
		this.move = move;
	}
	
}//end CommitTransmission_Message