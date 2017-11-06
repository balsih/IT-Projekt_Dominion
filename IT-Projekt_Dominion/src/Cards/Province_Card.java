package Cards;

import Server_GameLogic.Player;

/**
 * @author René
 * @version 1.0
 * @created 31-Okt-2017 16:58:10
 */
public class Province_Card extends Victory_Card {


	public Province_Card(){
		this.cardName = "Province";
		this.cost = 8;
		this.type = "victory";
		this.victoryPoints = 6;
	}

	/**
	 * 
	 * @param player
	 */
	@Override
	public void executeCard(Player player){
		player.setVictoryPoints(player.getVictoryPoints() + victoryPoints);
	}
	
}//end Province_Card