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
 * Workshop represents a action card and costs 3. 
 * 
 * @author Rene Schwab
 * 
 */
public class Workshop_Card extends Card {

	public Workshop_Card(){
		this.cardName = CardName.Workshop;
		this.cost = 3;
		this.type = CardType.Action;
	}

	/**
	 * Asks the player to chose a card costing up to 4.
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
		
		this.player = player;
		this.game = player.getGame();
		UpdateGame_Message ugmsg = new UpdateGame_Message();
		
		List<CardName> list = game.getBuyCards().keySet().stream()
				.filter(cardName -> (Card.getCard(cardName).getCost() <= 4) && (game.getBuyCards().get(cardName) > 0))
				.collect(Collectors.toList());
		
		LinkedList<CardName> availableCards = new LinkedList<CardName>();
		availableCards.addAll(list);
		
		ugmsg.setLog(player.getPlayerName()+": #played# "+"#"+this.cardName.toString()+"#"+" #card#. #Workshop1#");
			
		// update game Messages -> XML 
		ugmsg.setInteractionType(Interaction.Workshop);
		ugmsg.setCardSelection(availableCards);
		ugmsg.setPlayedCards(this);
		return ugmsg;
	}
	
	/**
	 * Ads the selected card to the discard pile 
	 * Changes related with the card get set in the UpdateGame_Message
	 * 
	 * @author Rene Schwab
	 * 
	 * @param selectedNameCard
	 * , selected card to pick 
	 * @return UpdateGame_Message
	 * , containing changes related with this card. 
	 */
	public Message executeWorkshop(CardName selectedNameCard) {
		UpdateGame_Message ugmsg = new UpdateGame_Message();
		
		Card selectedCard = this.player.pick(selectedNameCard);
		this.player.getDiscardPile().add(selectedCard);
		
		ugmsg.setBoughtCard(selectedCard);
		ugmsg.setDiscardPileTopCard(selectedCard);
		ugmsg.setDiscardPileCardNumber(this.player.getDiscardPile().size());
		ugmsg.setLog(player.getPlayerName()+": #picked# "+"#"+selectedNameCard.toString()+"#"+" #card#");
		
		// checks if the game is ended after playing this card and who wons it

		if (this.game.checkGameEnding()) {
			this.player.setActualPhase(Phase.Ending);
			this.game.checkWinner();

			this.player.sendToOpponent(this.player, this.player.getPlayerSuccessMsg());
			return this.player.getPlayerSuccessMsg();
		}
		
		// update game Messages -> XML 
		if (this.player.getActions() == 0 || !this.player.containsCardType(this.player.getHandCards(), CardType.Action))
			ugmsg = UpdateGame_Message.merge((UpdateGame_Message) this.player.skipPhase(), ugmsg);
		
		return ugmsg;
	}
	
}//end Workshop_Card