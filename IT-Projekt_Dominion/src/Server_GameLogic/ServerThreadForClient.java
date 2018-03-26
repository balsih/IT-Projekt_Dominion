package Server_GameLogic;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import java.util.logging.Logger;

import Cards.Card;
import Cards.Cellar_Card;
import Cards.Mine_Card;
import Cards.Remodel_Card;
import Cards.Workshop_Card;
import Messages.AskForChanges_Message;
import Messages.BuyCard_Message;
import Messages.Chat_Message;
import Messages.Commit_Message;
import Messages.CreateGame_Message;
import Messages.CreateNewPlayer_Message;
import Messages.Error_Message;
import Messages.Failure_Message;
import Messages.GameMode_Message;
import Messages.HighScore_Message;
import Messages.Interaction;
import Messages.Interaction_Message;
import Messages.Knock_Message;
import Messages.Login_Message;
import Messages.Message;
import Messages.GameSuccess;
import Messages.MessageType;
import Messages.PlayCard_Message;
import Messages.PlayerSuccess_Message;
import Messages.UpdateGame_Message;
import Server_Services.DB_Connector;


/**
 * Provides a Thread for client to communicate with.
 * The identifier is the clientName (String).
 * One Thread is exclusive for one client. He has priority for this Thread for 30 min.
 * In case he hasn't done something in the last 30 min, it is possible to kick the client with a different IP.
 * This is necessary if something happened like an unexpected computer-shutdown client-site.
 * 
 * The communication-format with this Tread is XML DOM.
 * 
 * @author Lukas
 */
public class ServerThreadForClient implements Runnable {
	
	private static LinkedList<String> onlineClients = new LinkedList<String>();
	private static HashMap<String, ServerThreadForClient> connections = new HashMap<String, ServerThreadForClient>();
	
	private static final Logger logger = Logger.getLogger("");
	private final Integer AFK_TIMER = 1800000;//30 min

	private Socket clientSocket;
	private Message msgIn;
	private Game game = null;
	private Player player;
	private InetAddress inetAddress;
	private Queue<Message> waitingMessages = new LinkedList<Message>();
	private Queue<Message> unsentMessages = new LinkedList<Message>();
	private String clientName = "unknown client";
	private long currentTime = System.currentTimeMillis();


	private ServerThreadForClient(){
		super();
	}

	/**
	 * Factory Pattern, if a new client connects to server, a new Thread will be created.
	 * If a client already had connected with server, the client will have the same Thread as before.
	 * Identifier is the clientName <String>
	 * 
	 * @author Lukas
	 * @param clientSocket
	 * @return A new or existing Thread. Result depends weather a Thread of the client already exists or not
	 */
	public static ServerThreadForClient getServerThreadForClient(Socket clientSocket){
		try{
			ServerThreadForClient client;
			Message msgIn = Message.receive(clientSocket);
			String clientName = msgIn.getClient();
			if(connections.containsKey(clientName)){
				client = connections.get(clientName);
			}else{
				client = new ServerThreadForClient();
				connections.put(clientName, client);
			}
			client.addClientSocket(clientSocket);
			client.setMsgIn(msgIn);
			return client;
		}catch(Exception e){
			logger.severe("Exception in gerServerThreadForClient(Socket clientSocket): "+e.toString());
			return null;
		}
	}

	/**
	 * Everytime a Message received from Client, the Message has to be identified and processed
	 * 
	 * @author Lukas, source: Bradley Richards
	 */
	@Override
	public void run(){
		Message msgOut = processMessage(this.msgIn);
		msgOut.send(clientSocket, this);
		try { if (clientSocket != null) clientSocket.close(); } catch (IOException e) {}
		try{				// Read a message from the client
		}catch(Exception e) {
			logger.severe(e.toString());
		}finally{
            try { if (clientSocket != null) clientSocket.close(); } catch (IOException e) {}
		}
	}
	
	/**
	 * Checks the type of Message and process it
	 * 
	 * @author Lukas
	 * @param msgIn
	 * 				The Message to process
	 * @return Message, individual sub-type
	 */
    private Message processMessage(Message msgIn) {
		Message msgOut = null;
		
		//Checks if the client is allowed to do something in the system
		if(!(msgIn instanceof Knock_Message) && !(msgIn instanceof Login_Message) && !(msgIn instanceof CreateNewPlayer_Message)){
			
			/*
			 * Checks if the client (Requester) is logged in into the system to avoid abuse.
			 * Otherwise he could send his messages to the Thread and it will be processed although the client isn't logged in.
			 */
			if(!onlineClients.contains(msgIn.getClient())){
				return new Failure_Message();
				
				//Without checking inetAddress, the client would be kicked out after the specified time-limit (30 min)
			}else if(!this.clientSocket.getInetAddress().equals(this.inetAddress) 
					&& System.currentTimeMillis() - this.currentTime > this.AFK_TIMER){
				Failure_Message fmsg = new Failure_Message();
				fmsg.setNotification("#clientUsed#");
				return fmsg;
				
				
				 //Refreshes the currentTime. Knock-Login- and CreatePlayer_Message exclusive to avoid abuse (identifier is just clientName)
			}else{
				this.currentTime = System.currentTimeMillis();
			}
		}
		
		//Sets the clientName
		if(msgIn instanceof Login_Message || msgIn instanceof CreateNewPlayer_Message){
			this.clientName = msgIn.getClient();
		}
		
		//Logs the "important" Message-requests
		if(!(msgIn instanceof AskForChanges_Message))
			logger.info("Received from "+this.clientName+": "+msgIn.getType().toString());
		
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
		case GiveUp:
			msgOut = this.processGiveUp(msgIn);
			break;
		case Interaction:
			msgOut = this.processInteraction(msgIn);
			break;
		case Logout:
			msgOut = this.processLogout(msgIn);
			break;
		case Knock:
			msgOut = new Commit_Message();
			break;
		case Request:
			msgOut = this.processRequest(msgIn);
			break;
		case StartBotGame:
			msgOut = this.processStartBotGame(msgIn);
			break;
		default:
			msgOut = new Error_Message();
		}
		msgOut.setClient(this.clientName);
    	return msgOut;
    }
    
	/**
	 * Checks if the name is stored in the database.
	 * If it does, it has to be the adequate password
	 * 
	 * @author Lukas
	 * @param msgIn
	 * 				Login_Message to process
	 * @return 	<li>Commit_Message if the login succeeded (clientName and password are correct)
	 * 			<li>Failure_Message if login failed (clientName and/or password are wrong)
	 */
	private Message processLogin(Message msgIn) {
		Login_Message lmsg = (Login_Message) msgIn;
		DB_Connector dbConnector = DB_Connector.getDB_Connector();
		String clientName = lmsg.getClient();
		boolean success = false;
		
		/*
		 * Checks if the requesting client already logged in
		 * If no, the client can try to login
		 * If yes, the client which is registered in the onlineClients has to be at least 30 min absent to succeed the new login
		 */
		if(!onlineClients.contains(clientName) || System.currentTimeMillis() - this.currentTime > this.AFK_TIMER){
			success = dbConnector.checkLoginInput(this.clientName, lmsg.getPassword());
		}
		
		//Set the new states if login succeeded
		if(success){
			logger.info(this.clientName+"'s login succeeded");
			if(!onlineClients.contains(clientName))
				onlineClients.add(this.clientName);
			this.currentTime = System.currentTimeMillis();
			this.inetAddress = this.clientSocket.getInetAddress();
			Commit_Message cmsg = new Commit_Message();
			return cmsg;
			
			//Tells the client that his login failed
		}else{
			Failure_Message fmsg = new Failure_Message();
			if(onlineClients.contains(clientName)){
				logger.info(this.clientName+"'s login failed because player is already logged in");
				fmsg.setNotification("#clientUsed#");
			}else{
				logger.info(this.clientName+"'s login failed because of wrong inputs");
				fmsg.setNotification("#loginFailed#");
			}
			return fmsg;
		}
	}
	
    /**
     * Try to store a new Player into the database
     * If clientName is unique, the player will be stored successful
     * 
     * @author Lukas
     * @param msgIn
     * 				CreateNewPlayer_Message to process
     * @return 	<li>Commit_Message if storage succeeded
     * 			<li>Failure_Message if storage failed
     */
	private Message processCreateNewPlayer(Message msgIn) {
		CreateNewPlayer_Message cnpmsg = (CreateNewPlayer_Message) msgIn;
		String password = cnpmsg.getPassword();
		
		DB_Connector dbConnector = DB_Connector.getDB_Connector();
		boolean success = dbConnector.addNewPlayer(this.clientName, password);
		if(success){
			logger.info(this.clientName+"'s storage succeeded");
			onlineClients.add(this.clientName);
			this.currentTime = System.currentTimeMillis();
			this.inetAddress = this.clientSocket.getInetAddress();
			Commit_Message cmsg = new Commit_Message();
			return cmsg;
			
		}else{
			logger.info(this.clientName+"'s storage failed");
			Failure_Message fmsg = new Failure_Message();
			fmsg.setNotification(this.clientName+" #isUsed#");
			return fmsg;
		}
	}
	
	/**
	 * Gives the highscore from the database
	 * 
	 * @author Lukas
	 * @param msgIn
	 * 				Not used
	 * @return HighScore_Message, content of the top5 (name, points) in CSV-String
	 */
	private Message processHighScore(Message msgIn) {
		DB_Connector dbConnector = DB_Connector.getDB_Connector();
		HighScore_Message hsmsg = new HighScore_Message();
		String highScore = dbConnector.getHighScore();
		if(highScore.length() == 0){
			hsmsg.setHighScore("#noHighscore#");
		}else{
			hsmsg.setHighScore(highScore);
		}
		logger.info("send highscore to "+this.clientName);
		return hsmsg;
	}
	
	/**
	 * Gets the chosen (Singleplayer or Multiplayer) Game
	 * If Client is the first Player in Multiplayer-Mode, client has to wait for second Player
	 * 
	 * @author Lukas
	 * @param msgIn
	 * 				GameMode_Message to process
	 * @return Commit_Message
	 */
	private Message processGameMode(Message msgIn) {
		this.waitingMessages.clear();
		this.unsentMessages.clear();
		
		GameMode_Message gmmsg = (GameMode_Message) msgIn;
		GameMode gameMode = gmmsg.getMode();
		this.player = new Player(this.clientName, this);
		this.game = Game.getGame(gameMode, this.player);
		this.player.setGame(this.game);
		Commit_Message cmsg = new Commit_Message();
		return cmsg;
	}


	/**
	 * Tries to play the card
	 * 
	 * @author Lukas
	 * @param msgIn
	 * 				PlayCard_Message to process
	 * @return 	<li>UpdateGame_Message if play of the card succeeded
	 * 			<li>Failure_Message if play of the card failed
	 */
	private Message processPlayCard(Message msgIn) {
		PlayCard_Message pcmsg = (PlayCard_Message) msgIn;
		for(Card card: this.player.handCards){
			if(card.getCardName().equals(pcmsg.getCard().getCardName())){
				return this.player.play(card);
			}
		}
		return new Failure_Message();
	}
	
	/**
	 * Creates a CreateGame_Message
	 * 
	 * @author Lukas
	 * @return CreateGame_Message with set content
	 */
	protected CreateGame_Message getCG_Message(Game game){
		CreateGame_Message cgmsg = new CreateGame_Message();
		cgmsg.setBuyCards(game.getBuyCards());
		cgmsg.setHandCards(this.player.getHandCards());
		cgmsg.setDeckPile(this.player.getDeckPile());
		cgmsg.setOpponent(game.getOpponent(this.player).getPlayerName());
		cgmsg.setDeckNumber(game.getOpponent(this.player).getDeckPile().size());
		cgmsg.setHandNumber(game.getOpponent(this.player).getHandCards().size());
		cgmsg.setStartingPlayer(game.getCurrentPlayer().getPlayerName());
		cgmsg.setPhase(this.player.getActualPhase());
		return cgmsg;
	}


	/**
	 * Sends the chat wrote by client to each player.
	 * Adds the clientName to the Chat for better reading
	 * 
	 * @author Lukas
	 * @param msgIn
	 * 				Chat_Message to process
	 * @return UpdateGame_Message with set chat
	 */
	private Message processChat(Message msgIn) {
		Chat_Message cmsg = (Chat_Message) msgIn;
		String chat = cmsg.getChat();
		chat = this.player.getPlayerName()+": "+chat;
		UpdateGame_Message ugmsg = new UpdateGame_Message();
		ugmsg.setChat(chat);
		this.player.sendToOpponent(this.player, ugmsg);
		logger.info(cmsg.getClient()+" has sent chat to "+this.game.getOpponent(this.player).getPlayerName()+": "+cmsg.getChat());
		return ugmsg;
	}

	/**
	 * Tries to buy a card
	 * 
	 * @author Lukas
	 * @param msgIn
	 * 				BuyCard_Message to process
	 * @return 	<li>UpdateGame_Message if the buy succeeded
	 * 			<li>Failure_Message if the buy failed
	 * 			<li>PlayerSuccess_Message if the game ended with this buy
	 */
	private Message processBuyCard(Message msgIn) {
		BuyCard_Message bcmsg = (BuyCard_Message) msgIn;
		return this.player.buy(bcmsg.getCard());
	}
	
	/**
	 * Processes the specified interaction
	 * 
	 * @author Lukas
	 * @param msgIn
	 * @return 	<li>UpdateGame_Message if Interaction succeeded. Content depends on which Interaction is running.
	 * 			<li>Failure_Message if Interaction failed.
	 */
	private Message processInteraction(Message msgIn) {
		Interaction_Message imsg = (Interaction_Message) msgIn;
		switch(imsg.getInteractionType()){
		
		//Player skips his turn
		case Skip:
			Message msg = this.player.skipPhase();
			this.player.sendToOpponent(this.player, msg);
			return msg;
			
		//Player ends his turn with the specified Top-Card
		case EndOfTurn:
			Card EOTCard = this.getRealHandCard(imsg.getDiscardCard());
			if(EOTCard != null){
				Message EOTmsg = this.player.cleanUp(EOTCard);
				this.player.sendToOpponent(this.player, EOTmsg);
				return EOTmsg;
			}
			logger.info("Interaction "+Interaction.EndOfTurn.toString()+" failed");
			return new Failure_Message();
			
		//Player discards his chosen handCards
		case Cellar:
			Cellar_Card cellarCard = (Cellar_Card) this.player.getPlayedCards().get(this.player.getPlayedCards().size()-1);
			Message cellarUpdate = cellarCard.executeCellar(imsg.getCellarDiscardCards());
			this.player.sendToOpponent(this.player, cellarUpdate);
			return cellarUpdate;
			
		//Player chose his card from the buyCards. Max-costs: 4 coins
		case Workshop:
			Workshop_Card workshopCard = (Workshop_Card) this.player.getPlayedCards().get(this.player.getPlayedCards().size()-1);
			Message workshopUpdate = workshopCard.executeWorkshop(imsg.getWorkshopChoice());
			this.player.sendToOpponent(this.player, workshopUpdate);
			return workshopUpdate;
			
		//Player chose his card to dispose. He can chose to take a card that costs until: disposedCard_cost +2 coins
		case Remodel1:
			Remodel_Card remodel1Card = (Remodel_Card) this.player.getPlayedCards().get(this.player.getPlayedCards().size()-1);
			Card disposeRemodelCard = this.getRealHandCard(imsg.getDisposeRemodelCard());
			if(disposeRemodelCard != null){
				Message remodel1Update = remodel1Card.executeRemodel1(disposeRemodelCard);
				this.player.sendToOpponent(this.player, remodel1Update);
				return remodel1Update;
			}
			logger.info("Interaction "+Interaction.Remodel1.toString()+" failed");
			return new Failure_Message();
			
		//Player chose a card from the buyCards. Max-costs: disposedCard_cost +2 coins
		case Remodel2:
			Remodel_Card remodel2Card = (Remodel_Card) this.player.getPlayedCards().get(this.player.getPlayedCards().size()-1);
			Message remodel2Update = remodel2Card.executeRemodel2(imsg.getRemodelChoice());
			this.player.sendToOpponent(this.player, remodel2Update);
			return remodel2Update;
			
		//Player chose copper or silver to dispose and gets the next-better coin-card directly into his hand
		case Mine:
			Mine_Card mineCard = (Mine_Card) this.player.getPlayedCards().get(this.player.getPlayedCards().size()-1);
			Card disposedMineCard = this.getRealHandCard(imsg.getDisposedMineCard());
			if(disposedMineCard != null){
				Message mineUpdate = mineCard.executeMine(disposedMineCard);
				this.player.sendToOpponent(this.player, mineUpdate);
				return mineUpdate;
			}
			logger.info("Interaction "+Interaction.Mine.toString()+" failed");
			return new Failure_Message();
		default:
			return null;
		}
	}
	
	/**
	 * Gets an alias of the handCard with the same object-type as the input (card)
	 * 
	 * @author Lukas
	 * @param card
	 * 			,the card to look for
	 * @return 	<li>handCard, the real object to use for methods
	 * 			<li>null if there is no object in the hand
	 */
	private Card getRealHandCard(Card card){
		for(Card handCard: this.player.handCards){
			if(card.getCardName().equals(handCard.getCardName()))
				return handCard;
		}
		return null;
	}

	/**
	 * Processes the AskForChanges-request from the client weather something has changed in the Game
	 * 
	 * @author Lukas
	 * @param msgIn
	 * 				Not used
	 * @return <li>UpdateGame_Message or PlayerSuccess_Message if something has changed.
	 * 			<li>Commit_Message when nothing changed
	 */
	private Message processAskForChanges(Message msgIn) {
		if(this.waitingMessages.size() > 0){
			return this.waitingMessages.poll();
		}else{
			return new Commit_Message();
		}
	}
	
	/**
	 * Starts the Bot
	 * 
	 * @param msgIn
	 * 				Not used
	 * @return 	<li>Commit_Message if the threat-start succeeded
	 * 			<li>Failure_Message if the threat-start failed
	 */
	private Message processStartBotGame(Message msgIn){
		if(this.game.getGameMode() == GameMode.Singleplayer){
			Bot bot = (Bot) this.game.getOpponent(this.player);
			new Thread(bot).start();
			return new Commit_Message();
		}
		
		return new Failure_Message();
	}
	
	/**
	 * The player gave up his game. Sends a Success_Message to opponent to tell that he that he wins.
	 * In addition it stores the result of both players into the database.
	 * 
	 * @author Lukas
	 * @param msgIn
	 * 				Not used
	 * @return 	<li>PlayerSuccess_Message if giveUp succeeded
	 * 			<li>Failure_Message if the game already ended somehow
	 */
    private Message processGiveUp(Message msgIn) {
    	PlayerSuccess_Message psmsg = new PlayerSuccess_Message();
    	if(!this.game.getGameEnded()){
    		this.player.countVictoryPoints();
    		this.player.setStatus(GameSuccess.Lost);
        	psmsg.setPlayer1(this.player);
    		Player opponent = this.game.getOpponent(this.player);
    		this.game.setGameEnded(true);
    		
    		/*
    		 * If the client leaves the game in Multiplayer-mode before the opponent entered the game,
    		 * the counter for correct allocation has to be corrected
    		 * Else the client loses the game not matter how many points he/she collected
    		 */
    		
    		if(opponent == null){
	    		Game.setGameCounter(Game.getGameCounter()+1);
	    		psmsg.setNumOfPlayers(1);
    		}else{
        		opponent.countVictoryPoints();
        		opponent.setStatus(GameSuccess.Won);
            	psmsg.setPlayer2(opponent);
            	this.player.sendToOpponent(this.player, psmsg);
    		}
        	logger.info(this.player.getPlayerName()+" "+GameSuccess.Lost.toString()+"! (gave up)");
        	
        	return psmsg;
    	}
    	return new Failure_Message();
	}
    
    /**
     * Deletes the client from the list to enable further login's
     * 
     * @author Lukas
     * @param msgIn
     * 				Not used
     * @return Commit_Message
     */
    private Message processLogout(Message msgIn){
    	onlineClients.remove(this.clientName);
    	return new Commit_Message();
    }
    
    /**
     * Processes the Request-request from the client to send back the unsent Message
     * 
     * @author Lukas
     * @param msgIn
     * 				Not used
     * @return 	<li>unsent Message
     * 			<li>if empty a new Commit_Message
     */
    private Message processRequest(Message msgIn){
    	if(this.unsentMessages.size() > 0){
    		return this.unsentMessages.poll();
    	}else{
    		return new Commit_Message();
    	}
    }

	/**
	 * Adds the messages (UpdateGame_Message or PlayerSuccess_Message) to take from the client's AskForChanges_Messages requests.
	 * 
	 * @author Lukas
	 * @param message
	 * 				The Message which waits for a request from client
	 */
	public void addWaitingMessages(Message message){
		this.waitingMessages.offer(message);
	}
	
	/**
	 * Adds the unsent messages to take from the client's Request_Messages requests.
	 * 
	 * @author Lukas
	 * @param message
	 * 				The Message which waits for a request from client because it wasn't sent
	 */
	public void addUnsentMessages(Message message){
		this.waitingMessages.offer(message);
	}
	
	/**
	 * Adds a new Socket for client.
	 * 
	 * @author Lukas
	 * @param clientSocket
	 */
	private void addClientSocket(Socket clientSocket){
		this.clientSocket = clientSocket;
	}
	
	/**
	 * Set the processed Message to the Thread
	 * 
	 * @author Lukas
	 * @param msgIn
	 * 				The received and processed Message
	 */
	private void setMsgIn(Message msgIn){
		this.msgIn = msgIn;
	}
}//end ServerThreadForClient