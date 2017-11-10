package Cards;

import Messages.UpdateGame_Message;
import Server_GameLogic.Game;
import Server_GameLogic.Player;

/**
 * @author René
 * @version 1.0
 * @created 31-Okt-2017 16:58:07
 */
public class Estate_Card extends Victory_Card {


	public Estate_Card(){
		this.cardName = "Estade_Card";
		this.cost = 2;
		this.type = "victory";
		this.victoryPoints = 1;
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
		
		ugmsg.setLog(player.getPlayerName()+": played Estade card");
		game.sendToOpponent(player, ugmsg); // info for opponent
		
		return ugmsg;
	}
	
}//end Estate_Card