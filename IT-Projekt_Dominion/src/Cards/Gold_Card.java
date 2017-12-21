package Cards;

import Messages.UpdateGame_Message;
import Server_GameLogic.Player;

/**
 * Gold represents a treasure card. This card has a coin value of 3 and costs 6. 
 * 
 * @author Rene Schwab
 * 
 */
public class Gold_Card extends Treasure_Card {

	public Gold_Card(){
		this.cardName = CardName.Gold;
		this.cost = 6;
		this.coinValue = 3;
	}
	
	/**
	 * Coins of the current player get increased by 3.  
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
	@Override
	public UpdateGame_Message executeCard(Player player){
		player.setCoins(player.getCoins() + coinValue); // increment coin value
		
		UpdateGame_Message ugmsg = new UpdateGame_Message();
		
		ugmsg.setLog(player.getPlayerName()+": #played# #"+this.cardName.toString()+"# #card#");
		
		// update game Messages -> XML 
		ugmsg.setCoins(player.getCoins());
		ugmsg.setPlayedCards(this);
		
		return ugmsg;
	}
	
}//end Gold_Card