package Messages;

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
import Cards.Cellar_Card;
import Cards.Copper_Card;
import Cards.Duchy_Card;
import Cards.Gold_Card;
import Cards.Market_Card;
import Cards.Mine_Card;
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
import javafx.application.Platform;

public class TestMessages {

	public static void main(String[] args) {
		GameApp_Model model = new GameApp_Model(new Dominion_Main());
		model.init("127.0.0.1");
		String login = model.sendLogin("Lukas", "password");
		if(login == null)
			System.out.println("test failed");
		else System.out.println("test succeeded");
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
		LinkedList<Card> cardSelection = new LinkedList<Card>();
		cardSelection.add(new Cellar_Card());
		cardSelection.add(new Market_Card());
		cardSelection.add(new Smithy_Card());
		cardSelection.add(new Workshop_Card());
		
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

}
