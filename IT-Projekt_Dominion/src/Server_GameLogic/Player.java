import Klassendiagramm.Cards.Card;

/**
 * @author Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:08:57
 */
public class Player {

	protected int actions;
	protected int buys;
	protected int coins;
	protected Stack deckPile;
	protected Stack discardPile;
	private Game gameThread;
	protected LinkedList handCards;
	private LinkedList playedCards;
	private String playerName;
	protected int victoryPoints;

	public Player(){

	}

	public void finalize() throws Throwable {

	}
	/**
	 * 
	 * @param name
	 */
	public Player(String name){

	}

	/**
	 * 
	 * @param gameThread
	 */
	public addGameThread(Game gameThread){

	}

	/**
	 * 
	 * @param cardName
	 */
	public Card buy(String cardName){
		return null;
	}

	private cleanUp(){

	}

	/**
	 * 
	 * @param number
	 */
	public draw(int number){

	}

	/**
	 * 
	 * @param cardName
	 */
	public play(String cardName){

	}

	public skipPhase(){

	}
}//end Player