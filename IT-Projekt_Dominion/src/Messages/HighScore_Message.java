package Klassendiagramm.Messages;


/**
 * @author Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:01:15
 */
public class HighScore_Message extends Message {

	private static final String ELEMENT_HIGHSCORE;
	private String highScore;



	public void finalize() throws Throwable {
		super.finalize();
	}
	public HighScore_Message(){

	}

	/**
	 * 
	 * @param docIn
	 */
	protected addNodes(Document docIn){

	}

	public String getHighScore(){
		return "";
	}

	/**
	 * 
	 * @param docIn
	 */
	protected init(Document docIn){

	}

	/**
	 * 
	 * @param highScore
	 */
	public setHighScore(String highScore){

	}
}//end HighScore_Message