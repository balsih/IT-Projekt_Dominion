package Messages;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * The clients choses to play singleplayer or multiplayer
 * 
 * @author Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:01:14
 */
public class GameMode_Message extends Message {

	private static final String ELEMENT_MODE = "mode";
	private String mode;


	public GameMode_Message(){
		super();
	}

	/**
	 * 
	 * @param docIn
	 */
	@Override
	protected void addNodes(Document docIn){
        Element root = docIn.getDocumentElement();
		
		Element mode = docIn.createElement(ELEMENT_MODE);
		mode.setTextContent(this.mode);
		root.appendChild(mode);
	}


	/**
	 * 
	 * @param docIn
	 */
	@Override
	protected void init(Document docIn){
		Element root = docIn.getDocumentElement();
		
		NodeList tmpElements = root.getElementsByTagName(ELEMENT_MODE);
        if (tmpElements.getLength() > 0) {
            Element mode = (Element) tmpElements.item(0);
            this.mode = mode.getTextContent();
        }
	}

	
	public String getMode(){
		return this.mode;
	}
	
	public void setMode(String mode){
		this.mode = mode;
	}
}//end GameMode_Message