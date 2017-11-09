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
		return this.cost;
	}

	public ImageView getImage(){
		return this.image;
	}

	public String getType(){
		return this.type;
	}
	
	public void setImage(ImageView image){
		this.image = image;
	}
	
	// Method gives a card back based on the cardName and the language
	// and sets the corresponding image 
	public static Card getCard(String cardName) {
		Translator t = ServiceLocator.getServiceLocator().getTranslator();
		Card card;
		switch (cardName) {
		case "Copper_Card":
			card = new Copper_Card();
			card.setImage(Gallery.getImage(t.getString(cardName)));
			break;
		case "Cellar_Card":
			card = new Cellar_Card();
			card.setImage(Gallery.getImage(t.getString(cardName)));
			break;
		case "Duchy_Card":
			card = new Duchy_Card();
			card.setImage(Gallery.getImage(t.getString(cardName)));
			break;
		case "Estate_Card":
			card = new Estate_Card();
			card.setImage(Gallery.getImage(t.getString(cardName)));
			break;
		case "Gold_Card":
			card = new Gold_Card();
			card.setImage(Gallery.getImage(t.getString(cardName)));
			break;
		case "Market_Card":
			card = new Market_Card();
			card.setImage(Gallery.getImage(t.getString(cardName)));
			break;
		case "Mine_Card":
			card = new Mine_Card();
			card.setImage(Gallery.getImage(t.getString(cardName)));
			break;
		case "Province_Card":
			card = new Province_Card();
			card.setImage(Gallery.getImage(t.getString(cardName)));
			break;
		case "Remodel_Card":
			card = new Remodel_Card();
			card.setImage(Gallery.getImage(t.getString(cardName)));
			break;
		case "Silver_Card":
			card = new Silver_Card();
			card.setImage(Gallery.getImage(t.getString(cardName)));
			break;
		case "Smithy_Card":
			card = new Smithy_Card();
			card.setImage(Gallery.getImage(t.getString(cardName)));
			break;
		case "Village_Card":
			card = new Village_Card();
			card.setImage(Gallery.getImage(t.getString(cardName)));
			break;
		case "Woodcutter_Card":
			card = new Woodcutter_Card();
			card.setImage(Gallery.getImage(t.getString(cardName)));
			break;
		case "Workshop_Card":
			card = new Workshop_Card();
			card.setImage(Gallery.getImage(t.getString(cardName)));
			break;
		default:
			card = null;
			//System.out.println(" no cards available");
		}
		return card;
	}

	
	public String toString() {
		return this.cardName;
	}
	
	
	
	
	
}//end Card