package Messages;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import Server_GameLogic.GameMode;

/**
 * This Message is to send the server the client's chosen GameMode (Singleplayer or Multiplayer).
 * <li>Communication: client --> server
 * 
 * @author Lukas
 */
public class GameMode_Message extends Message {

	private static final String ELEMENT_MODE = "mode";
	private GameMode mode;


	public GameMode_Message(){
		super();
	}

	/**
	 * Adds the mode <GameMode> to XML
	 * 
	 * @author Lukas
	 * @param docIn
	 * 				XML-Document
	 */
	@Override
	protected void addNodes(Document docIn){
        Element root = docIn.getDocumentElement();
		
		Element mode = docIn.createElement(ELEMENT_MODE);
		mode.setTextContent(this.mode.toString());
		root.appendChild(mode);
	}


	/**
	 * Creates the object mode <GameMode> from XML
	 * 
	 * @author Lukas
	 * @param docIn
	 * 				XML-Document
	 */
	@Override
	protected void init(Document docIn){
		Element root = docIn.getDocumentElement();
		
		NodeList tmpElements = root.getElementsByTagName(ELEMENT_MODE);
        if (tmpElements.getLength() > 0) {
            Element mode = (Element) tmpElements.item(0);
            this.mode = GameMode.parseGameMode(mode.getTextContent());
        }
	}

	
	public GameMode getMode(){
		return this.mode;
	}
	
	public void setMode(GameMode mode){
		this.mode = mode;
	}
}//end GameMode_Message