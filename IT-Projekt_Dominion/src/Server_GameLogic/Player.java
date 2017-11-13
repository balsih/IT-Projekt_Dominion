package Server_GameLogic;

import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

import Cards.Card;
import Cards.CardName;
import Cards.Copper_Card;
import Messages.Failure_Message;
import Messages.Message;
import Messages.UpdateGame_Message;

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
	protected Game game;
	protected LinkedList<Card> handCards;
	protected LinkedList<Card> playedCards;
	protected String playerName;
	protected int victoryPoints;

	protected final int NUM_OF_HANDCARDS = 5;

	protected Socket clientSocket;

	protected boolean isFinished;

	protected String actualPhase;

	protected static int counter;
	
	protected String log;
	
	private ServerThreadForClient serverThreadForClient;
	
	private UpdateGame_Message ugmsg;
	private Failure_Message fmsg;

	/**
	 * 
	 * @param name
	 */
	public Player(String name, ServerThreadForClient serverThreadForClient) {
		this.deckPile = new Stack<Card>();
		this.discardPile = new Stack<Card>();
		this.handCards = new LinkedList<Card>();
		this.playedCards = new LinkedList<Card>();

		this.coins = 0;
		this.actions = 1;
		this.buys = 0;
		counter = 0;

		this.isFinished = false;

		this.actualPhase = "";
		
		this.log = "";
		
		
		this.serverThreadForClient = serverThreadForClient;
		
		this.ugmsg = new UpdateGame_Message();
		this.fmsg = new Failure_Message();
	}
	
	public Player(String name){
		this.deckPile = new Stack<Card>();
		this.discardPile = new Stack<Card>();
		this.handCards = new LinkedList<Card>();
		this.playedCards = new LinkedList<Card>();

		this.coins = 0;
		this.actions = 1;
		this.buys = 0;
		counter = 0;

		this.isFinished = false;

		this.actualPhase = "";
		
		this.log = "";
		
		this.ugmsg = new UpdateGame_Message();
		this.fmsg = new Failure_Message();
	}
	
	/**
	 * 
	 * @param gameThread
	 */
	public void addGame(Game game) {
		this.game = game;
	}
	

	/**
	 * buys a card and lays the buyed card into the discard pile
	 */
	public Message buy(CardName cardName) {
		Card buyedCard = null;
		this.actualPhase = "buy";
		
		switch (cardName) {
		case Copper:
			buyedCard = this.game.getCopperPile().pop();
			this.discardPile.push(buyedCard);
			break;
		case Cellar:
			buyedCard = this.game.getCellarPile().pop();
			this.discardPile.push(buyedCard);
			break;
		case Duchy:
			buyedCard = this.game.getDuchyPile().pop();
			this.discardPile.push(buyedCard);
			break;
		case Estate:
			buyedCard = this.game.getEstatePile().pop();
			this.discardPile.push(buyedCard);
			break;
		case Gold:
			buyedCard = this.game.getGoldPile().pop();
			this.discardPile.push(buyedCard);
			break;
		case Market:
			buyedCard = this.game.getMarketPile().pop();
			this.discardPile.push(buyedCard);
			break;
		case Mine:
			buyedCard = this.game.getMinePile().pop();
			this.discardPile.push(buyedCard);
			break;
		case Province:
			buyedCard = this.game.getProvincePile().pop();
			this.discardPile.push(buyedCard);
			break;
		case Remodel:
			buyedCard = this.game.getRemodelPile().pop();
			this.discardPile.push(buyedCard);
			break;
		case Silver:
			buyedCard = this.game.getSilverPile().pop();
			this.discardPile.push(buyedCard);
			break;
		case Smithy:
			buyedCard = this.game.getSmithyPile().pop();
			this.discardPile.push(buyedCard);
			break;
		case Village:
			buyedCard = this.game.getVillagePile().pop();
			this.discardPile.push(buyedCard);
			break;
		case Woodcutter:
			buyedCard = this.game.getWoodcutterPile().pop();
			this.discardPile.push(buyedCard);
			break;
		case Workshop:
			buyedCard = this.game.getWorkshopPile().pop();
			this.discardPile.push(buyedCard);
			break;
		}

		if(buyedCard.getCost() <= this.getCoins() && this.getBuys() > 0){
			this.ugmsg.setLog("");
			this.ugmsg.setCurrentPlayer(this.getPlayerName());
			this.ugmsg.setCoins(this.getCoins());
			this.ugmsg.setActions(this.getActions());
			this.ugmsg.setBuys(this.getBuys());
			this.ugmsg.setCurrentPhase(actualPhase);
			this.ugmsg.setDiscardPileTopCard(this.discardPile.firstElement());
			this.ugmsg.setDiscardPileCardNumber(this.discardPile.size());
			this.ugmsg.setDeckPileCardNumber(this.deckPile.size());
			this.ugmsg.setBuyedCard(buyedCard);
			this.ugmsg.setNewHandCards(this.handCards);
			this.ugmsg.setChat("");
			
			return this.ugmsg;
		} else
			return this.fmsg;

	}
	
//	//checks if the card can be buyed
//	public boolean buyed(String cardName){
//		Card buyedCard = this.buy(cardName);
//		
//		if(buyedCard.getCost() <= this.getCoins() && this.getBuys() > 0)
//			return true;
//		else
//			return false;
//	}

	//Cleans up
	public void cleanUp() {
		this.actualPhase = "cleanUp";

		while (!playedCards.isEmpty()) {
			this.discardPile.push(playedCards.remove());
		}

		while (!handCards.isEmpty()) {
			this.discardPile.push(handCards.remove());
		}

		this.draw(this.NUM_OF_HANDCARDS);

		this.setFinished(true);

		game.checkGameEnding();
	}

	/**
	 * If Deckpile is empty, the discard pile fills the deckPile. Eventually the
	 * deckPiles get shuffled and the player draws the number of layed down
	 * Cards from deckPile to HandPile.
	 *
	 * Else If the deckpile size is lower than 5, the rest of deckPiles will be
	 * drawed and the discard pile fills the deckPile. eventually the deckPile
	 * get shuffled and the player draws the number of layed down Cards in the
	 * HandPile.
	 *
	 * Else if they are enough cards in the deckPile, the player draws the
	 * number of layed down cards respectively 5 Cards into the handPile
	 */
	public void draw(int numOfCards) {
		for (int i = 0; i < numOfCards; i++) {
			if (deckPile.isEmpty()) {
				while (!discardPile.isEmpty())
					deckPile.push(discardPile.pop());
				Collections.shuffle(deckPile);
				for (int y = 0; y < numOfCards; y++)
					handCards.add(deckPile.pop());
			} else if (deckPile.size() < numOfCards) {
				while (!deckPile.isEmpty())
					handCards.add(deckPile.pop());
				while (!discardPile.isEmpty())
					deckPile.push(discardPile.pop());
				Collections.shuffle(deckPile);
				for (int y = 0; y < numOfCards; y++)
					handCards.add(deckPile.pop());
			} else {
				for (int y = 0; y < numOfCards - handCards.size(); y++)
					handCards.add(deckPile.pop());
			}
		}
	}

	/**
	 * Lays the selected card down from Handcards into discardPile and counts
	 * the number of layed down cards and returns this number
	 */
	public int layDown(String cardName) {
		Card layedDownCard = null;
		int index = 0;

		index = this.handCards.indexOf(cardName);
		layedDownCard = this.handCards.remove(index);
		this.discardPile.push(layedDownCard);

		counter++;
		return counter;
	}

	/**
	 * plays the selected card and execute this card
	 *
	 */
	public Message play(CardName cardName, int index) {
		Card playedCard = null;
		this.actualPhase = "play";
		
		playedCard = this.handCards.remove(index);
		playedCard.executeCard(this);
		playedCards.add(playedCard);
		
		if (this.getActions() > 0){
			this.ugmsg.setLog("");
			this.ugmsg.setCurrentPlayer(this.getPlayerName());
			this.ugmsg.setCoins(this.getCoins());
			this.ugmsg.setActions(this.getActions());
			this.ugmsg.setBuys(this.getBuys());
			this.ugmsg.setCurrentPhase(this.actualPhase);
			this.ugmsg.setDiscardPileTopCard(this.discardPile.firstElement());
			this.ugmsg.setDiscardPileCardNumber(this.discardPile.size());
			this.ugmsg.setDeckPileCardNumber(this.deckPile.size());
			this.ugmsg.setNewHandCards(handCards);
			this.ugmsg.setPlayedCards(playedCard);
			this.ugmsg.setChat("");
			
			return this.ugmsg;	
		}
		else
			return this.fmsg;
	}
	
//	//Checks if a card can be played
//	public boolean played(){
//		if(this.getActions() > 0)
//			return true;
//		else
//			return false;
//	}

	/**
	 * skips actual phase and goes to the next phase
	 */
	public Message skipPhase() {
		switch (this.actualPhase) {
		case "play":
			//this.buy(cardName);
		case "buy":
			this.cleanUp();
		}
		
		return this.ugmsg;
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

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
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

	public boolean isFinished() {
		return isFinished;
	}

	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}

	public String getActualPhase() {
		return actualPhase;
	}

	public void setActualPhase(String actualPhase) {
		this.actualPhase = actualPhase;
	}

	public ServerThreadForClient getServerThreadForClient() {
		return serverThreadForClient;
	}

	public void setServerThreadForClient(ServerThreadForClient serverThreadForClient) {
		this.serverThreadForClient = serverThreadForClient;
	}
}// end Player