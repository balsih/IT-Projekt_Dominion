package Cards;

import Messages.UpdateGame_Message;
import Server_GameLogic.Game;
import Server_GameLogic.Player;

/**
 * @author René
 * @version 1.0
 * @created 31-Okt-2017 16:58:06
 */
public class Duchy_Card extends Victory_Card {


	public Duchy_Card(){
		this.cardName = CardName.Duchy;
		this.cost = 5;
		this.type = CardType.Victory;
		this.victoryPoints = 3;
	}

	/**
	 * 
	 * @param player
	 */
	@Override
	public UpdateGame_Message executeCard(Player player){
		player.setVictoryPoints(player.getVictoryPoints() + victoryPoints);
		
		Game game = player.getGame();
		UpdateGame_Message ugmsg = new UpdateGame_Message();
		
		ugmsg.setLog(player.getPlayerName()+": played "+this.cardName.toString()+" card");
		game.sendToOpponent(player, ugmsg); // info for opponent
		
		return ugmsg;
	}
	
}//end Duchy_Card