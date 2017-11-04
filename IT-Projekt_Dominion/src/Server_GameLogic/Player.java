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

	public void cleanUp(){

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
	
	public void shuffle(){
		
	}

	public int getActions() {
		return actions;
	}

	public void setActions(int actions) {
		this.actions = actions;
	}

	public int getCoins() {
		return coins;
	}

	public void setCoins(int coins) {
		this.coins = coins;
	}

	public Stack<Card> getDeckPile() {
		return deckPile;
	}

	public void setDeckPile(Stack<Card> deckPile) {
		this.deckPile = deckPile;
	}

	public int getBuys() {
		return buys;
	}

	public void setBuys(int buys) {
		this.buys = buys;
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

	public LinkedList<Card> getPlayedCards() {
		return playedCards;
	}

	public void setPlayedCards(LinkedList<Card> playedCards) {
		this.playedCards = playedCards;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public int getVictoryPoints() {
		return victoryPoints;
	}

	public void setVictoryPoints(int victoryPoints) {
		this.victoryPoints = victoryPoints;
	}
}//end Player