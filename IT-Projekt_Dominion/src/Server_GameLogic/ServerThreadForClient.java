package Server_GameLogic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Queue;

import java.util.logging.Logger;

import Cards.CardName;
import Messages.BuyCard_Message;
import Messages.Chat_Message;
import Messages.Commit_Message;
import Messages.CreateGame_Message;
import Messages.CreateNewPlayer_Message;
import Messages.Error_Message;
import Messages.Failure_Message;
import Messages.GameMode_Message;
import Messages.HighScore_Message;
import Messages.Login_Message;
import Messages.Message;
import Messages.Content;
import Messages.MessageType;
import Messages.PlayCard_Message;
import Messages.PlayerSuccess_Message;
import Messages.SkipPhase_Message;
import Messages.UpdateGame_Message;
import Server_Services.DB_Connector;


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
	public static ServerThreadForClient getServerThreadForClient(Socket clientSocket){
		ServerThreadForClient client;
		InetAddress inetAddress = clientSocket.getInetAddress();
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
		case GiveUp:
			msgOut = this.processGiveUp(msgIn);
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
		return this.player.skipPhase();
	}

	/**
	 * Checks if the hands of client and server are equal
	 * If yes (should be usual), Player "trys" to play card. If not able it will be visible in the ugmsg
	 * 
	 * @param msgIn, PlayCard_Message
	 * @return UpdateGame_Message, content depends if player was able to play the card or Failure_Message
	 * PlayerSuccess_Message if player ended the game with this move
	 */
	private Message processPlayCard(Message msgIn) {
		PlayCard_Message pcmsg = (PlayCard_Message) msgIn;
		CardName cardName = pcmsg.getCard().getCardName();
		Integer index = pcmsg.getIndex();
		if(cardName == this.player.getHandCards().get(index).getCardName()){
			return this.player.play(cardName, index);
		}else{//the cards on the client and server are not the same(should not be possible)
			this.logger.severe(pcmsg.getClient()+"'s handcards aren't equals to the cards in the game");
			Failure_Message fmsg = new Failure_Message();
			fmsg.setMove(Content.PlayCard);
			return fmsg;
		}
	}

	/**
	 * Checks if the name is stored in the database and if yes,
	 * it has to be the adequate password
	 * 
	 * @param msgIn, Login_Message
	 * @return Commit_Message, content depends on if clientName and password are correct, or Failure_Message
	 */
	private Message processLogin(Message msgIn) {
		Login_Message lmsg = (Login_Message) msgIn;
		String clientName = lmsg.getClient();
		
		DB_Connector dbConnector = DB_Connector.getDB_Connector();
		boolean success = dbConnector.checkLoginInput(clientName, lmsg.getPassword());
		if(success){
			this.logger.severe(clientName+" "+Content.Login.toString()+" succeeded");
			Commit_Message cmsg = new Commit_Message();
			cmsg.setMove(Content.Login);
			return cmsg;
		}else{
			this.logger.severe(clientName+" "+Content.Login.toString()+" failed");
			Failure_Message fmsg = new Failure_Message();
			fmsg.setMove(Content.Login);
			fmsg.setNotification(Content.Login.toString()+" failed");
			return fmsg;
		}
	}

	/**
	 * Takes the highscore from the database
	 * 
	 * @param msgIn, HighScore_Message
	 * @return HighScore_Message, content of the top5 (name, points) in one String
	 */
	private Message processHighScore(Message msgIn) {
		DB_Connector dbConnector = DB_Connector.getDB_Connector();
		HighScore_Message hsmsg = new HighScore_Message();
		hsmsg.setHighScore(dbConnector.getHighScore());
		this.logger.severe("send highscore to "+hsmsg.getClient());
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
		Content mode = gmmsg.getMode();
		Content move = Content.GameMode;
		//get a new Game and add player and Bot (if singlePlayer) to the game, 
		//Game will start immediately if singlePlayer or your secondPlayer
		if(mode == Content.SinglePlayer || mode == Content.MultiPlayer){
			this.player = new Player(gmmsg.getClient());
			this.game = Game.getGame(mode, this.player);
			if(this.game.isReadyToStart()){
				CreateGame_Message cgmsg = new CreateGame_Message();
				cgmsg.setBuyCards(this.game.getBuyCards());
				cgmsg.setHandCards(this.player.getHandCards());
				cgmsg.setDeckPile(this.player.getDeckPile());
				cgmsg.setOpponent(this.game.getOpponent().getPlayerName());
				this.logger.severe(gmmsg.getClient()+" starts a"+mode.toString()+" game");
				return cgmsg;
			}
			this.logger.severe(gmmsg.getClient()+"waits for opponent");
			Commit_Message cmsg = new Commit_Message();
			cmsg.setMove(move);
			cmsg.setNotification("Waiting for opponent");
			return cmsg;
		}else{
			//if content is somehow invalid (should not be possible)
			this.logger.severe("invalid "+Content.GameMode.toString()+" from "+gmmsg.getClient());
			Failure_Message fmsg = new Failure_Message();
			fmsg.setMove(move);
			return fmsg;
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
		
		DB_Connector dbConnector = DB_Connector.getDB_Connector();
		boolean success = dbConnector.addNewPlayer(clientName, password);
		Content move = Content.CreateNewPlayer;
		if(success){
			this.logger.severe(clientName+"'s storation succeeded");
			Commit_Message cmsg = new Commit_Message();
			cmsg.setMove(move);
			return cmsg;
		}else{
			this.logger.severe(clientName+"'s storation failed");
			Failure_Message fmsg = new Failure_Message();
			fmsg.setMove(move);
			fmsg.setNotification(clientName+" is already occupied");
			return fmsg;
		}
	}


	/**
	 * The Chat_Message wrote by client has to be sent to opponent.
	 * The name has to be adapted to the Chat for better reading
	 * 
	 * @param msgIn, Chat_Message
	 * @return UpdateGame_Message with chatContent
	 */
	private Message processChat(Message msgIn) {
		Chat_Message cmsg = (Chat_Message) msgIn;
		String chat = cmsg.getChat();
		chat = this.player.getPlayerName()+": "+chat;
		UpdateGame_Message ugmsg = new UpdateGame_Message();
		ugmsg.setChat(chat);
		this.game.sendToOpponent(this.player, ugmsg);
		this.logger.severe(cmsg.getClient()+" has sent chat to"+this.game.getOpponent().getPlayerName()+": "+cmsg.getChat());
		return ugmsg;
	}

	/**
	 * Try to buy a card
	 * 
	 * @param msgIn, BuyCard_Message
	 * @return UpdateGame_Message with the correct buyed card or Failure_Message if player wasn't able to buy
	 * PlayerSuccess_Message if the player ended the game with this buy
	 */
	private Message processBuyCard(Message msgIn) {
		BuyCard_Message bcmsg = new BuyCard_Message();
		return this.player.buy(bcmsg.getCard().getCardName());
	}

	/**
	 * Client wants to know if something has changed in the Game
	 * 
	 * @param msgIn, AskForChanges_Message
	 * @return UpdateGame_Message if something has changed. Commit_Message if nothing has changed
	 */
	private Message processAskForChanges(Message msgIn) {
		if(this.waitingMessages.size() > 0){
			return this.waitingMessages.poll();
		}else{
			Commit_Message cmsg = new Commit_Message();
			cmsg.setMove(Content.AskForChanges);
			return cmsg;
		}
	}
	
	/**
	 * The player gave up his game. Opponent has to know he that he won
	 * 
	 * @param msgIn, GiveUp_Message
	 * @return PlayerSuccess_Message, you lost
	 */
    private Message processGiveUp(Message msgIn) {
    	PlayerSuccess_Message psmsgOpponent = new PlayerSuccess_Message();
    	psmsgOpponent.setSuccess(Content.Won);
    	this.game.sendToOpponent(this.player, psmsgOpponent);
    	PlayerSuccess_Message psmsgSelf = new PlayerSuccess_Message();
    	psmsgSelf.setSuccess(Content.Lost);
    	this.logger.severe(this.game.getOpponent().getPlayerName()+" "+Content.Won.toString()+"!");
    	return psmsgSelf;
	}

	
	public void addWaitingMessages(Message ugmsg){
		this.waitingMessages.offer(ugmsg);
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