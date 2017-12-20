package Cards;

import Messages.UpdateGame_Message;
import Server_GameLogic.Game;
import Server_GameLogic.Player;

/**
 * @author René
 * @version 1.0
 * @created 31-Okt-2017 16:58:09
 */
public class Market_Card extends Card {


	public Market_Card(){
		this.cardName = CardName.Market;
		this.cost = 5;
		this.type = CardType.Action;
	}

	/**
	 * 
	 * @param player
	 */
	@Override
	public UpdateGame_Message executeCard(Player player){
		
		UpdateGame_Message ugmsg = player.draw(1); // draw 1 card
		player.setActions(player.getActions() + 1);
		player.setBuys(player.getBuys() + 1);
		player.setCoins(player.getCoins() + 1);		
		
		ugmsg.setLog(player.getPlayerName()+": #played# #"+this.cardName.toString()+"# #card#=="+player.getPlayerName()+": #received# #Market1#");
		
		// update game Messages -> XML 
		ugmsg.setActions(player.getActions());
		ugmsg.setBuys(player.getBuys());
		ugmsg.setCoins(player.getCoins());
		ugmsg.setPlayedCards(this);
		
		return ugmsg;
	}
	
}//end Market_Card