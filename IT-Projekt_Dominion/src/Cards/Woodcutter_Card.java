package Cards;

import Messages.UpdateGame_Message;
import Server_GameLogic.Player;

/**
 * Woodcutter represents a action card and costs 3. 
 * 
 * @author Rene Schwab
 * 
 */
public class Woodcutter_Card extends Card {

	public Woodcutter_Card(){
		this.cardName = CardName.Woodcutter;
		this.cost = 3;
		this.type = CardType.Action;
	}

	/**
	 * Player gets 1 buy and 2 coins.  
	 * Changes related with the card get set in the UpdateGame_Message
	 * 
	 * @author Rene Schwab
	 * 
	 * @param player
	 * , current player 
	 * @return UpdateGame_Message
	 * , containing changes related with this card. 
	 */
	public UpdateGame_Message executeCard(Player player){
		
		player.setCoins(player.getCoins() + 2);
		player.setBuys(player.getBuys() + 1);
		
		UpdateGame_Message ugmsg = new UpdateGame_Message();
		ugmsg.setLog(player.getPlayerName()+": #received# #Woodcutter1#=="+player.getPlayerName()+": #played# #"+this.cardName.toString()+"# #card#");
		
		// update game Messages -> XML 
		ugmsg.setBuys(player.getBuys());
		ugmsg.setCoins(player.getCoins());
		ugmsg.setPlayedCards(this);
		
		return ugmsg;
	}
	
}//end Woodcutter_Card