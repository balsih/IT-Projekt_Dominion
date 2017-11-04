package Cards;

import Server_GameLogic.Player;

/**
 * @author René
 * @version 1.0
 * @created 31-Okt-2017 16:58:17
 */
public class Workshop_Card extends Card {


	public Workshop_Card(){
		this.cardName = "Workshop";
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
		// Karte aufnehmen mit Wert <= 4
	}
	
}//end Workshop_Card