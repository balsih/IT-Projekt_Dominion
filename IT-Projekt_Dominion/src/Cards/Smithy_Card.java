package Cards;

import Messages.UpdateGame_Message;
import Server_GameLogic.Player;

/**
 * Smithy represents a action card and costs 4. 
 * 
 * @author Rene Schwab
 * 
 */
public class Smithy_Card extends Card {

	public Smithy_Card(){
		this.cardName = CardName.Smithy;
		this.cost = 4;
		this.type = CardType.Action;
	}

	/**
	 * Player gets 3 additional handcards from the deck pile.  
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
		
		UpdateGame_Message ugmsg = player.draw(3); // draw 3 cards
		
		ugmsg.setLog(player.getPlayerName()+": #received# #Smithy1#=="+player.getPlayerName()+": #played# #"+this.cardName.toString()+"# #card#");
		ugmsg.setPlayedCards(this);
		
		return ugmsg;
	}
	
}//end Smithy_Card