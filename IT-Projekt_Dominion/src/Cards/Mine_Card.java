package Cards;

import Server_GameLogic.Player;

/**
 * @author René
 * @version 1.0
 * @created 31-Okt-2017 16:58:09
 */
public class Mine_Card extends Card {


	public Mine_Card(){
		this.cardName = "Mine";
		this.cost = 5;
		this.type = "action";
	}

	/**
	 * 
	 * @param player
	 */
	@Override
	public void executeCard(Player player){
		player.setActions(player.getActions() - 1);
		player.setCoins(player.getCoins() ); //?
		// eine geldkarte entsorgen und eine andere aufnehmen in die hand

	}
	
}//end Mine_Card