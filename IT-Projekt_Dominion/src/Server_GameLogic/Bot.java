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

public class Bot extends Player {
	private HashMap<CardName, Integer> priorityList = new HashMap<CardName, Integer>();
	private static final ArrayList<String> NAMES = new ArrayList<String>();
	private static final int MIN_TIME_BEFORE_EXECUTING = 1000, MAX_TIME_BEFORE_EXECUTING = 3000;
	private static final double SHARE_OF_TREASURE_CARDS = 0.35;
	private static final int MIN_CARDS_PER_DECKPILE = 3;
	private double numberOfTreasureCards = 7.0;
	private double numberOfCards = 10.0;
	private int indexToPlay;
	private CardName cardToPlay;
	private CardName cardToBuy;
	private boolean buyOneMore;
	private boolean moreVictoryCards = false;
	private boolean moreTreasureCards = true;
	// reduce class variable where possible

	public Bot(String name) {
		super(name);
	}

	/**
	 * executes the Bot with all its stages play and buy
	 */
	public void execute() {

		// preparation
		initializePriorityList();

		// play
		// still under construction
		while (actions > 0) {
			calculatePlay();
			play(cardToPlay, indexToPlay);
			makeBreak();
			buyOneMore = true;
		}
		// buy
		// still under construction
		while (buys > 0 && buyOneMore == true) {
			estimateGamePhase();
			playTreasureCards();
			calculateBuy();
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
			}
		}
	}

	private void calculatePlay() {
		int tempPriority = 0;
		for (Card card : handCards) {
			if (card.getType().equals(CardType.Action)) {
				if (tempPriority < priorityList.get(card.getCardName())) {
					tempPriority = priorityList.get(card.getCardName());
					// add messages --> if play not possible --> try second best prio
				}
			}
		}

	}

	private void calculateBuy() {
		boolean succeededBuy = false;
		
		// still under work
		// cardToBuy = ;
		buy(cardToBuy);

	}

	/**
	 * Sets the start priorities for all used card of the bot.
	 */
	private void initializePriorityList() {
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
			priorityList.replace(CardName.Duchy, 100);
			priorityList.replace(CardName.Province, 100);
		} // no else because this method will only be executed at the end of a game
	}

	/**
	 * changes the Priority of GoldCard and SilverCard
	 */
	private void changePriorityTreasureCards() {
		if (moreTreasureCards == true) {
			priorityList.replace(CardName.Gold, 80);
			priorityList.replace(CardName.Silver, 70);
		} else {
			priorityList.replace(CardName.Gold, 70);
			priorityList.replace(CardName.Silver, 40);
		}
	}
}