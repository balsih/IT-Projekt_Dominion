package Cards;

import Messages.UpdateGame_Message;
import Server_GameLogic.Player;

/**
 * Duchy represents a victory card. This card is worth 3 victory points and costs 5. 
 * 
 * @author Rene Schwab
 * 
 */
public class Duchy_Card extends Victory_Card {

	public Duchy_Card(){
		this.cardName = CardName.Duchy;
		this.cost = 5;
		this.victoryPoints = 3;
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

}//end Duchy_Card