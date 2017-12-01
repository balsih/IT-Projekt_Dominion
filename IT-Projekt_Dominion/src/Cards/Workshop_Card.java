package Cards;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import Messages.Interaction;
import Messages.UpdateGame_Message;
import Server_GameLogic.Game;
import Server_GameLogic.GameMode;
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
		
		// noch fehlender Code bzw. Funktionalität 
		
		Game game = player.getGame();
		UpdateGame_Message ugmsg = new UpdateGame_Message();
		
		List<CardName> list = game.getBuyCards().keySet().stream()
				.filter(cardName -> (Card.getCard(cardName).getCost() <= 4) && (game.getBuyCards().get(cardName) > 0))
				.collect(Collectors.toList());
		
		LinkedList<CardName> availableCards = new LinkedList<CardName>();
		availableCards.addAll(list);
		
		ugmsg.setLog(player.getPlayerName()+": played "+this.cardName.toString()+" card");
		player.sendToOpponent(player, ugmsg); // info for opponent
			
		// update game Messages -> XML 
		ugmsg.setActions(player.getActions());
		ugmsg.setBuys(player.getBuys());
		ugmsg.setCoins(player.getCoins());
		ugmsg.setInteractionType(Interaction.Workshop);
		ugmsg.setCardSelection(availableCards);
		return ugmsg;
	}
	
	
	public UpdateGame_Message executeWorkshop(CardName selectedCard) {
		UpdateGame_Message ugmsg = new UpdateGame_Message();
		this.player.getHandCards().add(this.player.pick(selectedCard));
		
		ugmsg.setLog(player.getPlayerName()+": picked "+selectedCard.toString()+" card");
		
		// update game Messages -> XML 
		ugmsg.setNewHandCards(player.getHandCards());
		
		return ugmsg;
	}
	
}//end Workshop_Card