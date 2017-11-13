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

	protected CardName cardName;
	protected int cost;
	protected ImageView image;
	protected CardType type; // action, treasure, victory 	

	protected Card(){
	}
	

	/**
	 * 
	 * @param player
	 * @return 
	 */
	public abstract UpdateGame_Message executeCard(Player player);
	

	public CardName getCardName(){
		return this.cardName;
	}

	public int getCost(){
		return this.cost;
	}

	public ImageView getImage(){
		return this.image;
	}

	public CardType getType(){
		return this.type;
	}
	
	public void setImage(ImageView image){
		this.image = image;
	}
	
	// Method gives a card back based on the cardName and the language
	// and sets the corresponding image 
	public static Card getCard(CardName cardName) {
		Translator t = ServiceLocator.getServiceLocator().getTranslator();
		Card card = null;
		switch (cardName) {
		case Copper:
			card = new Copper_Card();
			card.setImage(Gallery.getImage(t.getString(cardName.toString())));
			break;
		case Cellar:
			card = new Cellar_Card();
			card.setImage(Gallery.getImage(t.getString(cardName.toString())));
			break;
		case Duchy:
			card = new Duchy_Card();
			card.setImage(Gallery.getImage(t.getString(cardName.toString())));
			break;
		case Estate:
			card = new Estate_Card();
			card.setImage(Gallery.getImage(t.getString(cardName.toString())));
			break;
		case Gold:
			card = new Gold_Card();
			card.setImage(Gallery.getImage(t.getString(cardName.toString())));
			break;
		case Market:
			card = new Market_Card();
			card.setImage(Gallery.getImage(t.getString(cardName.toString())));
			break;
		case Mine:
			card = new Mine_Card();
			card.setImage(Gallery.getImage(t.getString(cardName.toString())));
			break;
		case Province:
			card = new Province_Card();
			card.setImage(Gallery.getImage(t.getString(cardName.toString())));
			break;
		case Remodel:
			card = new Remodel_Card();
			card.setImage(Gallery.getImage(t.getString(cardName.toString())));
			break;
		case Silver:
			card = new Silver_Card();
			card.setImage(Gallery.getImage(t.getString(cardName.toString())));
			break;
		case Smithy:
			card = new Smithy_Card();
			card.setImage(Gallery.getImage(t.getString(cardName.toString())));
			break;
		case Village:
			card = new Village_Card();
			card.setImage(Gallery.getImage(t.getString(cardName.toString())));
			break;
		case Woodcutter:
			card = new Woodcutter_Card();
			card.setImage(Gallery.getImage(t.getString(cardName.toString())));
			break;
		case Workshop:
			card = new Workshop_Card();
			card.setImage(Gallery.getImage(t.getString(cardName.toString())));
			break;
		}
		return card;
	}

	
	public String toString() {
		return this.cardName.toString();
	}
	
	
	
	
	
}//end Card