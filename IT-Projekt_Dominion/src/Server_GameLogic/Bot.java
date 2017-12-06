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
import Cards.Cellar_Card;
import Cards.Mine_Card;
import Cards.Remodel_Card;
import Cards.Workshop_Card;
import Messages.Failure_Message;
import Messages.Interaction;
import Messages.Message;
import Messages.PlayerSuccess_Message;
import Messages.UpdateGame_Message;

/**
 * @author Simon
 * @version 1.0
 * @created 15-Nov-2017 08:36:00
 * @lastEdited 5-Dec-2017 23:25:00
 */
public class Bot extends Player implements Runnable {
	private HashMap<CardName, Integer> buyPrioOneCard = new HashMap<CardName, Integer>();
	private HashMap<CardName, Integer> buyPrioMoreCards = new HashMap<CardName, Integer>();
	private HashMap<CardName, Integer> prioListForPlaying = new HashMap<CardName, Integer>();
	private HashMap<CardName, Integer> prioListForRemodel = new HashMap<CardName, Integer>();
	private HashMap<CardName, Integer> maxCardsOfAType = new HashMap<CardName, Integer>();
	private ArrayList<CardName> cardNamesOfActionCards = new ArrayList<CardName>();
	private ArrayList<CardName> cardNamesOfActionHandCards = new ArrayList<CardName>();
	private static final HashMap<CardName, Integer> PRIOLIST_TOPDISCARDPILE_CARD = new HashMap<CardName, Integer>();
	private static final ArrayList<String> NAMES = new ArrayList<String>();
	private static final double SHARE_OF_TREASURE_CARDS = 0.35;
	private static final int MIN_TIME_BEFORE_EXECUTING = 1000, MAX_TIME_BEFORE_EXECUTING = 3000;
	private static final int MAX_TREASURE_CARDS = 7, MAX_ACTION_CARDS = 10;
	private Message buyMessage = null;
	private Card cardToPlay = null;
	private CardName cardToBuy = null;
	private double numberOfGoldAndSilverCards = 0.0, numberOfTotalCards = 10.0;
	private int numberOfActionCards = 0, gameStage;
	private boolean buyOneMore = false;
	private boolean done0 = true, done1 = true, done2 = true, done3 = true;
	private LinkedList<Card> discardedCardsForCellar;

	public Bot(String name) {
		super(name);
		System.out.println(this.playerName + " Bot erstellt");

		maxCardsOfAType.put(CardName.Market, 3);
		maxCardsOfAType.put(CardName.Smithy, 2);
		maxCardsOfAType.put(CardName.Village, 3);
		maxCardsOfAType.put(CardName.Mine, 2);
		maxCardsOfAType.put(CardName.Cellar, 1);
		maxCardsOfAType.put(CardName.Workshop, 1);
		maxCardsOfAType.put(CardName.Remodel, 1);
		maxCardsOfAType.put(CardName.Woodcutter, 2);

		cardNamesOfActionCards.add(CardName.Cellar);
		cardNamesOfActionCards.add(CardName.Market);
		cardNamesOfActionCards.add(CardName.Mine);
		cardNamesOfActionCards.add(CardName.Smithy);
		cardNamesOfActionCards.add(CardName.Workshop);
		cardNamesOfActionCards.add(CardName.Village);
		cardNamesOfActionCards.add(CardName.Remodel);
		cardNamesOfActionCards.add(CardName.Woodcutter);

		PRIOLIST_TOPDISCARDPILE_CARD.put(CardName.Copper, 100);
		PRIOLIST_TOPDISCARDPILE_CARD.put(CardName.Cellar, 70);
		PRIOLIST_TOPDISCARDPILE_CARD.put(CardName.Duchy, 90);
		PRIOLIST_TOPDISCARDPILE_CARD.put(CardName.Estate, 95);
		PRIOLIST_TOPDISCARDPILE_CARD.put(CardName.Gold, 75);
		PRIOLIST_TOPDISCARDPILE_CARD.put(CardName.Market, 65);
		PRIOLIST_TOPDISCARDPILE_CARD.put(CardName.Mine, 55);
		PRIOLIST_TOPDISCARDPILE_CARD.put(CardName.Province, 85);
		PRIOLIST_TOPDISCARDPILE_CARD.put(CardName.Remodel, 50);
		PRIOLIST_TOPDISCARDPILE_CARD.put(CardName.Silver, 80);
		PRIOLIST_TOPDISCARDPILE_CARD.put(CardName.Smithy, 64);
		PRIOLIST_TOPDISCARDPILE_CARD.put(CardName.Village, 60);
		PRIOLIST_TOPDISCARDPILE_CARD.put(CardName.Woodcutter, 40);
		PRIOLIST_TOPDISCARDPILE_CARD.put(CardName.Workshop, 45);
	}

	/**
	 * Executes the Bot with all its stages play and buy.
	 */
	public void run() {
		System.out.println(this.playerName + " started run-Methode");
		//makeBreak();
		while (actions > 0 && actualPhase == Phase.Action) {
			estimatePlayPriorityOfActionCards();
			playActionCards();
		}

		System.out.println(this.playerName + " playPhase abgeschlossen");
		if (buys > 0 && actualPhase == Phase.Buy) {
			playTreasureCards();
			do {
				estimateBuyPriorityOfVictoryCards();
				estimateBuyPriorityOfTreasureCards();
				estimateBuyPriorityOfActionCards();
				buy();
				System.out.println(this.playerName + " buyPhase abgeschlossen");
			} while (buys > 0 && buyOneMore == true && actualPhase == Phase.Buy);
		}
		System.out.println(this.playerName + " Runde abgeschlossen");
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
				//makeBreak();
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
					System.out.println(this.playerName + " cardToPlay " + cardToPlay.toString());
				}
			}
		}
		//makeBreak();
	//	System.out.println(this.playerName + " spielt " +cardToPlay.toString());
//		if(cardToPlay == null)
//			skipPhase();
//		else {
		Message playMessage = play(cardToPlay);
		if (playMessage instanceof UpdateGame_Message) {
			UpdateGame_Message ugmsg = (UpdateGame_Message) playMessage;
			switch (cardToPlay.getCardName()) {
			case Mine:
				Card tempCard = null;
				for (Card card : handCards) {
					if (card.getCardName().equals(CardName.Copper))
						tempCard = card;
				}
				Mine_Card mCard = (Mine_Card) this.getPlayedCards().get(this.getPlayedCards().size() - 1);
				ugmsg = (UpdateGame_Message) mCard.executeMine(tempCard);
				if (tempCard.getCardName().equals(CardName.Copper))
					numberOfGoldAndSilverCards++;
				break;
			case Cellar:
				discardedCardsForCellar = null;
				for (Card card : handCards) {
					if (card.getType().equals(CardType.Victory))
						discardedCardsForCellar.add(card);
					if (actions == 0) {
						if (card.getType().equals(CardType.Action))
							discardedCardsForCellar.add(card);
					}
				}
				Cellar_Card cCard = (Cellar_Card) this.getPlayedCards().get(this.getPlayedCards().size() - 1);
				ugmsg = cCard.executeCellar(discardedCardsForCellar);
				break;
			case Remodel:
				prioListForRemodel.put(CardName.Cellar, 50);
				prioListForRemodel.put(CardName.Copper, 48);
				prioListForRemodel.put(CardName.Estate, 46);
				prioListForRemodel.put(CardName.Gold, 10);
				prioListForRemodel.put(CardName.Mine, 30);
				prioListForRemodel.put(CardName.Remodel, 94);
				prioListForRemodel.put(CardName.Silver, 20);
				prioListForRemodel.put(CardName.Smithy, 85);
				prioListForRemodel.put(CardName.Woodcutter, 89);
				prioListForRemodel.put(CardName.Workshop, 87);

				if (gameStage >= 75)
					prioListForRemodel.replace(CardName.Gold, 98);
				prioListForRemodel.replace(CardName.Silver, 96);
				if (getNumberOfOwnedCards(CardName.Gold) >= 3)
					prioListForRemodel.replace(CardName.Mine, 94);

				// executeRemodel1
				Card discardedCard1 = null;
				int tempValue = 0;
				for (Card card : handCards) {
					if (prioListForRemodel.containsKey(card.getCardName())) {
						if (tempValue <= prioListForRemodel.get(card.getCardName())) {
							tempValue = prioListForRemodel.get(card.getCardName());
							discardedCard1 = card;
						}
					}
				}
				Remodel_Card rCard1 = (Remodel_Card) this.getPlayedCards().get(this.getPlayedCards().size() - 1);
				ugmsg = rCard1.executeRemodel1(discardedCard1);

				// executeRemodel2
				LinkedList<CardName> takeChoices1 = ugmsg.getCardSelection();
				List<CardName> prioList1 = this.buyPrioOneCard.keySet().stream()
						.sorted((s1, s2) -> Integer.compare(this.buyPrioOneCard.get(s2), this.buyPrioOneCard.get(s1)))
						.collect(Collectors.toList());
				for (CardName discardedCard2 : prioList1) {
					if (takeChoices1.contains(discardedCard2)) {
						Remodel_Card rCard2 = (Remodel_Card) this.getPlayedCards()
								.get(this.getPlayedCards().size() - 1);
						ugmsg = rCard2.executeRemodel2(discardedCard2);
						numberOfTotalCards++;
						if (Card.getCard(cardToBuy).getType().equals(CardType.Action))
							numberOfActionCards++;
						else if (discardedCard2.equals(CardName.Silver) || discardedCard2.equals(CardName.Gold))
							numberOfGoldAndSilverCards++;
					}
					break;
				}
				break;
			case Workshop:
				LinkedList<CardName> takeChoices2 = ugmsg.getCardSelection();
				List<CardName> prioList2 = this.buyPrioOneCard.keySet().stream()
						.sorted((s1, s2) -> Integer.compare(this.buyPrioOneCard.get(s2), this.buyPrioOneCard.get(s1)))
						.collect(Collectors.toList());
				for (CardName newCard : prioList2) {
					if (takeChoices2.contains(newCard)) {
						Workshop_Card wCard = (Workshop_Card) this.getPlayedCards()
								.get(this.getPlayedCards().size() - 1);
						ugmsg = wCard.executeWorkshop(newCard);
						numberOfTotalCards++;
						if (Card.getCard(cardToBuy).getType().equals(CardType.Action))
							numberOfActionCards++;
						else if (newCard.equals(CardName.Silver) || newCard.equals(CardName.Gold))
							numberOfGoldAndSilverCards++;
						break;
					}
				}
				break;
			default:
				break;
			}
			if (ugmsg.getInteractionType().equals(Interaction.EndOfTurn)) {
				List<CardName> cardToChoose = PRIOLIST_TOPDISCARDPILE_CARD.keySet().stream().sorted((s1, s2) -> Integer
						.compare(PRIOLIST_TOPDISCARDPILE_CARD.get(s2), PRIOLIST_TOPDISCARDPILE_CARD.get(s1)))
						.collect(Collectors.toList());
				for (int indexCounter2 = 0; indexCounter2 < cardToChoose.size(); indexCounter2++) {
					CardName cardname = cardToChoose.get(indexCounter2);
					Card discardPileTopCard = Card.getCard(cardname);
					if (handCards.contains(discardPileTopCard)) {
						Card card = handCards.get(handCards.indexOf(discardPileTopCard));
						cleanUp(card);
						break;
					}
				}
			}

		} else if (playMessage instanceof Failure_Message)
			skipPhase();
		// if nothing applies playMessage must be a PlayerSuccess_Message --> do nothing
		cardToPlay = null;
	}
//	}

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
		for (int indexCounter1 = 0; indexCounter1 < list.size(); indexCounter1++) {
			cardToBuy = list.get(indexCounter1);
			buyMessage = buy(cardToBuy);

			// if PlayerSuccess_Message --> terminate buy();
			if (buyMessage instanceof PlayerSuccess_Message)
				break;
			// if UpdateGame_Message --> check if Interaction == EndOfTurn;
			else if (buyMessage instanceof UpdateGame_Message) {
				numberOfTotalCards++;
				if (cardToBuy.equals(CardName.Gold) || cardToBuy.equals(CardName.Silver))
					numberOfGoldAndSilverCards++;
				else if (Card.getCard(cardToBuy).getType().equals(CardType.Action))
					numberOfActionCards++;
				UpdateGame_Message ugmsg = (UpdateGame_Message) buyMessage;
				if (ugmsg.getInteractionType().equals(Interaction.EndOfTurn)) {
					List<CardName> cardToChoose = PRIOLIST_TOPDISCARDPILE_CARD.keySet().stream()
							.sorted((s1, s2) -> Integer.compare(PRIOLIST_TOPDISCARDPILE_CARD.get(s2),
									PRIOLIST_TOPDISCARDPILE_CARD.get(s1)))
							.collect(Collectors.toList());
					for (int indexCounter2 = 0; indexCounter2 < cardToChoose.size(); indexCounter2++) {
						CardName cardname = cardToChoose.get(indexCounter2);
						Card discardPileTopCard = Card.getCard(cardname);
						if (handCards.contains(discardPileTopCard)) {
							Card card = handCards.get(handCards.indexOf(discardPileTopCard));
							cleanUp(card);
							break;
						}
					}
				} else if (coins >= 2) {
					buyOneMore = true;
				} else {
					buyOneMore = false;
					if (ugmsg.getCurrentPhase().equals(Phase.Buy))
						skipPhase();
				}
			}
			// if Failure_Message --> keep searching
			if (buyMessage instanceof Failure_Message)
				continue;
		}
		cardToBuy = null;
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
	 * Wait for a few milliseconds.
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
	// wenn gekauft, prio herabsetzen --> Methode dringend umbauen!
	private void estimateBuyPriorityOfActionCards() {
		for (int i = 0; i < cardNamesOfActionCards.size(); i++) {
			if (getNumberOfOwnedCards(cardNamesOfActionCards.get(i)) == maxCardsOfAType
					.get(cardNamesOfActionCards.get(i))) {
				int tempValue1 = buyPrioOneCard.get(cardNamesOfActionCards.get(i));
				int tempValue2 = buyPrioMoreCards.get(cardNamesOfActionCards.get(i));
				tempValue1 -= 10;
				tempValue2 -= 10;
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
		//cardNamesOfActionHandCards = null;

		// calculate types of handCards
		for (Card card : handCards) {
			if (card.getType().equals(CardType.Victory))
				numOfVictoryCards++;
			if (card.getCardName().equals(CardName.Copper) || card.getCardName().equals(CardName.Silver))
				numOfPossibleMine++;
			if (card.getType().equals(CardType.Action))
				cardNamesOfActionHandCards.add(card.getCardName());
		}

		// set priority for every owned card
		if (numOfVictoryCards >= 1 && cardNamesOfActionHandCards.contains(CardName.Cellar)) {
			prioListForPlaying.put(CardName.Cellar, 6);
			if (actions >= 2)
				prioListForPlaying.put(CardName.Smithy, 7);
		} else
			prioListForPlaying.remove(CardName.Cellar);

		if (numOfPossibleMine >= 1 && cardNamesOfActionHandCards.contains(CardName.Mine))
			prioListForPlaying.put(CardName.Mine, 5);
		else
			prioListForPlaying.remove(CardName.Mine);

		if (gameStage >= 75 && cardNamesOfActionHandCards.contains(CardName.Gold))
			prioListForPlaying.put(CardName.Woodcutter, 5);

		for (int i = 0; i < handCards.size(); i++)
			if (!prioListForRemodel.containsKey(handCards.get(i).getCardName()))
				prioListForPlaying.remove(CardName.Remodel);
	}

	/**
	 * Calculates who many cards are owned by the Bot of a specific card and returns
	 * the number as an Integer.
	 * 
	 * @param cardname
	 * @return specific number of cards
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