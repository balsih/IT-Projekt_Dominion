package Cards;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import Messages.Interaction;
import Messages.Message;
import Messages.UpdateGame_Message;
import Server_GameLogic.Phase;
import Server_GameLogic.Player;

/**
 * Remodel represents a action card and costs 4. 
 * 
 * @author Rene Schwab
 * 
 */
public class Remodel_Card extends Card {

	public Remodel_Card(){
		this.cardName = CardName.Remodel;
		this.cost = 4;
		this.type = CardType.Action;
	}

	/**
	 * Asks the player to chose a card to trash.
	 * Changes related with the card get set in the UpdateGame_Message
	 * 
	 * @author Rene Schwab
	 * 
	 * @param player
	 * , current player 
	 * @return UpdateGame_Message
	 * , containing changes related with this card. 
	 */
	public UpdateGame_Message executeCard(Player player){
		UpdateGame_Message ugmsg = new UpdateGame_Message();
		this.player = player;
		
		//#DisposeCard# Chose a card to get rid of
		ugmsg.setLog(player.getPlayerName()+": #played# "+"#"+this.cardName.toString()+"#"+" #card#. #DisposeCard#");
		
		// update game Messages -> XML 
		ugmsg.setInteractionType(Interaction.Remodel1);
		ugmsg.setPlayedCards(this);
		
		return ugmsg;
	}
	
	/**
	 * Trashes the selected card and allows the player to select a card 
	 * that costs up to 2 more than the trashed card.    
	 * Changes related with the card get set in the UpdateGame_Message
	 * 
	 * @author Rene Schwab
	 * 
	 * @param disposedCard
	 * , selected card to trash
	 * @return UpdateGame_Message
	 * , containing changes related with this card. 
	 */
	public UpdateGame_Message executeRemodel1(Card disposedCard) { 
		UpdateGame_Message ugmsg = new UpdateGame_Message();
		
		this.game = this.player.getGame();
		
		List<CardName> list = this.game.getBuyCards().keySet().stream()
				.filter(cardName -> (Card.getCard(cardName).getCost() <= disposedCard.getCost() + 2) && (this.game.getBuyCards().get(cardName) > 0))
				.collect(Collectors.toList());
		
		LinkedList<CardName> availableCards = new LinkedList<CardName>();
		availableCards.addAll(list);
		
		this.player.getHandCards().remove(disposedCard); // removes the selected card from handcards  
		
		//#ChoseRemodel1# Chose a card with max 2 higher costs than the disposed card
		ugmsg.setLog(player.getPlayerName()+": #disposed# "+"#"+disposedCard.getCardName().toString()+"#"+" #card#. #ChoseRemodel1#"); 
		
		ugmsg.setInteractionType(Interaction.Remodel2);
		ugmsg.setCardSelection(availableCards);
		
		return ugmsg;
	}

	/**
	 * Ads the selected card to the handcards 
	 * Changes related with the card get set in the UpdateGame_Message
	 * 
	 * @author Rene Schwab
	 * 
	 * @param pickedCardName
	 * , selected card to pick 
	 * @return UpdateGame_Message
	 * , containing changes related with this card. 
	 */
	public Message executeRemodel2(CardName pickedCardName) {
		UpdateGame_Message ugmsg = new UpdateGame_Message();

		Card pickedCard = this.player.pick(pickedCardName);
		this.player.getDiscardPile().add(pickedCard);
		
		ugmsg.setLog(player.getPlayerName()+": #picked# "+"#"+pickedCardName.toString()+"#"+" #card#");
		
		// update game Messages -> XML
		LinkedList<Card> newHandCard = new LinkedList<Card>();
		newHandCard.add(pickedCard);
		ugmsg.setBoughtCard(pickedCard);
		ugmsg.setDiscardPileTopCard(pickedCard);
		ugmsg.setDiscardPileCardNumber(this.player.getDiscardPile().size());
		
		// checks if the game is ended after playing this card and who did win
		if (this.game.checkGameEnding()) {
			this.player.setActualPhase(Phase.Ending);
			this.game.checkWinner();

			this.player.sendToOpponent(this.player, this.player.getPlayerSuccessMsg());
			return this.player.getPlayerSuccessMsg();
		}
		
		if (this.player.getActions() == 0 || !this.player.containsCardType(this.player.getHandCards(), CardType.Action))
			ugmsg = UpdateGame_Message.merge((UpdateGame_Message) this.player.skipPhase(), ugmsg);
		
		return ugmsg;
	}
	
}//end Remodel_Card