package Server_GameLogic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

import Cards.Copper_Card;
import Cards.Card;
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
import Messages.GameSuccess;
import Messages.Interaction;

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
	private final int NUM_OF_VICTORYCARDS = 8;
	private final int NUM_OF_ACTIONCARDS = 10;

	private static int gameCounter = 0;
	private boolean gameEnded;

	private Player player1;
	private Player player2;
	private Player currentPlayer;
	private Bot bot;

	private static Game existingGame;
	private GameMode gameMode;

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

		this.gameEnded = false;
		this.buyCards = new HashMap<CardName, Integer>();
	}

	/**
	 * @author Bodo Gruetter
	 * 
	 *         builds the stacks for the treasure cards with 30 cards per stack.
	 */
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

	/**
	 * @author Bodo Gruetter
	 * 
	 *         builds the stacks for the victory cards with 20 cards per stack.
	 */
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

	/**
	 * @author Bodo Gruetter
	 * 
	 *         builds the stacks for the action cards with 10 cards per stack.
	 */
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

	/**
	 * @author Bodo Gruetter
	 * 
	 *         fills the deckpile of the player with 7 copper cards and 3 estate
	 *         cards. Each player draws 5 cards of its stack in the hand.
	 *         finally the the starter of the game will be determined.
	 */
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
		this.currentPlayer.startMove();
	}

	/**
	 * @author Bodo Gruetter
	 * 
	 *         determines randomly the player who starts.
	 * 
	 * @return the player who starts.
	 */
	private Player getStarter() {
		Random rand = new Random();
		int starter = rand.nextInt(2);
		if (starter == 0)
			return player1;

		return player2;
	}

	/**
	 * @author Bodo Gruetter
	 * 
	 *         switches the current player.
	 */
	public void switchPlayer() {
			if (currentPlayer.equals(this.player1)) {
				this.currentPlayer = player2;

				if(this.gameMode == GameMode.Singleplayer){
					new Thread(bot).start();
				}
			} else {
				this.currentPlayer = player1;
			}
			
			currentPlayer.startMove();
	}

	/**
	 * @author Bodo Gruetter
	 * 
	 *         lets the players count their points, checks the winner of a game
	 *         and sets the player status.
	 */
	public void checkWinner() {
		player1.countVictoryPoints();
		player2.countVictoryPoints();

		if (player1.getVictoryPoints() > player2.getVictoryPoints()) {
			player1.setStatus(GameSuccess.Won);
			player2.setStatus(GameSuccess.Lost);
		} else if (player1.getVictoryPoints() == player2.getVictoryPoints()) {
			if (player1.getMoves() > player2.getMoves()) {
				player1.setStatus(GameSuccess.Won);
				player2.setStatus(GameSuccess.Lost);
			} else if (player1.getMoves() == player2.getMoves()) {
				player1.setStatus(GameSuccess.Won);
				player2.setStatus(GameSuccess.Won);
			} else {
				player2.setStatus(GameSuccess.Lost);
				player2.setStatus(GameSuccess.Won);
			}
		} else {
			player1.setStatus(GameSuccess.Lost);
			player2.setStatus(GameSuccess.Won);
		}
	}

	/**
	 * @author Bodo Gruetter
	 * 
	 *         checks if the game is over.
	 * 
	 * @return true or false dependng if the game is finished.
	 */
	public boolean checkGameEnding() {
		int counter = 0;

		Iterator<Integer> valueIterator = this.getBuyCards().values().iterator();

		while (valueIterator.hasNext()) {
			if (valueIterator.next() == 0)
				counter++;
		}

		if (this.provincePile.isEmpty() || counter == 3)
			return true;
		else
			return false;
	}

	/**
	 * @author Bodo Gruetter
	 * 
	 * @param the
	 *            selected gameMode and the player who starts a game.
	 * @return an existing or a new game depending on gameMode and if a player
	 *         is waiting for another.
	 */
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
				existingGame.startGame();
				
				existingGame.getPlayer1().getServerThreadForClient()
						.addWaitingMessages(existingGame.getPlayer1().getServerThreadForClient().getCG_Message(existingGame));
				existingGame.getPlayer2().getServerThreadForClient()
						.addWaitingMessages(existingGame.getPlayer2().getServerThreadForClient().getCG_Message(existingGame));
			}

			existingGame.gameMode = GameMode.Multiplayer;
			return existingGame;
		} else {
			Game game = new Game();
			game.bot = new Bot(Bot.getNameOfBot());
			game.setPlayer1(player);
			game.setPlayer2(game.bot);
			game.bot.addGame(game);
			game.startGame();
			game.gameMode = GameMode.Singleplayer;
			
			game.getPlayer1().getServerThreadForClient()
					.addWaitingMessages(game.getPlayer1().getServerThreadForClient().getCG_Message(game));
			return game;
		}

	}

	/**
	 * @author Bodo Gruetter
	 * 
	 *         fills a hashmap with all stacks and numbers of cards which a
	 *         player could buy.
	 * 
	 * @return a hashmap with the stackname and the number of cards
	 */
	public HashMap<CardName, Integer> getBuyCards() {
			this.buyCards.put(CardName.Province, this.provincePile.size());
			this.buyCards.put(CardName.Duchy, this.duchyPile.size());
			this.buyCards.put(CardName.Estate, this.estatePile.size());

			this.buyCards.put(CardName.Copper, this.copperPile.size());
			this.buyCards.put(CardName.Gold, this.goldPile.size());
			this.buyCards.put(CardName.Silver, this.silverPile.size());

			this.buyCards.put(CardName.Workshop, this.workshopPile.size());
			this.buyCards.put(CardName.Woodcutter, this.woodcutterPile.size());
			this.buyCards.put(CardName.Village, this.villagePile.size());
			this.buyCards.put(CardName.Smithy, this.smithyPile.size());
			this.buyCards.put(CardName.Remodel, this.remodelPile.size());
			this.buyCards.put(CardName.Mine, this.minePile.size());
			this.buyCards.put(CardName.Market, this.marketPile.size());
			this.buyCards.put(CardName.Cellar, this.cellarPile.size());

		return this.buyCards;
	}

	/**
	 * @author Bodo Gruetter
	 * 
	 *         checks the opponent of the current player.
	 * 
	 * @param the
	 *            current Player
	 * @return the opponent of the currentplayer
	 */
	public Player getOpponent(Player currentPlayer) {
		if (currentPlayer.equals(player1))
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