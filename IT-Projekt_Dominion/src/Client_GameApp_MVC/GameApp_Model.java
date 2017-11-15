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

/**
 * @author Adrian
 * @version 1.0
 * @created 31-Okt-2017 17:04:41
 */
public class GameApp_Model extends Model {
	
	private static final String NO_CONNECTION = "No connection to Server";

	protected int actions;
	protected int buys;
	protected String clientName;
	private String clientNameRegex;
	protected String currentPlayer;
	private LinkedList<Card> deck;
	private LinkedList<Card> discardPile;
	private HashMap<CardName, Integer> buyCards;
	protected String gameMode;
	protected LinkedList<Card> handCards;
	protected String highScore;
	private String ipAddress;
	private String ipRegex;
	protected String opponent;
	protected int opponentHandCards;
	private String passwordRegex;
	protected LinkedList<Card> playedCards;
	private int port;
	protected boolean won;
	private boolean listenToServer;
	private Dominion_Main main;


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
	public void init(String ipAdress, int port){
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
	public void sendBuyCard(Card card){
		Socket socket = connect();
		if(socket != null){
			BuyCard_Message bcmsg = new BuyCard_Message();
			bcmsg.setCard(card);
			try{
				bcmsg.send(socket);
				Message msgIn = Message.receive(socket);
				if(msgIn.getType().equals(MessageType.UpdateGame)){
					this.processUpdateGame(msgIn);
				}else if(msgIn.getType().equals(MessageType.Failure)){
					//nothing toDo here
				}
			}catch(Exception e){
				System.out.println(e.toString());
			}
			try { if (socket != null) socket.close(); } catch (IOException e) {}
		}
	}

	/**
	 * The clients sends a Chat_Message to the opponent. The chat of the client will also be sent to server and back.
	 * 
	 * @param chat
	 */
	public void sendChat(String chat){
		Socket socket = connect();
		if(socket != null){
			Chat_Message cmsg = new Chat_Message();
			cmsg.setChat(chat);
			try{
				cmsg.send(socket);
				Message msgIn = Message.receive(socket);
				this.processUpdateGame(msgIn);
			}catch(Exception e){
				System.out.println(e.toString());
			}
			try { if (socket != null) socket.close(); } catch (IOException e) {}
		}
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
		Socket socket = connect();
		if(socket != null){
			CreateNewPlayer_Message cnpmsg = new CreateNewPlayer_Message();
			String encryptedPassword = this.encryptPassword(password);
			cnpmsg.setPassword(encryptedPassword);
			try{
				cnpmsg.send(socket);
				Message msgIn = Message.receive(socket);
				if(msgIn.getType().equals(MessageType.Commit)){
					this.main.startMainMenu();
				}else if(msgIn.getType().equals(MessageType.Failure)){
					Failure_Message fmsg = (Failure_Message) msgIn;
					result = fmsg.getNotification();
				}
			}catch(Exception e){
				System.out.println(e.toString());
			}
			try { if (socket != null) socket.close(); } catch (IOException e) {}
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
	public String sendGameMode(Content mode){
		String result = NO_CONNECTION;
		Socket socket = connect();
		if(socket != null){
			GameMode_Message gmmsg = new GameMode_Message();
			gmmsg.setClient(this.clientName);//set the clientName and mode(SinglePlayer or MultiPlayer) to XML
			gmmsg.setMode(mode);
			this.gameMode = mode.toString();
			try{
				gmmsg.send(socket);
				Message msgIn = Message.receive(socket);
				this.listenToServer = true;
				if(msgIn.getType().equals(MessageType.CreateGame)){//------> outSource because of Thread has to listen too
					processCreateGame(msgIn);//setUp a new Game
				}else if(msgIn.getType().equals(MessageType.Commit)){
					Commit_Message cmsg = (Commit_Message) msgIn;//waiting for other Player (only MultiPlayer)
					result = cmsg.getNotification();
				}else if(msgIn.getType().equals(MessageType.Failure)){
					result = mode.toString()+" is no valid mode";//wrong mode, should not be possible
				}
			}catch(Exception e){
				System.out.println(e.toString());
			}
			try { if (socket != null) socket.close(); } catch (IOException e) {}
		}
		return result;
	}

	/**
	 * @author Lukas
	 * Create a new Game
	 * 
	 * @param msgIn
	 */
	private void processCreateGame(Message msgIn) {
		CreateGame_Message cgmsg = (CreateGame_Message) msgIn;
		this.handCards = cgmsg.getHandCards();
		for(Card card: cgmsg.getDeckPile())
			this.deck.add(card);
		this.buyCards = cgmsg.getBuyCards();
		this.opponent = cgmsg.getOpponent();
		this.main.startGameApp();
	}
	
	/**
	 * @author Adrian
	 * Updates the view after a message has been received.
	 * 
	 * @param msgIn
	 */
	private void processUpdateGame(Message msgIn) {
		UpdateGame_Message ugmsg = (UpdateGame_Message) msgIn;
		if(ugmsg.getLog() != null)
			//workToDo
		if(ugmsg.getChat() != null)
			// Adds the updated chat message to the chat area.
			btnSendChatArea.setOnAction(event -> {
				String existingMessages = txtaChatArea.getText();
				String newMessage = txtfChatArea.getText();
				if(existingMessages.length() == 0)
					txtaChatArea.setText(newMessage);
				else 
					txtaChatArea.setText(existingMessages+"\n"+newMessage);
				txtfChatArea.setText("");
			});
		if(ugmsg.getActions() != null)
			//workToDo
		if(ugmsg.getBuys() != null)
			//workToDo
		if(ugmsg.getCoins() != null)
			//workToDo
		if(ugmsg.getCurrentPlayer() != null)
			//workToDo
		if(ugmsg.getCurrentPhase() != null)
			//workToDo
		if(ugmsg.getBuyedCard() != null)
			//workToDo
		if(ugmsg.getDeckPileCardNumber() != null)
			//workToDo
		if(ugmsg.getDiscardPileCardNumber() != null)
			//workToDo
		if(ugmsg.getDiscardPileTopCard() != null)
			//workToDo
		if(ugmsg.getNewHandCards() != null)
			//workToDo
		if(ugmsg.getPlayedCard() != null)
			//workToDo
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
        this.clientName = clientName;
        Socket socket = connect();
        if (socket != null) {
    		Login_Message lmsg = new Login_Message();
    		lmsg.setClient(this.clientName);//set the clientName and encrypted password to XML
    		String encryptedPassword = this.encryptPassword(password);
    		lmsg.setPassword(encryptedPassword);
    		try {
    			lmsg.send(socket);
    			Message msgIn = Message.receive(socket);
    			if(msgIn.getType().equals(MessageType.Commit)){
    				this.main.startMainMenu();//login succeeded
    			}else if(msgIn.getType().equals(MessageType.Failure)){
    				Failure_Message fmsg = (Failure_Message) msgIn;//login failed, no password with clientName available
    				return fmsg.getNotification();
    			}
    		}catch(Exception e) {
    			System.out.println(e.toString());
    		}
            try { if (socket != null) socket.close(); } catch (IOException e) {}
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
	public void sendPlayCard(Card card, Integer index){
		Socket socket = connect();
		if(socket != null){
			PlayCard_Message pcmsg = new PlayCard_Message();
			pcmsg.setCard(card);
			pcmsg.setIndex(index);
			try{
				pcmsg.send(socket);
				Message msgIn = Message.receive(socket);
				if(msgIn.getType().equals(MessageType.UpdateGame)){
					this.processUpdateGame(msgIn);
				}else if(msgIn.getType().equals(MessageType.Failure)){
					//nothing toDo here
				}
			}catch(Exception e){
				System.out.println(e.toString());
			}
			try { if (socket != null) socket.close(); } catch (IOException e) {}
		}
	}
	
	/**
	 * @author Lukas
	 * The client wants to skip the currentPhase. Success depends on if it is his turn or not
	 */
	public void sendSkipPhase(){
		Socket socket = connect();
		if(socket != null){
			SkipPhase_Message spmsg = new SkipPhase_Message();
			try{
				spmsg.send(socket);
				Message msgIn = Message.receive(socket);
				if(msgIn.getType().equals(MessageType.UpdateGame)){
					this.processUpdateGame(msgIn);
				}else if(msgIn.getType().equals(MessageType.Failure)){
					//nothing toDo here
				}
			}catch(Exception e){
				System.out.println(e.toString());
			}
			try { if (socket != null) socket.close(); } catch (IOException e) {}
		}
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