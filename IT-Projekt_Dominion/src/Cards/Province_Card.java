package Cards;

import Messages.UpdateGame_Message;
import Server_GameLogic.Player;

/**
 * Province represents a victory card. This card is worth 6 victory points and costs 8. 
 * 
 * @author Rene Schwab
 * 
 */
public class Province_Card extends Victory_Card {

	public Province_Card(){
		this.cardName = CardName.Province;
		this.cost = 8;
		this.victoryPoints = 6;
	}
	
	/**
	 * Victory points of the current player get increased by 3.  
	 * 
	 * 
	 * @author Rene Schwab
	 * 
	 * @param player
	 * , current player 
	 * @return null
	 * , no message required, points are set direct on player
	 * 
	 */
	@Override
	public UpdateGame_Message executeCard(Player player){
		player.setVictoryPoints(player.getVictoryPoints() + victoryPoints);
		return null;
	}

}//end Province_Card