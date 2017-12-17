package Messages;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * This Message is to get the highscore from the server.
 * <li>Communication: client --> server (request)
 * <li>Communication: server --> client (with highscore)
 * 
 * @author Lukas
 */
public class HighScore_Message extends Message {

	private static final String ELEMENT_HIGHSCORE = "highScore";
	private String highScore = null;


	public HighScore_Message(){
		super();
	}

	/**
	 * Adds the highscore <String> to XML
	 * 
	 * @author Lukas
	 * @param docIn
	 * 				XML-Document
	 */
	@Override
	protected void addNodes(Document docIn){
        Element root = docIn.getDocumentElement();
		
        if(this.highScore != null){
    		Element highScore = docIn.createElement(ELEMENT_HIGHSCORE);
    		highScore.setTextContent(this.highScore);
    		root.appendChild(highScore);
        }
	}

	/**
	 * Creates the object highScore <String> from XML
	 * 
	 * @author Lukas
	 * @param docIn
	 */
	@Override
	protected void init(Document docIn){
		Element root = docIn.getDocumentElement();
		
		NodeList tmpElements = root.getElementsByTagName(ELEMENT_HIGHSCORE);
        if (tmpElements.getLength() > 0) {
            Element highScore = (Element) tmpElements.item(0);
            this.highScore = highScore.getTextContent();
        }
	}

	
	public String getHighScore(){
		return this.highScore;
	}
	
	public void setHighScore(String highScore){
		this.highScore = highScore;
	}
}//end HighScore_Message