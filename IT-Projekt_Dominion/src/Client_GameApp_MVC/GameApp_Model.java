import Klassendiagramm.Cards.Card;

/**
 * @author Renate
 * @version 1.0
 * @created 31-Okt-2017 17:04:41
 */
public class GameApp_Model {

	protected int actions;
	protected int buys;
	protected String clientName;
	private String clientNameRegex;
	protected String currentPlayer;
	private LinkedList deck;
	private LinkedList discardPile;
	private LinkedList fieldCards;
	protected String gameMode;
	protected LinkedList handCards;
	protected String highScore;
	private String ipAdress;
	private String ipRegex;
	protected String opponent;
	protected int opponentHandCards;
	private String password;
	private String passwordRegex;
	protected LinkedList playedCards;
	private int port;
	protected boolean won;
	public Dominion_Main m_Dominion_Main;
	public Card m_Card;

	public GameApp_Model(){

	}

	public void finalize() throws Throwable {

	}
	/**
	 * 
	 * @param moveType
	 */
	public boolean checkMoveValidity(String moveType){
		return false;
	}

	/**
	 * 
	 * @param userInput
	 * @param inputType
	 */
	private boolean checkUserInput(String userInput, String inputType){
		return false;
	}

	private Socket connect(){
		return null;
	}

	public cryptPassword(){

	}

	private decryptPassword(){

	}

	public String getPassword(){
		return "";
	}

	/**
	 * 
	 * @param ipAdress
	 * @param port
	 */
	public init(String ipAdress, int port){

	}

	public initializeServerListening(){

	}

	/**
	 * 
	 * @param cardName
	 */
	public sendBuyCard(String cardName){

	}

	/**
	 * 
	 * @param chat
	 */
	public sendChat(String chat){

	}

	public sendCreateNewPlayer(){

	}

	public sendGameMode(){

	}

	public sendLogin(){

	}

	/**
	 * 
	 * @param cardName
	 */
	public int sendPlayCard(String cardName){
		return 0;
	}

	public sendSkipPhase(){

	}

	/**
	 * 
	 * @param gameMode
	 */
	public setGameMode(enum gameMode){

	}

	/**
	 * 
	 * @param password
	 */
	public setPassword(String password){

	}
}//end GameApp_Model