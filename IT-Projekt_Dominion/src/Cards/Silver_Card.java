package Cards;

import Server_GameLogic.Player;

/**
 * @author René
 * @version 1.0
 * @created 31-Okt-2017 16:58:12
 */
public class Silver_Card extends Treasure_Card {


	public Silver_Card(){
		this.cardName = "Silver";
		this.cost = 3;
		this.type = "treasure";
	}

	/**
	 * 
	 * @param player
	 */
	@Override
	public void executeCard(Player player){
		player.setCoins(player.getCoins() + 2); // ?

	}
}//end Silver_Card