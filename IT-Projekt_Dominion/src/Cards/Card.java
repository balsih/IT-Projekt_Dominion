package Cards;

import Client_Services.Gallery;
import Client_Services.ServiceLocator;
import Client_Services.Translator;
import Messages.UpdateGame_Message;
import Server_GameLogic.Player;
import javafx.scene.image.ImageView;

/**
 * @author René
 * @version 1.0
 * @created 31-Okt-2017 16:58:04
 */
public abstract class Card {
	
	ServiceLocator sl = ServiceLocator.getServiceLocator();

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
	public abstract UpdateGame_Message executeCard(Player player);
	

	public String getCardName(){
		return this.cardName;
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

	public static Card getCard(String cardName, Translator t) {
		Card card;
		switch (cardName) {
		case "Bronce_Card":
			card = new Copper_Card();
			card.setImage(Gallery.getImage(t.getString(cardName)));
			break;
		case "Silver_Card":
			card = new Silver_Card();
			card.setImage(Gallery.getImage(t.getString(cardName)));
			break;
		case "Gold_Card":
			card = new Gold_Card();
			card.setImage(Gallery.getImage(t.getString(cardName)));
			break;
		default:
			System.out.println(" no cards available");
		}
		return card;
	}
	
	
	
	
}//end Card