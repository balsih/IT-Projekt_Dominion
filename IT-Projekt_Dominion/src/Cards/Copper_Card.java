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
	}

	/**
	 * 
	 * @param player
	 */
	@Override
	public void executeCard(Player player){
		player.setCoins(player.getCoins() + 1); // ?

	}
}//end Bronce_Card