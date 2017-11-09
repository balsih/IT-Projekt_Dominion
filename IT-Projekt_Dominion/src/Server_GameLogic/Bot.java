package Server_GameLogic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

import Cards.Card;

/**
 * @author Simon
 * @version 1.0
 * @created 31-Okt-2017 17:08:40
 */
public class Bot extends Player {

	private static final int NUMSTAGE_1 = 4;
	private static final int NUMSTAGE_2 = 7;
	private static final int MAXVILLAGECARDS = 2;
	private static final int MAXSMITHYCARDS = 1;
	private static final int MAXWORKSHOPCARDS = 2;
	private static final int MAXCELLARCARDS = 2;
	private int numturns = 0;
	private int numvillagecards;
	private int numsmithycards;
	private int numworkshopcards;
	private int numcellarcards;
	private ArrayList<String> actioncardlist = new ArrayList<String>(5);

	public Bot(String name) {
		super(name);
	}

	// main method
	public void execute() {
		// action phase
		if (actions >= 1) {
			for (int i = 0; i < handCards.size(); i++) {
				String tempCard = null;
				if (handCards.get(i).getType().equals("action")) {
					tempCard = handCards.get(i).getCardName();
					actioncardlist.add(tempCard);
				}
			}
		} else {
			skipPhase();
		}

		// buy phase
		try {
			while (buys != 0) {
				if (numturns < NUMSTAGE_1) {
					buyTreasureCards();
				} else {
					if (numturns < NUMSTAGE_2) {
						buyActionCards();
					} else {
						buyMixedCards();

					}

				}
			}
			numturns++;

		} catch (Exception e) {
			skipPhase();
			numturns++;
			e.getMessage().toString();
			System.out.println(e);
		}
		// clean up phase
		cleanUp();
	} // end of execute methode

	// helper methods
	private void buyTreasureCards() {
		if (coins >= 6) {
			buy("Gold_Card");
		} else {
			if (coins >= 3) {
				buy("Silver_Card");
			} else {
				if (coins == 2) {
					if (numcellarcards < MAXCELLARCARDS)
						buy("Cellar_Card");
				} else {
					buy("Bronze_Card");
				}
			}
		}

	}

	private void buyMixedCards() {
		switch (coins) {
		case 8:
			buy("Province_Card");
			break;
		case 7:
			buy("Gold_Card");
			break;
		case 6:
			buy("Gold_Card");
			break;
		case 5:
			buy("Duchy_Card");
			break;
		case 4:
			buyActionCards();
			break;
		case 3:
			buyActionCards();
			break;
		case 2:
			buyActionCards();
			break;
		case 1:
			buy("Bronze_Card");
			break;
		default:
			buy("Bronze_Card");
			break;
		}
	}

	private void buyActionCards() {
		if (coins >= 8) {
			buy("Province_Card");
		} else {
			if (coins >= 6) {
				buy("Gold_Card");
			} else {
				if (numvillagecards < MAXVILLAGECARDS && coins >= 3) {
					buy("Village_Card");
				} else {
					if (numsmithycards < MAXSMITHYCARDS && coins >= 4) {
						buy("Smithy_Card");
					} else {
						if (numworkshopcards < MAXWORKSHOPCARDS && coins >= 3) {
							buy("Workshop_Card");
						} else {
							buyTreasureCards();
						}
					}
				}
			}
		}
	}
}// end Bot