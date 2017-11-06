package Cards;

import Server_GameLogic.Player;

/**
 * @author René
 * @version 1.0
 * @created 31-Okt-2017 16:58:08
 */
public class Gold_Card extends Treasure_Card {


	public Gold_Card(){
		this.cardName = "Gold";
		this.cost = 6;
		this.type = "treasure";
		this.coinValue = 3;
	}

	/**
	 * 
	 * @param player
	 */
	@Override
	public void executeCard(Player player){
		player.setCoins(player.getCoins() + coinValue);
	}
	
}//end Gold_Card