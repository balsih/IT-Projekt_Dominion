package Cards;

import Messages.UpdateGame_Message;
import Server_GameLogic.Player;

/**
 * @author Adrian
 *
 */
public class Flipside_Card extends Card {
	
	public Flipside_Card(){
		this.cardName = CardName.Flipside;
	}

	@Override
	public UpdateGame_Message executeCard(Player player) {
		return null;
	}
}
