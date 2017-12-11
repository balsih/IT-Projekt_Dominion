package Messages;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Client --> Server: Just a request for the highscore
 * Server --> Client: Transports the Top5 highscore from server to client in one String
 * 
 * @author Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:01:15
 */
public class HighScore_Message extends Message {

	private static final String ELEMENT_HIGHSCORE = "highScore";
	private static final String ELEMENT_TRANSLATION = "translation";
	private String highScore;
	
	/*
	 * 0 = translation(true)
	 * 1 = translation(false)
	 */
	private Integer translation;


	public HighScore_Message(){
		super();
	}

	/**
	 * 
	 * @param docIn
	 */
	@Override
	protected void addNodes(Document docIn){
        Element root = docIn.getDocumentElement();
		
		Element highScore = docIn.createElement(ELEMENT_HIGHSCORE);
		highScore.setTextContent(this.highScore);
		root.appendChild(highScore);
		
		Element translation = docIn.createElement(ELEMENT_TRANSLATION);
		translation.setTextContent(this.translation.toString());
		root.appendChild(translation);
	}

	/**
	 * 
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
        
		tmpElements = root.getElementsByTagName(ELEMENT_TRANSLATION);
        if (tmpElements.getLength() > 0) {
            Element translation = (Element) tmpElements.item(0);
            this.translation = Integer.parseInt(translation.getTextContent());
        }
	}

	
	public String getHighScore(){
		return this.highScore;
	}
	
	/**
	 * @return 0 if translation(true)
	 * 			,1 if translation(false)
	 */
	public Integer getTranslation(){
		return this.translation;
	}

	public void setHighScore(String highScore){
		this.highScore = highScore;
	}
	
	/**
	 * @param translation
	 * 			0 if translation(true)
	 * 			,1 if translation(false)
	 */
	public void setTranslation(Integer translation){
		this.translation = translation;
	}
}//end HighScore_Message