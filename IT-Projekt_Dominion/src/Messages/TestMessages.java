package Messages;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;

import Cards.Card;
import Cards.CardName;
import Cards.Market_Card;
import Cards.Mine_Card;
import Cards.Smithy_Card;
import Cards.Village_Card;
import Cards.Woodcutter_Card;
import Client_Services.ServiceLocator;

public class TestMessages {

	public static void main(String[] args) {
		testUpdateGame_Message();
		
	}

	
	private static void testUpdateGame_Message(){
		
		String log = "testLog";
		
		String currentPlayer = "Lukas";
		
		Integer coins = 5;
		
		Integer actions = 2;
		
		Integer buys = 3;
		
		String currentPhase = "actionPhase";
		
		String discardPileTopCard = "gold";
		Integer discardPileCardNumber = 13;
		
		Integer deckPileCardNumber = 21;
		
		String buyedCard = CardName.Gold.toString();
		
		String chat = "how are you?";
		
		String playedCard = "newSmithyCard";
		
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
		
		cgmsg.toString();
		System.out.println(cgmsg);
		
	}

}
