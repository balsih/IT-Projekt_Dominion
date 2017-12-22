package Cards;

import Messages.UpdateGame_Message;
import Server_GameLogic.Player;

/**
 * Copper represents a treasure card. This card has a coin value of 1 and costs 0. 
 * 
 * @author Rene Schwab
 * 
 */
public class Copper_Card extends Treasure_Card {

	public Copper_Card(){
		this.cardName = CardName.Copper;
		this.cost = 0;
		this.coinValue = 1;
	}

	/**
	 * Coins of the current player get increased by 1.  
	 * Changes related with the card get set in the UpdateGame_Message
	 * 
	 * @author Rene Schwab
	 * 
	 * @param player
	 * , current player 
	 * @return UpdateGame_Message
	 * , containing all changes related with this card. 
	 * 
	 */
	public UpdateGame_Message executeCard(Player player){
		player.setCoins(player.getCoins() + coinValue); // increment coin value
		
		UpdateGame_Message ugmsg = new UpdateGame_Message();
		
		ugmsg.setLog(player.getPlayerName()+": #played# #"+this.cardName.toString()+"# #card#");
		
		// update game Messages -> XML 
		ugmsg.setCoins(player.getCoins());
		ugmsg.setPlayedCards(this);
		
		return ugmsg;
	}
	
}//end Bronce_Card