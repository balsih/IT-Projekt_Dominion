package Cards;

import Server_GameLogic.Player;

/**
 * @author Renate
 * @version 1.0
 * @created 31-Okt-2017 16:58:05
 */
public class Cellar_Card extends Card {


	public Cellar_Card(){
		this.cardName = "Cellar";
		this.cost = 2;
		this.type = "action";
	}

	/**
	 * 
	 * @param player
	 */
	@Override
	public void executeCard(Player player){
		player.setActions(player.getActions() - 1);
		player.draw(player.getHandCards().size()); // beliebige NAzahl aus der Hand ablegen und für jede eine neue aufnehmen
		

	}
}//end Cellar_Card