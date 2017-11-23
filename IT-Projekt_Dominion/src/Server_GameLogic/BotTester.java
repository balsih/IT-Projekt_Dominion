package Server_GameLogic;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class BotTester {

	public static void main(String[] args) {
		// Bot b = new Bot(Bot.getNameOfBot());
		// String test = Bot.getNameOfBot();
		System.out.println("test");
		// makeBreak();
		System.out.println("test");

		DecimalFormat fmt = new DecimalFormat("0.0000");
		double z1 = 2.0 / 10.0;
		System.out.println(fmt.format(z1));
		if (z1 < 0.35) {
			System.out.println("true");
		}
	}
}
