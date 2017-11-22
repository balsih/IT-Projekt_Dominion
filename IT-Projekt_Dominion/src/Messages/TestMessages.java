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
import java.util.stream.Collectors;

import com.sun.xml.internal.txw2.Document;

import Cards.Card;
import Cards.CardName;
import Cards.Cellar_Card;
import Cards.Copper_Card;
import Cards.Gold_Card;
import Cards.Market_Card;
import Cards.Mine_Card;
import Cards.Smithy_Card;
import Cards.Village_Card;
import Cards.Woodcutter_Card;
import Cards.Workshop_Card;
import Client_Services.ServiceLocator;
import Server_GameLogic.Phase;

public class TestMessages {

	public static void main(String[] args) {
		
//		HashMap<CardName, Integer> priorityList = new HashMap<CardName, Integer>();
//		// priorityList.put(CardName.Cellar, 10); do not use but implement first if enough time
//		priorityList.put(CardName.Copper, 20);
//		priorityList.put(CardName.Duchy, 50);
//		priorityList.put(CardName.Estate, 10);
//		priorityList.put(CardName.Gold, 70);
//		priorityList.put(CardName.Market, 68);
//		// priorityList.put(CardName.Mine, 10); do not use
//		priorityList.put(CardName.Province, 90);
//		priorityList.put(CardName.Remodel, 66);
//		priorityList.put(CardName.Silver, 40);
//		priorityList.put(CardName.Smithy, 67);
//		priorityList.put(CardName.Village, 69);
//		// priorityList.put(CardName.Woodcutter, 10); do not use
//		priorityList.put(CardName.Workshop, 10); // do not use
//		
//		//@author Lukas, get the correct order for a prioList
//		List<CardName> list = priorityList.keySet()
//		.stream()
//		.sorted((c1, c2) -> Integer.compare(priorityList.get(c2), priorityList.get(c1)))
//		.collect(Collectors.toList());
//		
//		for(CardName name: list){
//		System.out.println(name.toString());
//	}
//		
//		//@author Lukas, get the correct order for a prioList
//		LinkedList<CardName> currentPrioList = new LinkedList<CardName>();
//		currentPrioList.addAll(priorityList.keySet());
//		currentPrioList.sort((c1, c2) -> Integer.compare(priorityList.get(c2), priorityList.get(c1)));
//		
//		for(CardName name: currentPrioList){
//			System.out.println(name.toString());
//		}
		
		testUpdateGame_Message();
		
	}
	
	private static void testInteraction_Message(){
		
		Interaction_Message imsg = new Interaction_Message();
		
//		//EndOfTurn simulation
//		Interaction interaction = Interaction.EndOfTurn;
//		CardName EOTCard = CardName.Duchy;
//		imsg.setInteractionType(interaction);
//		imsg.setDiscardCard(EOTCard);
		
		//Cellar simulation
		Interaction interaction = Interaction.Cellar;
		LinkedList<CardName> discardCards = new LinkedList<CardName>();
		discardCards.add(CardName.Copper);
		discardCards.add(CardName.Copper);
		discardCards.add(CardName.Mine);
		imsg.setInteractionType(Interaction.Cellar);
		imsg.setCellarDiscardCards(discardCards);
		
//		//Workshop simulation
//		Interaction interaction = Interaction.Workshop;
//		CardName workshopChoice = CardName.Market;
//		imsg.setInteractionType(interaction);
//		imsg.setWorkshopChoice(workshopChoice);
		
//		//Remodel1 simulation
//		Interaction interaction = Interaction.Remodel1;
//		CardName disposedCard = CardName.Woodcutter;
//		imsg.setInteractionType(interaction);
//		imsg.setDisposeCard(disposedCard);
		
//		//Remodel2 simulation
//		Interaction interaction = Interaction.Remodel2;
//		CardName remodelChoice = CardName.Province;
//		imsg.setInteractionType(interaction);
//		imsg.setRemodelChoice(remodelChoice);
		
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
