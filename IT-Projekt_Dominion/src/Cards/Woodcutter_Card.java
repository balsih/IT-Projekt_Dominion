package Cards;

import Messages.UpdateGame_Message;
import Server_GameLogic.Game;
import Server_GameLogic.Player;

/**
 * @author René
 * @version 1.0
 * @created 31-Okt-2017 16:58:16
 */
public class Woodcutter_Card extends Card {


	public Woodcutter_Card(){
		this.cardName = CardName.Woodcutter;
		this.cost = 3;
		this.type = CardType.Action;
	}

	/**
	 * 
	 * @param player
	 */
	@Override
	public UpdateGame_Message executeCard(Player player){
		
		player.setCoins(player.getCoins() + 2);
		player.setBuys(player.getBuys() + 1);
		
		UpdateGame_Message ugmsg = new UpdateGame_Message();
		
		ugmsg.setLog(player.getPlayerName()+": #played# #"+this.cardName.toString()+"# #card#=="+player.getPlayerName()+": #received# #Woodcutter1#");
		
		// update game Messages -> XML 
		ugmsg.setBuys(player.getBuys());
		ugmsg.setCoins(player.getCoins());
		ugmsg.setPlayedCards(this);
		
		return ugmsg;
	}
	
}//end Woodcutter_Card