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
 * @author Rene
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
		
		this.player = player;
		
		Game game = player.getGame();
		UpdateGame_Message ugmsg = new UpdateGame_Message();
		
		List<CardName> list = game.getBuyCards().keySet().stream()
				.filter(cardName -> (Card.getCard(cardName).getCost() <= 4) && (game.getBuyCards().get(cardName) > 0))
				.collect(Collectors.toList());
		
		LinkedList<CardName> availableCards = new LinkedList<CardName>();
		availableCards.addAll(list);
		
		ugmsg.setLog(player.getPlayerName()+": #played# "+this.cardName.toString()+" #card#. #Workshop1#");
			
		// update game Messages -> XML 
		ugmsg.setInteractionType(Interaction.Workshop);
		ugmsg.setCardSelection(availableCards);
		ugmsg.setPlayedCards(this);
		return ugmsg;
	}
	
	
	public UpdateGame_Message executeWorkshop(CardName selectedNameCard) {
		UpdateGame_Message ugmsg = new UpdateGame_Message();
		
		Card selectedCard = this.player.pick(selectedNameCard);
		this.player.getDiscardPile().add(selectedCard);
		
		ugmsg.setBuyedCard(selectedCard);
		ugmsg.setDiscardPileTopCard(selectedCard);
		ugmsg.setDiscardPileCardNumber(this.player.getDiscardPile().size());
		ugmsg.setLog(player.getPlayerName()+": #picked# "+selectedNameCard.toString()+" #card#");
		
		// update game Messages -> XML 
		if (this.player.getActions() == 0 || !this.player.containsCardType(this.player.getHandCards(), CardType.Action))
			ugmsg = UpdateGame_Message.merge((UpdateGame_Message) this.player.skipPhase(), ugmsg);
		
		return ugmsg;
	}
	
}//end Workshop_Card