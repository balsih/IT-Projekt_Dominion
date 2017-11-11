package Server_GameLogic;

import java.util.ArrayList;
import Cards.Card;

/**
 * @author Simon
 * @version 2.0
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

	public Bot(String name) {
		super(name);
	}

	// main method
	public void execute() {
		// action phase
		try {
			this.actualPhase = "play";
			for (int i = 0; i < handCards.size(); i++) {
				if (handCards.get(i).getType().equals("action")) {
					actioncardlist.add(handCards.get(i).getCardName());
				}
			}
			while (!actioncardlist.isEmpty()) {
				playActionCards();
			}
		} catch (Exception e) {
			skipPhase();
			e.getMessage().toString();
			System.out.println(e);
		} // next step buy phase... (Method necessary?)
		skipPhase();

		// buy phase
		try {
			while (buys != 0) { // number of buys where to change?
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
		cleanUp();
	} // end of execute method

	// helper methods
	private void playActionCards() {
		while (actions >= 1) {
			String stringCase = null;
			if (actioncardlist.contains("Village_Card")) {
				stringCase = "Village_Card";
			} else if (actioncardlist.contains("Smithy_Card")) {
				stringCase = "Smithy_Card";
			} else if (actioncardlist.contains("Workshop_Card")) {
				stringCase = "Workshop";
			} else if (actioncardlist.contains("Cellar_Card")) {
				stringCase = "Cellar_Card";
			}

			// René fragen
			Card playedCard = null;
			int index;
			switch (stringCase) {
			case "Village_Card":
				index = this.handCards.indexOf("Village_Card");
				playedCard = this.handCards.remove(index);
				playedCard.executeCard(this);
				playedCards.add(playedCard);
				actioncardlist.remove("Village_Card");
				break;
			case "Smithy_Card":
				index = this.handCards.indexOf("Smithy_Card");
				playedCard = this.handCards.remove(index);
				playedCard.executeCard(this);
				playedCards.add(playedCard);
				actioncardlist.remove("Smithy_Card");
				break;
			case "Workshop_Card":
				index = this.handCards.indexOf("Workshop_Card");
				playedCard = this.handCards.remove(index);
				playedCard.executeCard(this);
				playedCards.add(playedCard);
				actioncardlist.remove("Workshop_Card");
				break;
			case "Cellar_Card":
				index = this.handCards.indexOf("Cellar_Card");
				playedCard = this.handCards.remove(index);
				playedCard.executeCard(this);
				playedCards.add(playedCard);
				actioncardlist.remove("Cellar_Card");
				break;
			default:
				actioncardlist.clear();
				actions = 0;
			}

		}
	}

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
				buyVictoryCards(); // sure about that?
				break;
			case 4:
				buyActionCards(); // could buy a SilverCard! Change this!
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

	private void buyActionCards() {
		if (numvillagecards <= MAX_VILLAGE_CARDS && coins >= 3) {
			buy("Village_Card");
			numvillagecards++;
		} else if (numsmithycards <= MAX_SMITHY_CARDS && coins >= 4) {
			buy("Smithy_Card");
			numsmithycards++;
		} else if (numworkshopcards <= MAX_WORKSHOP_CARDS && coins >= 3) {
			buy("Workshop_Card");
			numworkshopcards++;
		} else if (numcellarcards <= MAX_CELLAR_CARDS && coins >= 2) {
			buy("Cellar_Card");
			numcellarcards++;
		} else {
			buyTreasureCards(); // if Bot has reached maximum Action_Cards, only buy Treasure_Cards
		}
	}

	private void buyTreasureCards() {
		if (coins >= 6) {
			buy("Gold_Card");
			numgoldcards++;
		} else if (coins >= 3) {
			buy("Silver_Card");
			numsilvercards++;
		} else {
			buy("Cooper_Card"); // a Cooper_Card is free --> so this is always the ultimate possibility
			numcoopercards++;
		}
	}

	private void buyVictoryCards() {
		if (coins >= 8)
			buy("Province_Card");
		else if (coins >= 5)
			buy("Duchy_Card");
		else if (coins >= 2)
			buy("Estade_Card");
		else
			buyTreasureCards(); // if no Victory_Card can be bought --> buy a Cooper_Card
	}
}// end Bot