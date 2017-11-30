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

public class Bot extends Player implements Runnable {
	private HashMap<CardName, Integer> prioListForBuying1 = new HashMap<CardName, Integer>();
	private HashMap<CardName, Integer> prioListForBuying2 = new HashMap<CardName, Integer>();
	private HashMap<CardName, Integer> prioListForPlaying = new HashMap<CardName, Integer>();
	private HashMap<CardName, Integer> prioListForRemodel = new HashMap<CardName, Integer>();
	private HashMap<CardName, Integer> maxCardsOfAType = new HashMap<CardName, Integer>();
	private ArrayList<CardName> cardNamesOfActionCards = new ArrayList<CardName>();
	private static final ArrayList<String> NAMES = new ArrayList<String>();
	private static final double SHARE_OF_TREASURE_CARDS = 0.35;
	private static final int MIN_TIME_BEFORE_EXECUTING = 1000, MAX_TIME_BEFORE_EXECUTING = 3000;
	private static final int MAX_TREASURE_CARDS = 7, MAX_ACTION_CARDS = 10;
	private double numberOfGoldAndSilverCards = 0.0, numberOfTotalCards = 10.0;
	private int numberOfActionCards = 0;
	private Card cardToPlay;
	private CardName cardToBuy;
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
		prioListForBuying1.put(CardName.Woodcutter, 20);
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
		prioListForBuying2.put(CardName.Woodcutter, 20);
		prioListForBuying2.put(CardName.Workshop, 58);

		prioListForPlaying.put(CardName.Cellar, 30);
		prioListForPlaying.put(CardName.Market, 90);
		prioListForPlaying.put(CardName.Mine, 50);
		prioListForPlaying.put(CardName.Remodel, 40);
		prioListForPlaying.put(CardName.Smithy, 66);
		prioListForPlaying.put(CardName.Village, 80);
		prioListForPlaying.put(CardName.Woodcutter, 20);
		prioListForPlaying.put(CardName.Workshop, 60);

		// gewisse Karten rausnehmen
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
		prioListForRemodel.put(CardName.Woodcutter, 87);
		prioListForRemodel.put(CardName.Workshop, 85);

		cardNamesOfActionCards.add(CardName.Cellar);
		cardNamesOfActionCards.add(CardName.Market);
		cardNamesOfActionCards.add(CardName.Mine);
		cardNamesOfActionCards.add(CardName.Smithy);
		cardNamesOfActionCards.add(CardName.Workshop);
		cardNamesOfActionCards.add(CardName.Village);
		cardNamesOfActionCards.add(CardName.Remodel);
		cardNamesOfActionCards.add(CardName.Woodcutter);

		maxCardsOfAType.put(CardName.Market, 2);
		maxCardsOfAType.put(CardName.Smithy, 1);
		maxCardsOfAType.put(CardName.Village, 2);
		maxCardsOfAType.put(CardName.Mine, 1);
		maxCardsOfAType.put(CardName.Cellar, 1);
		maxCardsOfAType.put(CardName.Workshop, 1);
		maxCardsOfAType.put(CardName.Remodel, 1);
		maxCardsOfAType.put(CardName.Woodcutter, 1);
	}

	/**
	 * Executes the Bot with all its stages play and buy.
	 */
	public void run() {
		do {
			estimatePlayPriorityOfActionCards();
			playActionCards();
		} while (actions > 0 && playOneMore == true && actualPhase == Phase.Action);

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
				cardToPlay = card;
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
					cardToPlay = card;
				}
			}
		}
		// if(cardToPlay == null) do nothing
		play(cardToPlay);
		makeBreak();

		// do something with cards
		switch (cardToPlay.getCardName()) {
		case Mine:
			// messages
			numberOfGoldAndSilverCards++;
			break;
		case Cellar:
			break;
		case Remodel:
			break;
		case Workshop:
			break;
		default:
			break;
		}
		cardToPlay = null;

		// one more play?
		if (actions > 0) {
			if (coins >= 2)
				playOneMore = true;
		} else {
			playOneMore = false;
			makeBreak();
			skipPhase();
		}
	}

	/**
	 * Calculates which card has to be bought and checks if a second buy would make
	 * sense.
	 */
	//  zuerst und dann zuweisung
	private void buy() {
		Boolean succeededBuy = false;
		List<CardName> list;
		if(buys > 2) {
			List<CardName> list2 = prioListForBuying2.keySet().stream()
					.sorted((s1, s2) -> Integer.compare(prioListForBuying1.get(s2), prioListForBuying1.get(s1)))
					.collect(Collectors.toList());
			list = list2;
		} else {
		List<CardName> list1 = prioListForBuying1.keySet().stream()
				.sorted((s1, s2) -> Integer.compare(prioListForBuying1.get(s2), prioListForBuying1.get(s1)))
				.collect(Collectors.toList());
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
					numberOfGoldAndSilverCards++;
				if (Card.getCard(cardToBuy).getType().equals(CardType.Action))
					numberOfActionCards++;
				makeBreak();
				break;
			} else {
				succeededBuy = false;
				cardToBuy = null;
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

		HashMap<CardName, Integer> buyCards;
		buyCards = game.getBuyCards();
		List<CardName> list0 = buyCards.keySet().stream().filter(c -> buyCards.get(c) <= 3)
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
		if (numberOfGoldAndSilverCards >= MAX_TREASURE_CARDS) {
			prioListForBuying1.remove(CardName.Gold);
			prioListForBuying1.remove(CardName.Silver);
		} else {
			int tempGold = prioListForBuying1.get(CardName.Gold);
			int tempSilver = prioListForBuying1.get(CardName.Silver);
			if (numberOfGoldAndSilverCards / numberOfTotalCards < SHARE_OF_TREASURE_CARDS) {
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
	 * Calculate the priority of each ActionCards for the buying decision.
	 */
	// wenn gekauft, prio herabsetzen
	private void estimateBuyPriorityOfActionCards() {
		for (int i = 0; i < cardNamesOfActionCards.size(); i++) {
			if (getNumberOfOwnedCards(cardNamesOfActionCards.get(i)) == maxCardsOfAType
					.get(cardNamesOfActionCards.get(i))) {
				int tempValue1 = prioListForBuying1.get(cardNamesOfActionCards.get(i));
				int tempValue2 = prioListForBuying2.get(cardNamesOfActionCards.get(i));
				tempValue1 -= 50;
				tempValue2 -= 50;
				prioListForBuying1.replace(cardNamesOfActionCards.get(i), tempValue1);
				prioListForBuying2.replace(cardNamesOfActionCards.get(i), tempValue2);
			}
		}
		if (numberOfActionCards >= MAX_ACTION_CARDS) {
			for (int i = 0; i < cardNamesOfActionCards.size(); i++) {
				prioListForBuying1.remove(cardNamesOfActionCards.get(i));
				prioListForBuying2.remove(cardNamesOfActionCards.get(i));
			}
		}
	}

	/**
	 * Calculate the priority of ActionCards for the playing phase.
	 */
	private void estimatePlayPriorityOfActionCards() {
		// smithy, workshop
		// remodel nur wenn schlechte karte
		// village und market --> immer gleich!
		int numOfVictoryCards = 0;
		int numOfPossibleMine = 0;

		for (Card card : handCards) {
			if (card.getType().equals(CardType.Victory))
				numOfVictoryCards++;
			if (card.getCardName().equals(CardName.Copper) || card.getCardName().equals(CardName.Silver))
				numOfPossibleMine++;
		}

		// smithy oder cellar? mit actions arbeiten
		if (numOfVictoryCards >= 2) {
			if (prioListForPlaying.containsKey(CardName.Cellar))
				prioListForPlaying.replace(CardName.Cellar, 70);
			else
				prioListForPlaying.put(CardName.Cellar, 70);
		} else if (numOfVictoryCards == 0)
			prioListForPlaying.remove(CardName.Cellar);

		// cooper or silver? mit actions arbeiten
		if (numOfPossibleMine >= 1) {
			if (prioListForPlaying.containsKey(CardName.Mine))
				prioListForPlaying.replace(CardName.Mine, 70);
			else
				prioListForPlaying.put(CardName.Mine, 70);
		} else if (numOfPossibleMine == 0)
			prioListForPlaying.remove(CardName.Mine);
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