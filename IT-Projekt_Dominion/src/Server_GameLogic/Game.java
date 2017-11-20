package Server_GameLogic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

import Cards.Copper_Card;
import Cards.CardName;
import Cards.Cellar_Card;
import Cards.Duchy_Card;
import Cards.Estate_Card;
import Cards.Gold_Card;
import Cards.Market_Card;
import Cards.Mine_Card;
import Cards.Province_Card;
import Cards.Remodel_Card;
import Cards.Silver_Card;
import Cards.Smithy_Card;
import Cards.Village_Card;
import Cards.Woodcutter_Card;
import Cards.Workshop_Card;
import Messages.Message;

/**
 * @author Bodo
 * @version 1.0
 * @created 31-Okt-2017 17:08:54
 */
public class Game {

	private Stack<Copper_Card> copperPile;
	private Stack<Cellar_Card> cellarPile;
	private Stack<Duchy_Card> duchyPile;
	private Stack<Estate_Card> estatePile;
	private Stack<Gold_Card> goldPile;
	private Stack<Market_Card> marketPile;
	private Stack<Mine_Card> minePile;
	private Stack<Province_Card> provincePile;
	private Stack<Remodel_Card> remodelPile;
	private Stack<Silver_Card> silverPile;
	private Stack<Smithy_Card> smithyPile;
	private Stack<Village_Card> villagePile;
	private Stack<Woodcutter_Card> woodcutterPile;
	private Stack<Workshop_Card> workshopPile;
	private HashMap<CardName, Integer> buyCards;

	private final int NUM_OF_TREASURECARDS = 30;
	private final int NUM_OF_VICTORYCARDS = 30;
	private final int NUM_OF_ACTIONCARDS = 10;

	private static int gameCounter = 0;
	private boolean gameEnded;

	private Player player1;
	private Player player2;
	private Player currentPlayer;

	private static Game existingGame;

	/**
	 * 
	 * @param clientSocket
	 * @param gameMode
	 * @param player
	 */
	private Game() {
		// Build treasure stacks for a new game
		this.buildTreasureCardStacks();
		this.buildVictoryCardStacks();
		this.buildActionCardStacks();

		this.setGameEnded(false);
		this.buyCards = new HashMap<CardName, Integer>();
	}

	// Builds stacks for the treasure cards
	private void buildTreasureCardStacks() {
		this.copperPile = new Stack<Copper_Card>();
		this.silverPile = new Stack<Silver_Card>();
		this.goldPile = new Stack<Gold_Card>();

		for (int i = 0; i < NUM_OF_TREASURECARDS; i++) {
			this.copperPile.push(new Copper_Card());
			this.silverPile.push(new Silver_Card());
			this.goldPile.push(new Gold_Card());
		}
	}

	// Builds stacks for the victory cards
	private void buildVictoryCardStacks() {
		this.estatePile = new Stack<Estate_Card>();
		this.duchyPile = new Stack<Duchy_Card>();
		this.provincePile = new Stack<Province_Card>();

		for (int i = 0; i < NUM_OF_VICTORYCARDS; i++) {
			this.estatePile.push(new Estate_Card());
			this.duchyPile.push(new Duchy_Card());
			this.provincePile.push(new Province_Card());
		}
	}

	// Builds stacks for the action cards
	private void buildActionCardStacks() {
		this.cellarPile = new Stack<Cellar_Card>();
		this.marketPile = new Stack<Market_Card>();
		this.minePile = new Stack<Mine_Card>();
		this.remodelPile = new Stack<Remodel_Card>();
		this.smithyPile = new Stack<Smithy_Card>();
		this.villagePile = new Stack<Village_Card>();
		this.woodcutterPile = new Stack<Woodcutter_Card>();
		this.workshopPile = new Stack<Workshop_Card>();

		for (int i = 0; i < NUM_OF_ACTIONCARDS; i++) {
			this.cellarPile.push(new Cellar_Card());
			this.marketPile.push(new Market_Card());
			this.minePile.push(new Mine_Card());
			this.remodelPile.push(new Remodel_Card());
			this.smithyPile.push(new Smithy_Card());
			this.villagePile.push(new Village_Card());
			this.woodcutterPile.push(new Woodcutter_Card());
			this.workshopPile.push(new Workshop_Card());
		}
	}

	// initialites the players to start a game
	public void startGame() {
		for (int i = 0; i < 10; i++) {
			if (i < 7) {
				this.player1.deckPile.push(this.copperPile.pop());
				this.player2.deckPile.push(this.copperPile.pop());
			}
			if (i >= 7) {
				this.player1.deckPile.push(this.estatePile.pop());
				this.player2.deckPile.push(this.estatePile.pop());
			}
		}

		this.player1.draw(player1.NUM_OF_HANDCARDS);
		this.player2.draw(player2.NUM_OF_HANDCARDS);
		this.currentPlayer = this.getStarter();
	}

	// Determines randomly the starter between player1 and player2
	private Player getStarter() {
		Random rand = new Random();
		int starter = rand.nextInt(2);
		if (starter == 0)
			return player1;

		return player2;
	}

	/**
	 * switches Player
	 */
	public void switchPlayer() {
		if (currentPlayer.equals(this.player1)) {
			this.currentPlayer = player2;
		} else {
			this.currentPlayer = player1;
		}

	}

	public void checkWinner() {
		if (this.checkGameEnding()) {
			player1.countVictoryPoints();
			player2.countVictoryPoints();
			
			if (player1.getVictoryPoints() > player2.getVictoryPoints())
				player1.isWinner(true);
			else if (player1.getVictoryPoints() == player2.getVictoryPoints()) {
				if (player1.getMoves() > player2.getMoves())
					player1.isWinner(true);
				else if (player1.getMoves() == player2.getMoves()) {
					player1.isWinner(true);
					player2.isWinner(true);
				} else
					player2.isWinner(true);
			} else
				player2.isWinner(true);
		}
	}

	public boolean checkGameEnding() {
		int counter = 0;
		LinkedList<Stack> allStacks = new LinkedList<Stack>();
		allStacks.add(this.cellarPile);
		allStacks.add(this.copperPile);
		allStacks.add(this.duchyPile);
		allStacks.add(this.estatePile);
		allStacks.add(this.goldPile);
		allStacks.add(this.marketPile);
		allStacks.add(this.minePile);
		allStacks.add(this.remodelPile);
		allStacks.add(this.silverPile);
		allStacks.add(this.smithyPile);
		allStacks.add(this.villagePile);
		allStacks.add(this.woodcutterPile);
		allStacks.add(this.workshopPile);

		Iterator<Stack> iter = allStacks.iterator();

		while (iter.hasNext()) {
			if (iter.next().isEmpty())
				counter++;
		}

		if (this.provincePile.isEmpty() || counter == 3)
			return true;
		else
			return false;
	}
	
	public static Game getGame(GameMode gameMode, Player player) {
		if (gameMode == GameMode.Multiplayer) {
			if (gameCounter % 2 == 0) {
				Game game = new Game();

				game.setPlayer1(player);
				existingGame = game;

				gameCounter++;
			} else {
				existingGame.setPlayer2(player);
				gameCounter++;
				existingGame.getPlayer1().getServerThreadForClient().addWaitingMessages(existingGame.getPlayer1().getServerThreadForClient().getCG_Message());
				existingGame.getPlayer2().getServerThreadForClient().addWaitingMessages(existingGame.getPlayer2().getServerThreadForClient().getCG_Message());
			}
			return existingGame;
		} else {
			Game game = new Game();
			Bot bot = new Bot("Bobby");
			game.setPlayer1(player);
			game.setPlayer2(bot);
			bot.addGame(game);
			game.getPlayer1().getServerThreadForClient().addWaitingMessages(existingGame.getPlayer1().getServerThreadForClient().getCG_Message());
			return game;
		}

	}

	// Fills a HashMap with the cardName and size of the actual stack of the
	// cards, which the player could buy
	public HashMap<CardName, Integer> getBuyCards() {
		for (int i = 0; i < NUM_OF_VICTORYCARDS; i++) {
			this.buyCards.put(this.provincePile.firstElement().getCardName(), this.provincePile.size());
			this.buyCards.put(this.duchyPile.firstElement().getCardName(), this.duchyPile.size());
			this.buyCards.put(this.estatePile.firstElement().getCardName(), this.estatePile.size());
		}

		for (int i = 0; i < NUM_OF_TREASURECARDS; i++) {
			this.buyCards.put(this.copperPile.firstElement().getCardName(), this.copperPile.size());
			this.buyCards.put(this.goldPile.firstElement().getCardName(), this.goldPile.size());
			this.buyCards.put(this.silverPile.firstElement().getCardName(), this.silverPile.size());
		}

		for (int i = 0; i < NUM_OF_ACTIONCARDS; i++) {
			this.buyCards.put(this.workshopPile.firstElement().getCardName(), this.workshopPile.size());
			this.buyCards.put(this.woodcutterPile.firstElement().getCardName(), this.woodcutterPile.size());
			this.buyCards.put(this.villagePile.firstElement().getCardName(), this.villagePile.size());
			this.buyCards.put(this.smithyPile.firstElement().getCardName(), this.smithyPile.size());
			this.buyCards.put(this.remodelPile.firstElement().getCardName(), this.remodelPile.size());
			this.buyCards.put(this.minePile.firstElement().getCardName(), this.minePile.size());
			this.buyCards.put(this.marketPile.firstElement().getCardName(), this.marketPile.size());
			this.buyCards.put(this.cellarPile.firstElement().getCardName(), this.cellarPile.size());
		}

		return this.buyCards;
	}
	

	
	public void sendToOpponent(Player source, Message msg) {
		source.getServerThreadForClient().addWaitingMessages(msg);
	}

	public Player getOpponent(Player source) {
		if (source.equals(player1))
			return player2;

		return player1;
	}

	public int getGameCounter() {
		return gameCounter;
	}

	public Stack<Copper_Card> getCopperPile() {
		return copperPile;
	}

	public Stack<Silver_Card> getSilverPile() {
		return silverPile;
	}

	public Stack<Gold_Card> getGoldPile() {
		return goldPile;
	}

	public Stack<Estate_Card> getEstatePile() {
		return estatePile;
	}

	public Stack<Duchy_Card> getDuchyPile() {
		return duchyPile;
	}

	public Stack<Province_Card> getProvincePile() {
		return provincePile;
	}

	public Stack<Cellar_Card> getCellarPile() {
		return cellarPile;

	}

	public Stack<Market_Card> getMarketPile() {
		return marketPile;
	}

	public Stack<Mine_Card> getMinePile() {
		return minePile;
	}

	public Stack<Remodel_Card> getRemodelPile() {
		return remodelPile;
	}

	public Stack<Smithy_Card> getSmithyPile() {
		return smithyPile;
	}

	public Stack<Village_Card> getVillagePile() {
		return villagePile;
	}

	public Stack<Woodcutter_Card> getWoodcutterPile() {
		return woodcutterPile;
	}

	public Stack<Workshop_Card> getWorkshopPile() {
		return workshopPile;
	}

	public boolean isGameEnded() {
		return gameEnded;
	}

	public void setGameEnded(boolean gameEnded) {
		this.gameEnded = gameEnded;
	}

	public Player getPlayer1() {
		return player1;
	}

	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
}// end Game