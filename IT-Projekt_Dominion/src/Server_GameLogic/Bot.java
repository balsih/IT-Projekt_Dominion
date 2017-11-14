package Server_GameLogic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import Cards.Card;
import Cards.CardName;
import Cards.CardType;

/**
 * @author Simon
 * @version 3.0
 * @created 31-Okt-2017 17:08:40
 * @lastEdited 09-Nov-2017 20:00:00
 */
public class Bot extends Player {

	private static final int NUM_STAGE_1 = 3, NUM_STAGE_2 = 5, NUM_STAGE_3 = 14, MAX_VILLAGE_CARDS = 2,
			MAX_SMITHY_CARDS = 1, MAX_WORKSHOP_CARDS = 2, MAX_CELLAR_CARDS = 2, MAX_GOLD_CARDS = 6,
			MAX_SILVER_CARDS = 4, MAX_COOPER_CARDS = 6, MIN_TIME_BEFORE_EXECUTING = 100,
			MAX_TIME_BEFORE_EXECUTING = 900;
	private int numbuys = 0, numvillagecards, numsmithycards, numworkshopcards, numcellarcards, numgoldcards,
			numsilvercards, numcoopercards;
	private ArrayList<String> actioncardlist = new ArrayList<String>(5);
//	private Arrays[] array;

	public Bot(String name) {
		super(name);
		//Fill Array with names or get Names from DB?
	}

	// Random BotName
	//public String getBotName(){
	//	String name;
	//  return name;
		
	//}
	
	/**
	 * executes the Bot with all its stages: play, buy and cleanup
	 * 
	 * @throws InterruptedException
	 */
	public void execute() {
		try {
			this.actualPhase = "play";
			for (int i = 0; i < handCards.size(); i++) {
				if (handCards.get(i).getType().equals(CardType.Action)) {
					actioncardlist.add(handCards.get(i).getCardName().toString());
				}
			}
			while (!actioncardlist.isEmpty()) {
				makeabreak();
				playActionCards();
			}
		} catch (Exception e) {
			skipPhase();
			e.getMessage().toString();
			System.out.println(e);
		}
		skipPhase();// next step buy phase... (Method necessary?)

		try {
			while (buys != 0) { // number of buys where to change?
				makeabreak();
				if (numbuys <= NUM_STAGE_1)
					buyTreasureCards();
				else if (numbuys <= NUM_STAGE_2)
					buyActionCards();
				else if (numbuys <= NUM_STAGE_3)
					buyMixedCards();
				else
					buyVictoryCards(); // after 16 buys --> only buy Victory_Cards

			}
			numbuys++;

		} catch (Exception e) {
			skipPhase();
			e.getMessage().toString();
			System.out.println(e);
		}
		// clean up phase
		makeabreak();
		cleanUp();
	}

	/**
	 * wait for a few milliseconds before executing the next step
	 * 
	 * @throws InterruptedException
	 */
	private void makeabreak() {
		Random rand = new Random();
		int time = rand.nextInt((MAX_TIME_BEFORE_EXECUTING - MIN_TIME_BEFORE_EXECUTING) + 1)
				+ MIN_TIME_BEFORE_EXECUTING;
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * plays Action_Cards according some rules of priority
	 */
	private void playActionCards() {
		while (actions >= 1) {
			CardName name = null;
			int index = 0;
			if (actioncardlist.contains("Village_Card")) {
				name = CardName.Village;
				index = handCards.indexOf(CardName.Village);
			} else if (actioncardlist.contains("Smithy_Card")) {
				name = CardName.Smithy;
				index = handCards.indexOf(CardName.Smithy);
			} else if (actioncardlist.contains("Workshop_Card")) {
				name = CardName.Workshop;
				index = handCards.indexOf(CardName.Workshop);
			} else if (actioncardlist.contains("Cellar_Card")) {
				name = CardName.Cellar;
				index = handCards.indexOf(CardName.Cellar);
			}
			play(name, index);
		}
	}

	/**
	 * buys mixed cards
	 */
	private void buyMixedCards() {
		if (coins >= 8) {
			buyVictoryCards(); // more than 8 coins --> buy Province_Card
		} else {
			switch (coins) {
			case 7: // treasurecard else actioncard else coopercard
				buyTreasureCards();
				break;
			case 6:
				if (numgoldcards <= MAX_GOLD_CARDS) // if Bot has 6 coins, always try to buy a Gold_Card
					buyTreasureCards();
				else
					buyActionCards();
				break;
			case 5:
				if (numvillagecards <= MAX_VILLAGE_CARDS)
					buyActionCards();
				else
					buyVictoryCards();
				break;
			case 4:
				buyActionCards();
				break;
			case 3:
				if (numsilvercards <= MAX_SILVER_CARDS)
					buyTreasureCards();
				else
					buyActionCards();
				break;
			case 2:
				buyActionCards();
				break;
			case 1:
				buyTreasureCards();
				break;
			default:
				buyTreasureCards();
				break;
			}
		}
	}

	/**
	 * buys Action_Cards and if the limit of each Action_Card is reached, it will
	 * buy Treasure_Cards
	 */
	private void buyActionCards() {
		if (numvillagecards <= MAX_VILLAGE_CARDS && coins >= 3) {
			buy(CardName.Village);
			numvillagecards++;
		} else if (numsmithycards <= MAX_SMITHY_CARDS && coins >= 4) {
			buy(CardName.Smithy);
			numsmithycards++;
		} else if (numworkshopcards <= MAX_WORKSHOP_CARDS && coins >= 3) {
			buy(CardName.Workshop);
			numworkshopcards++;
		} else if (numcellarcards <= MAX_CELLAR_CARDS && coins >= 2) {
			buy(CardName.Cellar);
			numcellarcards++;
		} else {
			buyTreasureCards(); // if Bot has reached maximum Action_Cards, only buy Treasure_Cards
		}
	}

	/**
	 * buys Treasure_Cards
	 */
	private void buyTreasureCards() {
		if (coins >= 6) {
			buy(CardName.Gold);
			numgoldcards++;
		} else if (coins >= 3) {
			buy(CardName.Silver);
			numsilvercards++;
		} else {
			buy(CardName.Copper); // a Cooper_Card is free --> so this is always the ultimate possibility
			numcoopercards++;
		}
	}

	/**
	 * buys Victory_Cards and if that is not possible, it will buy Treasure_Cards
	 */
	private void buyVictoryCards() {
		if (coins >= 8)
			buy(CardName.Province);
		else if (coins >= 5)
			buy(CardName.Duchy);
		else if (coins >= 2)
			buy(CardName.Estate);
		else
			buyTreasureCards(); // if no Victory_Card can be bought --> buy a Cooper_Card
	}
}// end Bot