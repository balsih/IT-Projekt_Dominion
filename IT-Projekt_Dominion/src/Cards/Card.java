package Cards;

import Server_GameLogic.Player;
import javafx.scene.image.ImageView;

/**
 * @author Renate
 * @version 1.0
 * @created 31-Okt-2017 16:58:04
 */
public abstract class Card {

	private String cardName;
	private int cost;
	private ImageView image;
	private String type;


	protected Card(){

	}

	/**
	 * 
	 * @param player
	 * @return 
	 */
	public abstract void executeCard(Player player);

	public String getCardName(){
		return "";
	}

	public int getCost(){
		return 0;
	}

	public ImageView getImage(){
		return null;
	}

	public String getType(){
		return "";
	}
}//end Card