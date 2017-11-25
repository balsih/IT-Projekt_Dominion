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
	private HashMap<CardName, Integer> prioListForRemodel = new HashMap<CardName, Integer>();
	private HashMap<CardName, Integer> maxCardsOfAType = new HashMap<CardName, Integer>();
	private ArrayList<CardName> cardNamesOfActionCards = new ArrayList<CardName>();
	private static final ArrayList<String> NAMES = new ArrayList<String>();
	private static final double SHARE_OF_TREASURE_CARDS = 0.35;
	private static final int MIN_TIME_BEFORE_EXECUTING = 1000, MAX_TIME_BEFORE_EXECUTING = 3000;
	private static final int MAX_TREASURE_CARDS = 7, MAX_ACTION_CARDS = 5;
	private double numberOfTreasureCards = 0.0, numberOfTotalCards = 10.0;
	private int numberOfActionCards = 0;
	private CardName cardToPlay, cardToBuy;
	private boolean buyOneMore = false, playOneMore = false;

	public Bot(String name) {
		super(name);
		prioListForBuying1.put(CardName.Cellar, 58);
		prioListForBuying1.put(CardName.Duchy, 50);
		prioListForBuying1.put(CardName.Estate, 10);
		prioListForBuying1.put(CardName.Gold, 70);
		prioListForBuying1.put(CardName.Market, 68);
		prioListForBuying1.put(CardName.Mine, 62);
		prioListForBuying1.put(CardName.Province, 90);
		prioListForBuying1.put(CardName.Remodel, 60);
		prioListForBuying1.put(CardName.Silver, 40);
		prioListForBuying1.put(CardName.Smithy, 64);
		prioListForBuying1.put(CardName.Village, 66);
		// prioListForBuying1.put(CardName.Woodcutter, 10); do not use
		prioListForBuying1.put(CardName.Workshop, 58);

		prioListForBuying2.put(CardName.Cellar, 10);
		prioListForBuying2.put(CardName.Duchy, 40);
		prioListForBuying2.put(CardName.Estate, 10);
		prioListForBuying2.put(CardName.Gold, 50);
		prioListForBuying2.put(CardName.Market, 76);
		prioListForBuying2.put(CardName.Mine, 72);
		prioListForBuying2.put(CardName.Province, 60);
		prioListForBuying2.put(CardName.Remodel, 56);
		prioListForBuying2.put(CardName.Silver, 70);
		prioListForBuying2.put(CardName.Smithy, 74);
		prioListForBuying2.put(CardName.Village, 78);
		// prioListForBuying2.put(CardName.Woodcutter, 10); do not use
		prioListForBuying2.put(CardName.Workshop, 58);

		prioListForPlaying.put(CardName.Cellar, 30);
		prioListForPlaying.put(CardName.Market, 90);
		prioListForPlaying.put(CardName.Mine, 60);
		prioListForPlaying.put(CardName.Remodel, 40);
		prioListForPlaying.put(CardName.Smithy, 70);
		prioListForPlaying.put(CardName.Village, 80);
		// prioListForPlaying.put(CardName.Woodcutter, 20); do not use
		prioListForPlaying.put(CardName.Workshop, 50);

		prioListForRemodel.put(CardName.Cellar, 10);
		prioListForRemodel.put(CardName.Copper, 20);
		prioListForRemodel.put(CardName.Duchy, 12);
		prioListForRemodel.put(CardName.Estate, 11);
		prioListForRemodel.put(CardName.Gold, 62);
		prioListForRemodel.put(CardName.Market, 99);
		prioListForRemodel.put(CardName.Mine, 92);
		prioListForRemodel.put(CardName.Province, 10);
		prioListForRemodel.put(CardName.Remodel, 89);
		prioListForRemodel.put(CardName.Silver, 61);
		prioListForRemodel.put(CardName.Smithy, 94);
		prioListForRemodel.put(CardName.Village, 97);
		// prioListForRemodel.put(CardName.Woodcutter, 87); do not use
		prioListForRemodel.put(CardName.Workshop, 85);

		cardNamesOfActionCards.add(CardName.Cellar);
		cardNamesOfActionCards.add(CardName.Market);
		cardNamesOfActionCards.add(CardName.Mine);
		cardNamesOfActionCards.add(CardName.Smithy);
		cardNamesOfActionCards.add(CardName.Workshop);
		cardNamesOfActionCards.add(CardName.Village);
		// cardname.add(CardName.Woodcutter); do not use

		maxCardsOfAType.put(CardName.Market, 2);
		maxCardsOfAType.put(CardName.Smithy, 1);
		maxCardsOfAType.put(CardName.Village, 1);
		maxCardsOfAType.put(CardName.Mine, 1);
		maxCardsOfAType.put(CardName.Cellar, 1);
		maxCardsOfAType.put(CardName.Workshop, 1);
		// maxCardsOfAType.put(CardName.Woodcutter, 1); do not use
	}

	/**
	 * Executes the Bot with all its stages play and buy.
	 */
	public void execute() {
		if (actions > 0 && actualPhase == Phase.Action) {
			do {
				estimatePlayPriorityOfActionCards();
				playActionCards();
			} while (actions > 0 && playOneMore == true && actualPhase == Phase.Action);
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
		if (cardToPlay.equals(CardName.Mine))
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

		// überprüfung
		playOneMore = true;
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
		// one ore more buys possible?
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
				numberOfTotalCards++;
				succeededBuy = true;
				if (cardToBuy.equals(CardName.Gold) || cardToBuy.equals(CardName.Silver))
					numberOfTreasureCards++;
				for (int i = 0; i < cardNamesOfActionCards.size(); i++) {
					if (cardToBuy.equals(cardNamesOfActionCards.get(i)))
						;
					numberOfActionCards++;
				}
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
		// calculate gameStage number
		int gameStage = 0;
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
			if (numberOfTreasureCards / numberOfTotalCards < SHARE_OF_TREASURE_CARDS) {
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
	 * Calculates or removes the priority of each ActionCards for the buying decision.
	 */
	private void estimateBuyPriorityOfActionCards() {
		// remove an ActionCard from the prioListForBuying 1 and 2 if the ActionCard
		// reached his maximum
		for (int i = 0; i < cardNamesOfActionCards.size(); i++) {
			if (getNumberOfOwnedCards(cardNamesOfActionCards.get(i)) == maxCardsOfAType
					.get(cardNamesOfActionCards.get(i))) {
				prioListForBuying1.remove(cardNamesOfActionCards.get(i));
				prioListForBuying2.remove(cardNamesOfActionCards.get(i));
			}
		}
		// remove the RemodelCard from the prioListForBuying 1 and 2 if the RemodelCard
		// reached his maximum
		int max_remodel_cards = 0;
		if (getNumberOfOwnedCards(CardName.Copper) > 5) {
			max_remodel_cards = 1;
		}
		if (getNumberOfOwnedCards(CardName.Remodel) >= max_remodel_cards)
			prioListForBuying1.remove(CardName.Remodel);
			prioListForBuying2.remove(CardName.Remodel);
	}
	
	// more Code (was ist wichtiger und was wenn das maximum an action karten erreicht wurde?)

	private void estimatePlayPriorityOfActionCards() {

		// to-do --> cellar, remodel, mine are special!
		// market, village sind immer gleich
		// smithy, workshop unbekannt
		prioListForPlaying.put(CardName.Cellar, 30);
		prioListForPlaying.put(CardName.Market, 90);
		prioListForPlaying.put(CardName.Mine, 60);
		prioListForPlaying.put(CardName.Remodel, 40);
		prioListForPlaying.put(CardName.Smithy, 70);
		prioListForPlaying.put(CardName.Village, 80);
		// prioListForPlaying.put(CardName.Woodcutter, 20); do not use
		prioListForPlaying.put(CardName.Workshop, 50);
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