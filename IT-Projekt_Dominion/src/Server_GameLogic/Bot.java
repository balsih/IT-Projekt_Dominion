package Server_GameLogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import Cards.Card;
import Cards.CardName;
import Cards.CardType;
import Cards.Workshop_Card;
import Messages.Message;
import Messages.UpdateGame_Message;

/**
 * @author Simon
 * @version 1.0
 * @created 15-Nov-2017 08:36:00
 * @lastEdited 25-Nov-2017 08:36:00
 */

// if (m instanceof UpdateGame_Message && playOneMore == false) {
// UpdateGame_Message ugmsg = (UpdateGame_Message) m;
// if (ugmsg.getCurrentPhase() == Phase.Action)
// skipPhase();
// every method which is just used once by another method --> extract method
// reduce class variables where possible
// UpdateGame_Message ugmsg = (UpdateGame_Message) m;
// listnames anpassen

public class Bot extends Player implements Runnable {
	private HashMap<CardName, Integer> buyPrioOneCard = new HashMap<CardName, Integer>();
	private HashMap<CardName, Integer> buyPrioMoreCards = new HashMap<CardName, Integer>();
	HashMap<CardName, Integer> prioListForPlaying = new HashMap<CardName, Integer>();
	private HashMap<CardName, Integer> prioListForRemodel = new HashMap<CardName, Integer>();
	private HashMap<CardName, Integer> maxCardsOfAType = new HashMap<CardName, Integer>();
	private ArrayList<CardName> cardNamesOfActionCards = new ArrayList<CardName>();
	private static final ArrayList<String> NAMES = new ArrayList<String>();
	private static final double SHARE_OF_TREASURE_CARDS = 0.35;
	private static final int MIN_TIME_BEFORE_EXECUTING = 1000, MAX_TIME_BEFORE_EXECUTING = 3000;
	private static final int MAX_TREASURE_CARDS = 7, MAX_ACTION_CARDS = 10;
	private double numberOfGoldAndSilverCards = 0.0, numberOfTotalCards = 10.0;
	private int numberOfActionCards = 0, gameStage;
	private Card cardToPlay = null;
	private CardName cardToBuy = null;
	private boolean buyOneMore = false;
	boolean done0 = true, done1 = true, done2 = true, done3 = true;
	private ArrayList<String> cardNamesOfActionHandCards;

	public Bot(String name) {
		super(name);
		buyPrioOneCard.put(CardName.Cellar, 58);
		buyPrioOneCard.put(CardName.Duchy, 50);
		buyPrioOneCard.put(CardName.Estate, 10);
		buyPrioOneCard.put(CardName.Gold, 70);
		buyPrioOneCard.put(CardName.Market, 68);
		buyPrioOneCard.put(CardName.Mine, 62);
		buyPrioOneCard.put(CardName.Province, 90);
		buyPrioOneCard.put(CardName.Remodel, 60);
		buyPrioOneCard.put(CardName.Silver, 40);
		buyPrioOneCard.put(CardName.Smithy, 64);
		buyPrioOneCard.put(CardName.Village, 66);
		buyPrioOneCard.put(CardName.Woodcutter, 20);
		buyPrioOneCard.put(CardName.Workshop, 58);

		buyPrioMoreCards.put(CardName.Cellar, 10);
		buyPrioMoreCards.put(CardName.Duchy, 40);
		buyPrioMoreCards.put(CardName.Estate, 10);
		buyPrioMoreCards.put(CardName.Gold, 50);
		buyPrioMoreCards.put(CardName.Market, 76);
		buyPrioMoreCards.put(CardName.Mine, 72);
		buyPrioMoreCards.put(CardName.Province, 60);
		buyPrioMoreCards.put(CardName.Remodel, 56);
		buyPrioMoreCards.put(CardName.Silver, 70);
		buyPrioMoreCards.put(CardName.Smithy, 74);
		buyPrioMoreCards.put(CardName.Village, 78);
		buyPrioMoreCards.put(CardName.Woodcutter, 20);
		buyPrioMoreCards.put(CardName.Workshop, 58);

		// gewisse Karten rausnehmen
		prioListForRemodel.put(CardName.Cellar, 10);
		prioListForRemodel.put(CardName.Copper, 20);
		prioListForRemodel.put(CardName.Estate, 11);
		prioListForRemodel.put(CardName.Gold, 62); // if game-end near
		prioListForRemodel.put(CardName.Mine, 92);
		prioListForRemodel.put(CardName.Remodel, 89);
		prioListForRemodel.put(CardName.Silver, 61);
		prioListForRemodel.put(CardName.Smithy, 94);
		prioListForRemodel.put(CardName.Woodcutter, 87);
		prioListForRemodel.put(CardName.Workshop, 85);

		// getBuyCards filter --> action cards
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
		makeBreak();
		do {
			estimatePlayPriorityOfActionCards();
			playActionCards();
		} while (actions > 0 && actualPhase == Phase.Action);

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
		makeBreak();
		if (cardToPlay != null) {
			Message m = play(cardToPlay);
			UpdateGame_Message ugmsg = (UpdateGame_Message) m;
			switch (cardToPlay.getCardName()) {
			case Mine:
			//	ugmsg.getInteractionType().Mine.
				
				numberOfGoldAndSilverCards++;
				break;
			case Cellar:
				break;
			case Remodel:
				break;
			case Workshop:
				LinkedList<CardName> takeChoices = ugmsg.getCardSelection();
				List<CardName> prioList = this.buyPrioOneCard.keySet().stream()
						.sorted((s1, s2) -> Integer.compare(this.buyPrioOneCard.get(s2), this.buyPrioOneCard.get(s1)))
						.collect(Collectors.toList());
				for(CardName cardName: prioList){
					if(takeChoices.contains(cardName)){
						Workshop_Card wCard = (Workshop_Card) this.getPlayedCards().get(this.getPlayedCards().size()-1);
						ugmsg = wCard.executeWorkshop(cardName);
						//hier oder später musst du selbst entscheiden, ob du skippen willst (wenn es das spiel nicht schon gemacht hat)
						//wahrscheinlich erst vor dem else
						break;
					}
				}
				break;
			default:
				break;
			}
			cardToPlay = null;
		} else {
			skipPhase();
		}
	}

	/**
	 * Calculates which card has to be bought and checks if a second buy would make
	 * sense.
	 */
	private void buy() {
		makeBreak();
		List<CardName> list;
		if (buys > 2 && gameStage <= 75) {
			List<CardName> list2 = buyPrioMoreCards.keySet().stream()
					.sorted((s1, s2) -> Integer.compare(buyPrioOneCard.get(s2), buyPrioOneCard.get(s1)))
					.collect(Collectors.toList());
			list = list2;
		} else {
			List<CardName> list1 = buyPrioOneCard.keySet().stream()
					.sorted((s1, s2) -> Integer.compare(buyPrioOneCard.get(s2), buyPrioOneCard.get(s1)))
					.collect(Collectors.toList());
			list = list1;
		}

		for (int indexCounter = 0; indexCounter < list.size(); indexCounter++) {
			cardToBuy = list.get(indexCounter);
			Message m = buy(cardToBuy);
			// interaction Yes --> cleanUp/ no --> do nothing
			// wenn mer als 1 karte --> karte auswählen
			if (m instanceof UpdateGame_Message) {
				numberOfTotalCards++;
				if (cardToBuy.equals(CardName.Gold) || cardToBuy.equals(CardName.Silver))
					numberOfGoldAndSilverCards++;
				if (Card.getCard(cardToBuy).getType().equals(CardType.Action))
					numberOfActionCards++;
				break;
			} else {
				cardToBuy = null; // nötig?
			}
		}
		// one more buy?
		if (coins >= 2) {
			buyOneMore = true;
		} else {
			buyOneMore = false;
			// updategamemessage phase abfragen (actualPhase)
			Message m = skipPhase();
			//
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
	 * Changes the priority of VictoryCards if game ending is close.
	 */
	private void estimateBuyPriorityOfVictoryCards() {
		calculateGameStage();
		// change priorities according gameStage number
		int tempEstate = buyPrioOneCard.get(CardName.Estate);
		int tempDuchy = buyPrioOneCard.get(CardName.Duchy);
		int tempProvince = buyPrioOneCard.get(CardName.Province);
		if (gameStage == 100 && done0) {
			tempEstate += 70;
			tempDuchy += 20;
			tempProvince += 10;
			done0 = false;
		} else if (gameStage == 75 && done1) {
			tempEstate += 10;
			tempDuchy += 10;
			tempProvince += 10;
			done1 = false;
		} else if (gameStage == 50 && done2) {
			tempDuchy += 10;
			tempProvince += 10;
			done2 = false;
		} else if (gameStage == 25 && done3) {
			tempDuchy += 10;
			tempProvince += 10;
			done3 = false;
		}
		buyPrioOneCard.replace(CardName.Estate, tempEstate);
		buyPrioOneCard.replace(CardName.Duchy, tempDuchy);
		buyPrioOneCard.replace(CardName.Province, tempProvince);
	}

	/**
	 * Calculates the game status.
	 */
	private void calculateGameStage() {
		// calculate points
		int points = 0;
		int sizeProvincePile = game.getProvincePile().size();
		int sizeDuchyPile = game.getDuchyPile().size();

		if (sizeProvincePile <= 2 && sizeDuchyPile <= 2)
			points += 10;
		else {
			if (sizeProvincePile < 3 || sizeDuchyPile < 4)
				points += 7;
			else if (sizeProvincePile <= 4)
				points += 7;
			else if (sizeProvincePile <= 5)
				points += 5;
			else if (sizeDuchyPile <= 6)
				points += 2;
		}

		HashMap<CardName, Integer> buyCards;
		buyCards = game.getBuyCards();
		List<CardName> list0 = buyCards.keySet().stream().filter(c -> buyCards.get(c) <= 3)
				.collect(Collectors.toList());
		int lowStacks = list0.size();
		if (lowStacks == 3)
			points += 5;
		else if (lowStacks >= 4)
			points += 7;

		// convert points into gameStage
		if (points >= 2)
			gameStage = 25;
		if (points >= 5)
			gameStage = 50;
		if (points >= 7)
			gameStage = 75;
		if (points >= 10)
			gameStage = 100;
	}

	/**
	 * Calculates the share of TreasureCards in relation to the total number of
	 * owned cards and changes the Priority of GoldCard and SilverCard.
	 */
	private void estimateBuyPriorityOfTreasureCards() {
		if (numberOfGoldAndSilverCards >= MAX_TREASURE_CARDS || gameStage >= 75) {
			buyPrioOneCard.remove(CardName.Gold);
			buyPrioOneCard.remove(CardName.Silver);
		} else {
			int tempGold = buyPrioOneCard.get(CardName.Gold);
			int tempSilver = buyPrioOneCard.get(CardName.Silver);
			if (numberOfGoldAndSilverCards / numberOfTotalCards < SHARE_OF_TREASURE_CARDS) {
				tempGold += 10;
				tempSilver += 10;
			} else {
				tempGold -= 10;
				tempSilver -= 10;
			}
			buyPrioOneCard.replace(CardName.Gold, tempGold);
			buyPrioOneCard.replace(CardName.Silver, tempSilver);
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
				int tempValue1 = buyPrioOneCard.get(cardNamesOfActionCards.get(i));
				int tempValue2 = buyPrioMoreCards.get(cardNamesOfActionCards.get(i));
				tempValue1 -= 50;
				tempValue2 -= 50;
				buyPrioOneCard.replace(cardNamesOfActionCards.get(i), tempValue1);
				buyPrioMoreCards.replace(cardNamesOfActionCards.get(i), tempValue2);
			}
		}
		if (numberOfActionCards >= MAX_ACTION_CARDS) {
			for (int i = 0; i < cardNamesOfActionCards.size(); i++) {
				buyPrioOneCard.remove(cardNamesOfActionCards.get(i));
				buyPrioMoreCards.remove(cardNamesOfActionCards.get(i));
			}
		}
	}

	/**
	 * Calculate the priority of ActionCards for the playing phase.
	 */
	private void estimatePlayPriorityOfActionCards() {
		// prepare for calculations
		prioListForPlaying.put(CardName.Cellar, 1);
		prioListForPlaying.put(CardName.Market, 9);
		prioListForPlaying.put(CardName.Mine, 2);
		prioListForPlaying.put(CardName.Remodel, 1);
		prioListForPlaying.put(CardName.Smithy, 4);
		prioListForPlaying.put(CardName.Village, 8);
		prioListForPlaying.put(CardName.Woodcutter, 3);
		prioListForPlaying.put(CardName.Workshop, 2);
		int numOfVictoryCards = 0;
		int numOfPossibleMine = 0;
		cardNamesOfActionHandCards = null;

		// calculate types of handCards
		for (Card card : handCards) {
			if (card.getType().equals(CardType.Victory))
				numOfVictoryCards++;
			if (card.getCardName().equals(CardName.Copper) || card.getCardName().equals(CardName.Silver))
				numOfPossibleMine++;
			if (card.getType().equals(CardType.Action))
				cardNamesOfActionHandCards.add(card.getCardName().toString());
		}

		// set priority for every owned card
		if (numOfVictoryCards >= 1 && cardNamesOfActionHandCards.contains(CardName.Cellar.toString())) {
			prioListForPlaying.put(CardName.Cellar, 6);
			if (actions >= 2)
				prioListForPlaying.put(CardName.Smithy, 7);
		} else
			prioListForPlaying.remove(CardName.Cellar);

		if (numOfPossibleMine >= 1 && cardNamesOfActionHandCards.contains(CardName.Mine.toString()))
			prioListForPlaying.put(CardName.Mine, 5);
		else
			prioListForPlaying.remove(CardName.Mine);

		if (gameStage >= 75 && cardNamesOfActionHandCards.contains(CardName.Gold.toString()))
			prioListForPlaying.put(CardName.Woodcutter, 5);
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