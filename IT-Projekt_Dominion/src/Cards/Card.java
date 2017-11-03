package Cards;

import Server_GameLogic.Player;
import javafx.scene.image.ImageView;

/**
 * @author Renate
 * @version 1.0
 * @created 31-Okt-2017 16:58:04
 */
public abstract class Card {

	protected String cardName;
	protected int cost;
	protected ImageView image;
	protected String type; // action, treasure, victory 
	


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
	
	public void setImage(ImageView image){
		this.image = image;
	}
	
	// new code -> vorerst dummy Methode 

	public static Card getCard(String cardName) {
		Card card;
		switch (cardName) {
		case "Bronce_Card":
			card = new Bronce_Card();
			break;
		case "Silver_Card":
			card = getSilverCard();
			break;
		case "Gold_Card":
			card = getGoldCard();
			break;
		default:
			System.out.println(" no cards available");
		}
		return card;
	}
	
	
	
	
}//end Card