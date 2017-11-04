package Server_GameLogic;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
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

	private final int NUM_OF_HANDCARDS = 5;

	/**
	 * 
	 * @param name
	 */
	public Player(String name) {

	}

	/**
	 * 
	 * @param gameThread
	 */
	public void addGameThread(Game gameThread) {

	}

	/**
	 * 
	 * @param cardName
	 */
	public Card buy(String cardName) {

		return null;
	}

	public void cleanUp() {

	}

	// If Deckpile is empty, the discard pile fills the deckPile. Eventually
	// the deckPiles get shuffled and the player draws 5 Cards from deckPile
	// to HandPile.
	//
	// Else If the deckpile size is lower than 5, the rest of deckPiles
	// will be drawed and the discard pile fills the deckPile.
	// eventually the deckPiles get shuffled and the player draws the
	// rest of the Cards until he has 5 Cards in the HandPile.
	//
	// Else if they are enough cards in the deckPile, the player draws 5
	// cards into the handPile

	public void draw(int number) {

		if (deckPile.isEmpty()) {
			while (!discardPile.isEmpty())
				deckPile.push(discardPile.pop());
			Collections.shuffle(deckPile);
			for (int i = 0; i < NUM_OF_HANDCARDS; i++)
				handCards.add(deckPile.pop());
		} else if (deckPile.size() < NUM_OF_HANDCARDS) {
			while (!deckPile.isEmpty())
				handCards.add(deckPile.pop());
			while (!discardPile.isEmpty())
				deckPile.push(discardPile.pop());
			Collections.shuffle(deckPile);
			for (int i = 0; i < NUM_OF_HANDCARDS - handCards.size(); i++)
				handCards.add(deckPile.pop());
		} else {
			for (int i = 0; i < NUM_OF_HANDCARDS; i++)
				handCards.add(deckPile.pop());
		}
	}

	/**
	 * 
	 * @param cardName
	 */
	public void play(String cardName) {

	}

	public void skipPhase() {

	}

	public void shuffle() {

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
}// end Player