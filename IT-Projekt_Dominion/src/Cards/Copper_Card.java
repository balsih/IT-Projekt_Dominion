package Cards;

import Messages.UpdateGame_Message;
import Server_GameLogic.Game;
import Server_GameLogic.Player;

/**
 * @author René
 * @version 1.0
 * @created 31-Okt-2017 16:58:01
 */
public class Copper_Card extends Treasure_Card {


	public Copper_Card(){
		this.cardName = CardName.Copper;
		this.cost = 0;
		this.coinValue = 1;
	}

	/**
	 * 
	 * @param player
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
	

}//end Bronce_Card