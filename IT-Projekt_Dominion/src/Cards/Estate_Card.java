package Cards;

import Messages.UpdateGame_Message;
import Server_GameLogic.Player;

/**
 * Estate represents a victory card. This card is worth 1 victory points and costs 3. 
 * 
 * @author Rene Schwab
 * 
 */
public class Estate_Card extends Victory_Card {

	public Estate_Card(){
		this.cardName = CardName.Estate;
		this.cost = 2;
		this.victoryPoints = 1;
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
	
}//end Estate_Card