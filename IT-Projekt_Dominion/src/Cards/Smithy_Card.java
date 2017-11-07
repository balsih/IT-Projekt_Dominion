package Cards;

import Server_GameLogic.Player;

/**
 * @author René
 * @version 1.0
 * @created 31-Okt-2017 16:58:13
 */
public class Smithy_Card extends Card {


	public Smithy_Card(){
		this.cardName = "Smithy";
		this.cost = 4;
		this.type = "action";
	}

	/**
	 * 
	 * @param player
	 */
	@Override
	public void executeCard(Player player){
		player.setActions(player.getActions() - 1);
		player.draw(3); // draw 3 cards
	}
	
}//end Smithy_Card