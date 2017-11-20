package Client_GameApp_MVC;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;

import Abstract_MVC.Model;
import Cards.Card;
import Cards.CardName;
import MainClasses.Dominion_Main;
import Messages.AskForChanges_Message;
import Messages.BuyCard_Message;
import Messages.Chat_Message;
import Messages.Commit_Message;
import Messages.Content;
import Messages.CreateGame_Message;
import Messages.CreateNewPlayer_Message;
import Messages.Failure_Message;
import Messages.GameMode_Message;
import Messages.Login_Message;
import Messages.Message;
import Messages.MessageType;
import Messages.PlayCard_Message;
import Messages.PlayerSuccess_Message;
import Messages.SkipPhase_Message;
import Messages.UpdateGame_Message;
import Server_GameLogic.GameMode;
import Server_GameLogic.Phase;

/**
 * @author Adrian
 * @version 1.0
 * @created 31-Okt-2017 17:04:41
 */
public class GameApp_Model extends Model {
	
	private static final String NO_CONNECTION = "No connection to Server";

	protected String clientName;
	protected String opponent;
	protected String currentPlayer;
	
	protected int yourActions;
	protected int opponentActions;
	protected int yourBuys;
	protected int opponentBuys;
	protected int yourCoins;
	protected int opponentCoins;
	
	protected LinkedList<Card> yourHandCards;
	protected int opponentHandCards;
	protected LinkedList<Card> yourDeck;
	protected int opponentDeck;
	protected LinkedList<Card> yourDiscardPile;
	protected int opponentDiscardPile;
	protected LinkedList<Card> yourPlayedCards;
	protected LinkedList<Card> opponentPlayedCards;
	protected Card yourBuyedCard;
	protected Card opponentBuyedCard;
	protected Card yourDiscardPileTopCard;
	protected Card opponentDiscardPileTopCard;
	
	protected Content success;
	protected int victoryPoints;
	
	protected String gameMode;
	protected HashMap<CardName, Integer> buyCards;
	protected Phase currentPhase;
	
	private boolean listenToServer;
	private Dominion_Main main;
	private String clientNameRegex;
	private String highScore;
	private String ipAddress;
	private String ipRegex;
	private String passwordRegex;
	private int port;


	public GameApp_Model(Dominion_Main main){
		super();
		this.main = main;
		this.listenToServer = false;
	}

	/**
	 * 
	 * @param moveType
	 */
	public boolean checkMoveValidity(String moveType){
		return false;
	}
	

	public String encryptPassword(String password){
		return "";
	}

	private void decryptPassword(){

	}
	

	/**
	 * 
	 * @param userInput
	 * @param inputType
	 */
	private boolean checkUserInput(String userInput, String inputType){
		return false;
	}
	
	/**
	 * @author Bradley Richards
	 * The IP and Port will be set here
	 * 
	 * @param ipAdress
	 * @param port
	 */
	public void init(String ipAddress, int port){
	    this.ipAddress = ipAddress;
	    this.port = port;
	}

	/**
	 *@author Bradley Richards
	 *Creates a new Socket with the set IP and Port
	 * 
	 * @return Socket
	 */
	private Socket connect(){
	    Socket socket = null;
        try {
            socket = new Socket(ipAddress, port);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
	    return socket;
	}

	/**
	 * The client wants to buy a card. The result depends on the players validity to buy.
	 * 
	 * @param card
	 */
	public boolean sendBuyCard(Card card){
		BuyCard_Message bcmsg = new BuyCard_Message();
		bcmsg.setCard(card);
		boolean update = false;
		Message msgIn = this.processMessage(bcmsg);
		if(msgIn.getType().equals(MessageType.UpdateGame)){
			this.processUpdateGame(msgIn);
			update = true;
		}else if(msgIn.getType().equals(MessageType.PlayerSuccess)){
			this.processPlayerSuccess(msgIn);
			update = true;
		}else if(msgIn.getType().equals(MessageType.Failure)){
			//nothing toDo here
		}
		return update;
	}

	/**
	 * The clients sends a Chat_Message to the opponent. The chat of the client will also be sent to server and back.
	 * 
	 * @param chat
	 */
	public boolean sendChat(String chat){
		Chat_Message cmsg = new Chat_Message();
		cmsg.setChat(chat);
		boolean update = false;
		Message msgIn = this.processMessage(cmsg);
		if(msgIn.getType().equals(MessageType.UpdateGame)){
			this.processUpdateGame(msgIn);
			update = true;
		}
		return update;
	}
	

	/**
	 * @author Lukas
	 * The client wants to create his own profile. For this purpose the clientName has to be unique in the database.
	 * If the storage process succeeded, the client will get into the MainMenu.
	 * 
	 * @param password
	 * @return String, usually only necessary if clientName is already occupied
	 */
	public String sendCreateNewPlayer(String clientName, String password){
		String result = NO_CONNECTION;
		this.clientName = clientName;
		CreateNewPlayer_Message cnpmsg = new CreateNewPlayer_Message();
		String encryptedPassword = this.encryptPassword(password);
		cnpmsg.setPassword(encryptedPassword);
		Message msgIn = this.processMessage(cnpmsg);
		if(msgIn.getType().equals(MessageType.Commit)){
			this.main.startMainMenu();
		}else if(msgIn.getType().equals(MessageType.Failure)){
			Failure_Message fmsg = (Failure_Message) msgIn;
			result = fmsg.getNotification();
		}
		return result;
	}

	/**
	 * @author Lukas
	 * The client sends his GameMode (SinglePlayer or MultiPlayer) to Server.
	 * The result depends weather the client can start a game instantly or has to wait for an opponent
	 * 
	 * @param mode, SinglePlayer or MultiPlayer
	 * @return String, usually only necessary if the client has to wait for an opponent
	 */
	public String sendGameMode(GameMode mode){
		String result = NO_CONNECTION;
		GameMode_Message gmmsg = new GameMode_Message();
		gmmsg.setClient(this.clientName);//set the clientName and mode(SinglePlayer or MultiPlayer) to XML
		gmmsg.setMode(mode);
		this.gameMode = mode.toString();
		Message msgIn = this.processMessage(gmmsg);
		if(msgIn.getType().equals(MessageType.Commit)){
			this.listenToServer = true;
			this.main.startGameApp();
		}
		return result;
	}
	
	/**
	 * @author Lukas
	 * The client sends his encrypted password to server and will get to the MainMenu if the password is appropriate to clientName
	 * 
	 * @param password
	 * @return String, usually only necessary if clientName and password don't work
	 */
	public String sendLogin(String clientName, String password){
        String result = NO_CONNECTION;
		Login_Message lmsg = new Login_Message();
		lmsg.setClient(this.clientName);//set the clientName and encrypted password to XML
		String encryptedPassword = this.encryptPassword(password);
		lmsg.setPassword(encryptedPassword);
        this.clientName = clientName;
        Message msgIn = this.processMessage(lmsg);
		if(msgIn.getType().equals(MessageType.Commit)){
			this.main.startMainMenu();//login succeeded
		}else if(msgIn.getType().equals(MessageType.Failure)){
			Failure_Message fmsg = (Failure_Message) msgIn;//login failed, no password with clientName available
			result = fmsg.getNotification();
		}
        return result;
	}

	/**
	 * @author Lukas
	 * The client wants to play a chosen Card. The result depends on the validity of the move
	 * 
	 * @param card, chosen Card
	 * @param index, index of the chosen Card
	 */
	public boolean sendPlayCard(Card card){
		PlayCard_Message pcmsg = new PlayCard_Message();
		pcmsg.setCard(card);
		Integer index = null;
		for(int i = 0; i < this.yourHandCards.size(); i++){
			if(this.yourHandCards.get(i) == card)
				index = i;
		}
		pcmsg.setIndex(index);
		boolean update = false;
		Message msgIn = this.processMessage(pcmsg);
		if(msgIn.getType().equals(MessageType.UpdateGame)){
			this.processUpdateGame(msgIn);
			update = true;
		}else if(msgIn.getType().equals(MessageType.Failure)){
			//nothing toDo here
		}
		return update;
	}
	
	/**
	 * @author Lukas
	 * The client wants to skip the currentPhase. Success depends on if it is his turn or not
	 */
	public boolean sendSkipPhase(){
		boolean update = false;
		Message msgIn = this.processMessage(new SkipPhase_Message());
		if(msgIn.getType().equals(MessageType.UpdateGame)){
			this.processUpdateGame(msgIn);
			update = true;
		}else if(msgIn.getType().equals(MessageType.Failure)){
			//nothing toDo here
		}
		return update;
	}

	/**
	 * @author Lukas
	 * Create a new Game
	 * 
	 * @param msgIn
	 */
	private void processCreateGame(Message msgIn) {
		CreateGame_Message cgmsg = (CreateGame_Message) msgIn;
		this.yourHandCards = cgmsg.getHandCards();
		for(Card card: cgmsg.getDeckPile())
			this.yourDeck.add(card);
		this.buyCards = cgmsg.getBuyCards();
		this.opponent = cgmsg.getOpponent();
	}
	
	/**
	 * @author Lukas
	 * 
	 * @param msgIn
	 */
	private void processPlayerSuccess(Message msgIn) {
		PlayerSuccess_Message psmsg = (PlayerSuccess_Message) msgIn;
		this.success = psmsg.getSuccess();
		this.victoryPoints = psmsg.getVictoryPoints();
	}

	
	/**
	 * @author Adrian
	 * Updates the view after a message has been received.
	 * 
	 * @param msgIn
	 */
	
	private void processUpdateGame(Message msgIn) {	
		UpdateGame_Message ugmsg = (UpdateGame_Message) msgIn;

		if(ugmsg.getCurrentPlayer() != null)//If currentPlayer is set, the currentPlayer's turn ends
			this.currentPlayer = ugmsg.getCurrentPlayer();
		
		if(ugmsg.getActions() != null && ugmsg.getCurrentPlayer() == null){//Has to be calculated during the player's turn. Always currentPlayer
			this.yourActions += ugmsg.getActions();
		}else{
			this.yourActions = ugmsg.getActions();
		}
			
		if(ugmsg.getBuys() != null && ugmsg.getCurrentPlayer() == null){//Has to be calculated during the player's turn. Always currentPlayer
			this.yourBuys += ugmsg.getBuys();
		}else{
			this.yourBuys = ugmsg.getBuys();
		}
			
		if(ugmsg.getCoins() != null && ugmsg.getCurrentPlayer() == null){//Has to be calculated during the player's turn. Always currentPlayer
			this.yourCoins += ugmsg.getCoins();
		}else{
			this.yourCoins = ugmsg.getCoins();
		}
			
		if(ugmsg.getCurrentPhase() != null)//If Phase changed. Always currentPlayer
			this.currentPhase = ugmsg.getCurrentPhase();
			
		if(ugmsg.getBuyedCard() != null)//If a buy was successful. Always currentPlayer
			this.yourBuyedCard = ugmsg.getBuyedCard();
		
		if(ugmsg.getDeckPileCardNumber() != null)//Always currentPlayer
			this.deckPileCardNumber = ugmsg.getDeckPileCardNumber();
			
		if(ugmsg.getDiscardPileCardNumber() != null)//Always currentPlayer
			this.discardPileCardNumber = ugmsg.getDiscardPileCardNumber();
			
		if(ugmsg.getDiscardPileTopCard() != null)//Always currentPlayer. Only necessary to know opponents TopCard
			this.yourDiscardPileTopCard = ugmsg.getDiscardPileTopCard();
			
		if(ugmsg.getNewHandCards() != null)//The new handCards just drawn. Always currentPlayer
			System.out.println();
			
		if(ugmsg.getPlayedCard() != null)
			System.out.println();
	}

	
	/**
	 * 
	 * @param message
	 * @return
	 */
	private Message processMessage(Message message){
		Socket socket = connect();
		Message msgIn = null;
		if(socket != null){
			try{
				message.send(socket);
				msgIn = Message.receive(socket);
			}catch(Exception e){
				System.out.println(e.toString());
			}
			try { if (socket != null) socket.close(); } catch (IOException e) {}
		}
		return msgIn;
	}
	
	/**
	 * @author Lukas
	 * This method starts the ServerListening
	 */
	public void initializeServerListening(){
		new Thread(new ServerListening()).start();
	}
	
	/**
	 * Inner Class to use the methods of the outer Class comfortable.
	 * Necessary if a client wants to play a second game. The thread has to be started again
	 * 
	 * @author Lukas
	 *
	 */
	public class ServerListening implements Runnable{
		
		@Override
		public void run() {
			while(listenToServer){
				try {
					Thread.sleep(100);//10 request per second if something changed in the game
				} catch (InterruptedException e1) {
					System.out.println(e1.toString());
				}
				Socket socket = connect();
				if(socket != null){
					AskForChanges_Message afcmsg = new AskForChanges_Message();	
					try{
						afcmsg.send(socket);
						Message msgIn = Message.receive(socket);
						if(msgIn.getType().equals(MessageType.Commit)){
							//nothing toDo here
						}else if(msgIn.getType().equals(MessageType.UpdateGame)){
							processUpdateGame(msgIn);
						}else if(msgIn.getType().equals(MessageType.CreateGame)){
							processCreateGame(msgIn);
						}else if(msgIn.getType().equals(MessageType.PlayerSuccess)){
							PlayerSuccess_Message psmsg = (PlayerSuccess_Message) msgIn;
//							main.startSuccess(psmsg.getSuccess());
							listenToServer = false;
						}
					}catch(Exception e){
						System.out.println(e.toString());
					}
					try { if (socket != null) socket.close(); } catch (IOException e) {}
				}
			}
		}
	}

	
	

	public String getPassword(){
		return "";
	}

	public void setGameMode(String gameMode){

	}

	public void setPassword(String password){

	}
}//end GameApp_Model