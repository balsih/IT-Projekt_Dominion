package Client_GameApp_MVC;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import Messages.GiveUp_Message;
import Messages.CreateGame_Message;
import Messages.CreateNewPlayer_Message;
import Messages.Failure_Message;
import Messages.GameMode_Message;
import Messages.HighScore_Message;
import Messages.Interaction;
import Messages.Interaction_Message;
import Messages.Knock_Message;
import Messages.Login_Message;
import Messages.Logout_Message;
import Messages.Message;
import Messages.PlayCard_Message;
import Messages.PlayerSuccess_Message;
import Messages.Request_Message;
import Messages.UpdateGame_Message;
import Server_GameLogic.GameMode;
import Server_GameLogic.Phase;
import Server_GameLogic.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * Performs the application-logic of this Game (Dominion).
 * This class provides a Thin-Client concept.
 * It provides a set of methods to communicate with the server.
 * It also checks several client-site inputs to keep the network-communication to a minimum.
 * 
 * @author Adrian & Lukas
 */
public class GameApp_Model extends Model {

	private final String NO_CONNECTION = "#NoConnection#";
	private final String TRANSLATE_REGEX = "#[\\w\\s]*#";
	private final String LINE_SEPARATOR_REGEX = "==";
	private final String HIGHSCORE_REGEX = "\\s*,\\s*";
	private final String SALT = "[B@d8c7b51";

	private ServiceLocator sl = ServiceLocator.getServiceLocator();
	private Translator t = sl.getTranslator();
	private Integer requestCounter = 0;
	
	protected Dominion_Main main;
	private String ipAddress;
	private Integer port;
	
	protected Integer actions = 1;
	protected Integer buys = 1;
	protected Integer coins = 0;

	protected String clientName = null;
	protected String opponent = null;
	protected String currentPlayer = null;
	protected boolean failure = true;

	protected LinkedList<Card> yourNewHandCards = null;
	protected Integer opponentHandCards = null;
	protected Integer yourDeck = null;
	protected Integer opponentDeck = null;
	protected Integer yourDiscardPile = 0;
	protected Integer opponentDiscardPile = null;
	protected Card newPlayedCard = null;
	protected Card yourDiscardPileTopCard = null;
	protected String newChat = null;
	protected boolean chatSent = false;
	protected String newLog = null;
	
	protected Interaction interaction = Interaction.Skip;
	protected LinkedList<CardName> cardSelection = null;
	protected Card discardCard = null;
	protected LinkedList<Card> cellarDiscards = new LinkedList<Card>();

	protected GameMode gameMode = null;
	protected HashMap<CardName, Integer> buyCards;
	protected CardName buyChoice = null;
	protected Phase currentPhase = null;
	protected boolean phaseChanged = false;
	protected boolean turnEnded = false;
	
	protected Player clientPlayer = null;
	protected Player opponentPlayer = null;

	public enum UserInput {
		clientName,
		ipAddress,
		port,
		password
	}

	private MediaPlayer mediaPlayer; // sound
	private MediaPlayer mediaPlayerBtn; // Button sound


	public GameApp_Model(Dominion_Main main){
		super();
		this.main = main;

		this.startMediaPlayer("Medieval_Camelot.mp3"); // start sound 
	}

	/**
	 * @author Adrian
	 * Encrypts a password using the secure hash algorithm (SHA-512) and returns it.
	 * @param unencryptedPassword
	 * @return encryptedPassword
	 * @throws NoSuchAlgorithmException
	 */
	private String encryptPassword(String unencryptedPassword) throws NoSuchAlgorithmException {
		String generatedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(this.SALT.getBytes());
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
	 * Checks if the user entered a valid input. This method is applicable the the inputs clientName, ipAddress, port and password.
	 * Returns true if the input is valid.
	 * 
	 * @param userInput
	 * @param inputType
	 * @return boolean input correct/incorrect
	 */
	public boolean checkUserInput(String userInput, UserInput inputType){
		boolean valid = false;
		final int MAX_INPUT_LENGTH = 15;
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
		case port:
			// The port must be an integer from 1 to 65535. 
			try {
				int number = Integer.parseInt(userInput);
				if (number > 0 && number <= 65535) valid = true;
			} catch (NumberFormatException e) {
				// input was not an integer
				valid = false;
			}
			break;
		}
		return valid;
	}

	/**
	 * Translates any parts of a String between two #: i.e. #translateThisString#
	 * 
	 * @author Lukas
	 * @param input
	 * @return translated input
	 */
	private String translate(String input){
		Pattern p = Pattern.compile(this.TRANSLATE_REGEX);
		Matcher m = p.matcher(input);
		int tmpIndex = 0;
		String output = "";
		int endIndex = 0;
		
		while(m.find()){
			int startIndex = m.start();
			endIndex = m.end();
			output += input.substring(tmpIndex, startIndex);
			output += t.getString(input.substring(startIndex+1, endIndex-1));//+- to filter #
			tmpIndex = endIndex;
		}
		
		//if there was at least 1 Tag
		if(output.length() > 0){
			//if the last match was not the end of the translation
			if(endIndex != input.length()){
				output += input.substring(endIndex, input.length());
			}
			return output;
		}
		return t.getString(input);
	}
	
	/**
	 * Separates server-site line-separators client-site. Regex is "==".
	 * Translation inclusive if translation.
	 * 
	 * @author Lukas
	 * @param input
	 * 				The input (String) to translate
	 * @param translation
	 * 				true if translation including, else false
	 * @return output, one String with system-specific separators
	 */
	private String lineSeparator(String input, boolean translation){
		String processedInput;
		if(translation){
			processedInput = this.translate(input);
		}else{
			processedInput = input;
		}
		String[] lines = processedInput.split(LINE_SEPARATOR_REGEX);
		String output = "";
		
		//separates the lines if there are multiple lines
		if(lines.length > 1){
			for(int i = 0; i < lines.length-1; i++){
				output += lines[i]+System.lineSeparator();
			}
			output += lines[lines.length-1];
		}else{
			output = processedInput;;
		}
		return output;
	}


	/**
	 * Sets the IP and the port of the server to connect.
	 * In addition, it knocks to server if the port with the given IP is listening.
	 * 
	 * @author Lukas Gehrig
	 * @param ipAddress
	 * @param port
	 * @return result, translated NO_CONNECTION to server String.
	 * 					Just necessary if the knock failed
	 */
	public String init(String ipAddress, Integer port){
		this.ipAddress = ipAddress;
		this.port = port;
		
		String result = NO_CONNECTION;
		this.failure = true;
		Knock_Message kmsg = new Knock_Message();
		Message msgIn = this.processMessage(kmsg);
		if(msgIn instanceof Commit_Message)
			this.failure = false;
		
		return this.translate(result);
	}

	/**
	 * Creates a new Socket with the set IP and Port.
	 *
	 * @author Lukas, source: Bradley Richards
	 * @return Socket
	 */
	private Socket connect(){
		Socket socket = null;
		try {
			socket = new Socket(this.ipAddress, this.port);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return socket;
	}

	/**
	 * The client sends his encrypted password to server and will get to the MainMenu if the password is appropriate to clientName.
	 * 
	 * @author Lukas
	 * @param clientName
	 * @param password
	 * @return result, usually only necessary if clientName and password don't work or the client lost connection to server
	 */
	public String sendLogin(String clientName, String password){
		String result = NO_CONNECTION;
		this.failure = true;
		this.clientName = clientName;
		Login_Message lmsg = new Login_Message();
		
		//Set the encrypted password to the Login_Message
		try {
			lmsg.setPassword(this.encryptPassword(password));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		Message msgIn = this.processMessage(lmsg);
		if(msgIn instanceof Commit_Message){
			this.failure = false;//login succeeded
			this.main.startMainMenu();

		}else if(msgIn instanceof Failure_Message){
			Failure_Message fmsg = (Failure_Message) msgIn;//login failed, clientName and/or password wrong
			result = fmsg.getNotification();
		}
		return this.translate(result);
	}

	/**
	 * Sends the clientName and the encrypted password to server.
	 * When the clientName was unique, the name and encrypted password will be stored in the server's database.
	 * 
	 * @author Lukas
	 * @param clientName
	 * @param password
	 * @return result, usually only necessary if clientName is already set or there is no connection to server
	 */
	public String sendCreateNewPlayer(String clientName, String password){
		String result = NO_CONNECTION;
		this.failure = true;
		this.clientName = clientName;
		
		//Set the clientName and encrypted password to XML
		CreateNewPlayer_Message cnpmsg = new CreateNewPlayer_Message();
		try {
			cnpmsg.setPassword(this.encryptPassword(password));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		Message msgIn = this.processMessage(cnpmsg);
		if(msgIn instanceof Commit_Message){
			this.failure = false;//createNewPlayer succeeded
			this.main.startMainMenu();

		}else if(msgIn instanceof Failure_Message){
			Failure_Message fmsg = (Failure_Message) msgIn;//createNewPlayer failed
			result = fmsg.getNotification();
		}
		return this.translate(result);
	}

	/**
	 * Sends a request to server for the top5 highscore.
	 * 
	 * @author Lukas
	 * @return result, ObservableList with the highscore
	 */
	public ObservableList<Highscore> sendHighScoreRequest(){
		HighScore_Message hsmsg = new HighScore_Message();
		LinkedList<Highscore> highscoreList = new LinkedList<Highscore>();

		Message msgIn = this.processMessage(hsmsg);
		if(msgIn instanceof HighScore_Message){
			HighScore_Message nhsmsg = (HighScore_Message) msgIn;
			String[] elements = nhsmsg.getHighScore().split(this.HIGHSCORE_REGEX);
			if(elements.length > 2){
				for(int i = 0; i < elements.length; i+= 3){
					highscoreList.add(new Highscore(elements[i], elements[i+1], elements[i+2]));
				}
			}
		}
		ObservableList<Highscore> observableHighscoreList = FXCollections.observableList(highscoreList);
		return observableHighscoreList;
	}

	/**
	 * Sends the client's cosen GameMode (Singleplayer or Multiplayer) to server.
	 * 
	 * @author Lukas
	 * @param mode
	 * 				The mode (GameMode) the client wants to play
	 * @return result, usually only necessary if the client lost connection to server
	 */
	public String sendGameMode(GameMode mode){
		String result = NO_CONNECTION;
		this.failure = true;
		GameMode_Message gmmsg = new GameMode_Message();
		gmmsg.setMode(mode);
		this.gameMode = mode;

		Message msgIn = this.processMessage(gmmsg);
		if(msgIn instanceof Commit_Message){
			this.failure = false;
			this.main.startGameApp();
		}else if(msgIn instanceof Failure_Message){
			Failure_Message fmsg = (Failure_Message) msgIn;
			result =fmsg.getNotification();
		}
		return this.translate(result);
	}

	/**
	 * Sends the card the client wants to buy. The result depends on the players validity to buy.
	 * 
	 * @author Lukas
	 * @param cardName
	 * 				The card (CardName) the client wants to buy
	 * @return update, tells the controller if the game has to be updated
	 */
	protected boolean sendBuyCard(CardName cardName){
		BuyCard_Message bcmsg = new BuyCard_Message();
		bcmsg.setCard(cardName);
		boolean update = false;

		Message msgIn = this.processMessage(bcmsg);
		if(msgIn instanceof UpdateGame_Message){//buy succeeded
			this.processUpdateGame(msgIn);
			update = true;

		}else if(msgIn instanceof PlayerSuccess_Message){//the game ended after this buy
			this.processPlayerSuccess(msgIn);
			update = true;

		}else if(msgIn instanceof Failure_Message){//it was not allowed to buy this card
			//nothing toDo here
		}
		return update;
	}

	/**
	 * The client wants to play a chosen Card. The result depends on the validity of the move.
	 * 
	 * @author Lukas
	 * @param card
	 * 				The chosen card the client wants to play
	 * @return update, tells the controller if the game has to be updated
	 */
	protected boolean sendPlayCard(Card card){
		PlayCard_Message pcmsg = new PlayCard_Message();
		pcmsg.setCard(card);
		boolean update = false;

		Message msgIn = this.processMessage(pcmsg);
		if(msgIn instanceof UpdateGame_Message){
			this.processUpdateGame(msgIn);
			update = true;
		}else if(msgIn instanceof Failure_Message){
			//nothing toDo here
		}
		return update;
	}

	/**
	 * Sends a Chat_Message to the opponent. The chat of the client will return from server.
	 * 
	 * @author Lukas
	 * @param chat
	 * @return update, tells the controller if the game has to be updated
	 */
	protected boolean sendChat(String chat){
		Chat_Message cmsg = new Chat_Message();
		cmsg.setChat(chat);
		boolean update = false;

		Message msgIn = this.processMessage(cmsg);
		if(msgIn instanceof UpdateGame_Message){
			this.processUpdateGame(msgIn);
			update = true;
		}
		return update;
	}

	/**
	 * Sends the specified interaction with content to server.
	 * 
	 * @author Lukas
	 * @return update, tells the controller if the game has to be updated
	 */
	protected boolean sendInteraction(){
		Interaction_Message imsg = new Interaction_Message();
		boolean update = false;
		imsg.setInteractionType(this.interaction);

		switch(this.interaction){
		case Skip:
			//skip is already set in this.interaction, nothing toDo here
			break;
		case EndOfTurn:
			imsg.setDiscardCard(this.discardCard);
			break;
		case Cellar:
			imsg.setCellarDiscardCards(this.cellarDiscards);
			break;
		case Workshop:
			imsg.setWorkshopChoice(this.buyChoice);
			break;
		case Remodel1:
			imsg.setDisposeRemodelCard(this.discardCard);
			break;
		case Remodel2:
			imsg.setRemodelChoice(this.buyChoice);
			break;
		case Mine:
			imsg.setDisposedMineCard(this.discardCard);
			break;
		default:
			return false;
		}

		Message msgIn = this.processMessage(imsg);
		if(msgIn instanceof UpdateGame_Message){
			UpdateGame_Message ugmsg = (UpdateGame_Message) msgIn;
			update = true;
			this.discardCard = null;
			this.buyChoice = null;
			
			//The picked card with mine will come into the hand and not to discardPile. But the buyCards has to be decreased
			if(this.interaction == Interaction.Mine){
				this.yourNewHandCards.add(ugmsg.getBoughtCard());
				this.buyCards.replace(ugmsg.getBoughtCard().getCardName(), this.buyCards.get(ugmsg.getBoughtCard().getCardName())-1);
				ugmsg.setBoughtCard(null);
			}
			this.interaction = Interaction.Skip;//defaultSetting
			this.processUpdateGame(ugmsg);

		}else if(msgIn instanceof PlayerSuccess_Message){
			this.processPlayerSuccess(msgIn);
			update = true;
		}
		return update;
	}
	
	/**
	 * Sends a GiveUp_Message to Server.
	 * 
	 * @author Lukas
	 * @return update, if communication to server was successful
	 */
	protected boolean sendGiveUp(){
		GiveUp_Message gumsg = new GiveUp_Message();
		boolean update = false;

		Message msgIn = this.processMessage(gumsg);
		if(msgIn instanceof PlayerSuccess_Message){
			this.processPlayerSuccess(msgIn);
			update = true;
		}
		return update;
	}
	
	public void sendLogout(){
		Logout_Message lmsg = new Logout_Message();
		this.processMessage(lmsg);
	}


	/**
	 * Set all necessary variables to creates a new Game.
	 * 
	 * @author Lukas
	 * @param msgIn
	 * 				The CreateGame_Message to process
	 */
	protected void processCreateGame(Message msgIn) {		
		CreateGame_Message cgmsg = (CreateGame_Message) msgIn;
		this.yourNewHandCards = cgmsg.getHandCards();
		this.buyCards = cgmsg.getBuyCards();
		this.opponent = cgmsg.getOpponent();
		this.opponentDeck = cgmsg.getDeckNumber();
		this.opponentHandCards = cgmsg.getHandNumber();
		this.currentPlayer = cgmsg.getStartingPlayer();
		this.currentPhase = cgmsg.getPhase();
		this.yourDeck = cgmsg.getDeckPile().size();
	}

	/**
	 * Set the players with set success and victoryPoints. Result depends weather you won or lost.
	 * 
	 * @author Lukas
	 * @param msgIn
	 * 				The PlayerSuccess_Message to process
	 */
	protected void processPlayerSuccess(Message msgIn) {
		PlayerSuccess_Message psmsg = (PlayerSuccess_Message) msgIn;
		if(psmsg.getPlayer1().getPlayerName().compareTo(this.clientName) == 0){
			this.clientPlayer = psmsg.getPlayer1();
			this.opponentPlayer = psmsg.getPlayer2();
		}else{
			this.opponentPlayer = psmsg.getPlayer1();
			this.clientPlayer = psmsg.getPlayer2();
		}
	}


	/**
	 * Interprets all updates and provides structures for further work.
	 * 
	 * @author Lukas
	 * @param msgIn
	 * 				 UpdateGame_Message. Can consist various contents
	 */

	protected void processUpdateGame(Message msgIn) {	
		UpdateGame_Message ugmsg = (UpdateGame_Message) msgIn;
		
		//If currentPlayer is set, the currentPlayer's turn ends
		if(ugmsg.getCurrentPlayer() != null && ugmsg.getCurrentPlayer().compareTo(this.currentPlayer) != 0){
			if(this.currentPlayer.compareTo(this.opponent) == 0)
				this.opponentHandCards = 0;
			this.turnEnded = true;
			this.currentPlayer = ugmsg.getCurrentPlayer();
		}else{
			this.turnEnded = false;
		}

		//If something necessary happened in the Game, it will be provided to show
		if(ugmsg.getLog() != null){
			this.newLog = this.lineSeparator(ugmsg.getLog(), true);
		}

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
		if(ugmsg.getCurrentPhase() != null){
			this.currentPhase = ugmsg.getCurrentPhase();
			this.phaseChanged = true;
		}

		//If a buy was successful. Always currentPlayer
		//stores the boughtCard of the currentPlayer and reduces the value of the buyCards(Cards which can be bought)
		if(ugmsg.getBoughtCard() != null)
			this.buyCards.replace(ugmsg.getBoughtCard().getCardName(), this.buyCards.get(ugmsg.getBoughtCard().getCardName())-1);
		

		//Just necessary to show opponent's size of discardPile
		if(ugmsg.getDeckPileCardNumber() != null && 
				((this.currentPlayer.compareTo(this.clientName) == 0 && ugmsg.getCurrentPlayer() == null)
				|| (this.currentPlayer.compareTo(this.clientName) != 0 && ugmsg.getCurrentPlayer() != null))){
			this.yourDeck = ugmsg.getDeckPileCardNumber();
		}else if(ugmsg.getDeckPileCardNumber() != null){
			this.opponentDeck = ugmsg.getDeckPileCardNumber();
		}

		//Just necessary to show opponent's size of deckPile
		if(ugmsg.getDiscardPileCardNumber() != null && 
				((this.currentPlayer.compareTo(this.clientName) == 0 && ugmsg.getCurrentPlayer() == null)
				|| (this.currentPlayer.compareTo(this.clientName) != 0 && ugmsg.getCurrentPlayer() != null))){
			this.yourDiscardPile = ugmsg.getDiscardPileCardNumber();
		}else if(ugmsg.getDiscardPileCardNumber() != null){
			this.opponentDiscardPile = ugmsg.getDiscardPileCardNumber();
		}

		//Always client's topCard
		if(ugmsg.getDiscardPileTopCard() != null && 
				((this.currentPlayer.compareTo(this.clientName) == 0 && ugmsg.getCurrentPlayer() == null)
				|| (this.currentPlayer.compareTo(this.clientName) != 0 && ugmsg.getCurrentPlayer() != null))){
			this.yourDiscardPileTopCard = ugmsg.getDiscardPileTopCard();
		}

		//The new handCards just drawn. Always currentPlayer
		//Move the drawn cards from the deck into yourNewHandCards
		if(ugmsg.getNewHandCards() != null && 
				((this.currentPlayer.compareTo(this.clientName) == 0 && ugmsg.getCurrentPlayer() == null)
				|| (this.currentPlayer.compareTo(this.clientName) != 0 && ugmsg.getCurrentPlayer() != null))){
			this.yourNewHandCards = ugmsg.getNewHandCards();
		}else if(ugmsg.getNewHandCards() != null){//for opponent
			this.opponentHandCards += ugmsg.getNewHandCards().size();
		}

		//If a card was played, it will be provided
		//Move the played Card from the hand into newPlayedCard
		if(ugmsg.getPlayedCard() != null){
			this.newPlayedCard = ugmsg.getPlayedCard();
		}

		//If interaction is set, the Type of Interaction can be checked (i.e. meaning of the commit_Button)
		if(ugmsg.getInteractionType() != null &&
				((this.currentPlayer.compareTo(this.clientName) == 0 && ugmsg.getCurrentPlayer() == null)
				|| (this.currentPlayer.compareTo(this.clientName) != 0 && ugmsg.getCurrentPlayer() != null))){
			this.interaction = ugmsg.getInteractionType();
		}

		//If cardSelection is set, it consists a selection of the cards to chose
		if(ugmsg.getCardSelection() != null &&
				((this.currentPlayer.compareTo(this.clientName) == 0 && ugmsg.getCurrentPlayer() == null)
				|| (this.currentPlayer.compareTo(this.clientName) != 0 && ugmsg.getCurrentPlayer() != null))){
			this.cardSelection = ugmsg.getCardSelection();
		}

	}


	/**
	 * SetUp a socket_connection to server with the given message and returns the answer.
	 * 
	 * @author Lukas, source:  partly(Bradley Richards)
	 * @param message
	 * @return msgIn, individual InputMessage
	 */
	protected synchronized Message processMessage(Message message){//synchronized is necessary because of the Thread in controller uses this method too
		Socket socket = connect();
		Message msgIn = null;
		if(socket != null){
			try{
				message.setClient(this.clientName);
				message.send(socket, null);
				msgIn = Message.receive(socket);
			}catch(Exception e){
				System.out.println(e.toString());
			}
			try { if (socket != null) socket.close(); } catch (IOException e) {}
			
			//Tries to get the lost Message on server (max tries: 20 to avoid infiniteLoops) i.e. a socketException server-site happened
			if(msgIn == null){
				this.requestCounter++;
				if(this.requestCounter <= 20){
					try{
						Thread.sleep(10);
					}catch(Exception e){
						//noting toDo here
					}
					this.processMessage(new Request_Message());
				}else{
					System.out.println("Request failed");
				}
			}
			this.requestCounter = 0;
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
		// Mediaplayer: new music
		if (mediaPlayer != null) {
			mediaPlayer.stop();
		}
		URL resource = getClass().getResource(soundFileName);
		mediaPlayer = new MediaPlayer(new Media(resource.toString()));
		mediaPlayer.setOnEndOfMedia(new Runnable() {
			public void run() {
				mediaPlayer.seek(Duration.ZERO);
			}
		});
		mediaPlayer.play();
	}
	
	
	// Button click sound
	public void startBtnClickSound() {
		if (mediaPlayerBtn != null) {
			mediaPlayerBtn.stop();
		}
		URL resource = getClass().getResource("button_click_sound.mp3");
		mediaPlayerBtn = new MediaPlayer(new Media(resource.toString()));
		mediaPlayerBtn.play();
	}
	
	// Game stard sound
		public void gameStartSound() {
			if (mediaPlayerBtn != null) {
				mediaPlayerBtn.stop();
			}
			URL resource = getClass().getResource("Sword_Sound.mp3");
			mediaPlayerBtn = new MediaPlayer(new Media(resource.toString()));
			mediaPlayerBtn.play();
		}
	

	public String getClientName(){
		return this.clientName;
	}
	
	public boolean getFailure(){
		return this.failure;
	}
	
	public void setGameMode(GameMode gameMode){
		this.gameMode = gameMode;
	}

	public void setIP(String ipAddress){
		this.ipAddress = ipAddress;
	}
	
	public void setTranslator(Translator translator){
		this.t = translator;
	}

	public void setClientName(String clientName){
		this.clientName = clientName;
	}
}//end GameApp_Model