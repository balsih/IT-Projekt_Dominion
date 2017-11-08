package Cards;

import Messages.UpdateGame_Message;
import Server_GameLogic.Player;

/**
 * @author René
 * @version 1.0
 * @created 31-Okt-2017 16:58:08
 */
public class Gold_Card extends Treasure_Card {


	public Gold_Card(){
		this.cardName = "Gold_Card";
		this.cost = 6;
		this.type = "treasure";
		this.coinValue = 3;
	}

	/**
	 * 
	 * @param player
	 */
	@Override
	public UpdateGame_Message executeCard(Player player){
		player.setCoins(player.getCoins() + coinValue); // increment coin value
	}
	
}//end Gold_Card