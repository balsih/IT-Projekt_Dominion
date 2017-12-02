package Cards;

import Messages.UpdateGame_Message;
import Server_GameLogic.Game;
import Server_GameLogic.Player;

/**
 * @author René
 * @version 1.0
 * @created 31-Okt-2017 16:58:10
 */
public class Province_Card extends Victory_Card {


	public Province_Card(){
		this.cardName = CardName.Province;
		this.cost = 8;
		this.victoryPoints = 6;
	}

	/**
	 * 
	 * @param player
	 */
	@Override
	public UpdateGame_Message executeCard(Player player){
		player.setVictoryPoints(player.getVictoryPoints() + victoryPoints);
		
		return null;
	}

	
}//end Province_Card