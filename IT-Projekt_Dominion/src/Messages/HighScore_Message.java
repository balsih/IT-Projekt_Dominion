package Messages;

import com.sun.xml.internal.txw2.Document;

/**
 * @author Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:01:15
 */
public class HighScore_Message extends Message {

	private static final String ELEMENT_HIGHSCORE = "highscore";
	private String highScore;


	public HighScore_Message(){

	}

	/**
	 * 
	 * @param docIn
	 */
	@Override
	protected void addNodes(Document docIn){

	}

	public String getHighScore(){
		return "";
	}

	/**
	 * 
	 * @param docIn
	 */
	@Override
	protected void init(Document docIn){

	}

	/**
	 * 
	 * @param highScore
	 */
	public void setHighScore(String highScore){

	}
}//end HighScore_Message