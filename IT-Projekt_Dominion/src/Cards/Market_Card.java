package Cards;

import Server_GameLogic.Player;

/**
 * @author René
 * @version 1.0
 * @created 31-Okt-2017 16:58:09
 */
public class Market_Card extends Card {


	public Market_Card(){
		this.cardName = "Market";
		this.cost = 5;
		this.type = "action";
	}

	/**
	 * 
	 * @param player
	 */
	@Override
	public void executeCard(Player player){
		player.setCoins(player.getCoins() + 1);
		player.setBuys(player.getBuys() + 1);
	}
	
}//end Market_Card