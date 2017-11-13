package Cards;

import Messages.UpdateGame_Message;
import Server_GameLogic.Game;
import Server_GameLogic.Player;

/**
 * @author Renate
 * @version 1.0
 * @created 31-Okt-2017 16:58:05
 */
public class Cellar_Card extends Card {


	public Cellar_Card(){
		this.cardName = CardName.Cellar;
		this.cost = 2;
		this.type = CardType.Action;
	}

	/**
	 * 
	 * @param player
	 */
	@Override
	public UpdateGame_Message executeCard(Player player){
		player.setActions(player.getActions() - 1);
		
		player.draw(player.getHandCards().size()); // beliebige Anzahl aus der Hand ablegen und für jede eine neue aufnehmen
		
		
		
		// noch fehlender Code bzw. Funktionalität 
		
		Game game = player.getGame();
		UpdateGame_Message ugmsg = new UpdateGame_Message();
		
		ugmsg.setLog(player.getPlayerName()+": played "+this.cardName.toString()+" card");
		game.sendToOpponent(player, ugmsg); // info for opponent
		
		// update game Messages -> XML 
		ugmsg.setActions(player.getActions());
		ugmsg.setBuys(player.getBuys());
		ugmsg.setCoins(player.getCoins());
		
		return ugmsg;
	}
}//end Cellar_Card