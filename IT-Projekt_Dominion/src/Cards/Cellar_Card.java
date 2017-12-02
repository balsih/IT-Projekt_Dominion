package Cards;

import java.util.LinkedList;

import Messages.Interaction;
import Messages.UpdateGame_Message;
import Server_GameLogic.Game;
import Server_GameLogic.Player;

/**
 * @author Renate
 * @version 1.0
 * @created 31-Okt-2017 16:58:05
 */
public class Cellar_Card extends Card {


	public Cellar_Card(){
		this.cardName = CardName.Cellar;
		this.cost = 2;
		this.type = CardType.Action;
	}

	/**
	 * 
	 * @param player
	 */
	@Override
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
	
	public UpdateGame_Message executeCellar(LinkedList<Card> discardedCards) {
		
		int numDiscardedCards = discardedCards.size();
		this.player.getHandCards().removeAll(discardedCards);
		while(!discardedCards.isEmpty()){
			this.player.getDiscardPile().push(discardedCards.removeFirst());
		}
		
		UpdateGame_Message ugmsg = this.player.draw(numDiscardedCards);
		ugmsg.setLog(player.getPlayerName()+": #picked# #"+discardedCards.size()+"# #cards#"); // how many card have been picked 
		if (!this.player.containsCardType(this.player.getHandCards(), CardType.Action))
			ugmsg = UpdateGame_Message.merge((UpdateGame_Message) this.player.skipPhase(), ugmsg);
		
		return ugmsg;
	}
	
}//end Cellar_Card