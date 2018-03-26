package Cards;

/**
 * Abstract super class for all treasure cards, extends class Card. 
 * This class contains the coin value and set the CardTyp to Treasure.
 * 
 * @author Rene Schwab
 * 
 */
public abstract class Treasure_Card extends Card {

	protected int coinValue;

	protected Treasure_Card(){
		this.type = CardType.Treasure;
	}
	
}//end Treasure_Card