package Messages;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

import Cards.Card;
import Cards.Market_Card;
import Cards.Mine_Card;
import Cards.Smithy_Card;
import Cards.Village_Card;
import Cards.Woodcutter_Card;

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
		
		String cardBuyed = "smithy";
		
		String chat = "how are you?";
		
		LinkedList<Card> handCards = new LinkedList<Card>();
		handCards.add(new Smithy_Card());
		handCards.add(new Mine_Card());
		handCards.add(new Market_Card());
		
		LinkedList<Card> playedCards = new LinkedList<Card>();
		playedCards.add(new Village_Card());
		playedCards.add(new Woodcutter_Card());
		
		
		UpdateGame_Message ugmsg = new UpdateGame_Message();
		ugmsg.setLog(log);
		ugmsg.setCurrentPlayer(currentPlayer);
		ugmsg.setCoins(coins);
		ugmsg.setActions(actions);
		ugmsg.setBuys(buys);
		ugmsg.setCurrentPhase(currentPhase);
		ugmsg.setDiscardPile(discardPileTopCard);
		ugmsg.setDiscardPileCardNumber(discardPileCardNumber);
		ugmsg.setDeckPileCardNumber(deckPileCardNumber);
		ugmsg.setCardBuyed(cardBuyed);
		ugmsg.setChat(chat);
		ugmsg.setHandCards(handCards);
		ugmsg.setPlayedCards(playedCards);
		
		ugmsg.toString();
		System.out.println(ugmsg);
	}
	
	private static void testCreateGame_Message(){
		
		String opponent = "mordrag";
		
		HashMap<String, Integer> buyCards = new HashMap<String, Integer>();
		buyCards.put("duchy", 5);
		buyCards.put("smithy", 12);
		
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
