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
		this.cardName = "Woodcutter";
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
		player.setCoins(player.getCoins() + 2);
		player.setBuys(player.getBuys() + 1);
		
		Game game = player.getGame();
		UpdateGame_Message ugmsg = new UpdateGame_Message();
		
		ugmsg.setLog(player.getPlayerName()+": played Woodcutter card");
		
		ugmsg.setActions(player.getActions());
		ugmsg.setBuys(player.getBuys());
		ugmsg.setCoins(player.getCoins());
		
		return ugmsg;
	}
	
}//end Woodcutter_Card