package Client_GameApp_MVC;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;

import Abstract_MVC.Model;
import Cards.Card;
import Cards.CardName;
import MainClasses.Dominion_Main;
import Messages.Commit_Message;
import Messages.Content;
import Messages.CreateGame_Message;
import Messages.Failure_Message;
import Messages.GameMode_Message;
import Messages.Login_Message;
import Messages.Message;
import Messages.MessageType;

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
	private String password;
	private String passwordRegex;
	protected LinkedList<Card> playedCards;
	private int port;
	protected boolean won;
	private Dominion_Main main;


	public GameApp_Model(Dominion_Main main){
		super();
		this.main = main;
	}

	/**
	 * 
	 * @param moveType
	 */
	public boolean checkMoveValidity(String moveType){
		return false;
	}
	

	public void encryptPassword(){

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

	public void sendBuyCard(CardName cardName){
		String result = NO_CONNECTION;
		Socket socket = connect();
		if(socket != null){
			//work toDo here
			try{
				//work toDo here
			}catch(Exception e){
				System.out.println(e.toString());
			}
			try { if (socket != null) socket.close(); } catch (IOException e) {}
		}
//		return result;
	}

	public void sendChat(String chat){
		String result = NO_CONNECTION;
		Socket socket = connect();
		if(socket != null){
			//work toDo here
			try{
				//work toDo here
			}catch(Exception e){
				System.out.println(e.toString());
			}
			try { if (socket != null) socket.close(); } catch (IOException e) {}
		}
//		return result;
	}

	public String sendCreateNewPlayer(){
		String result = NO_CONNECTION;
		Socket socket = connect();
		if(socket != null){
			//work toDo here
			try{
				//work toDo here
			}catch(Exception e){
				System.out.println(e.toString());
			}
			try { if (socket != null) socket.close(); } catch (IOException e) {}
		}
		return result;
	}

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
				if(msgIn.getType().equals(MessageType.CreateGame)){//------> outSource because of Thread has to listen too
					CreateGame_Message cgmsg = (CreateGame_Message) msgIn;//setUp a new Game
					this.handCards = cgmsg.getHandCards();
					for(Card card: cgmsg.getDeckPile())
						this.deck.add(card);
					this.buyCards = cgmsg.getBuyCards();
					this.opponent = cgmsg.getOpponent();
					this.main.startGameApp();
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

	public String sendLogin(){
        String result = NO_CONNECTION;
        Socket socket = connect();
        if (socket != null) {
    		Login_Message lmsg = new Login_Message();
    		lmsg.setClient(this.clientName);//set the clientName and encrypted password to XML
    		lmsg.setPassword(this.password);
    		try {
    			lmsg.send(socket);
    			Message msgIn = Message.receive(socket);
    			if(msgIn.getType().equals(MessageType.Commit)){
    				Commit_Message cmsg = (Commit_Message) msgIn;//login succeeded
    				this.main.startMainMenu();
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


	public int sendPlayCard(CardName cardName){
		String result = NO_CONNECTION;
		Socket socket = connect();
		if(socket != null){
			//work toDo here
			try{
				//work toDo here
			}catch(Exception e){
				System.out.println(e.toString());
			}
			try { if (socket != null) socket.close(); } catch (IOException e) {}
		}
//		return result;
		return 0;
	}

	public void sendSkipPhase(){
		String result = NO_CONNECTION;
		Socket socket = connect();
		if(socket != null){
			//work toDo here
			try{
				//work toDo here
			}catch(Exception e){
				System.out.println(e.toString());
			}
			try { if (socket != null) socket.close(); } catch (IOException e) {}
		}
//		return result;
	}
	
	public void initializeServerListening(){

	}

	
	

	public String getPassword(){
		return "";
	}

	public void setGameMode(String gameMode){

	}

	public void setPassword(String password){

	}
}//end GameApp_Model