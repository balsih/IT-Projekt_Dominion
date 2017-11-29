package Cards;

import java.util.Iterator;
import java.util.LinkedList;

import Messages.Failure_Message;
import Messages.Interaction;
import Messages.Message;
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
		this.player = player;
		
		// eine geldkarte entsorgen und eine andere aufnehmen in die hand
		
		
		// noch fehlender Code bzw. Funktionalität 
		
		Game game = player.getGame();
		UpdateGame_Message ugmsg = new UpdateGame_Message();
				
		ugmsg.setLog(player.getPlayerName()+": played Mine card"); // hashtags setzen
		player.sendToOpponent(player, ugmsg); // info for opponent
		
		
		// update game Messages -> XML 
		ugmsg.setActions(player.getActions());
		ugmsg.setBuys(player.getBuys());
		ugmsg.setCoins(player.getCoins());
		
		return ugmsg;
	}
	/**
	 * @author Bodo Gruetter
	 * 
	 * @param the from the player discarded Card     
	 * @return a linkedlist with all available cards
	 */
	
	public Message executeMine(Card discardedCard){
		UpdateGame_Message ugmsg = new UpdateGame_Message();
		Game game = player.getGame();
		player.getHandCards().remove(discardedCard); // removes the selected card
		
		// add a treasure card with a higher value than the removed one
		if (discardedCard.getCardName() == CardName.Copper){
			player.getHandCards().add(game.getSilverPile().pop());
			ugmsg.setNewHandCards(player.getHandCards());
			return ugmsg;
		} else if (discardedCard.getCardName() == CardName.Silver){
			player.getHandCards().add(game.getGoldPile().pop());
			ugmsg.setNewHandCards(player.getHandCards());
			return ugmsg;
		} 
		// returns failure if discarded card is not copper or silver
		return new Failure_Message(); 
		
		
	}
	
}//end Mine_Card