package Cards;




/**
 * @author René
 * @version 1.0
 * @created 31-Okt-2017 16:58:14
 */
public abstract class Victory_Card extends Card {

	protected int victoryPoints;

	protected Victory_Card(){
	}
	
	abstract int getVictoryPoints();


}//end Victory_Card