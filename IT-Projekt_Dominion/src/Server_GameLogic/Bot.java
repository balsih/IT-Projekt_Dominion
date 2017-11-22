package Server_GameLogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;

import Cards.Card;
import Cards.CardName;
import Cards.CardType;
import Cards.Province_Card;

/**
 * @author Simon
 * @version 1.0
 * @created 15-Nov-2017 08:36:00
 * @lastEdited 15-Nov-2017 08:36:00
 */

// every method which is just used once by another method --> extract method

public class Bot extends Player {
	private HashMap<CardName, Integer> prioListForBuying = new HashMap<CardName, Integer>();
	private HashMap<CardName, Integer> prioListForPlaying = new HashMap<CardName, Integer>();
	private static final ArrayList<String> NAMES = new ArrayList<String>();
	private static final int MIN_TIME_BEFORE_EXECUTING = 1000, MAX_TIME_BEFORE_EXECUTING = 3000;
	private static final double SHARE_OF_TREASURE_CARDS = 0.35;
	private int numberOfCooperCards, numberOfSilverCards, numberOfGoldCards;
	private static final int MIN_CARDS_PER_DECKPILE = 3;
	private double numberOfTreasureCards = 7.0;
	private double numberOfCards = 10.0;
	private CardName cardToPlay;
	private CardName cardToBuy;
	private boolean buyOneMore = false;
	private int gameStage;
	private int numOfStacksWithLessThanFourCards;
	private boolean moreTreasureCards = true;
	// reduce class variables where possible

	public Bot(String name) {
		super(name);
				// prioListForBuying.put(CardName.Cellar, 10); do not use but implement first if
				// enough time
				// prioListForBuying.put(CardName.Copper, 20); do not buy CooperCards
				prioListForBuying.put(CardName.Duchy, 50);
				prioListForBuying.put(CardName.Estate, 10);
				prioListForBuying.put(CardName.Gold, 70);
				prioListForBuying.put(CardName.Market, 68);
				// prioListForBuying.put(CardName.Mine, 10); do not use
				prioListForBuying.put(CardName.Province, 90);
				prioListForBuying.put(CardName.Remodel, 66);
				prioListForBuying.put(CardName.Silver, 40);
				prioListForBuying.put(CardName.Smithy, 67);
				prioListForBuying.put(CardName.Village, 69);
				// prioListForBuying.put(CardName.Woodcutter, 10); do not use
				// priorityList.put(CardName.Workshop, 10); do not use
				
				// prioListForPlaying.put(CardName.Cellar, 30); do not use but implement first
				// if enough time
				prioListForPlaying.put(CardName.Market, 80);
				// prioListForPlaying.put(CardName.Mine, 40); do not use
				prioListForPlaying.put(CardName.Remodel, 60);
				prioListForPlaying.put(CardName.Smithy, 70);
				prioListForPlaying.put(CardName.Village, 90);
				// prioListForPlaying.put(CardName.Woodcutter, 20); do not use
				// prioListForPlaying.put(CardName.Workshop, 50); do not use
	}

	/**
	 * executes the Bot with all its stages play and buy
	 */
	public void execute() {
		// action phase
		while (actions > 0 && actualPhase == Phase.Action) {
			playActionCards();
		}
		// buy phase --> STILL UNDER CONSTRUCTION (Verschachtelung noch unbekannt)
		if (buys > 0 && actualPhase == Phase.Buy) {
			playTreasureCards();
			do {
				// zuerst Kontrollen durchführen --> Änderungen der Prios vornehmen, bevor
				// Karten gekauft werden!
				// nach einem Einkauf sofort nochmals durchführen
				estimatePriorityOfVictoryCards();
				estimateShareOfTreasureCards();
				buy();
				makeBreak();
			} while (buys > 0 && buyOneMore == true);
		}
	} // end of execute method

	/**
	 * Plays all the available TreasureCards which are in the hands of the bot,
	 * before buying cards.
	 */
	private void playTreasureCards() {
		for (Card card : handCards) {
			if (card.getType().equals(CardType.Treasure)) {
				cardToPlay = card.getCardName();
				play(cardToPlay);
				makeBreak();
			}
		}
		cardToPlay = null;
	}

	/**
	 * Calculates which card is the best to play.
	 */
	private void playActionCards() {
		int tempPriority = 0;
		for (Card card : handCards) {
			if (card.getType().equals(CardType.Action)) {
				if (tempPriority < prioListForPlaying.get(card.getCardName())) {
					tempPriority = prioListForPlaying.get(card.getCardName());
					cardToPlay = card.getCardName();
				}
			}
		}
		play(cardToPlay);
		makeBreak();
		// do something with card --> check packages Cards
		cardToPlay = null;
	}

	// STILL UNDER CONSTRUCTION...
	/**
	 * Calculates which card has to be bought and checks if a second buy would make
	 * sense.
	 */
	private void buy() {
		boolean succeededBuy = false;
		boolean possibleGoodCard = false;
		// still under work
		// add messages --> if buy not possible --> try second best prio

		// cardToBuy =
		buy(cardToBuy);
		// valid play? --> ugmsg / failed play --> fmsg

		makeBreak();

		// next buy?
		if (buys > 0) {

			if (possibleGoodCard == true)
				buyOneMore = true;
		} else {
			buyOneMore = false;
		}
		// valid play? --> ugmsg / failed play --> fmsg

		// else
		makeBreak();
		skipPhase();
	}

	/**
	 * Fills a name-list with entries, chooses one and gives it back. public static
	 */
	String getNameOfBot() {
		NAMES.add("Philipp");
		NAMES.add("Simon");
		NAMES.add("Alexa");
		NAMES.add("Google");
		Random rand = new Random();
		String nameOfBot = NAMES.get(rand.nextInt(4));
		return nameOfBot;
	}

	/**
	 * wait for a few milliseconds before executing the next step
	 * 
	 * @throws InterruptedException
	 */
	private static void makeBreak() {
		Random rand = new Random();
		int timeToWait = rand.nextInt(MAX_TIME_BEFORE_EXECUTING - MIN_TIME_BEFORE_EXECUTING)
				+ MIN_TIME_BEFORE_EXECUTING;
		try {
			Thread.sleep(timeToWait);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks the game status and changes the priority of VictoryCards if game
	 * ending is close.
	 */
	private void estimatePriorityOfVictoryCards() {
		
		if(game.getProvincePile().size() <= 3)
			gameStage = 99;
//		if (temp.size() <= MIN_CARDS_PER_DECKPILE) {
//		}
		
		// PROBLEM HERE!
		// game.getBuyCards().keySet().stream().filter(game.buyCards.containsValue(3));
		
		if(gameStage >= 60)
			prioListForBuying.replace(CardName.Duchy, 99);
			prioListForBuying.replace(CardName.Province, 100);
		if(gameStage == 99) {
			prioListForBuying.replace(CardName.Duchy, 99);
			prioListForBuying.replace(CardName.Province, 100);
		}
	}
	
	/**
	 * Calculates the share of TreasureCards in relation to the total number of
	 * owned cards and changes the Priority of GoldCard and SilverCard.
	 * 
	 */
	private void estimateShareOfTreasureCards() {
		if (numberOfTreasureCards / numberOfCards < SHARE_OF_TREASURE_CARDS) {
			moreTreasureCards = true;
		} else {
			moreTreasureCards = false;
		}
		if (moreTreasureCards == true) {
			prioListForBuying.replace(CardName.Gold, 80);
			prioListForBuying.replace(CardName.Silver, 70);
		} else {
			prioListForBuying.replace(CardName.Gold, 70);
			prioListForBuying.replace(CardName.Silver, 40);
		}
	}
}