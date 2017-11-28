package Cards;

import java.util.Iterator;
import java.util.LinkedList;

import Messages.Interaction;
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
		this.cardName = CardName.Workshop;
		this.cost = 3;
		this.type = CardType.Action;

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
				
		ugmsg.setLog(player.getPlayerName()+": played "+this.cardName.toString()+" card");
		player.sendToOpponent(player, ugmsg); // info for opponent
				
		// update game Messages -> XML 
		ugmsg.setActions(player.getActions());
		ugmsg.setBuys(player.getBuys());
		ugmsg.setCoins(player.getCoins());
				
		return ugmsg;
	}
	
	
	public UpdateGame_Message executeWorkshop(CardName cardName) {
		UpdateGame_Message ugmsg = (UpdateGame_Message) this.buy(cardName);

		return ugmsg;
	}
	
	/**
	 * @author Bodo Gruetter
	 * 
	 * @param the
	 *            from the player discarded Card
	 * @return a linkedlist with all available cards
	 */
	public LinkedList<Card> getAvailableWorkshopCards(Card discardedCard, Interaction interaction) {
		LinkedList<Card> availableCards = new LinkedList<Card>();
		Iterator<CardName> keyIterator = game.getBuyCards().keySet().iterator();

		while (keyIterator.hasNext()) {
			if (Card.getCard(keyIterator.next()).getCost() <= 4)
				availableCards.add(Card.getCard(keyIterator.next()));
		}

		return availableCards;
	}
}//end Workshop_Card