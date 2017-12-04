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
		
		UpdateGame_Message ugmsg = new UpdateGame_Message();
		
		//#DisposeCard# Chose a card to get rid of
		ugmsg.setLog(player.getPlayerName()+": #played# #"+this.cardName.toString()+"# #card#. #DisposeCard#");
		
		// update game Messages -> XML 
		ugmsg.setInteractionType(Interaction.Remodel1);
		ugmsg.setPlayedCards(this);
		
		return ugmsg;
	}
	
	
	public UpdateGame_Message executeRemodel1(Card disposedCard) { 
		UpdateGame_Message ugmsg = new UpdateGame_Message();
		
		Game game = this.player.getGame();
		
		List<CardName> list = game.getBuyCards().keySet().stream()
				.filter(cardName -> (Card.getCard(cardName).getCost() <= disposedCard.getCost() + 2) && (game.getBuyCards().get(cardName) > 0))
				.collect(Collectors.toList());
		
		LinkedList<CardName> availableCards = new LinkedList<CardName>();
		availableCards.addAll(list);
		
		this.player.getHandCards().remove(disposedCard); // removes card from hand 
		
		//#ChoseRemodel1# Chose a card with max 2 higher cots than the disposed card
		ugmsg.setLog(player.getPlayerName()+": #disposed# #"+disposedCard.getCardName().toString()+"# #card#. #ChoseRemodel1#"); 
		
		ugmsg.setInteractionType(Interaction.Remodel2);
		ugmsg.setCardSelection(availableCards);

		return ugmsg;
	}

	public UpdateGame_Message executeRemodel2(CardName pickedCardName) {
		UpdateGame_Message ugmsg = new UpdateGame_Message();

		Card pickedCard = this.player.pick(pickedCardName);
		this.player.getDiscardPile().add(pickedCard);
		
		ugmsg.setLog(player.getPlayerName()+": #picked# #"+pickedCardName.toString()+"# #card#");
		
		// update game Messages -> XML
		LinkedList<Card> newHandCard = new LinkedList<Card>();
		newHandCard.add(pickedCard);
		ugmsg.setBuyedCard(pickedCard);
		if (this.player.getActions() == 0 || !this.player.containsCardType(this.player.getHandCards(), CardType.Action))
			ugmsg = UpdateGame_Message.merge((UpdateGame_Message) this.player.skipPhase(), ugmsg);
		
		return ugmsg;
	}
	
}//end Remodel_Card