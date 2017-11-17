package Cards;

import Messages.UpdateGame_Message;
import Server_GameLogic.Game;
import Server_GameLogic.Player;

/**
 * @author René
 * @version 1.0
 * @created 31-Okt-2017 16:58:08
 */
public class Gold_Card extends Treasure_Card {


	public Gold_Card(){
		this.cardName = CardName.Gold;
		this.cost = 6;
		this.type = CardType.Treasure;
		this.coinValue = 3;
	}

	/**
	 * 
	 * @param player
	 */
	@Override
	public UpdateGame_Message executeCard(Player player){
		player.setCoins(player.getCoins() + coinValue); // increment coin value
		
		Game game = player.getGame();
		UpdateGame_Message ugmsg = new UpdateGame_Message();
		
		ugmsg.setLog(player.getPlayerName()+": played "+this.cardName.toString()+" card");
		game.sendToOpponent(player, ugmsg); // info for opponent
		
		// update game Messages -> XML 
		ugmsg.setCoins(player.getCoins());
		
		return ugmsg;
	}
	
	
}//end Gold_Card