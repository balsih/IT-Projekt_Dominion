package Cards;

import Messages.UpdateGame_Message;
import Server_GameLogic.Player;

/**
 * @author René
 * @version 1.0
 * @created 31-Okt-2017 16:58:12
 */
public class Silver_Card extends Treasure_Card {


	public Silver_Card(){
		this.cardName = "Silver_Card";
		this.cost = 3;
		this.type = "treasure";
		this.coinValue = 2;
	}

	/**
	 * 
	 * @param player
	 */
	@Override
	public UpdateGame_Message executeCard(Player player){
		player.setCoins(player.getCoins() + coinValue); // increment coin value

	}
}//end Silver_Card