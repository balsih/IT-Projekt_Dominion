package Messages;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.sun.xml.internal.txw2.Document;

import Cards.Card;
import Cards.CardName;
import Cards.CardType;
import Cards.Cellar_Card;
import Cards.Copper_Card;
import Cards.Duchy_Card;
import Cards.Estate_Card;
import Cards.Gold_Card;
import Cards.Market_Card;
import Cards.Mine_Card;
import Cards.Province_Card;
import Cards.Remodel_Card;
import Cards.Silver_Card;
import Cards.Smithy_Card;
import Cards.Village_Card;
import Cards.Woodcutter_Card;
import Cards.Workshop_Card;
import Client_GameApp_MVC.GameApp_Model;
import Client_Services.ServiceLocator;
import MainClasses.Dominion_Main;
import Server_GameLogic.Game;
import Server_GameLogic.GameMode;
import Server_GameLogic.Phase;
import Server_GameLogic.Player;
import Server_Services.DB_Connector;
import javafx.application.Platform;

public class TestMessages {
	

	public static void main(String[] args) {
		GameApp_Model model = new GameApp_Model(new Dominion_Main());
		model.init("127.0.0.1", 8080);
		model.sendLogin("Lukas", "Lukas");
		sendGameMode(GameMode.Singleplayer, model);
		askForChanges(model);
		if(model.currentPlayer.compareTo(model.clientName) == 0){
			for(int i = 0; i < 2; i++){
				for(Card card: model.yourHandCards){
					if(card.getType() == CardType.Treasure){
						model.sendPlayCard(card);
						break;
					}
				}
			}
			model.sendBuyCard(CardName.Cellar);
			System.out.println("You have bought: "+model.yourBuyedCard);
		}else{
			System.out.println("you're not currentPlayer, try again");
		}
	}

	
	public static void checkPlayActionCard(GameApp_Model model){
		
		sendGameMode(GameMode.Singleplayer, model);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println("Thread.sleep throwed exception");
		}
		askForChanges(model);
		System.out.println("currentPlayer: "+model.currentPlayer);
		if(model.currentPlayer.compareTo(model.clientName) == 0){
			model.yourHandCards = model.yourNewHandCards;
			boolean actionCard = false;
			for(int j = 0; j < model.yourHandCards.size(); j++){
				if(model.yourHandCards.get(j).getType() == CardType.Action)
					actionCard = true;
			}
			if(!actionCard){
				System.out.println("no ActionCards in the hand");
			}
			for(int i = 0; i < model.yourHandCards.size(); i++){
				if(model.yourHandCards.get(i).getType() == CardType.Action){
					Card card = model.yourHandCards.get(i);
					System.out.println("wanna play: "+card.toString());
					boolean success = sendPlayCard(card);
					if(success){
						System.out.println("you played the "+card.toString()+" successful");
					}else{
						System.out.println("failed to play the "+card.toString()+" card");
					}
					break;
				}
			}
		}else{
			System.out.println("try again, you're not currentPlayer");
		}
	}
	
	/**TESTED
	 * @author Lukas
	 * The client sends his GameMode (Singleplayer or Multiplayer) to Server.
	 * 
	 * @param mode
	 * @return result, usually only necessary if the client lost connection to server
	 */
	public static String sendGameMode(GameMode mode, GameApp_Model model){
		String result = "no connection";
		GameMode_Message gmmsg = new GameMode_Message();
		gmmsg.setClient(model.clientName);//set the clientName and mode(SinglePlayer or MultiPlayer) to XML
		gmmsg.setMode(mode);
		model.gameMode = mode.toString();

		Message msgIn = processMessage(gmmsg);
		if(msgIn instanceof Commit_Message){
			System.out.println(mode.toString()+": succeeded");
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
	public static boolean sendPlayCard(Card card){
		PlayCard_Message pcmsg = new PlayCard_Message();
		pcmsg.setCard(card);
		boolean update = false;

		Message msgIn = processMessage(pcmsg);
		if(msgIn instanceof UpdateGame_Message){
			processUpdateGame(msgIn);
			update = true;
		}else if(msgIn instanceof Failure_Message){
			//nothing toDo here
		}
		return update;
	}
	
	/**
	 * @author Lukas
	 * Interpret all updates and provides structures for further work
	 * 
	 * @param msgIn, UpdateGame_Message. Can consist various contents
	 */

	private static void processUpdateGame(Message msgIn) {	
		
		String clientName = "Lukas";
		String currentPlayer = "Lukas";
		String opponent = "Bodo";
		
		
		HashMap<CardName, Integer> buyCards = new HashMap<CardName, Integer>();
		buyCards.put(CardName.Province, 8);
		buyCards.put(CardName.Duchy, 8);
		buyCards.put(CardName.Estate, 8);

		buyCards.put(CardName.Copper, 30);
		buyCards.put(CardName.Gold, 30);
		buyCards.put(CardName.Silver, 30);

		buyCards.put(CardName.Workshop, 10);
		buyCards.put(CardName.Woodcutter, 10);
		buyCards.put(CardName.Village, 10);
		buyCards.put(CardName.Smithy, 10);
		buyCards.put(CardName.Remodel, 10);
		buyCards.put(CardName.Mine, 10);
		buyCards.put(CardName.Market, 10);
		buyCards.put(CardName.Cellar, 10);
		
		UpdateGame_Message ugmsg = (UpdateGame_Message) msgIn;

		//If something necessary happened in the Game, it will be provided to show
		if(ugmsg.getLog() != null)
			System.out.println("Log: "+ugmsg.getLog());
		//			this.newLog = this.translate(ugmsg.getLog());

		//If the client or opponent sent a chat, it will be provided to show
		if(ugmsg.getChat() != null)
			System.out.println("Chat: "+ugmsg.getChat());

		//Always currentPlayer
		if(ugmsg.getActions() != null)
			System.out.println("Actions: "+ugmsg.getActions().toString());

		//Always currentPlayer
		if(ugmsg.getBuys() != null)
			System.out.println("Buys: "+ugmsg.getBuys().toString());;

		//Always currentPlayer
		if(ugmsg.getCoins() != null)
			System.out.println("Coins: "+ugmsg.getCoins().toString());

		//Always currentPlayer
		if(ugmsg.getCurrentPhase() != null)
			System.out.println("CurrentPhase: "+ugmsg.getCurrentPhase().toString());

		//If a buy was successful. Always currentPlayer
		//stores the buyedCard of the currentPlayer and reduces the value of the buyCards(Cards which can be bought)
		if(ugmsg.getBuyedCard() != null && currentPlayer == clientName){
			Card yourBuyedCard = ugmsg.getBuyedCard();
			System.out.println("yourBuyedCard: "+yourBuyedCard.toString());
			buyCards.replace(yourBuyedCard.getCardName(), buyCards.get(yourBuyedCard.getCardName())-1);
			System.out.println("buyCards: "+buyCards.toString());
		}else if(ugmsg.getBuyedCard() != null){
			Card opponentBuyedCard = ugmsg.getBuyedCard();
			System.out.println("opponentBuyedCard: "+opponentBuyedCard.toString());
			buyCards.replace(opponentBuyedCard.getCardName(), buyCards.get(opponentBuyedCard.getCardName())-1);
			System.out.println("buyCards: "+buyCards.toString());
		}

		//Just necessary to show opponent's size of discardPile
		if(ugmsg.getDeckPileCardNumber() != null && currentPlayer == opponent)
			System.out.println("DiscardPileCardNumber: "+ugmsg.getDeckPileCardNumber().toString());

		//Just necessary to show opponent's size of deckPile
		if(ugmsg.getDiscardPileCardNumber() != null && currentPlayer == opponent)
			System.out.println("DiscardPileCardNumber: "+ugmsg.getDiscardPileCardNumber().toString());

		//Always client's topCard
		if(ugmsg.getDiscardPileTopCard() != null && currentPlayer == clientName)
			System.out.println("DiscardPileTopCard: "+ugmsg.getDiscardPileTopCard().toString());

		//If currentPlayer is set, the currentPlayer's turn ends
		if(ugmsg.getCurrentPlayer() != null){
			if(ugmsg.getCurrentPlayer() != currentPlayer){
				System.out.println("turnEnded!!");
				if(ugmsg.getCurrentPlayer() == opponent){//if it was your turn that ended
					System.out.println("CleanUp your hand and board, "+ugmsg.getCurrentPlayer()+"'s turn");
				}else{//if it was your opponents turn that ended
					System.out.println("CleanUp opponents playedCard, "+ugmsg.getCurrentPlayer()+"'s turn");
				}
			}
			currentPlayer = ugmsg.getCurrentPlayer();
		}

		//The new handCards just drawn. Always currentPlayer
		//Move the drawn cards from the deck into yourNewHandCards
		if(ugmsg.getNewHandCards() != null && (currentPlayer == clientName) || (ugmsg.getCurrentPlayer() == opponent)){
			LinkedList<Card> newHandCards = ugmsg.getNewHandCards();
			System.out.println("YourNewHandCards:");
			for(int i = 0; i < newHandCards.size(); i++){
				System.out.println(newHandCards.get(i).toString());
			}
		}else if(ugmsg.getNewHandCards() != null){//for opponent
			System.out.println("opponent has drawn "+ugmsg.getNewHandCards().size()+" Cards");
		}

		//If a card was played, it will be provided
		//Move the played Card from the hand into newPlayedCard
		if(ugmsg.getPlayedCard() != null && currentPlayer == clientName){
			System.out.println("you played "+ugmsg.getPlayedCard().toString()+"successful");
		}else if(ugmsg.getPlayedCard() != null){//for opponent
			System.out.println("opponent played "+ugmsg.getPlayedCard().toString());
		}

		//If interaction is set, the Type of Interaction can be checked (i.e. meaning of the commit_Button)
		if(ugmsg.getInteractionType() != null && currentPlayer == clientName)
			System.out.println(ugmsg.getInteractionType().toString()+" Interaction activated");

		//If cardSelection is set, it consists a selection of the cards to chose
		if(ugmsg.getCardSelection() != null && currentPlayer == clientName){
			System.out.println("your cardselection:");
			for(int i = 0; i < ugmsg.getCardSelection().size(); i++){
				System.out.println(ugmsg.getCardSelection().get(i));
			}
		}
			

	}
	
	private static boolean askForChanges(GameApp_Model model){
		boolean update = false;

		Message msgIn = processMessage(new AskForChanges_Message());
		if(msgIn instanceof UpdateGame_Message){
			processUpdateGame(msgIn);
			update = true;
		}else if(msgIn instanceof Commit_Message){
			System.out.println("AskForChanges commited");
		}else if(msgIn instanceof CreateGame_Message){
			CreateGame_Message cgmsg = (CreateGame_Message) msgIn;
			System.out.println("opponent: "+cgmsg.getOpponent());
			System.out.println("startingPlayer: "+cgmsg.getStartingPlayer());
			System.out.println("deckPile: "+cgmsg.getDeckPile().toString());
			System.out.println("handCards: "+cgmsg.getHandCards().toString());
			System.out.println("handNumber: "+cgmsg.getHandNumber().toString());
			System.out.println("deckNumber: "+cgmsg.getDeckNumber().toString());
			System.out.println("buyCards:"+cgmsg.getBuyCards().toString());
			model.processCreateGame(msgIn);
			model.yourHandCards = model.yourNewHandCards;
		}
		return update;
	}
	
	/**
	 * @author Lukas
	 * SetUp a socket_connection to server with the given message and returns the answer
	 * 
	 * @param message
	 * @return msgIn, individual InputMessage
	 */
	private static Message processMessage(Message message){
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
	 *@author Bradley Richards
	 *Creates a new Socket with the set IP and Port
	 * 
	 * @return Socket
	 */
	private static Socket connect(){
		Socket socket = null;
		try {
			socket = new Socket("127.0.0.1", 8080);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return socket;
	}
	
	/**
	 * @author Lukas
	 * Translates any parts of a String between two #
	 * 
	 * @param input
	 * @return translated input
	 */
	private static String translate(String input){
		Pattern p = Pattern.compile("#[\\w\\s]*#");
		Matcher m = p.matcher(input);
		int tmpIndex = 0;
		String output = "";
		String[] list = input.split("#[\\w\\s]*#");
		String lastPart = list[list.length-1];
		while(m.find()){
			int startIndex = m.start();
			int endIndex = m.end();
			output += input.substring(tmpIndex, startIndex);
			output += input.substring(startIndex+1, endIndex-1);//+- to filter #
			tmpIndex = endIndex;
		}
		if(output.length() > 0){
			output += lastPart;
			return output;
		}
		return input;
	}
	
	private static void testMapFilter(){
		
		HashMap<CardName, Integer> buyCards = new HashMap<CardName, Integer>();
		buyCards.put(CardName.Cellar, 12);
		buyCards.put(CardName.Copper, 25);
		buyCards.put(CardName.Duchy, 2);
		buyCards.put(CardName.Estate, 6);
		buyCards.put(CardName.Gold, 24);
		buyCards.put(CardName.Market, 6);
		buyCards.put(CardName.Mine, 1);
		buyCards.put(CardName.Province, 3);
		buyCards.put(CardName.Remodel, 5);
		buyCards.put(CardName.Silver, 15);
		buyCards.put(CardName.Smithy, 7);
		buyCards.put(CardName.Village, 0);
		buyCards.put(CardName.Woodcutter, 1);
		buyCards.put(CardName.Workshop, 2);
		
		List<CardName> list0 = buyCards.keySet()
				.stream()
				.filter(c -> buyCards.get(c) <= 3)
				.collect(Collectors.toList());
		for(CardName cardName: list0){
			System.out.println(cardName.toString());
		}
		
	}
	
	private static void testMapSort(){
		HashMap<CardName, Integer> priorityList = new HashMap<CardName, Integer>();
		// priorityList.put(CardName.Cellar, 10); do not use but implement first if enough time
		priorityList.put(CardName.Copper, 20);
		priorityList.put(CardName.Duchy, 50);
		priorityList.put(CardName.Estate, 10);
		priorityList.put(CardName.Gold, 70);
		priorityList.put(CardName.Market, 68);
		// priorityList.put(CardName.Mine, 10); do not use
		priorityList.put(CardName.Province, 90);
		priorityList.put(CardName.Remodel, 66);
		priorityList.put(CardName.Silver, 40);
		priorityList.put(CardName.Smithy, 67);
		priorityList.put(CardName.Village, 69);
		// priorityList.put(CardName.Woodcutter, 10); do not use
		priorityList.put(CardName.Workshop, 10); // do not use
		
		//@author Lukas, get the correct order for a prioList
		List<CardName> list = priorityList.keySet()
		.stream()
		.sorted((c1, c2) -> Integer.compare(priorityList.get(c2), priorityList.get(c1)))
		.collect(Collectors.toList());
		
		for(CardName name: list){
		System.out.println(name.toString());
		}
		
		//@author Lukas, get the correct order for a prioList
		LinkedList<CardName> currentPrioList = new LinkedList<CardName>();
		currentPrioList.addAll(priorityList.keySet());
		currentPrioList.sort((c1, c2) -> Integer.compare(priorityList.get(c2), priorityList.get(c1)));
		
		for(CardName name: currentPrioList){
			System.out.println(name.toString());
		}
	}
	
	private static void testInteraction_Message(){
		
		Interaction_Message imsg = new Interaction_Message();
		
//		//EndOfTurn simulation
//		Interaction interaction = Interaction.EndOfTurn;
//		Card EOTCard = new Workshop_Card();
//		imsg.setInteractionType(interaction);
//		imsg.setDiscardCard(EOTCard);
		
//		//Cellar simulation
//		Interaction interaction = Interaction.Cellar;
//		LinkedList<Card> discardCards = new LinkedList<Card>();
//		discardCards.add(new Copper_Card());
//		discardCards.add(new Copper_Card());
//		discardCards.add(new Mine_Card());
//		imsg.setInteractionType(Interaction.Cellar);
//		imsg.setCellarDiscardCards(discardCards);
		
//		//Workshop simulation
//		Interaction interaction = Interaction.Workshop;
//		CardName workshopChoice = CardName.Market;
//		imsg.setInteractionType(interaction);
//		imsg.setWorkshopChoice(workshopChoice);
		
//		//Remodel1 simulation
//		Interaction interaction = Interaction.Remodel1;
//		Card disposedCard = new Woodcutter_Card();
//		imsg.setInteractionType(interaction);
//		imsg.setDisposeCard(disposedCard);
		
		//Remodel2 simulation
		Interaction interaction = Interaction.Remodel2;
		CardName remodelChoice = CardName.Province;
		imsg.setInteractionType(interaction);
		imsg.setRemodelChoice(remodelChoice);
		
		imsg.toString();
		System.out.println(imsg);
	}

	
	private static void testUpdateGame_Message(){
		
		String log = "testLog";
		
		String currentPlayer = "Lukas";
		
		Integer coins = 5;
		
		Integer actions = 2;
		
		Integer buys = 3;
		
		Phase currentPhase = Phase.Action;
		
		Integer discardPileCardNumber = 13;
		
		Integer deckPileCardNumber = 21;
		
		String chat = "how are you?";
		
		Card playedCard = new Smithy_Card();
		
		Card buyedCard = new Market_Card();
		
		Card discardPileTopCard = new Gold_Card();
		
		Interaction interaction = Interaction.EndOfTurn;
		LinkedList<CardName> cardSelection = new LinkedList<CardName>();
		cardSelection.add(CardName.Cellar);
		cardSelection.add(CardName.Market);
		cardSelection.add(CardName.Smithy);
		cardSelection.add(CardName.Workshop);
		
		LinkedList<Card> handCards = new LinkedList<Card>();
		handCards.add(new Smithy_Card());
		handCards.add(new Mine_Card());
		handCards.add(new Market_Card());
		

		UpdateGame_Message ugmsg = new UpdateGame_Message();
		ugmsg.setLog(log);
		ugmsg.setCurrentPlayer(currentPlayer);
		ugmsg.setCoins(coins);
		ugmsg.setActions(actions);
		ugmsg.setBuys(buys);
		ugmsg.setCurrentPhase(currentPhase);
		ugmsg.setDiscardPileTopCard(discardPileTopCard);
		ugmsg.setDiscardPileCardNumber(discardPileCardNumber);
		ugmsg.setDeckPileCardNumber(deckPileCardNumber);
		ugmsg.setBuyedCard(buyedCard);
		ugmsg.setChat(chat);
		ugmsg.setNewHandCards(handCards);
		ugmsg.setPlayedCards(playedCard);
		ugmsg.setInteractionType(Interaction.EndOfTurn);
		ugmsg.setCardSelection(cardSelection);
		
		ugmsg.toString();
		System.out.println(ugmsg);
	}
	
	private static void testCreateGame_Message(){
		
		String opponent = "mordrag";
		
		HashMap<CardName, Integer> buyCards = new HashMap<CardName, Integer>();
		buyCards.put(CardName.Duchy, 5);
		buyCards.put(CardName.Smithy, 12);
		
		Stack<Card> deckPile = new Stack<Card>();
		deckPile.push(new Village_Card());
		deckPile.push(new Woodcutter_Card());
		
		LinkedList<Card> handCards = new LinkedList<Card>();
		handCards.add(new Smithy_Card());
		handCards.add(new Mine_Card());
		handCards.add(new Market_Card());
		
		CreateGame_Message cgmsg = new CreateGame_Message();
		
		cgmsg.setBuyCards(buyCards);
		cgmsg.setDeckPile(deckPile);
		cgmsg.setOpponent(opponent);
		cgmsg.setHandCards(handCards);
		cgmsg.setHandNumber(5);
		cgmsg.setDeckNumber(5);
		
		cgmsg.toString();
		System.out.println(cgmsg);
		
	}
	
	public static Card getCard(CardName cardName){
		
		Card card = null;
		
		switch (cardName) {
		case Copper:
			card = new Copper_Card();
			break;
		case Cellar:
			card = new Cellar_Card();
			break;
		case Duchy:
			card = new Duchy_Card();
			break;
		case Estate:
			card = new Estate_Card();
			break;
		case Gold:
			card = new Gold_Card();
			break;
		case Market:
			card = new Market_Card();
			break;
		case Mine:
			card = new Mine_Card();
			break;
		case Province:
			card = new Province_Card();
			break;
		case Remodel:
			card = new Remodel_Card();
			break;
		case Silver:
			card = new Silver_Card();
			break;
		case Smithy:
			card = new Smithy_Card();
			break;
		case Village:
			card = new Village_Card();
			break;
		case Woodcutter:
			card = new Woodcutter_Card();
			break;
		case Workshop:
			card = new Workshop_Card();
			break;
		}
		
		return card;
	}

}
