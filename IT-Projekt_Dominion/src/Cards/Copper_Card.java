package Cards;

import Server_GameLogic.Player;

/**
 * @author René
 * @version 1.0
 * @created 31-Okt-2017 16:58:01
 */
public class Copper_Card extends Treasure_Card {


	public Copper_Card(){
		this.cardName = "Copper";
		this.cost = 0;
		this.type = "treasure";
		this.coinValue = 1;
	}

	/**
	 * 
	 * @param player
	 */
	@Override
	public void executeCard(Player player){
		player.setCoins(player.getCoins() + coinValue);

	}
}//end Bronce_Card