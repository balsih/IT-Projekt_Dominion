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
	private static final int MIN_CARDS_PER_DECKPILE = 3;
	private double numberOfTreasureCards = 7.0;
	private double numberOfCards = 10.0;
	private int numberOfActionCards;
	private int indexToPlay;
	private CardName cardToPlay;
	private CardName cardToBuy;
	private boolean buyOneMore = false;
	private boolean moreVictoryCards = false;
	private boolean moreTreasureCards = true;
	// reduce class variables where possible

	public Bot(String name) {
		super(name);
	}

	/**
	 * executes the Bot with all its stages play and buy
	 */
	public void execute() {
		// preparation
		initializePrioListForBuying();
		initializePrioListForPlaying();
		// action phase
		startMove();
		calculateNumOfActionCards();
		while (actions > 0 && numberOfActionCards > 0 && actualPhase == Phase.Action) {
			playActionCards();
		}
		// buy phase --> STILL UNDER CONSTRUCTION (Verschachtelung noch unbekannt)
		if (buys > 0 && actualPhase == Phase.Buy) {
			playTreasureCards();
			do {
				// zuerst Kontrollen durchführen --> Änderungen der Prios vornehmen, bevor Karten gekauft werden!
				// nach einem Einkauf sofort nochmals durchführen
				estimateGamePhase();
				estimateShareOfTreasureCards();
				buy();
				makeBreak();
			} while (buys > 0 && buyOneMore == true);
		}
	} // end of execute method

	/**
	 * Calculate how many ActionCards are among all hand cards.
	 */
	private void calculateNumOfActionCards() {
		for (Card card : handCards) {
			if (card.getType().equals(CardType.Action))
				numberOfActionCards++;
		}
	}

	/**
	 * Plays all the available TreasureCards which are in the hands of the bot,
	 * before buying cards.
	 */
	private void playTreasureCards() {
		for (Card card : handCards) {
			if (card.getType().equals(CardType.Treasure)) {
				indexToPlay = handCards.indexOf(card);
				cardToPlay = handCards.get(indexToPlay).getCardName();
				play(cardToPlay, indexToPlay);
				makeBreak();
			}
		}
		indexToPlay = 0;
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
					indexToPlay = handCards.indexOf(card);
					cardToPlay = handCards.get(indexToPlay).getCardName();
				}
			}
		}
		if (cardToPlay != null && indexToPlay != 0) {
			play(cardToPlay, indexToPlay);
			makeBreak();
			// do something with card --> check packages Cards
			calculateNumOfActionCards();
			indexToPlay = 0;
			cardToPlay = null;
		} else {
			makeBreak();
			skipPhase();
		}
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
		
		//else
		makeBreak();
		skipPhase();
	}

	/**
	 * Sets the start priorities for all used card of the bot.
	 */
	private void initializePrioListForBuying() {
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
	}

	/**
	 * Sets the start priorities for all used card of the bot.
	 */
	private void initializePrioListForPlaying() {
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
	 * Checks if the game is near its end and sets a boolean value.
	 */
	private void estimateGamePhase() {
		Stack<Province_Card> temp = Game.getProvincePile();
		if (temp.size() < MIN_CARDS_PER_DECKPILE) {
			moreVictoryCards = true;
		} else {
			moreVictoryCards = false;
		}
	}

	// alle Methoden unterhalb sind noch zu programmieren, Verschachtelung noch nicht definitiv
	// wahrscheinlich alle "change-methoden" auflösen und den "estimate-methoden" direkt hinzufügen
	
	/**
	 * Calculates the share of TreasureCards in relation to the total number of
	 * owned cards and sets a boolean value.
	 * 
	 */
	private void estimateShareOfTreasureCards() {
		if (numberOfTreasureCards / numberOfCards < SHARE_OF_TREASURE_CARDS) {
			moreTreasureCards = true;
		} else {
			moreTreasureCards = false;
		}
	}

	/**
	 * changes the Priority of all VictoryCards
	 */
	private void changePriorityVictoryCards() {
		if (moreVictoryCards == true) {
			prioListForBuying.replace(CardName.Duchy, 100);
			prioListForBuying.replace(CardName.Province, 100);
		} // no else because this method will only be executed at the end of a game
	}

	/**
	 * changes the Priority of GoldCard and SilverCard
	 */
	private void changePriorityTreasureCards() {
		if (moreTreasureCards == true) {
			prioListForBuying.replace(CardName.Gold, 80);
			prioListForBuying.replace(CardName.Silver, 70);
		} else {
			prioListForBuying.replace(CardName.Gold, 70);
			prioListForBuying.replace(CardName.Silver, 40);
		}
	}
}