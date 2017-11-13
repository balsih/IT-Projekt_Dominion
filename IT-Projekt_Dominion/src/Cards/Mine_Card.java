package Cards;

import Messages.UpdateGame_Message;
import Server_GameLogic.Game;
import Server_GameLogic.Player;

/**
 * @author René
 * @version 1.0
 * @created 31-Okt-2017 16:58:09
 */
public class Mine_Card extends Card {


	public Mine_Card(){
		this.cardName = CardName.Mine;
		this.cost = 5;
		this.type = CardType.Action;
	}

	/**
	 * 
	 * @param player
	 */
	@Override
	public UpdateGame_Message executeCard(Player player){
		player.setActions(player.getActions() - 1);
		player.setCoins(player.getCoins() ); //?
		
		
		// eine geldkarte entsorgen und eine andere aufnehmen in die hand
		
		
		// noch fehlender Code bzw. Funktionalität 
		
		Game game = player.getGame();
		UpdateGame_Message ugmsg = new UpdateGame_Message();
				
		ugmsg.setLog(player.getPlayerName()+": played Mine card");
		game.sendToOpponent(player, ugmsg); // info for opponent
				
		// update game Messages -> XML 
		ugmsg.setActions(player.getActions());
		ugmsg.setBuys(player.getBuys());
		ugmsg.setCoins(player.getCoins());
				
		return ugmsg;
	}
}//end Mine_Card