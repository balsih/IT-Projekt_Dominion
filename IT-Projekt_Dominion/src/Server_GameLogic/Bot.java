package Server_GameLogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import Cards.Card;
import Cards.CardName;
import Cards.CardType;
import Messages.Message;
import Messages.UpdateGame_Message;

/**
 * @author Simon
 * @version 1.0
 * @created 15-Nov-2017 08:36:00
 * @lastEdited 25-Nov-2017 08:36:00
 */

// every method which is just used once by another method --> extract method
// reduce class variables where possible
// UpdateGame_Message ugmsg = (UpdateGame_Message) m;

public class Bot extends Player {
	private HashMap<CardName, Integer> prioListForBuying1 = new HashMap<CardName, Integer>();
	private HashMap<CardName, Integer> prioListForBuying2 = new HashMap<CardName, Integer>();
	private HashMap<CardName, Integer> prioListForPlaying = new HashMap<CardName, Integer>();
	private HashMap<CardName, Integer> prioListOfHandcards = new HashMap<CardName, Integer>();
	private static final ArrayList<String> NAMES = new ArrayList<String>();
	private static final int MIN_TIME_BEFORE_EXECUTING = 1000, MAX_TIME_BEFORE_EXECUTING = 3000, MAX_TREASURE_CARDS = 8;
	private static final double SHARE_OF_TREASURE_CARDS = 0.35;
	// private static final int MAX_CELLAR_CARDS, MAX_WOOKCUTER_CARDS ,
	// MAX_WORKSHOP_CARDS;
	private static final int MAX_MARKET_CARDS = 2, MAX_SMITHY_CARDS = 1, MAX_VILLAGE_CARDS = 1, MAX_MINE_CARDS = 1;
	private double numberOfTreasureCards, numberOfCards = 10.0;
	private CardName cardToPlay, cardToBuy;
	private boolean buyOneMore = false;

	public Bot(String name) {
		super(name);
		// prioListForBuying1.put(CardName.Cellar, 10); do not use
		// prioListForBuying1.put(CardName.Copper, 20); do not buy CooperCards
		prioListForBuying1.put(CardName.Duchy, 50);
		prioListForBuying1.put(CardName.Estate, 10);
		prioListForBuying1.put(CardName.Gold, 70);
		prioListForBuying1.put(CardName.Market, 66);
		prioListForBuying1.put(CardName.Mine, 62);
		prioListForBuying1.put(CardName.Province, 90);
		// prioListForBuying1.put(CardName.Remodel, 60);
		prioListForBuying1.put(CardName.Silver, 40);
		prioListForBuying1.put(CardName.Smithy, 64);
		prioListForBuying1.put(CardName.Village, 68);
		// prioListForBuying1.put(CardName.Woodcutter, 10); do not use
		// prioListForBuying1.put(CardName.Workshop, 10); do not use

		// prioListForBuying2.put(CardName.Cellar, 10); do not use
		// prioListForBuying2.put(CardName.Copper, 20); do not buy CooperCards
		prioListForBuying2.put(CardName.Duchy, 40);
		prioListForBuying2.put(CardName.Estate, 10);
		prioListForBuying2.put(CardName.Gold, 50);
		prioListForBuying2.put(CardName.Market, 76);
		prioListForBuying2.put(CardName.Mine, 72);
		prioListForBuying2.put(CardName.Province, 60);
		// prioListForBuying2.put(CardName.Remodel, 60);
		prioListForBuying2.put(CardName.Silver, 70);
		prioListForBuying2.put(CardName.Smithy, 74);
		prioListForBuying2.put(CardName.Village, 78);
		// prioListForBuying2.put(CardName.Woodcutter, 10); do not use
		// prioListForBuying2.put(CardName.Workshop, 10); do not use

		// prioListForPlaying.put(CardName.Cellar, 30); do not use
		prioListForPlaying.put(CardName.Market, 90);
		prioListForPlaying.put(CardName.Mine, 60);
		prioListForPlaying.put(CardName.Remodel, 50);
		prioListForPlaying.put(CardName.Smithy, 70);
		prioListForPlaying.put(CardName.Village, 80);
		// prioListForPlaying.put(CardName.Woodcutter, 20); do not use
		// prioListForPlaying.put(CardName.Workshop, 50); do not use

		// prioListOfHandcards.put(CardName.Cellar, 10); do not use
		// prioListOfHandcards.put(CardName.Copper, 20); do not buy CooperCards
		prioListOfHandcards.put(CardName.Duchy, 12);
		prioListOfHandcards.put(CardName.Estate, 11);
		prioListOfHandcards.put(CardName.Gold, 62);
		prioListOfHandcards.put(CardName.Market, 99);
		prioListOfHandcards.put(CardName.Mine, 92);
		prioListOfHandcards.put(CardName.Province, 10);
		prioListOfHandcards.put(CardName.Remodel, 89);
		prioListOfHandcards.put(CardName.Silver, 61);
		prioListOfHandcards.put(CardName.Smithy, 94);
		prioListOfHandcards.put(CardName.Village, 97);
		// prioListOfHandcards.put(CardName.Woodcutter, 87); do not use
		// prioListOfHandcards.put(CardName.Workshop, 85); do not use
	}

	/**
	 * Executes the Bot with all its stages play and buy.
	 */
	public void execute() {
		while (actions > 0 && actualPhase == Phase.Action) {
			playActionCards();
		}
		if (buys > 0 && actualPhase == Phase.Buy) {
			playTreasureCards();
			do {
				estimateBuyPriorityOfVictoryCards();
				estimateBuyPriorityOfTreasureCards();
				estimateBuyPriorityOfActionCards();
				buy();
			} while (buys > 0 && buyOneMore == true && actualPhase == Phase.Buy);
		}
	}

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
	 * Calculates which ActionCard is best to play.
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
		if(cardToPlay.equals(CardName.Mine))
			// fehlender Code
			// numberOfTreasureCards++;
		makeBreak();
		// do something with cards
		switch (cardToPlay) {
		// case Cellar: do not use
		// break;
		case Remodel:
			break;
		// case Workshop: do not use
		// break;
		default:
			break;
		}
		cardToPlay = null;
	}

	/**
	 * Calculates which card has to be bought and checks if a second buy would make
	 * sense.
	 */
	private void buy() {
		Boolean succeededBuy = false;
		List<CardName> list1 = prioListForBuying1.keySet().stream()
				.sorted((s1, s2) -> Integer.compare(prioListForBuying1.get(s2), prioListForBuying1.get(s1)))
				.collect(Collectors.toList());
		List<CardName> list2 = prioListForBuying2.keySet().stream()
				.sorted((s1, s2) -> Integer.compare(prioListForBuying1.get(s2), prioListForBuying1.get(s1)))
				.collect(Collectors.toList());
		List<CardName> list;
		if (buys >= 2) {
			list = list2;
		} else {
			list = list1;
		}
		for (int indexCounter = 0; indexCounter < list.size(); indexCounter++) {
			cardToBuy = list.get(indexCounter);
			buy(cardToBuy);
			Message m = buy(cardToBuy);
			if (m instanceof UpdateGame_Message) {
				numberOfCards++;
				succeededBuy = true;
				if (cardToBuy.equals(CardName.Gold) || cardToBuy.equals(CardName.Silver))
					numberOfTreasureCards++;
				makeBreak();
				break;
			} else {
				succeededBuy = false;
				cardToBuy = null;
				continue;
			}
		}
		// one more buy?
		if (buys > 0 && succeededBuy == true) {
			if (coins >= 2)
				buyOneMore = true;
		} else {
			buyOneMore = false;
			makeBreak();
			skipPhase();
		}
	}

	/**
	 * Fills a name-list with entries, chooses one and gives it back. public static
	 */
	public static String getNameOfBot() {
		NAMES.add("Bodo");
		NAMES.add("Lukas");
		NAMES.add("Simon");
		NAMES.add("Adrian");
		NAMES.add("René");
		Random rand = new Random();
		String nameOfBot = NAMES.get(rand.nextInt(5));
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
	 * Checks the game status and changes slightly the priority of VictoryCards
	 * while game ending is arriving.
	 */
	private void estimateBuyPriorityOfVictoryCards() {
		int gameStage = 0;

		// calculate gameStage number
		if (game.getProvincePile().size() < 5)
			gameStage += 5;
		else if (game.getProvincePile().size() < 3)
			gameStage += 10;
		else if (game.getProvincePile().size() < 2)
			gameStage += 20;

		if (game.getDuchyPile().size() < 6)
			gameStage += 5;
		else if (game.getDuchyPile().size() < 3)
			gameStage += 10;
		else if (game.getDuchyPile().size() < 2)
			gameStage += 20;

		List<CardName> list0 = game.getBuyCards().keySet().stream().filter(c -> game.getBuyCards().containsValue(3))
				// PROBLEM: 3, 2 und 1 zählen... aber ohne obengenannte Stacks
				.collect(Collectors.toList());
		int lowStacks = list0.size();
		if (lowStacks == 3)
			gameStage += 20;
		else if (lowStacks >= 4)
			gameStage += 40;

		// changes of priorities according gameStage number
		int tempEstate = prioListForBuying1.get(CardName.Estate);
		int tempDuchy = prioListForBuying1.get(CardName.Duchy);
		int tempProvince = prioListForBuying1.get(CardName.Province);
		boolean done0 = true, done1 = true, done2 = true, done3 = true;
		if (gameStage >= 40 && done0) {
			tempEstate += 70;
			tempDuchy += 20;
			tempProvince += 10;
			prioListForBuying1.replace(CardName.Estate, tempEstate);
			prioListForBuying1.replace(CardName.Duchy, tempDuchy);
			prioListForBuying1.replace(CardName.Province, tempProvince);
			done0 = false;
		} else if (gameStage >= 30 && done1) {
			tempEstate += 10;
			tempDuchy += 10;
			tempProvince += 10;
			prioListForBuying1.replace(CardName.Estate, tempEstate);
			prioListForBuying1.replace(CardName.Duchy, tempDuchy);
			prioListForBuying1.replace(CardName.Province, tempProvince);
			done1 = false;
		} else if (gameStage >= 20 && done2) {
			tempDuchy += 10;
			tempProvince += 10;
			prioListForBuying1.replace(CardName.Duchy, tempDuchy);
			prioListForBuying1.replace(CardName.Province, tempProvince);
			done2 = false;
		} else if (gameStage >= 10 && done3) {
			tempDuchy += 10;
			tempProvince += 10;
			prioListForBuying1.replace(CardName.Duchy, tempDuchy);
			prioListForBuying1.replace(CardName.Province, tempProvince);
			done3 = false;
		}
	}

	/**
	 * Calculates the share of TreasureCards in relation to the total number of
	 * owned cards and changes the Priority of GoldCard and SilverCard.
	 * 
	 */
	private void estimateBuyPriorityOfTreasureCards() {
		if (numberOfTreasureCards >= MAX_TREASURE_CARDS) {
			prioListForBuying1.remove(CardName.Gold);
			prioListForBuying1.remove(CardName.Silver);
		} else {
			int tempGold = prioListForBuying1.get(CardName.Gold);
			int tempSilver = prioListForBuying1.get(CardName.Silver);
			if (numberOfTreasureCards / numberOfCards < SHARE_OF_TREASURE_CARDS) {
				tempGold += 10;
				tempSilver += 10;
				prioListForBuying1.replace(CardName.Gold, tempGold);
				prioListForBuying1.replace(CardName.Silver, tempSilver);
			} else {
				tempGold -= 10;
				tempSilver -= 10;
				prioListForBuying1.replace(CardName.Gold, tempGold);
				prioListForBuying1.replace(CardName.Silver, tempSilver);
			}
		}
	}

	/**
	 * Calculates the priority of each ActionCards for the buying decision.
	 */
	private void estimateBuyPriorityOfActionCards() {
		// prioListForBuying1.remove(CardName.Cellar); do not use
		// prioListForBuying1.put(CardName.Woodcutter); do not use
		// prioListForBuying1.put(CardName.Workshop); do not use
		if (getNumberOfOwnedCards(CardName.Market) >= MAX_MARKET_CARDS) {
			prioListForBuying1.remove(CardName.Market);
			prioListForBuying2.remove(CardName.Market);
		}
		if (getNumberOfOwnedCards(CardName.Mine) >= MAX_MINE_CARDS) {
			prioListForBuying1.remove(CardName.Mine);
			prioListForBuying2.remove(CardName.Mine);
		}
		if (getNumberOfOwnedCards(CardName.Smithy) >= MAX_SMITHY_CARDS) {
			prioListForBuying1.remove(CardName.Smithy);
			prioListForBuying2.remove(CardName.Smithy);
		}
		if (getNumberOfOwnedCards(CardName.Village) >= MAX_VILLAGE_CARDS) {
			prioListForBuying1.remove(CardName.Village);
			prioListForBuying2.remove(CardName.Village);
		}

		int max_remodel_cards = 0;
		if(getNumberOfOwnedCards(CardName.Copper) > 10)
			max_remodel_cards = 2;
		
		if (getNumberOfOwnedCards(CardName.Remodel) >= max_remodel_cards)
			prioListForBuying1.remove(CardName.Remodel);

	}

	/**
	 * Calculates who many cards are owned by the Bot of a specific card and returns
	 * the number as an Integer.
	 * 
	 * @param cardname
	 * @return
	 */
	private int getNumberOfOwnedCards(CardName cardname) {
		int numberOfOwnedCards = 0;
		for (Card card : handCards) {
			if (card.getCardName().equals(cardname))
				numberOfOwnedCards++;
		}
		for (Card card : playedCards) {
			if (card.getCardName().equals(cardname))
				numberOfOwnedCards++;
		}
		for (Card card : discardPile) {
			if (card.getCardName().equals(cardname))
				numberOfOwnedCards++;
		}
		for (Card card : deckPile) {
			if (card.getCardName().equals(cardname))
				numberOfOwnedCards++;
		}
		return numberOfOwnedCards;
	}
}