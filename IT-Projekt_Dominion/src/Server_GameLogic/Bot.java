package Server_GameLogic;

/**
 * @author Simon
 * @version 1.0
 * @created 31-Okt-2017 17:08:40
 */
public class Bot extends Player {

	// private String actualbuy;
	private int numturns = 0;
	private final int NUMSTAGE_1 = 4;
	private final int NUMSTAGE_2 = 7;
	private final int MAXVILLAGECARDS = 2;
	private final int MAXSMITHYCARDS = 1;
	private final int MAXWORKSHOPCARDS = 2;
	private final int MAXCELLARCARDS = 2;
	private int numvillagecards;
	private int numsmithycards;
	private int numworkshopcards;
	private int numcellarcards;

	public Bot(String name) {
		super(name);
	}

	// main method
	public void execute() {
		// action phase
		//...
		// COUNT COINS AND BUYS!
		
		
		// buy phase
		while (buys != 0) {
			if (numturns < NUMSTAGE_1) {
				buytreasurecards();
			} else {
				if (numturns < NUMSTAGE_2) {
					buyactioncards();
				} else {
					switch (coins) {
					case 8:
						Player.buy(Province_Card);
						break;
					case 7:
						Player.buy(Gold_Card);
						break;
					case 6:
						Player.buy(Gold_Card);
						break;
					case 5:
						Player.buy(Duchy_Card);
						break;
					case 4:
						buyactioncards();
						break;
					case 3:
						buyactioncards();
						break;
					case 2:
						buyactioncards();
						break;
					case 1:
						Player.buy(Bronze_Card);
						break;
					default:
						Player.buy(Bronze_Card);
						break;

					}

				}

			}
		}
		numturns++;

	}// end of execute methode

// helper methods
	private void buytreasurecards() {
		if (coins >= 6) {
			Player.buy(Gold_Card);
		} else {
			if (coins >= 3) {
				Player.buy(Silver_Card);
			} else {
				if (coins == 2) {
					if (numcellarcards < MAXCELLARCARDS)
						Player.buy(Cellar_Card);
				} else {
					Player.buy(Bronze_Card);
				}
			}
		}

	}

	private void buyactioncards() {
		if (coins >= 8) {
			Player.buy(Province_Card);
		} else {
			if (coins >= 6) {
				Player.buy(Gold_Card);
			} else {
				if (numvillagecards < MAXVILLAGECARDS && coins >= 3) {
					Player.buy(Village_Card);
				} else {
					if (numsmithycards < MAXSMITHYCARDS && coins >= 4) {
						Player.buy(Smithy_Card);
					} else {
						if (numworkshopcards < MAXWORKSHOPCARDS && coins >= 3) {
							Player.buy(Workshop_Card);
						} else {
							buytreasurecards();
						}
					}
				}
			}
		}
	}
}// end Bot