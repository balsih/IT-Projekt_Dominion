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

	private int actions;
	private int buys;
	private int coins;
	private Stack<Card> deckPile;
	private Stack<Card> discardPile;
	private Game gameThread;
	private LinkedList<Card> handCards;
	private LinkedList<Card> playedCards;
	private String playerName;
	private int victoryPoints;


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

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public LinkedList<Card> getPlayedCards() {
		return playedCards;
	}

	public void setPlayedCards(LinkedList<Card> playedCards) {
		this.playedCards = playedCards;
	}

	public LinkedList<Card> getHandCards() {
		return handCards;
	}

	public void setHandCards(LinkedList<Card> handCards) {
		this.handCards = handCards;
	}

	public Game getGameThread() {
		return gameThread;
	}

	public void setGameThread(Game gameThread) {
		this.gameThread = gameThread;
	}

	public Stack<Card> getDiscardPile() {
		return discardPile;
	}

	public void setDiscardPile(Stack<Card> discardPile) {
		this.discardPile = discardPile;
	}

	public Stack<Card> getDeckPile() {
		return deckPile;
	}

	public void setDeckPile(Stack<Card> deckPile) {
		this.deckPile = deckPile;
	}

	public int getVictoryPoints() {
		return victoryPoints;
	}

	public void setVictoryPoints(int victoryPoints) {
		this.victoryPoints = victoryPoints;
	}

	public int getCoins() {
		return coins;
	}

	public void setCoins(int coins) {
		this.coins = coins;
	}

	public int getBuys() {
		return buys;
	}

	public void setBuys(int buys) {
		this.buys = buys;
	}

	public int getActions() {
		return actions;
	}

	public void setActions(int actions) {
		this.actions = actions;
	}
}//end Player