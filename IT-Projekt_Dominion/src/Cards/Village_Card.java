package Cards;

import Messages.UpdateGame_Message;
import Server_GameLogic.Game;
import Server_GameLogic.Player;

/**
 * @author René
 * @version 1.0
 * @created 31-Okt-2017 16:58:15
 */

public class Village_Card extends Card {

	public Village_Card(){
		this.cardName = CardName.Village;
		this.cost = 3;
		this.type = CardType.Action;
	}
	
	/**
	 * 
	 * @param player
	 */
	@Override
	public UpdateGame_Message executeCard(Player player){
		
		player.setActions(player.getActions() + 2);
		UpdateGame_Message ugmsg = player.draw(1);
		
		ugmsg.setLog(player.getPlayerName()+": #played# #"+this.cardName.toString()+"# #card#=="+player.getPlayerName()+": #received# #Village1#");
		
		// update game Messages -> XML 
		ugmsg.setPlayedCards(this);
		
		return ugmsg;
	}
	
}//end Village_Card