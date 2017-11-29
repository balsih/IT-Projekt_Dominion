package Cards;

import java.util.Iterator;
import java.util.LinkedList;

import Messages.Interaction;
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
		this.cardName = CardName.Remodel;
		this.cost = 4;
		this.type = CardType.Action;
	}

	/**
	 * 
	 * @param player
	 */
	@Override
	public UpdateGame_Message executeCard(Player player){
		
		player.setActions(player.getActions() - 1);
		
		//ugmsg.setLog(player.getPlayerName()+": choose a Card to get rid of!"); // usw.
		
		
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
	
	
	public UpdateGame_Message executeRemodel1(CardName discardedCard) { // card nicht cardName?
		UpdateGame_Message ugmsg = player.discard(discardedCard);

		return ugmsg;
	}

	public UpdateGame_Message executeRemodel2(CardName pickedCard) {
		UpdateGame_Message ugmsg = (UpdateGame_Message) player.buy(pickedCard);

		return ugmsg;
	}
	
	/**
	 * @author Bodo Gruetter
	 * 
	 * @param the
	 *            from the player discarded Card
	 * @return a linkedlist with all available cards
	 */
	public LinkedList<Card> getAvailableRemodelCards(Card discardedCard, Interaction interaction) {
		LinkedList<Card> availableCards = new LinkedList<Card>();
		Iterator<CardName> keyIterator = game.getBuyCards().keySet().iterator();

		while (keyIterator.hasNext()) {
			if (Card.getCard(keyIterator.next()).getCost() <= discardedCard.getCost() + 2)
				availableCards.add(Card.getCard(keyIterator.next()));
		}

		return availableCards;
	}
	
}//end Remodel_Card