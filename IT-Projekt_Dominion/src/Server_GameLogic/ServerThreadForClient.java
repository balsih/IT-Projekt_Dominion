package Server_GameLogic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Queue;

import java.util.logging.Logger;

import Messages.Commit_Message;
import Messages.CreateGame_Message;
import Messages.CreateNewPlayer_Message;
import Messages.Error_Message;
import Messages.Failure_Message;
import Messages.GameMode_Message;
import Messages.HighScore_Message;
import Messages.Login_Message;
import Messages.Message;
import Messages.MessageType;
import Server_Services.DB_Connector;
import Server_Services.ServiceLocator;


/**
<<<<<<< HEAD
 * @author Lukas
=======
 * @author default: Lukas
>>>>>>> branch 'master' of https://github.com/Eagleman1997/IT-Projekt_Dominion.git
 * @version 1.0
 * @created 31-Okt-2017 17:09:30
 */
public class ServerThreadForClient implements Runnable {
	
	private static HashMap<InetAddress, ServerThreadForClient> connections = new HashMap<InetAddress, ServerThreadForClient>();
	
	private ServiceLocator sl = ServiceLocator.getServiceLocator();
	private final Logger logger = Logger.getLogger("");

	private Socket clientSocket;
	private Game game;
	private Player player;
	private Queue<Message> waitingMessages;


	private ServerThreadForClient(){
		super();
	}

	/**
	 * Factory Pattern, if a new client connects to server, a new Thread will be created.
	 * If a client already had connected with server, he will have the same Thread as before
	 * 
	 * @param inetAddress, the address from the client to validate if a new Thread is necessary
	 * @param clientSocket, with DOM, a new Socket is required for each connection
	 * @return client, a new or existing Thread. Depends if a Thread of the client already exists
	 */
	public static ServerThreadForClient getServerThreadForClient(InetAddress inetAddress, Socket clientSocket){
		ServerThreadForClient client;
		if(connections.containsKey(inetAddress)){
			client = connections.get(inetAddress);
		}else{
			client = new ServerThreadForClient();
			connections.put(inetAddress, client);
		}
		client.addClientSocket(clientSocket);
		return client;
	}

	/**
	 * @author Bradley Richards
	 * 
	 * Everytime a Message received from Client, the Message has to be identified and processed
	 */
	@Override
	public void run(){
		try{			// Read a message from the client
			Message msgIn = Message.receive(this.clientSocket);
			Message msgOut = processMessage(msgIn);
			msgOut.send(clientSocket);		
		}catch(Exception e) {
			logger.severe(e.toString());
		}finally{
            try { if (clientSocket != null) clientSocket.close(); } catch (IOException e) {}
		}
	}
	
	/**
	 * Checks the type of Message and process it
	 * 
	 * @param msgIn, the Message to process
	 * @return msgOut, depends on the type of Message, which new Message will return to client
	 */
    private Message processMessage(Message msgIn) {
		logger.info("Message received from client: "+ msgIn.toString());
		String clientName = msgIn.getClient();		
		Message msgOut = null;
		
		switch (MessageType.getType(msgIn)) {
		case AskForChanges:
			msgOut = this.processAskForChanges(msgIn);
			break;
		case BuyCard:
			msgOut = this.processBuyCard(msgIn);
			break;
		case Chat:
			msgOut = this.processChat(msgIn);
			break;
		case CreateNewPlayer:
			msgOut = this.processCreateNewPlayer(msgIn);
			break;
		case GameMode:
			msgOut = this.processGameMode(msgIn);
			break;
		case HighScore:
			msgOut = this.processHighScore(msgIn);
			break;
		case Login:
			msgOut = this.processLogin(msgIn);
			break;
		case PlayCard:
			msgOut = this.processPlayCard(msgIn);
			break;
		case SkipPhase:
			msgOut = this.processSkipPhase(msgIn);
			break;
		default:
			msgOut = new Error_Message();
		}
		msgOut.setClient(clientName);
    	return msgOut;
    }

    /**
     * Tells the Game to skip the current Phase of this Player
     * 
     * @param msgIn, SkipPhase_Message
     * @return Commit_Message
     */
	private Message processSkipPhase(Message msgIn) {
		// TODO Auto-generated method stub
		return null;
	}

	private Message processPlayCard(Message msgIn) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Checks if the name is stored in the database and if yes,
	 * it has to be the adequate password
	 * 
	 * @param msgIn, Login_Message
	 * @return Commit_Message, content depends on if clientName and password are correct
	 */
	private Message processLogin(Message msgIn) {
		Login_Message lmsg = (Login_Message) msgIn;
		String clientName = lmsg.getClient();
		String password = lmsg.getPassword();
		
		DB_Connector dbConnector = this.sl.getDB_Connector();
		boolean success = dbConnector.checkPlayerInput(clientName, password);
		if(success){
			return new Commit_Message();
		}else{
			return new Failure_Message();
		}
	}

	/**
	 * Takes the highscore from the database
	 * 
	 * @param msgIn, HighScore_Message
	 * @return hsmsg, content of the top5 (name, points) in one String
	 */
	private Message processHighScore(Message msgIn) {
		DB_Connector dbConnector = this.sl.getDB_Connector();
		String highScore = dbConnector.getHighScore();
		HighScore_Message hsmsg = new HighScore_Message();
		hsmsg.setHighScore(highScore);
		return hsmsg;
	}

	/**
	 * If GameMode is valid, start a new Game if Game has two Players (including Bot).
	 * If Client is the first Player in multiplayerMode, client has to wait for second Player
	 * 
	 * @param msgIn, GameMode_Message
	 * @return CreateGame_Message, Commit_Message or Failure_Message, 
	 * depends if input is valid and if client has to wait for second Player
	 */
	private Message processGameMode(Message msgIn) {
		GameMode_Message gmmsg = (GameMode_Message) msgIn;
		String mode = gmmsg.getMode();
		//get a new Game and add player and Bot (if singlePlayer) to the game, 
		//Game will start immediately if singlePlayer or your secondPlayer
		if(mode == "singleplayer" || mode == "multiplayer"){
			this.player = new Player(gmmsg.getClient());
			this.game = Game.getGame(this.clientSocket, gmmsg.getMode(), this.player);
			if(this.game.isReadyToStart())
				return new CreateGame_Message();
			return new Commit_Message();
		}else{
			//if content is somehow invalid
			logger.severe("invalid GameMode from "+gmmsg.getClient());
			return new Failure_Message();
		}
	}

    /**
     * Try to store a new Player into the database
     * If clientName is unique, the player will be stored successful
     * 
     * @param msgIn, SkipPhase_Message
     * @return Commit_Message or Failure_Message, depends on the answer given from the database
     */
	private Message processCreateNewPlayer(Message msgIn) {
		CreateNewPlayer_Message cnpmsg = (CreateNewPlayer_Message) msgIn;
		String clientName = cnpmsg.getClient();
		String password = cnpmsg.getPassword();
		
		DB_Connector dbConnector = this.sl.getDB_Connector();
		boolean success = dbConnector.addNewPlayer(clientName, password);
		if(success){
			return new Commit_Message();
		}else{
			return new Failure_Message();
		}
	}


	private Message processChat(Message msgIn) {
		// TODO Auto-generated method stub
		return null;
	}

	private Message processBuyCard(Message msgIn) {
		// TODO Auto-generated method stub
		return null;
	}

	private Message processAskForChanges(Message msgIn) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @param message
	 */
	public void sendMessage(Message message){

	}
	/**
	 * Adds a new Socket for client
	 * 
	 * @param clientSocket, adds a new Socket for each connection
	 */
	private void addClientSocket(Socket clientSocket){
		this.clientSocket = clientSocket;
	}
}//end ServerThreadForClient