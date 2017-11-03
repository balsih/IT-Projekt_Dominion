package Cards;

import Server_GameLogic.Player;

/**
 * @author Renate
 * @version 1.0
 * @created 31-Okt-2017 16:58:15
 */
public class Village_Card extends Card {


	public Village_Card(){
		this.cardName = "Village";
		this.cost = 3;
		this.type = "action";
	}

	/**
	 * 
	 * @param player
	 */
	@Override
	public void executeCard(Player player){

	}
}//end Village_Card