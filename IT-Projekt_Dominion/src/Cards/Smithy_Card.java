package Cards;

import Messages.UpdateGame_Message;
import Server_GameLogic.Game;
import Server_GameLogic.Player;

/**
 * @author René
 * @version 1.0
 * @created 31-Okt-2017 16:58:13
 */
public class Smithy_Card extends Card {


	public Smithy_Card(){
		this.cardName = CardName.Smithy;
		this.cost = 4;
		this.type = CardType.Action;
	}

	/**
	 * 
	 * @param player
	 */
	@Override
	public UpdateGame_Message executeCard(Player player){
		
		UpdateGame_Message ugmsg = player.draw(3); // draw 3 cards
		
		ugmsg.setLog(player.getPlayerName()+": #played# #"+this.cardName.toString()+"# #card#");
		
		ugmsg.setPlayedCards(this);
		
		return ugmsg;
	}
	
	
}//end Smithy_Card