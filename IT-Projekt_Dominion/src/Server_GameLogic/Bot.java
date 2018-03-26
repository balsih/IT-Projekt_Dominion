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
 * Simulates a player for the singleplayer-modus.
 * 
 * @author Simon
 * @version 2.0
 * @created 15-Nov-2017 08:36:00
 * @lastEdited 21-Dec-2017 12:01:00
 */
public class Bot extends Player implements Runnable {
	private HashMap<CardName, Integer> buyPrioOneCard = new HashMap<CardName, Integer>();
	private HashMap<CardName, Integer> buyPrioMoreCards = new HashMap<CardName, Integer>();
	private HashMap<CardName, Integer> prioListForPlaying = new HashMap<CardName, Integer>();
	private HashMap<CardName, Integer> prioListForRemodel = new HashMap<CardName, Integer>();
	private HashMap<CardName, Integer> maxCardsOfAType = new HashMap<CardName, Integer>();
	private static final ArrayList<CardName> CARDNAMESOFACTIONCARDS = new ArrayList<CardName>();
	private ArrayList<CardName> cardNamesOfActionHandCards = new ArrayList<CardName>();
	private static final HashMap<CardName, Integer> PRIOLIST_TOPDISCARDPILE_CARD = new HashMap<CardName, Integer>();
	private static final ArrayList<String> NAMES = new ArrayList<String>();
	private static final int MIN_TIME_BEFORE_EXECUTING = 500, MAX_TIME_BEFORE_EXECUTING = 1500;
	private static final int MAX_TREASURE_CARDS = 16, MAX_ACTION_CARDS = 10;
	private Card cardToPlay = null;
	private CardName cardToBuy = null;
	private double numberOfGoldAndSilverCards = 0.0;
	private int numberOfActionCards = 0, gameStage;
	private boolean done0 = true, done1 = true, done2 = true, done3 = true, done4 = true;
	private LinkedList<Card> discardedCardsForCellar = new LinkedList<Card>();

	public Bot(String name, ServerThreadForClient thread) {
		super(name, thread);

		buyPrioOneCard.put(CardName.Province, 100);
		buyPrioOneCard.put(CardName.Gold, 60);
		buyPrioOneCard.put(CardName.Duchy, 56);
		buyPrioOneCard.put(CardName.Market, 58);
		buyPrioOneCard.put(CardName.Mine, 57);
		buyPrioOneCard.put(CardName.Remodel, 54);
		buyPrioOneCard.put(CardName.Smithy, 55);
		buyPrioOneCard.put(CardName.Silver, 50);
		buyPrioOneCard.put(CardName.Village, 52);
		buyPrioOneCard.put(CardName.Woodcutter, 51);
		buyPrioOneCard.put(CardName.Workshop, 44);
		buyPrioOneCard.put(CardName.Cellar, 30);

		buyPrioMoreCards.put(CardName.Province, 100);
		buyPrioMoreCards.put(CardName.Gold, 70);
		buyPrioMoreCards.put(CardName.Duchy, 56);
		buyPrioMoreCards.put(CardName.Market, 65);
		buyPrioMoreCards.put(CardName.Mine, 64);
		buyPrioMoreCards.put(CardName.Remodel, 40);
		buyPrioMoreCards.put(CardName.Smithy, 63);
		buyPrioMoreCards.put(CardName.Village, 61);
		buyPrioMoreCards.put(CardName.Woodcutter, 59);
		buyPrioMoreCards.put(CardName.Workshop, 49);
		buyPrioMoreCards.put(CardName.Silver, 50);
		buyPrioMoreCards.put(CardName.Cellar, 30);

		maxCardsOfAType.put(CardName.Market, 4);
		maxCardsOfAType.put(CardName.Smithy, 2);
		maxCardsOfAType.put(CardName.Village, 3);
		maxCardsOfAType.put(CardName.Mine, 2);
		maxCardsOfAType.put(CardName.Cellar, 1);
		maxCardsOfAType.put(CardName.Workshop, 1);
		maxCardsOfAType.put(CardName.Remodel, 1);
		maxCardsOfAType.put(CardName.Woodcutter, 2);

		CARDNAMESOFACTIONCARDS.add(CardName.Cellar);
		CARDNAMESOFACTIONCARDS.add(CardName.Market);
		CARDNAMESOFACTIONCARDS.add(CardName.Mine);
		CARDNAMESOFACTIONCARDS.add(CardName.Smithy);
		CARDNAMESOFACTIONCARDS.add(CardName.Workshop);
		CARDNAMESOFACTIONCARDS.add(CardName.Village);
		CARDNAMESOFACTIONCARDS.add(CardName.Remodel);
		CARDNAMESOFACTIONCARDS.add(CardName.Woodcutter);

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

		prioListForPlaying.put(CardName.Cellar, 1);
		prioListForPlaying.put(CardName.Market, 9);
		prioListForPlaying.put(CardName.Mine, 2);
		prioListForPlaying.put(CardName.Remodel, 1);
		prioListForPlaying.put(CardName.Smithy, 4);
		prioListForPlaying.put(CardName.Village, 8);
		prioListForPlaying.put(CardName.Woodcutter, 3);
		prioListForPlaying.put(CardName.Workshop, 2);
	}

	/**
	 * Executes the bot with its stages play and buy for one round.
	 * 
	 * @author Simon
	 */
	public void run() {
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		while (actions > 0 && actualPhase == Phase.Action) {
			estimatePlayPriorityOfActionCards();
			playActionCards();
		}

		if (buys > 0 && actualPhase == Phase.Buy) {
			playTreasureCards();
			do {
				estimateBuyPriorityOfVictoryCards();
				estimateBuyPriorityOfTreasureCards();
				estimateBuyPriorityOfActionCards();
				buy();
			} while (buys > 0 && actualPhase == Phase.Buy && game.getCurrentPlayer() == this);
		}
	}

	/**
	 * Plays all TreasureCards which are in the hands of the bot.
	 * 
	 * @author Simon
	 */
	private void playTreasureCards() {
		for (int index = 0; index < handCards.size(); index++) {
			if (handCards.get(index).getType().equals(CardType.Treasure)) {
				cardToPlay = handCards.get(index);
				play(cardToPlay);
				if (index != handCards.size()) {
					index--;
				}
			}
		}
		cardToPlay = null;
	}

	/**
	 * Calculates which ActionCard is best to play.
	 * 
	 * @author Simon
	 */
	private void playActionCards() {
		int tempPriority = 0;
		for (Card card : handCards) {
			if (card.getType().equals(CardType.Action) && (prioListForPlaying.containsKey(card.getCardName()))) {
				if (tempPriority < prioListForPlaying.get(card.getCardName())) {
					tempPriority = prioListForPlaying.get(card.getCardName());
					cardToPlay = card;
				}
			}
		}
		if (cardToPlay == null) {
			UpdateGame_Message ugmsg = (UpdateGame_Message) skipPhase();
			this.sendToOpponent(this, ugmsg);
		} else {
			Message playMessage = play(cardToPlay);
			makeBreak();

			// the card could be played
			if (playMessage instanceof UpdateGame_Message) {
				UpdateGame_Message ugmsg = (UpdateGame_Message) playMessage;

				switch (cardToPlay.getCardName()) {
				case Mine:
					Card tempCard = null;
					for (Card card : handCards) {
						if (card.getCardName().equals(CardName.Copper))
							tempCard = card;
					}
					if (tempCard == null) {
						for (Card card : handCards) {
							if (card.getCardName().equals(CardName.Silver))
								tempCard = card;
						}
					}

					Mine_Card mCard = (Mine_Card) this.getPlayedCards().get(this.getPlayedCards().size() - 1);
					ugmsg = (UpdateGame_Message) mCard.executeMine(tempCard);
					this.sendToOpponent(this, ugmsg);
					if (tempCard.getCardName().equals(CardName.Copper))
						numberOfGoldAndSilverCards++;
					break;

				case Cellar:
					for (Card card : handCards) {
						if (card.getType().equals(CardType.Victory))
							discardedCardsForCellar.add(card);
					}
					System.out.println(discardedCardsForCellar.toString());
					Cellar_Card cCard = (Cellar_Card) this.getPlayedCards().get(this.getPlayedCards().size() - 1);
					ugmsg = cCard.executeCellar(discardedCardsForCellar);
					this.sendToOpponent(this, ugmsg);
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
					Card discardedCard = null;
					int tempValue = 0;
					for (Card card : handCards) {
						if (prioListForRemodel.containsKey(card.getCardName())) {
							if (tempValue <= prioListForRemodel.get(card.getCardName())) {
								tempValue = prioListForRemodel.get(card.getCardName());
								discardedCard = card;
							}
						}
					}
					Remodel_Card rCard1 = (Remodel_Card) this.getPlayedCards().get(this.getPlayedCards().size() - 1);
					ugmsg = rCard1.executeRemodel1(discardedCard);
					this.sendToOpponent(this, ugmsg);

					// executeRemodel2
					LinkedList<CardName> availableCards = ugmsg.getCardSelection();
					List<CardName> prioList = this.buyPrioOneCard.keySet().stream().sorted(
							(c1, c2) -> Integer.compare(this.buyPrioOneCard.get(c2), this.buyPrioOneCard.get(c1)))
							.collect(Collectors.toList());
					for (CardName newCard : prioList) {
						if (availableCards.contains(newCard)) {
							Remodel_Card rCard2 = (Remodel_Card) this.getPlayedCards()
									.get(this.getPlayedCards().size() - 1);
							Message msg = rCard2.executeRemodel2(newCard);
							if (msg instanceof UpdateGame_Message) {
								this.sendToOpponent(this, ugmsg);
								if (Card.getCard(newCard).getType().equals(CardType.Action))
									numberOfActionCards++;
								else if (newCard.equals(CardName.Silver) || newCard.equals(CardName.Gold))
									numberOfGoldAndSilverCards++;
								break;
							}
							break;
						}
					}
					break;

				case Workshop:
					LinkedList<CardName> availableUpgrades = ugmsg.getCardSelection();
					List<CardName> prioList2 = this.buyPrioOneCard.keySet().stream().sorted(
							(c1, c2) -> Integer.compare(this.buyPrioOneCard.get(c2), this.buyPrioOneCard.get(c1)))
							.collect(Collectors.toList());
					for (CardName newCard : prioList2) {
						if (availableUpgrades.contains(newCard)) {
							Workshop_Card wCard = (Workshop_Card) this.getPlayedCards()
									.get(this.getPlayedCards().size() - 1);
							Message msg = wCard.executeWorkshop(newCard);
							if (msg instanceof UpdateGame_Message) {
								this.sendToOpponent(this, ugmsg);
								if (Card.getCard(newCard).getType().equals(CardType.Action))
									numberOfActionCards++;
								else if (newCard.equals(CardName.Silver) || newCard.equals(CardName.Gold))
									numberOfGoldAndSilverCards++;
								break;
							}
							break;
						}
					}
					break;
				default:
					break;
				}

				// if Bot couldn't buy card --> skipPhase
			} else if (playMessage instanceof Failure_Message) {
				UpdateGame_Message ugmsg = (UpdateGame_Message) skipPhase();
				this.sendToOpponent(this, ugmsg);
			}

			// if nothing applies playMessage must be a PlayerSuccess_Message --> do nothing
			cardToPlay = null;
		}
	}

	/**
	 * Calculates which card has to be bought and checks if a second buy would make
	 * sense.
	 * 
	 * @author Simon
	 */
	private void buy() {
		// choose list for buying process
		List<CardName> buyList;
		if (buys > 2 && gameStage <= 75) {
			List<CardName> list2 = buyPrioMoreCards.keySet().stream()
					.sorted((c1, c2) -> Integer.compare(buyPrioOneCard.get(c2), buyPrioOneCard.get(c1)))
					.collect(Collectors.toList());
			buyList = list2;
		} else {
			List<CardName> list1 = buyPrioOneCard.keySet().stream()
					.sorted((c1, c2) -> Integer.compare(buyPrioOneCard.get(c2), buyPrioOneCard.get(c1)))
					.collect(Collectors.toList());
			buyList = list1;
		}

		// try to buy a card
		Message buyMessage = null;
		for (int index = 0; index < buyList.size(); index++) {
			cardToBuy = buyList.get(index);
			buyMessage = buy(cardToBuy);

			// if PlayerSuccess_Message --> terminate buy();
			if (buyMessage instanceof PlayerSuccess_Message) {
				break;
			}

			// if UpdateGame_Message
			else if (buyMessage instanceof UpdateGame_Message) {
				if (cardToBuy.equals(CardName.Gold) || cardToBuy.equals(CardName.Silver))
					numberOfGoldAndSilverCards++;
				else if (Card.getCard(cardToBuy).getType().equals(CardType.Action))
					numberOfActionCards++;

				// test if manual cleanUp is necessary
				UpdateGame_Message ugmsg = (UpdateGame_Message) buyMessage;
				if (ugmsg.getInteractionType() == Interaction.EndOfTurn) {
					chooseDiscardPileTopCard();
					break;
				}

				// if there are still left some buys and less than 2 coins --> skipPhase, else
				// buy another card
				else {
					if (ugmsg.getCurrentPhase() == null) {
						if (coins <= 1) {
							UpdateGame_Message ugmsg1 = (UpdateGame_Message) skipPhase();
							this.sendToOpponent(this, ugmsg1);
							if (handCards.size() > 1) {
								chooseDiscardPileTopCard();
								break;
							}
						}
					}
				}
			}
			// if Failure_Message --> keep searching
			else if (buyMessage instanceof Failure_Message)
				if (index < buyList.size() - 1)
					continue;
				else {
					if (game.getCurrentPlayer() == this) {
						UpdateGame_Message ugmsg = (UpdateGame_Message) skipPhase();
						this.sendToOpponent(this, ugmsg);
						makeBreak();
						if (handCards.size() > 1) {
							chooseDiscardPileTopCard();
							break;
						}
					}
				}
		}
		cardToBuy = null;
	}

	/**
	 * Chooses a card as the TopCard of the DiscardPile.
	 * 
	 * @author Simon
	 */
	private void chooseDiscardPileTopCard() {
		List<CardName> cardToChoose = PRIOLIST_TOPDISCARDPILE_CARD.keySet().stream().sorted(
				(c1, c2) -> Integer.compare(PRIOLIST_TOPDISCARDPILE_CARD.get(c2), PRIOLIST_TOPDISCARDPILE_CARD.get(c1)))
				.collect(Collectors.toList());

		// choose a card for the cleanUp method
		for (int index = 0; index < cardToChoose.size(); index++) {
			CardName cardname = cardToChoose.get(index);
			if (this.containsCard(handCards, cardname)) {
				for (Card card : handCards) {
					if (card.getCardName().equals(cardname)) {
						UpdateGame_Message ugmsg = cleanUp(card);
						this.sendToOpponent(this, ugmsg);
						break;
					}
				}
				break;
			}
		}
	}

	/**
	 * Fills a name-list with entries, chooses one and gives it back.
	 * 
	 * @author Simon
	 */
	public static String getNameOfBot() {
		NAMES.add("COMPUTER-\"Bodo\"");
		NAMES.add("COMPUTER-\"Lukas\"");
		NAMES.add("COMPUTER-\"Simon\"");
		NAMES.add("COMPUTER-\"Adrian\"");
		NAMES.add("COMPUTER-\"René\"");
		Random rand = new Random();
		String nameOfBot = NAMES.get(rand.nextInt(5));
		return nameOfBot;
	}

	/**
	 * Wait for a few milliseconds.
	 * 
	 * @author Simon
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
	 * Changes the priority of VictoryCards if game-ending is close.
	 * 
	 * @author Simon
	 */
	private void estimateBuyPriorityOfVictoryCards() {
		calculateGameStage();

		// add now the Estate_Card
		if (gameStage >= 50 && done4) {
			buyPrioOneCard.put(CardName.Estate, 5);
			buyPrioMoreCards.put(CardName.Estate, 5);
			done4 = false;
		}

		// change priorities according gameStage number
		int tempEstate;
		if (gameStage >= 50) {
			tempEstate = buyPrioOneCard.get(CardName.Estate);
		} else
			tempEstate = 0;

		int tempDuchy = buyPrioOneCard.get(CardName.Duchy);
		if (gameStage == 100 && done0) {
			tempEstate += 49;
			tempDuchy += 15;
			done0 = false;
		} else if (gameStage == 75 && done1) {
			tempEstate += 26;
			tempDuchy += 20;
			done1 = false;
		} else if (gameStage == 50 && done2) {
			tempDuchy += 2;
			done2 = false;
		} else if (gameStage == 25 && done3) {
			tempDuchy += 2;
			done3 = false;
		}

		buyPrioOneCard.replace(CardName.Duchy, tempDuchy);
		buyPrioMoreCards.replace(CardName.Duchy, tempDuchy);

		if (tempEstate != 0) {
			buyPrioOneCard.replace(CardName.Estate, tempEstate);
			buyPrioMoreCards.replace(CardName.Estate, tempEstate);
		}
	}

	/**
	 * Calculates the game status.
	 * 
	 * @author Simon
	 */
	private void calculateGameStage() {
		// calculate points
		int points = 0;
		int sizeProvincePile = game.getProvincePile().size();
		int sizeDuchyPile = game.getDuchyPile().size();

		if (sizeProvincePile <= 2 && sizeDuchyPile <= 2)
			points += 10;
		else {
			if (sizeProvincePile < 3)
				points += 10;
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
	 * Changes the priority of the GoldCard and SilverCard if game-ending is very
	 * close or if a specific amount of Cards has been reached.
	 * 
	 * @author Simon
	 */
	private void estimateBuyPriorityOfTreasureCards() {
		// remove TreasureCards when gameEnd is near
		if (numberOfGoldAndSilverCards >= MAX_TREASURE_CARDS || gameStage == 100) {
			buyPrioOneCard.replace(CardName.Gold, 0);
			buyPrioOneCard.replace(CardName.Silver, 0);
			buyPrioMoreCards.replace(CardName.Gold, 0);
			buyPrioMoreCards.replace(CardName.Silver, 0);
		} else {
			int tempGold1 = buyPrioOneCard.get(CardName.Gold);
			int tempSilver1 = buyPrioOneCard.get(CardName.Silver);
			int tempGold2 = buyPrioMoreCards.get(CardName.Gold);
			int tempSilver2 = buyPrioMoreCards.get(CardName.Silver);

			if (getNumberOfOwnedCards(CardName.Gold) > 5) {
				tempGold1 -= 1;
				tempGold2 -= 5;
			}
			if (getNumberOfOwnedCards(CardName.Silver) > 3) {
				tempSilver1 -= 1;
				tempSilver2 -= 5;
			}

			buyPrioOneCard.replace(CardName.Gold, tempGold1);
			buyPrioOneCard.replace(CardName.Silver, tempSilver1);
			buyPrioMoreCards.replace(CardName.Gold, tempGold2);
			buyPrioMoreCards.replace(CardName.Silver, tempSilver2);
		}
	}

	/**
	 * Calculates the priority of each ActionCards for the buying decision.
	 * 
	 * @author Simon
	 */
	private void estimateBuyPriorityOfActionCards() {
		// change priority of MineCard according to the number of MarketCards
		if (buyPrioOneCard.get(CardName.Market) != 0 || buyPrioOneCard.get(CardName.Market) != 40) {
			if (getNumberOfOwnedCards(CardName.Market) == 2 || getNumberOfOwnedCards(CardName.Market) == 3) {
				buyPrioOneCard.replace(CardName.Mine, 59);
				buyPrioMoreCards.replace(CardName.Mine, 66);
			}
			if (getNumberOfOwnedCards(CardName.Market) >= 3 && getNumberOfOwnedCards(CardName.Mine) >= 2) {
				buyPrioOneCard.replace(CardName.Mine, 57);
				buyPrioMoreCards.replace(CardName.Mine, 64);
			} else if (getNumberOfOwnedCards(CardName.Market) == 2 && getNumberOfOwnedCards(CardName.Mine) == 1) {
				buyPrioOneCard.replace(CardName.Mine, 57);
				buyPrioMoreCards.replace(CardName.Mine, 64);
			}
		}

		// change priority of WoodcutterCard
		if (buyPrioOneCard.get(CardName.Village) != 0 || buyPrioOneCard.get(CardName.Market) != 35) {
			if (getNumberOfOwnedCards(CardName.Village) >= 1) {
				buyPrioOneCard.replace(CardName.Woodcutter, 53);
			}
			if (getNumberOfOwnedCards(CardName.Woodcutter) >= 1) {
				buyPrioOneCard.replace(CardName.Woodcutter, 51);
			}
		}

		// change priority of an ActionCard if its maximum is reached
		for (int i = 0; i < CARDNAMESOFACTIONCARDS.size(); i++) {
			if (getNumberOfOwnedCards(CARDNAMESOFACTIONCARDS.get(i)) == maxCardsOfAType
					.get(CARDNAMESOFACTIONCARDS.get(i))) {
				buyPrioOneCard.replace(CARDNAMESOFACTIONCARDS.get(i), 0);
				buyPrioMoreCards.replace(CARDNAMESOFACTIONCARDS.get(i), 0);
			}
		}

		// change priority of ActionCards if the maximum of ActionCards is reached
		if (numberOfActionCards >= MAX_ACTION_CARDS) {
			for (int i = 0; i < CARDNAMESOFACTIONCARDS.size(); i++) {
				buyPrioOneCard.replace(CARDNAMESOFACTIONCARDS.get(i), 0);
				buyPrioMoreCards.replace(CARDNAMESOFACTIONCARDS.get(i), 0);
			}
			// set standard priorities of important ActionCards
			buyPrioOneCard.replace(CardName.Market, 40);
			buyPrioMoreCards.replace(CardName.Market, 40);
			buyPrioOneCard.replace(CardName.Village, 35);
			buyPrioMoreCards.replace(CardName.Village, 35);
			if (!buyPrioOneCard.get(CardName.Mine).equals(0)) {
				buyPrioOneCard.replace(CardName.Mine, 38);
				buyPrioMoreCards.replace(CardName.Mine, 38);
			}
		}
	}

	/**
	 * Calculates the priority of ActionCards for the playing phase.
	 * 
	 * @author Simon
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

		if (gameStage >= 50 && cardNamesOfActionHandCards.contains(CardName.Gold))
			prioListForPlaying.put(CardName.Woodcutter, 5);

		boolean b = false;
		for (int i = 0; i < handCards.size(); i++) {
			if (prioListForRemodel.containsKey(handCards.get(i).getCardName()))
				b = true;
		}
		if (b == false)
			prioListForPlaying.remove(CardName.Remodel);
	}

	/**
	 * Calculates how many cards of a specific card are owned by the bot and returns
	 * the number as an Integer.
	 * 
	 * @author Simon
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