package Server_GameLogic;

public class BotTester {

	public static void main(String[] args) {
		
		Game existingGame = new Game();
		existingGame = Game.getGame(GameMode.Simulation, null);
		existingGame.startGame();
		System.out.println(existingGame.getPlayer1());
		System.out.println(existingGame.getPlayer2());
		System.out.println("Game started");
		//Bot b1 = (Bot) existingGame.getPlayer1();
		//b1.run();
		

//		Bot b = new Bot(Bot.getNameOfBot());
//		String test = Bot.getNameOfBot();
//		System.out.println("test");
//		makeBreak();
//		System.out.println("test");
//
//		DecimalFormat fmt = new DecimalFormat("0.0000");
//		double z1 = 2.0 / 10.0;
//		System.out.println(fmt.format(z1));
//		if (z1 < 0.35) {
//			System.out.println("true");
//
//		}
//		int test = 10;
//		test -= 2;
//		test -= 2;
//		System.out.println(test);
//
//		boolean test9 = false;
//		if (!test9)
//			System.out.println("ja");
//
//		do {
//			System.out.println("nein");
//		} while (test9);
//
//		HashMap<CardName, Integer> prioListForBuying = new HashMap<CardName, Integer>();
//		prioListForBuying.put(CardName.Market, 20);
//		System.out.println(prioListForBuying.size());
//		prioListForBuying.remove(CardName.Market);
//		System.out.println(prioListForBuying.size());
//		prioListForBuying.remove(CardName.Market);
//		System.out.println(prioListForBuying.size());
//
//		ArrayList<CardName> test2 = new ArrayList<CardName>();
//		test2.add(CardName.Cellar);
//		test2.add(CardName.Copper);
//		test2.add(CardName.Duchy);
//		System.out.println(test2.toString());
//		for (int i = test2.size(); i > 0; i--) {
//			test2.remove(0);
//		}
//		System.out.println(test2.toString());
	}

}