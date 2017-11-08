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
		this.cardName = "Market_Card";
		this.cost = 5;
		this.type = "action";
	}

	/**
	 * 
	 * @param player
	 */
	@Override
	public UpdateGame_Message executeCard(Player player){
		
		player.setCoins(player.getCoins() + 1);
		player.setBuys(player.getBuys() + 1);
		player.draw(1); // draw 1 card
		
		Game game = player.getGame();
		UpdateGame_Message ugmsg = new UpdateGame_Message();
		
		ugmsg.setLog(player.getPlayerName()+": played Market card");
		game.sendToOpponent(player, ugmsg); // info for opponent
		
		// update game Messages -> XML 
		ugmsg.setActions(player.getActions());
		ugmsg.setBuys(player.getBuys());
		ugmsg.setCoins(player.getCoins());
		
		return ugmsg;
	}
	
}//end Market_Card