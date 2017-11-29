package Client_GameApp_MVC;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Abstract_MVC.Model;
import Cards.Card;
import Cards.CardName;
import Client_Services.ServiceLocator;
import Client_Services.Translator;
import MainClasses.Dominion_Main;
import Messages.BuyCard_Message;
import Messages.Chat_Message;
import Messages.Commit_Message;
import Messages.GameSuccess;
import Messages.CreateGame_Message;
import Messages.CreateNewPlayer_Message;
import Messages.Failure_Message;
import Messages.GameMode_Message;
import Messages.HighScore_Message;
import Messages.Interaction;
import Messages.Interaction_Message;
import Messages.Login_Message;
import Messages.Message;
import Messages.MessageType;
import Messages.PlayCard_Message;
import Messages.PlayerSuccess_Message;
import Messages.UpdateGame_Message;
import Server_GameLogic.GameMode;
import Server_GameLogic.Phase;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * @author Adrian & Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:04:41
 */
public class GameApp_Model extends Model {

	private final String NO_CONNECTION = "No connection to Server";
	private final int PORT = 8080;
	private final String TRANSLATE_REGEX = "#[\\w\\s]*#";

	private ServiceLocator sl = ServiceLocator.getServiceLocator();
	private Translator t = sl.getTranslator();

	protected String clientName;
	protected String opponent;
	protected String currentPlayer;

	protected int actions;
	protected int buys;
	protected int coins;

	protected LinkedList<Card> yourNewHandCards;
	protected LinkedList<Card> yourHandCards;
	protected int opponentNewHandCards;
	protected int opponentHandCards;
	protected LinkedList<Card> yourDeck;
	protected int opponentDeck;
	protected LinkedList<Card> yourDiscardPile;
	protected int opponentDiscardPile;
	protected LinkedList<Card> playedCards;
	protected Card newPlayedCard;
	protected Card yourBuyedCard;
	protected Card opponentBuyedCard;
	protected Card yourDiscardPileTopCard;
	protected String newChat;
	protected String newLog;
	protected Interaction interaction = Interaction.Skip;
	protected LinkedList<Card> cardSelection;

	protected GameSuccess success;
	protected int victoryPoints;

	protected String gameMode;
	protected HashMap<CardName, Integer> buyCards;
	protected CardName buyChoice;
	protected Phase currentPhase;
	protected boolean turnEnded = false;

	private Dominion_Main main;
	private String ipAddress;

	public enum UserInput {
		clientName,
		ipAddress,
		port,
		password
	}

	private MediaPlayer mediaPlayer; // sound


	public GameApp_Model(Dominion_Main main){
		super();
		this.main = main;

		// start menusound
		//		this.startMediaPlayer("sound.mp3"); // start sound 
	}

	/**
	 * 
	 * @param moveType
	 */
	public boolean checkMoveValidity(String moveType){
		return false;
	}

	/**
	 * @author Adrian
	 * Encrypts a password using the secure hash algorithm (SHA-512) and returns it.
	 * @param unencryptedPassword
	 * @param salt
	 * @return encryptedPassword
	 * @throws NoSuchAlgorithmException
	 */
	public String encryptPassword(String unencryptedPassword, String salt) throws NoSuchAlgorithmException {
		String generatedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(salt.getBytes());
			byte[] bytes = md.digest(unencryptedPassword.getBytes());
			StringBuilder sb = new StringBuilder();

			for(int i=0; i< bytes.length ;i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedPassword;
	}

	/**
	 * @author Adrian
	 * Adds salt for usage in the method encryptPassword
	 * @return salt
	 * @throws NoSuchAlgorithmException
	 */
	private String getSalt() throws NoSuchAlgorithmException {
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[16];
		sr.nextBytes(salt);
		return salt.toString();
	}

	/**
	 * @author Adrian
	 * Checks if the user entered a valid input. This method is applicable for the inputs clientName, ipAddress, port and password.
	 * Returns true if the input is valid.
	 * 
	 * @param userInput
	 * @param inputType
	 * @return boolean user input correct/incorrect
	 */
	public boolean checkUserInput(String userInput, UserInput inputType){
		boolean valid = false;
		final int MAX_INPUT_LENGTH = 30;
		String[] parts = userInput.split("\\.");

		switch(inputType) {
		case clientName:
		case password:
			// ClientName and password can't be longer than MAX_INPUT_LENGTH
			if (userInput.length()<=MAX_INPUT_LENGTH && userInput.length() > 2)
				valid = true;
			break;
		case ipAddress:
			// The ipAddress must consist of 4 parts. Each part is an integer from 0 to 255.
			if (parts.length == 4){
				valid = true;
				for (String part : parts){
					try {
						int number = Integer.parseInt(part);
						if (number < 0 || number > 255) {
							valid = false;
						} else {
							valid = true;
							this.ipAddress = userInput;
						}
					} catch (NumberFormatException e) {
						// input was not an integer
						valid = false;
					}
				}		
			}
			break;
		}
		return valid;
	}

	/**SEMI_TESTED
	 * @author Lukas
	 * Translates any parts of a String between two #
	 * 
	 * @param input
	 * @return translated input
	 */
	private String translate(String input){
		Pattern p = Pattern.compile(this.TRANSLATE_REGEX);
		Matcher m = p.matcher(input);
		int tmpIndex = 0;
		String output = "";
		String[] splittedInput = input.split(this.TRANSLATE_REGEX);
		String lastPart = splittedInput[splittedInput.length-1];
		while(m.find()){
			int startIndex = m.start();
			int endIndex = m.end();
			output += input.substring(tmpIndex, startIndex);
			output += t.getString(input.substring(startIndex+1, endIndex-1));//+- to filter #
			tmpIndex = endIndex;
		}
		if(output.length() > 0){
			output += lastPart;
			return output;
		}
		return t.getString(input);
	}


	/**
	 * @author Lukas Gehrig
	 * The IP will be set here
	 * 
	 * @param ipAdress
	 */
	public void init(String ipAddress){
		this.ipAddress = ipAddress;
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
			socket = new Socket(this.ipAddress, this.PORT);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return socket;
	}
	
	/**TESTED
	 * @author Lukas (@author Adrian encryptPassword)
	 * The client sends his encrypted password to server and will get to the MainMenu if the password is appropriate to clientName
	 * 
	 * @param clientName
	 * @param password
	 * @return result, usually only necessary if clientName and password don't work or the client lost connection to server
	 */
	public String sendLogin(String clientName, String password){
		String result = NO_CONNECTION;
		this.clientName = clientName;
		Login_Message lmsg = new Login_Message();
		lmsg.setClient(clientName);//set the clientName and encrypted password to XML
		try {
			String salt = getSalt();
			lmsg.setPassword(this.encryptPassword(password, salt));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		Message msgIn = this.processMessage(lmsg);
		if(msgIn instanceof Commit_Message){
			this.main.startMainMenu();//login succeeded

		}else if(msgIn instanceof Failure_Message){
			Failure_Message fmsg = (Failure_Message) msgIn;//login failed, clientName and/or password wrong
			result = fmsg.getNotification();
		}
		return this.translate(result);
	}
	
	/**TESTED
	 * @author Lukas (@author Adrian encryptPassword)
	 * The client wants to create his own profile. For this purpose the clientName has to be unique in the database.
	 * If the storage process succeeded, the client will get into the MainMenu.
	 * 
	 * @param clientName
	 * @param password
	 * @return result, usually only necessary if clientName is already set
	 */
	public String sendCreateNewPlayer(String clientName, String password){
		String result = NO_CONNECTION;
		this.clientName = clientName;
		CreateNewPlayer_Message cnpmsg = new CreateNewPlayer_Message();
		cnpmsg.setClient(this.clientName);//set the clientName and encrypted password to XML
		try {
			String salt = getSalt();
			cnpmsg.setPassword(this.encryptPassword(password, salt));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		Message msgIn = this.processMessage(cnpmsg);
		if(msgIn instanceof Commit_Message){
			this.main.startMainMenu();//createNewPlayer succeeded

		}else if(msgIn instanceof Failure_Message){
			Failure_Message fmsg = (Failure_Message) msgIn;//createNewPlayer failed
			result = fmsg.getNotification();
		}
		return this.translate(result);
	}
	
	/**TESTED
	 * @author Lukas
	 * The client sends his GameMode (Singleplayer or Multiplayer) to Server.
	 * 
	 * @param mode
	 * @return result, usually only necessary if the client lost connection to server
	 */
	public String sendGameMode(GameMode mode){
		String result = NO_CONNECTION;
		GameMode_Message gmmsg = new GameMode_Message();
		gmmsg.setClient(this.clientName);//set the clientName and mode(SinglePlayer or MultiPlayer) to XML
		gmmsg.setMode(mode);
		this.gameMode = mode.toString();

		Message msgIn = this.processMessage(gmmsg);
		if(msgIn instanceof Commit_Message){
//			this.main.startGameApp();
		}
		return result;
//		return this.translate(result);
	}

	/**
	 * @author Lukas
	 * The client wants to buy a card. The result depends on the players validity to buy.
	 * 
	 * @param cardName
	 * @return update, tells the controller if the game has to be updated
	 */
	public boolean sendBuyCard(CardName cardName){
		BuyCard_Message bcmsg = new BuyCard_Message();
		bcmsg.setCard(cardName);
		boolean update = false;

		Message msgIn = this.processMessage(bcmsg);
		if(msgIn.getType().equals(MessageType.UpdateGame)){//buy succeeded
			this.processUpdateGame(msgIn);
			update = true;

		}else if(msgIn.getType().equals(MessageType.PlayerSuccess)){//the game ended after this buy
			this.processPlayerSuccess(msgIn);
			update = true;

		}else if(msgIn.getType().equals(MessageType.Failure)){//it was not allowed to buy this card
			//nothing toDo here
		}
		return update;
	}

	/**
	 * @author Lukas
	 * The clients sends a Chat_Message to the opponent. The chat of the client will also be sent to server and back.
	 * 
	 * @param chat
	 * @return update, tells the controller if the game has to be updated
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
	 * To interact once or multiple times with the server, the specified answers of the interactions has to be set
	 * 
	 * @return update, tells the controller if the game has to be updated
	 */
	public boolean sendInteraction(){
		Interaction_Message imsg = new Interaction_Message();
		boolean update = false;
		imsg.setInteractionType(this.interaction);

		switch(this.interaction){
		case Skip:
			break;
		case EndOfTurn:
			imsg.setDiscardCard(this.cardSelection.remove(0));
			break;
		case Cellar:
			imsg.setCellarDiscardCards(this.cardSelection);
			for(int i = 0; i < this.cardSelection.size(); i++)
				this.yourDiscardPile.add(this.cardSelection.remove(i));
			break;
		case Workshop:
			imsg.setWorkshopChoice(this.buyChoice);
			break;
		case Remodel1:
			imsg.setDisposeRemodelCard(this.cardSelection.remove(0));
			break;
		case Remodel2:
			imsg.setRemodelChoice(this.buyChoice);
			break;
		case Mine:
			imsg.setDisposedMineCard(this.cardSelection.remove(0));
			break;
		default:
			return false;
		}

		Message msgIn = this.processMessage(imsg);
		if(msgIn.getType().equals(MessageType.UpdateGame)){
			update = true;
			this.interaction = Interaction.Skip;//defaultSetting
		}
		return update;
	}


	/**
	 * @author Lukas
	 * The client sends a request to server for the top5 Highscore
	 * 
	 * @return result, the Highscore in one String or the message that client lost connection to server
	 */
	public String sendHighScoreRequest(){
		String result = this.NO_CONNECTION;
		//		String result = this.translate(NO_CONNECTION);
		HighScore_Message hsmsg = new HighScore_Message();

		Message msgIn = this.processMessage(hsmsg);
		if(msgIn.getType().equals(MessageType.HighScore)){
			HighScore_Message nhsmsg = (HighScore_Message) msgIn;
			result = nhsmsg.getHighScore();
		}
		return result;
	}

	/**
	 * @author Lukas
	 * The client wants to play a chosen Card. The result depends on the validity of the move
	 * 
	 * @param card
	 * @return update, tells the controller if the game has to be updated
	 */
	public boolean sendPlayCard(Card card){
		PlayCard_Message pcmsg = new PlayCard_Message();
		pcmsg.setCard(card);
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


	/**TESTED
	 * @author Lukas
	 * Creates a new Game
	 * 
	 * @param msgIn
	 */
	protected void processCreateGame(Message msgIn) {		
		CreateGame_Message cgmsg = (CreateGame_Message) msgIn;
		this.yourNewHandCards = cgmsg.getHandCards();
		this.buyCards = cgmsg.getBuyCards();
		this.opponent = cgmsg.getOpponent();
		this.opponentDeck = cgmsg.getDeckNumber();
		this.opponentHandCards = cgmsg.getHandNumber();
		this.currentPlayer = cgmsg.getStartingPlayer();
		for(Card card: cgmsg.getDeckPile())
			this.yourDeck.add(card);
	}

	/**
	 * @author Lukas
	 * Set success and victoryPoints. Result depends weather you won or lost
	 * 
	 * @param msgIn, PlayerSuccess_Message
	 */
	private void processPlayerSuccess(Message msgIn) {
		PlayerSuccess_Message psmsg = (PlayerSuccess_Message) msgIn;
		this.success = psmsg.getSuccess();
		this.victoryPoints = psmsg.getVictoryPoints();
	}


	/**
	 * @author Lukas
	 * Interpret all updates and provides structures for further work
	 * 
	 * @param msgIn, UpdateGame_Message. Can consist various contents
	 */

	protected void processUpdateGame(Message msgIn) {	
		UpdateGame_Message ugmsg = (UpdateGame_Message) msgIn;

		//If something necessary happened in the Game, it will be provided to show
		if(ugmsg.getLog() != null)
			this.newLog = ugmsg.getLog();
		//			this.newLog = this.translate(ugmsg.getLog());

		//If the client or opponent sent a chat, it will be provided to show
		if(ugmsg.getChat() != null)
			this.newChat = ugmsg.getChat();

		//Always currentPlayer
		if(ugmsg.getActions() != null)
			this.actions = ugmsg.getActions();

		//Always currentPlayer
		if(ugmsg.getBuys() != null)
			this.buys = ugmsg.getBuys();

		//Always currentPlayer
		if(ugmsg.getCoins() != null)
			this.coins = ugmsg.getCoins();

		//Always currentPlayer
		if(ugmsg.getCurrentPhase() != null)
			this.currentPhase = ugmsg.getCurrentPhase();

		//If a buy was successful. Always currentPlayer
		//stores the buyedCard of the currentPlayer and reduces the value of the buyCards(Cards which can be bought)
		if(ugmsg.getBuyedCard() != null && this.currentPlayer == this.clientName){
			this.yourBuyedCard = ugmsg.getBuyedCard();
			this.buyCards.replace(this.yourBuyedCard.getCardName(), this.buyCards.get(this.yourBuyedCard.getCardName())-1);
		}else{
			this.opponentBuyedCard = ugmsg.getBuyedCard();
			this.buyCards.replace(this.opponentBuyedCard.getCardName(), this.buyCards.get(this.opponentBuyedCard.getCardName())-1);
		}

		//Just necessary to show opponent's size of discardPile
		if(ugmsg.getDeckPileCardNumber() != null && this.currentPlayer == this.opponent)
			this.opponentDiscardPile = ugmsg.getDeckPileCardNumber();

		//Just necessary to show opponent's size of deckPile
		if(ugmsg.getDiscardPileCardNumber() != null && this.currentPlayer == this.opponent)
			this.opponentDeck = ugmsg.getDiscardPileCardNumber();

		//Always client's topCard
		if(ugmsg.getDiscardPileTopCard() != null && this.currentPlayer == this.clientName)
			this.yourDiscardPileTopCard = ugmsg.getDiscardPileTopCard();

		//If currentPlayer is set, the currentPlayer's turn ends
		if(ugmsg.getCurrentPlayer() != null){
			if(ugmsg.getCurrentPlayer() != this.currentPlayer){
				this.turnEnded = true;
				if(ugmsg.getCurrentPlayer() == this.opponent){//if it was your turn that ended
					for(int i = 0; i < this.playedCards.size(); i++)
						this.yourDiscardPile.add(this.playedCards.remove(i));
					for(int j = 0; j < this.yourHandCards.size(); j++)
						this.yourDiscardPile.add(this.yourHandCards.remove(j));
				}else{//if it was your opponents turn that ended
					this.playedCards.clear();
				}
			}
			this.currentPlayer = ugmsg.getCurrentPlayer();
		}

		//The new handCards just drawn. Always currentPlayer
		//Move the drawn cards from the deck into yourNewHandCards
		if(ugmsg.getNewHandCards() != null && (this.currentPlayer == this.clientName) || (ugmsg.getCurrentPlayer() == this.opponent)){
			LinkedList<Card> newHandCards = ugmsg.getNewHandCards();
			for(int i = 0; i < newHandCards.size(); i++){
				if(this.yourDeck.size() == 0){//Mandatory if the DeckPile is empty, the DiscardPile has to be added to the DeckPile
					for(int j = 0; j < this.yourDiscardPile.size(); j++){
						this.yourDeck.add(this.yourDiscardPile.remove());
					}
				}
				for(int k = 0; k < this.yourDeck.size(); k++){
					if(newHandCards.get(i).getCardName().equals(this.yourDeck.get(k).getCardName())){
						this.yourNewHandCards.add(this.yourDeck.remove(k));
						break;
					}
				}
			}
		}else{//for opponent
			this.opponentNewHandCards = ugmsg.getNewHandCards().size();
		}

		//If a card was played, it will be provided
		//Move the played Card from the hand into newPlayedCard
		if(ugmsg.getPlayedCard() != null && this.currentPlayer == this.clientName){
			for(int i = 0; i < this.yourHandCards.size(); i++){
				if(this.yourHandCards.get(i).getCardName().equals(ugmsg.getPlayedCard().getCardName())){
					this.newPlayedCard = this.yourHandCards.remove(i);
				}
			}
		}else if(ugmsg.getPlayedCard() != null){//for opponent
			this.newPlayedCard = ugmsg.getPlayedCard();
		}

		//If interaction is set, the Type of Interaction can be checked (i.e. meaning of the commit_Button)
		if(ugmsg.getInteractionType() != null && this.currentPlayer == this.clientName)
			this.interaction = ugmsg.getInteractionType();

		//If cardSelection is set, it consists a selection of the cards to chose
		if(ugmsg.getCardSelection() != null && this.currentPlayer == this.clientName)
			this.cardSelection = ugmsg.getCardSelection();

	}


	/**
	 * @author Lukas
	 * SetUp a socket_connection to server with the given message and returns the answer
	 * 
	 * @param message
	 * @return msgIn, individual InputMessage
	 */
	protected Message processMessage(Message message){
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


	/* Provisorischer Kommentar inkl. Quelle -> Rene
	   https://panjutorials.de/tutorials/javafx-8-gui/lektionen/audio-player-in-javafx-2/?cache-flush=1510439948.4916
	   hier legen wir die Resource an, welche unbedingt im entsprechenden Ordner sein muss

	 * URL resource = getClass().getResource("sound.mp3"); // wir legen das Mediaobjekt and und weisen unsere Resource zu 
	 * Media media = new Media(resource.toString()); // wir legen den Mediaplayer an und weisen
	 * ihm das Media Objekt zu mediaPlayer = new MediaPlayer(media);
	 */

	public void startMediaPlayer(String soundFileName) {
		// mediaplayer: new music
		if (mediaPlayer != null) {
			mediaPlayer.stop();
		}
		URL resource = getClass().getResource(soundFileName);
		Media media = new Media(resource.toString());
		mediaPlayer = new MediaPlayer(media);
		mediaPlayer.play();
	}


	public String getClientName(){
		return this.clientName;
	}

	public void setGameMode(String gameMode){

	}

	public void setIP(String ipAddress){
		this.ipAddress = ipAddress;
	}

	public void setClientName(String clientName){
		this.clientName = clientName;
	}
}//end GameApp_Model