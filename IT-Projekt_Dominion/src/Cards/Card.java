package Cards;

import Client_Services.ServiceLocator;
import Messages.UpdateGame_Message;
import Server_GameLogic.Game;
import Server_GameLogic.Player;
import javafx.scene.image.ImageView;

/**
 * Abstract super class for all cards. This class contains cardName, cardType, cost
 * and image of each card. 
 * 
 * @author Rene Schwab
 * 
 */
public abstract class Card {
	ServiceLocator sl = ServiceLocator.getServiceLocator();
	protected CardName cardName;
	protected int cost;
	protected ImageView image;
	protected CardType type; // action, treasure, victory 	
	protected Player player;
	protected Game game;
	protected Card(){
	}
	
	/**
	 * Sends all changes in the game in correlation with the played card 
	 * 
	 * @author Rene Schwab
	 * 
	 * @param player
	 * , current player who plays the card 
	 * @return UpdateGame_Message
	 * ,includes chances of values and set log messages for the executed card.
	 * 
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
	
	/**
	 * Gives a Card back and sets the corresponding image based on the card name 
	 * and the selected language of the player.
	 * 
	 * @author Rene Schwab
	 * 
	 * @param cardName
	 * , name of the card  
	 * @return ard
	 * , corresponding card object
	 * 
	 */
	public static Card getCard(CardName cardName) {

		Card card = null;
		switch (cardName) {
		case Copper:
			card = new Copper_Card();
			break;
		case Cellar:
			card = new Cellar_Card();
			break;
		case Duchy:
			card = new Duchy_Card();
			break;
		case Estate:
			card = new Estate_Card();
			break;
		case Gold:
			card = new Gold_Card();
			break;
		case Market:
			card = new Market_Card();
			break;
		case Mine:
			card = new Mine_Card();
			break;
		case Province:
			card = new Province_Card();
			break;
		case Remodel:
			card = new Remodel_Card();
			break;
		case Silver:
			card = new Silver_Card();
			break;
		case Smithy:
			card = new Smithy_Card();
			break;
		case Village:
			card = new Village_Card();
			break;
		case Woodcutter:
			card = new Woodcutter_Card();
			break;
		case Workshop:
			card = new Workshop_Card();
			break;
		case Flipside:
			card = new Flipside_Card();
		}
		
		ServiceLocator sl = ServiceLocator.getServiceLocator();
		// server uses method as well but has no sl 
		// -> if null image is not set (
		if(sl.getGallery() != null) 
			card.setImage(sl.getGallery().getImage(cardName));
		
		return card;
	}

	
	public String toString() {
		return this.cardName.toString();
	}
	
}//end Card