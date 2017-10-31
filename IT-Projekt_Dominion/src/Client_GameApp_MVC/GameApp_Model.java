package Client_GameApp_MVC;

import java.net.Socket;
import java.util.LinkedList;

import Abstract_MVC.Model;
import Cards.Card;
import MainClasses.Dominion_Main;

/**
 * @author Renate
 * @version 1.0
 * @created 31-Okt-2017 17:04:41
 */
public class GameApp_Model extends Model {

	protected int actions;
	protected int buys;
	protected String clientName;
	private String clientNameRegex;
	protected String currentPlayer;
	private LinkedList<Card> deck;
	private LinkedList<Card> discardPile;
	private LinkedList<Card> fieldCards;
	protected String gameMode;
	protected LinkedList<Card> handCards;
	protected String highScore;
	private String ipAdress;
	private String ipRegex;
	protected String opponent;
	protected int opponentHandCards;
	private String password;
	private String passwordRegex;
	protected LinkedList<Card> playedCards;
	private int port;
	protected boolean won;


	public GameApp_Model(){

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

	public void cryptPassword(){

	}

	private void decryptPassword(){

	}

	public String getPassword(){
		return "";
	}

	/**
	 * 
	 * @param ipAdress
	 * @param port
	 */
	public void init(String ipAdress, int port){

	}

	public void initializeServerListening(){

	}

	/**
	 * 
	 * @param cardName
	 */
	public void sendBuyCard(String cardName){

	}

	/**
	 * 
	 * @param chat
	 */
	public void sendChat(String chat){

	}

	public void sendCreateNewPlayer(){

	}

	public void sendGameMode(){

	}

	public void sendLogin(){

	}

	/**
	 * 
	 * @param cardName
	 */
	public int sendPlayCard(String cardName){
		return 0;
	}

	public void sendSkipPhase(){

	}

	/**
	 * 
	 * @param gameMode
	 */
	public void setGameMode(String gameMode){

	}

	/**
	 * 
	 * @param password
	 */
	public void setPassword(String password){

	}
}//end GameApp_Model