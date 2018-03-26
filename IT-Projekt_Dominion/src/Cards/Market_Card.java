package Cards;

import Messages.UpdateGame_Message;
import Server_GameLogic.Player;

/**
 * Market represents a action card and costs 5. 
 * 
 * @author Rene Schwab
 * 
 */
public class Market_Card extends Card {

	public Market_Card(){
		this.cardName = CardName.Market;
		this.cost = 5;
		this.type = CardType.Action;
	}

	/**
	 * Player gets 1 handcard from the deck pile, 1 action, 1 buy and 1 coin.   
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
		
		UpdateGame_Message ugmsg = player.draw(1); // draw 1 card
		player.setActions(player.getActions() + 1);
		player.setBuys(player.getBuys() + 1);
		player.setCoins(player.getCoins() + 1);		
		
		ugmsg.setLog(player.getPlayerName()+": #received# #Market1#=="+player.getPlayerName()+": #played# #"+this.cardName.toString()+"# #card#");
		
		// update game Messages -> XML 
		ugmsg.setActions(player.getActions());
		ugmsg.setBuys(player.getBuys());
		ugmsg.setCoins(player.getCoins());
		ugmsg.setPlayedCards(this);
		
		return ugmsg;
	}
	
}//end Market_Card