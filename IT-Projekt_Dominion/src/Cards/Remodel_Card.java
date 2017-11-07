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
		this.cardName = "Remodel";
		this.cost = 4;
		this.type = "action";
	}

	/**
	 * 
	 * @param player
	 */
	@Override
	public Message executeCard(Player player){
		Message massage;
		player.setActions(player.getActions() - 1);
		
		Game game = player.getGame();
		UpdateGame_Message ugmsg = new UpdateGame_Message();
		String log = player.getPlayerName()+": choose a Card to get rid of!";
		ugmsg.setLog(log);
		
		message = game.sendToOpponent(player, ugmsg);
		
		return message;

		
		
		// karte entsorgen + neue aufnehmen die bis zu 2 mehr kostet als entsorgte
	}
	
	//public Message executeCard(Player player){
		Message message = new Message("w");
		player.setActions(player.getActions() - 1);
		Game game = player.getGame();
		UpdateGame_Message ugmsg = new UpdateGame_Message();
		String log = player.getPlayerName()+": choose a Card to get rid of!";
		ugmsg.setLog(log);
		// karte entsorgen + neue aufnehmen die bis zu 2 mehr kostet als entsorgte
		
		if ()
			
		// Commit(Server -> Client): oder Failure(Server -> Client):

		
		return message;
	}
	
	
	
	
	
	
	
	
	
}//end Remodel_Card