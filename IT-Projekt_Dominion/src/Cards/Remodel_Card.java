package Cards;

import Messages.Message;
import Messages.UpdateGame_Message;
import Server_GameLogic.Game;
import Server_GameLogic.Player;

/**
 * @author René
 * @version 1.0
 * @created 31-Okt-2017 16:58:11
 */
public class Remodel_Card extends Card {


	public Remodel_Card(){
		this.cardName = "Remodel_Card";
		this.cost = 4;
		this.type = "action";
	}

	/**
	 * 
	 * @param player
	 */
	@Override
	public UpdateGame_Message executeCard(Player player){
		
		player.setActions(player.getActions() - 1);
		
		//ugmsg.setLog(player.getPlayerName()+": choose a Card to get rid of!"); // usw.
		
		
		
		// noch fehlender Code bzw. Funktionalität 
		
		Game game = player.getGame();
		UpdateGame_Message ugmsg = new UpdateGame_Message();
				
		ugmsg.setLog(player.getPlayerName()+": played Remodel card");
		game.sendToOpponent(player, ugmsg); // info for opponent
				
		// update game Messages -> XML 
		ugmsg.setActions(player.getActions());
		ugmsg.setBuys(player.getBuys());
		ugmsg.setCoins(player.getCoins());
				
		return ugmsg;
	}
}//end Remodel_Card