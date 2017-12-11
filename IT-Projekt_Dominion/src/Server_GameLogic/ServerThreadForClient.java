package Server_GameLogic;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import java.util.logging.Logger;

import Cards.Card;
import Cards.CardName;
import Cards.Cellar_Card;
import Cards.Gold_Card;
import Cards.Market_Card;
import Cards.Mine_Card;
import Cards.Remodel_Card;
import Cards.Smithy_Card;
import Cards.Village_Card;
import Cards.Woodcutter_Card;
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
	public Queue<Message> waitingMessages = new LinkedList<Message>();
	private String clientName = null;


	private ServerThreadForClient(){
		super();
	}

	/**
	 * @author Lukas
	 * Factory Pattern, if a new client connects to server, a new Thread will be created.
	 * If a client already had connected with server, he will have the same Thread as before
	 * 
	 * @param clientSocket
	 * @return client, a new or existing Thread. Result depends weather a Thread of the client already exists or not
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
		try{				// Read a message from the client
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
	 * @author Lukas
	 * Checks the type of Message and process it
	 * 
	 * @param msgIn
	 * @return msgOut, depends on the type of Message, which new Message will return to client
	 */
    private Message processMessage(Message msgIn) {
		Message msgOut = null;
		if(msgIn instanceof Knock_Message || msgIn instanceof Login_Message || msgIn instanceof CreateNewPlayer_Message){
			this.clientName = msgIn.getClient();
		}
		
		if(!(msgIn instanceof AskForChanges_Message))
			this.logger.info("Received from "+this.clientName+": "+msgIn.getType().toString());
		
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
		case Knock:
			msgOut = new Commit_Message();
			break;
		default:
			msgOut = new Error_Message();
		}
		msgOut.setClient(this.clientName);
    	return msgOut;
    }
    
	/**TESTED
	 * @author Lukas
	 * Checks if the name is stored in the database and if yes,
	 * it has to be the adequate password
	 * 
	 * @param msgIn
	 * @return cmsg, Commit_Message if the login succeeded (clientName and password are correct)
	 * @return fmsg, Failure_Message if login failed (clientName and/or password are wrong)
	 */
	private Message processLogin(Message msgIn) {
		Login_Message lmsg = (Login_Message) msgIn;
		DB_Connector dbConnector = DB_Connector.getDB_Connector();
		boolean success = dbConnector.checkLoginInput(this.clientName, lmsg.getPassword());
		
		if(success){
			this.logger.info(this.clientName+"'s login succeeded");
			Commit_Message cmsg = new Commit_Message();
			return cmsg;
			
		}else{
			this.logger.info(this.clientName+"'s login failed");
			Failure_Message fmsg = new Failure_Message();
			fmsg.setNotification("#loginFailed#");
			return fmsg;
		}
	}
	
    /**TESTED
     * @author Lukas
     * Try to store a new Player into the database
     * If clientName is unique, the player will be stored successful
     * 
     * @param msgIn
     * @return cmsg, Commit_Message if storage succeeded
     * @return fmsg, Failure_Message if storage failed
     */
	private Message processCreateNewPlayer(Message msgIn) {
		CreateNewPlayer_Message cnpmsg = (CreateNewPlayer_Message) msgIn;
		String password = cnpmsg.getPassword();
		
		DB_Connector dbConnector = DB_Connector.getDB_Connector();
		boolean success = dbConnector.addNewPlayer(this.clientName, password);
		if(success){
			this.logger.info(this.clientName+"'s storage succeeded");
			Commit_Message cmsg = new Commit_Message();
			return cmsg;
			
		}else{
			this.logger.info(this.clientName+"'s storage failed");
			Failure_Message fmsg = new Failure_Message();
			fmsg.setNotification(this.clientName+" #isUsed#");
			return fmsg;
		}
	}
	
	/**TESTED
	 * @author Lukas
	 * Takes the highscore from the database
	 * 
	 * @param msgIn
	 * @return HighScore_Message, content of the top5 (name, points) in one String
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
		this.logger.info("send highscore to "+this.clientName);
		return hsmsg;
	}
	
	/**TESTED
	 * @author Lukas
	 * Gets the chosen (singleplayer or multiplayer) Game
	 * If Client is the first Player in multiplayerMode, client has to wait for second Player
	 * 
	 * @param msgIn
	 * @return cmsg, Commit_Message
	 */
	private Message processGameMode(Message msgIn) {
		GameMode_Message gmmsg = (GameMode_Message) msgIn;
		GameMode gameMode = gmmsg.getMode();
		this.player = new Player(this.clientName, this);
		this.game = Game.getGame(gameMode, this.player);
		this.player.setGame(this.game);
		Commit_Message cmsg = new Commit_Message();
		return cmsg;
	}


	/**TESTED
	 * @author Lukas
	 * Checks if the hands of client and server are equal
	 * If yes (should be usual), Player "trys" to play card. If not able it will be visible in the ugmsg
	 * 
	 * @param msgIn, PlayCard_Message
	 * @return UpdateGame_Message, content depends if player was able to play the card or Failure_Message
	 * PlayerSuccess_Message if player ended the game with this move
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
	
	/**TESTED
	 * @author Lukas
	 * Creates a CreateGame_Message when the Game is ready to start (i.e. two Players or one player with Bot were added)
	 * 
	 * @return cgmsg, CreateGame_Message
	 */
	protected CreateGame_Message getCG_Message(Game game){
		CreateGame_Message cgmsg = new CreateGame_Message();
		cgmsg.setBuyCards(game.getBuyCards());
		this.player.getHandCards().add(new Gold_Card());
		this.player.getHandCards().add(new Gold_Card());
		this.player.getHandCards().add(new Woodcutter_Card());
		this.player.getHandCards().add(new Workshop_Card());
		cgmsg.setHandCards(this.player.getHandCards());
		cgmsg.setDeckPile(this.player.getDeckPile());
		cgmsg.setOpponent(game.getOpponent(this.player).getPlayerName());
		cgmsg.setDeckNumber(game.getOpponent(this.player).getDeckPile().size());
		cgmsg.setHandNumber(game.getOpponent(this.player).getHandCards().size());
		cgmsg.setStartingPlayer(game.getCurrentPlayer().getPlayerName());
		cgmsg.setPhase(this.player.getActualPhase());
		return cgmsg;
	}


	/**TESTED
	 * @author Lukas
	 * The Chat_Message wrote by client has to be sent to opponent.
	 * Addes the clientName to the Chat for better reading
	 * 
	 * @param msgIn
	 * @return ugmsg, UpdateGame_Message with set chat
	 */
	private Message processChat(Message msgIn) {
		Chat_Message cmsg = (Chat_Message) msgIn;
		String chat = cmsg.getChat();
		chat = this.player.getPlayerName()+": "+chat;
		UpdateGame_Message ugmsg = new UpdateGame_Message();
		ugmsg.setChat(chat);
		this.player.sendToOpponent(this.player, ugmsg);
		this.logger.info(cmsg.getClient()+" has sent chat to "+this.game.getOpponent(this.player).getPlayerName()+": "+cmsg.getChat());
		return ugmsg;
	}

	/**TESTED
	 * @author Lukas
	 * Try to buy a card
	 * 
	 * @param msgIn
	 * @return UpdateGame_Message with the correct buyed card or Failure_Message if player wasn't able to buy
	 * PlayerSuccess_Message if the player ended the game with this buy
	 */
	private Message processBuyCard(Message msgIn) {
		BuyCard_Message bcmsg = (BuyCard_Message) msgIn;
		return this.player.buy(bcmsg.getCard());
	}
	
	/**
	 * @author Lukas
	 * 
	 * 
	 * @param msgIn
	 * @return UpdateGame_Message, content depends on which Interaction is running
	 */
	private Message processInteraction(Message msgIn) {
		Interaction_Message imsg = (Interaction_Message) msgIn;
		switch(imsg.getInteractionType()){
		case Skip:
			Message msg = this.player.skipPhase();
			this.player.sendToOpponent(this.player, msg);
			return msg;
		case EndOfTurn:
			Card EOTCard = this.getRealHandCard(imsg.getDiscardCard());
			if(EOTCard != null){
				Message EOTmsg = this.player.cleanUp(EOTCard);
				this.player.sendToOpponent(this.player, EOTmsg);
				return EOTmsg;
			}
			this.logger.info("Interaction "+Interaction.EndOfTurn.toString()+" failed");
			return new Failure_Message();
		case Cellar:
			Cellar_Card cellarCard = (Cellar_Card) this.player.getPlayedCards().get(this.player.getPlayedCards().size()-1);
			return cellarCard.executeCellar(imsg.getCellarDiscardCards());
		case Workshop:
			Workshop_Card workshopCard = (Workshop_Card) this.player.getPlayedCards().get(this.player.getPlayedCards().size()-1);
			return workshopCard.executeWorkshop(imsg.getWorkshopChoice());
		case Remodel1:
			Remodel_Card remodel1Card = (Remodel_Card) this.player.getPlayedCards().get(this.player.getPlayedCards().size()-1);
			Card disposeRemodelCard = this.getRealHandCard(imsg.getDisposeRemodelCard());
			if(disposeRemodelCard != null)
				return remodel1Card.executeRemodel1(disposeRemodelCard);
			this.logger.info("Interaction "+Interaction.Remodel1.toString()+" failed");
			return new Failure_Message();
		case Remodel2:
			Remodel_Card remodel2Card = (Remodel_Card) this.player.getPlayedCards().get(this.player.getPlayedCards().size()-1);
			return remodel2Card.executeRemodel2(imsg.getRemodelChoice());
		case Mine:
			Mine_Card mineCard = (Mine_Card) this.player.getPlayedCards().get(this.player.getPlayedCards().size()-1);
			Card disposedMineCard = this.getRealHandCard(imsg.getDisposedMineCard());
			this.logger.info("The disposedCard serversite is (369): "+imsg.getDisposedMineCard());
			if(disposedMineCard != null)
				return mineCard.executeMine(disposedMineCard);
			this.logger.info("Interaction "+Interaction.Mine.toString()+" failed");
			return new Failure_Message();
		default:
			return null;
		}
	}
	
	/**
	 * @author Lukas
	 * 
	 * @param card
	 * @return handCard, the real object to use for methods
	 * 			null if there is no object in the hand
	 */
	private Card getRealHandCard(Card card){
		for(Card handCard: this.player.handCards){
			if(card.getCardName().equals(handCard.getCardName()))
				return handCard;
		}
		return null;
	}

	/**
	 * @author Lukas
	 * Processes the request from the client weather something has changed in the Game
	 * 
	 * @param msgIn
	 * @return UpdateGame_Message if something has changed.
	 * @return cmsg, Commit_Message when nothing has changed
	 */
	private Message processAskForChanges(Message msgIn) {
		if(this.waitingMessages.size() > 0){
			return this.waitingMessages.poll();
		}else{
			Commit_Message cmsg = new Commit_Message();
			return cmsg;
		}
	}
	
	/**
	 * @author Lukas
	 * The player gave up his game. Sends a Success_Message to opponent to tell that he that he wins
	 * 
	 * @param msgIn
	 * @return PlayerSuccess_Message, the client loses
	 */
    private Message processGiveUp(Message msgIn) {
    	PlayerSuccess_Message psmsgOpponent = new PlayerSuccess_Message();
    	Player opponent = this.game.getOpponent(this.player);
    	psmsgOpponent.setSuccess(GameSuccess.Won);
    	opponent.countVictoryPoints();
    	psmsgOpponent.setVictoryPoints(opponent.getVictoryPoints());
    	this.player.sendToOpponent(this.player, psmsgOpponent);
    	
    	PlayerSuccess_Message psmsgSelf = new PlayerSuccess_Message();
    	psmsgSelf.setSuccess(GameSuccess.Lost);
    	this.player.countVictoryPoints();
    	psmsgSelf.setVictoryPoints(this.player.getVictoryPoints());
    	this.logger.info(opponent.getPlayerName()+" "+GameSuccess.Won.toString()+"!");
    	return psmsgSelf;
	}

	
	public void addWaitingMessages(Message message){
		this.waitingMessages.offer(message);
	}
	
	/**
	 * @author Lukas
	 * Adds a new Socket for client
	 * 
	 * @param clientSocket
	 */
	private void addClientSocket(Socket clientSocket){
		this.clientSocket = clientSocket;
	}
}//end ServerThreadForClient