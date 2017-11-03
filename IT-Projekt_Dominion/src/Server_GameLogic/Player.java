package Server_GameLogic;

import java.util.LinkedList;
import java.util.Stack;

import Cards.Card;

/**
 * @author Bodo
 * @version 1.0
 * @created 31-Okt-2017 17:08:57
 */
public class Player {

	protected int actions;
	protected int buys;
	protected int coins;
	protected Stack<Card> deckPile;
	protected Stack<Card> discardPile;
	protected Game gameThread;
	protected LinkedList<Card> handCards;
	protected LinkedList<Card> playedCards;
	protected String playerName;
	protected int victoryPoints;


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
	public void addGameThread(Game gameThread){

	}

	/**
	 * 
	 * @param cardName
	 */
	public Card buy(String cardName){
		return null;
	}

	private void cleanUp(){

	}

	/**
	 * 
	 * @param number
	 */
	public void draw(int number){

	}

	/**
	 * 
	 * @param cardName
	 */
	public void play(String cardName){

	}

	public void skipPhase(){

	}
	
	protected void shuffle(){
		
	}
}//end Player