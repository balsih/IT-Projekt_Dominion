package Cards;

import Messages.UpdateGame_Message;
import Server_GameLogic.Player;

/**
 * Flipside card covers the deck pile and an empty stack.  
 * 
 * @author Adrian
 * 
 */
public class Flipside_Card extends Card {
	
	public Flipside_Card(){
		this.cardName = CardName.Flipside;
	}

	public UpdateGame_Message executeCard(Player player) {
		return null; // nothing do to here 
	}
}
