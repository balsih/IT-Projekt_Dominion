package Cards;

import Messages.UpdateGame_Message;
import Server_GameLogic.Game;
import Server_GameLogic.Player;

/**
 * @author René
 * @version 1.0
 * @created 31-Okt-2017 16:58:17
 */
public class Workshop_Card extends Card {


	public Workshop_Card(){
		this.cardName = "Workshop_Card";
		this.cost = 3;
		this.type = "action";

	}

	/**
	 * 
	 * @param player
	 */
	@Override
	public UpdateGame_Message executeCard(Player player){
		player.setActions(player.getActions() - 1);
		
		
		
		// noch fehlender Code bzw. Funktionalität 
		
		Game game = player.getGame();
		UpdateGame_Message ugmsg = new UpdateGame_Message();
				
		ugmsg.setLog(player.getPlayerName()+": played Workshop card");
		game.sendToOpponent(player, ugmsg); // info for opponent
				
		// update game Messages -> XML 
		ugmsg.setActions(player.getActions());
		ugmsg.setBuys(player.getBuys());
		ugmsg.setCoins(player.getCoins());
				
		return ugmsg;
	}
}//end Workshop_Card