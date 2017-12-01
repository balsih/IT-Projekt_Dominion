package Cards;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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
	
	
	public UpdateGame_Message executeRemodel1(Card discardedCard) { 
		UpdateGame_Message ugmsg = new UpdateGame_Message();
		
		Game game = this.player.getGame();
		
		List<CardName> list = game.getBuyCards().keySet().stream()
				.filter(cardName -> (Card.getCard(cardName).getCost() <= discardedCard.getCost() + 2) && (game.getBuyCards().get(cardName) > 0))
				.collect(Collectors.toList());
		
		LinkedList<CardName> availableCards = new LinkedList<CardName>();
		availableCards.addAll(list);
		
		this.player.getHandCards().remove(discardedCard); // removes card from hand 
		
		ugmsg.setLog(player.getPlayerName()+": disposed "+discardedCard.getCardName().toString()+" card"); 
		
		ugmsg.setCardSelection(availableCards);
		ugmsg.setNewHandCards(this.player.getHandCards());
		ugmsg.setDiscardPileCardNumber(this.player.getDiscardPile().size());
		ugmsg.setDiscardPileTopCard(this.player.getDiscardPile().peek());

		return ugmsg;
	}

	public UpdateGame_Message executeRemodel2(CardName pickedCard) {
		UpdateGame_Message ugmsg = new UpdateGame_Message();

		this.player.getHandCards().add(this.player.pick(pickedCard));
		
		ugmsg.setLog(player.getPlayerName()+": picked "+pickedCard.toString()+" card");
		
		// update game Messages -> XML 
		ugmsg.setNewHandCards(player.getHandCards());
		
		return ugmsg;
	}
	
}//end Remodel_Card