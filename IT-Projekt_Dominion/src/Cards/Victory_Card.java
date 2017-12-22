package Cards;

/**
 * Abstract super class for all victory cards, extends class Card. 
 * This class contains victory points and set the CardTyp to Victory. 
 * 
 * @author Rene Schwab
 * 
 */
public abstract class Victory_Card extends Card {

	protected int victoryPoints;

	protected Victory_Card(){
		this.type = CardType.Victory;
	}
	
}//end Victory_Card