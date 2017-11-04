package Cards;

import Server_GameLogic.Player;

/**
 * @author René
 * @version 1.0
 * @created 31-Okt-2017 16:58:16
 */
public class Woodcutter_Card extends Card {


	public Woodcutter_Card(){
		this.cardName = "Woodcutter";
		this.cost = 3;
		this.type = "action";
	}

	/**
	 * 
	 * @param player
	 */
	@Override
	public void executeCard(Player player){
		player.setActions(player.getActions() - 1);
		player.setCoins(player.getCoins() + 2);
		player.setBuys(player.getBuys() + 1);

	}
	
}//end Woodcutter_Card