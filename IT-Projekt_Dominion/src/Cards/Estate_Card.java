package Cards;

import Server_GameLogic.Player;

/**
 * @author René
 * @version 1.0
 * @created 31-Okt-2017 16:58:07
 */
public class Estate_Card extends Victory_Card {


	public Estate_Card(){
		this.cardName = "Estade";
		this.cost = 2;
		this.type = "victory";
		this.victoryPoints = 1;
	}

	/**
	 * 
	 * @param player
	 */
	@Override
	public void executeCard(Player player){
		player.setVictoryPoints(player.getVictoryPoints() + victoryPoints);
	}
	
}//end Estate_Card