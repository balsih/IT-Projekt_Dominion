package Cards;

import java.util.LinkedList;
import Messages.Interaction;
import Messages.UpdateGame_Message;
import Server_GameLogic.Player;

/**
 * Cellar represents a action card and costs 3. 
 * 
 * @author Rene Schwab
 * 
 */
public class Cellar_Card extends Card {
	
	public Cellar_Card(){
		this.cardName = CardName.Cellar;
		this.cost = 2;
		this.type = CardType.Action;
	}

	/**
	 * Actions get increased by 1. 
	 * Changes related with the card get set in the UpdateGame_Message
	 * 
	 * @author Rene Schwab
	 * 
	 * @param player
	 * , current player 
	 * @return UpdateGame_Message
	 * , containing changes related with this card. 
	 * 
	 */
	public UpdateGame_Message executeCard(Player player){
		this.player = player;
		player.setActions(player.getActions() + 1);
		
		UpdateGame_Message ugmsg = new UpdateGame_Message();
		
		//#DiscardCards# Chose cards to discard
		ugmsg.setLog(player.getPlayerName()+": #played# #"+this.cardName.toString()+"# #card#. #DiscardCards#");
		
		// update game Messages -> XML 
		ugmsg.setInteractionType(Interaction.Cellar);
		ugmsg.setPlayedCards(this);
		
		return ugmsg;
	}
	
	/**
	 * Allows the player to discard any number of cards  
	 * and draw a new one per card discarded. 
	 * Changes related with the card get set in the UpdateGame_Message
	 * 
	 * @author Rene Schwab
	 * 
	 * @param player
	 * , current player 
	 * @return UpdateGame_Message
	 * , containing changes related with this card. 
	 * 
	 */
	public UpdateGame_Message executeCellar(LinkedList<Card> discardedCards) {
		
		LinkedList<Card> realDiscardedCards = new LinkedList<Card>();
		LinkedList<Card> handCards = this.player.getHandCards();
		for(int i = 0; i < discardedCards.size(); i++){
			for(int j = 0; j < handCards.size(); j++){
				if(handCards.get(j).getCardName().equals(discardedCards.get(i).getCardName())){
					realDiscardedCards.add(handCards.remove(j));
					break;
				}
			}
		}
		
		this.player.getDiscardPile().addAll(realDiscardedCards);
		UpdateGame_Message ugmsg = this.player.draw(realDiscardedCards.size());
		
		ugmsg.setLog(player.getPlayerName()+": #picked# "+discardedCards.size()+" #cards#"); // how many card have been picked
		if(!this.player.getDiscardPile().isEmpty())
			ugmsg.setDiscardPileTopCard(this.player.getDiscardPile().peek());
		ugmsg.setDiscardPileCardNumber(this.player.getDiscardPile().size());
		
		if (!this.player.containsCardType(this.player.getHandCards(), CardType.Action))
			ugmsg = UpdateGame_Message.merge((UpdateGame_Message) this.player.skipPhase(), ugmsg);
		
		return ugmsg;
	}
	
}//end Cellar_Card