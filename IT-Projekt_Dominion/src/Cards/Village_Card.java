package Cards;

import Messages.UpdateGame_Message;
import Server_GameLogic.Player;

/**
 * Village represents a action card and costs 3. 
 * 
 * @author Rene Schwab
 * 
 */
public class Village_Card extends Card {

	public Village_Card(){
		this.cardName = CardName.Village;
		this.cost = 3;
		this.type = CardType.Action;
	}
	
	/**
	 * Player gets 1 handcard from the deck pile and 2 actions.   
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
		
		player.setActions(player.getActions() + 2);
		UpdateGame_Message ugmsg = player.draw(1);
		
		//ugmsg.setLog(player.getPlayerName()+": #played# #"+this.cardName.toString()+"# #card#=="+player.getPlayerName()+": #received# #Village1#");
		
		ugmsg.setLog(player.getPlayerName()+": #received# #Village1#=="+player.getPlayerName()+": #played# #"+this.cardName.toString()+"# #card#");
		
		// update game Messages -> XML 
		ugmsg.setPlayedCards(this);
		
		return ugmsg;
	}
	
}//end Village_Card