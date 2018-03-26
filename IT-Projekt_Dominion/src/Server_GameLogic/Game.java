package Server_GameLogic;

import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Stack;
import java.util.logging.Logger;
import Cards.Copper_Card;
import Cards.CardName;
import Cards.CardType;
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
import Server_Services.DB_Connector;

/**
 * The game class represents a game with all available cards and two players. If
 * player 2 is a human or a computer player depends on the game mode.
 * 
 * Simulation mode is just for testing the bot and is not used in final game.
 * 
 * @author Bodo Gruetter
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

	private Player player1 = null;
	private Player player2 = null;
	private Player currentPlayer;
	private Bot bot;
	private Bot bot2;

	private static Game existingGame;
	private GameMode gameMode;

	private final Logger logger = Logger.getLogger("");

	private Game() {
		// Build treasure stacks for a new game
		this.buildTreasureCardStacks();
		this.buildVictoryCardStacks();
		this.buildActionCardStacks();

		this.gameEnded = false;
		this.buyCards = new HashMap<CardName, Integer>();
	}

	/**
	 * Builds the stacks for the treasure cards with 30 cards per stack.
	 * 
	 * @author Bodo Gruetter
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
	 * Builds the stacks for the victory cards with 8 cards per stack.
	 * 
	 * @author Bodo Gruetter
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
	 * Builds the stacks for the action cards with 10 cards per stack.
	 * 
	 * @author Bodo Gruetter
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
	 * Fills the deck pile of the player with 7 copper cards and 3 estate cards
	 * and shuffles the deck. Each player draws 5 cards of its stack in the
	 * hand. Finally the starter of the game will be determined.
	 * 
	 * @author Bodo Gruetter
	 */
	public void startGame() {
		// fills the starter decks of the two players
		for (int i = 0; i < 10; i++) {
			if (i < 7) {
				this.player1.deckPile.push(new Copper_Card());
				this.player2.deckPile.push(new Copper_Card());
			}
			if (i >= 7) {
				this.player1.deckPile.push(new Estate_Card());
				this.player2.deckPile.push(new Estate_Card());
			}
		}

		// Shuffle the stacks and lets the players draw their first hand
		Collections.shuffle(this.player1.deckPile);
		this.player1.draw(player1.NUM_OF_HANDCARDS);
		
		Collections.shuffle(this.player2.deckPile);
		this.player2.draw(player2.NUM_OF_HANDCARDS);
		
		this.currentPlayer = this.getStarter();
		this.currentPlayer.resetStates();
		this.player1.setActualPhase(Phase.Buy);
		this.player2.setActualPhase(Phase.Buy);
	}

	/**
	 * Determines randomly who starts the game.
	 * 
	 * @author Bodo Gruetter
	 * 
	 * @return Player, the player who starts.
	 */
	private Player getStarter() {
		Random rand = new Random();
		int starter = rand.nextInt(2);
		if (starter == 0)
			return player1;

		return player2;
	}

	/**
	 * Switches the current player. In single player the bot will be executed if
	 * its turn. In multi player the current player will be initialized.
	 * 
	 * @author Bodo Gruetter
	 */
	public void switchPlayer() {

		currentPlayer.resetStates();

		if (currentPlayer.equals(this.player1)) {
			this.currentPlayer = player2;

			if (this.gameMode == GameMode.Singleplayer)
				new Thread(bot).start();
			else if (this.gameMode == GameMode.Simulation)
				new Thread(bot2).start();

		} else {
			this.currentPlayer = player1;

			if (this.gameMode == GameMode.Simulation)
				new Thread(bot).start();
		}

		if (!this.currentPlayer.containsCardType(this.currentPlayer.handCards, CardType.Action))
			this.currentPlayer.setActualPhase(Phase.Buy);
	}

	/**
	 * Lets the players count their points, checks the winner of a game and sets
	 * the player status.
	 * 
	 * @author Bodo Gruetter
	 */
	public void checkWinner() {
		this.player1.countVictoryPoints();
		this.player2.countVictoryPoints();

		if (this.player1.getVictoryPoints() > this.player2.getVictoryPoints()) {
			this.player1.setStatus(GameSuccess.Won);
			this.player2.setStatus(GameSuccess.Lost);
		} else if (this.player1.getVictoryPoints() == this.player2.getVictoryPoints()) {
			if (this.player1.getMoves() < this.player2.getMoves()) {
				this.player1.setStatus(GameSuccess.Won);
				this.player2.setStatus(GameSuccess.Lost);
			} else if (this.player1.getMoves() == this.player2.getMoves()) {
				this.player1.setStatus(GameSuccess.Won);
				this.player2.setStatus(GameSuccess.Won);
			} else {
				this.player1.setStatus(GameSuccess.Lost);
				this.player2.setStatus(GameSuccess.Won);
			}
		} else {
			this.player1.setStatus(GameSuccess.Lost);
			this.player2.setStatus(GameSuccess.Won);
		}

		// saves score in database
		this.saveScore();
	}

	/**
	 * Saves the score of the winner excluding bot in database.
	 * 
	 * @author Bodo Gruetter
	 */
	private void saveScore() {
		DB_Connector connector = DB_Connector.getDB_Connector();

		if ((this.gameMode.equals(GameMode.Singleplayer) || this.gameMode.equals(GameMode.Multiplayer))
				&& this.player1.getStatus().equals(GameSuccess.Won))
			connector.addScore(this.player1, this.player1.getVictoryPoints(), this.player1.getMoves());
		if (this.gameMode.equals(GameMode.Multiplayer) && this.player2.getStatus().equals(GameSuccess.Won))
			connector.addScore(this.player2, this.player2.getVictoryPoints(), this.player2.getMoves());
	}

	/**
	 * Checks if the game is over.
	 * 
	 * @author Bodo Gruetter
	 * 
	 * @return true or false, depending if the game is finished.
	 */
	public boolean checkGameEnding() {
		int counter = 0;

		/*
		 * iterates through all stacks and the number of remaining cards to
		 * count how much stacks are empty.
		 */
		Iterator<Integer> valueIterator = this.getBuyCards().values().iterator();
		while (valueIterator.hasNext()) {
			if (valueIterator.next() == 0)
				counter++;
		}

		if (this.provincePile.isEmpty() || counter == 3)
			return this.gameEnded = true;

		return false;
	}

	/**
	 * Prepares a game for playing.
	 * 
	 * @author Bodo Gruetter
	 * 
	 * @param gameMode
	 * , the selected gameMode
	 * @param player
	 * , the player who starts a game.
	 * @return 
	 * Game, an existing or a new game depending on gameMode and if a
	 * player is waiting for another.
	 */
	public static Game getGame(GameMode gameMode, Player player) {
		/*
		 * checks in multiplayer mode if already a game of a waiting player
		 * exists, and if necessary creates one, sets the two players and starts
		 * the game.
		 */
		if (gameMode == GameMode.Multiplayer) {
			if (gameCounter % 2 == 0) {
				Game game = new Game();

				game.setPlayer1(player);
				existingGame = game;
				existingGame.player1.setGame(existingGame);

				gameCounter++;
			} else {
				existingGame.setPlayer2(player);
				existingGame.player2.setGame(existingGame);

				gameCounter++;
				existingGame.gameMode = GameMode.Multiplayer;
				existingGame.startGame();

				existingGame.getPlayer1().getServerThreadForClient().addWaitingMessages(
						existingGame.getPlayer1().getServerThreadForClient().getCG_Message(existingGame));
				existingGame.getPlayer2().getServerThreadForClient().addWaitingMessages(
						existingGame.getPlayer2().getServerThreadForClient().getCG_Message(existingGame));
				existingGame.logger.info(existingGame.player1.getPlayerName() + " started a multiplayer game versus "
						+ existingGame.player2.getPlayerName());

			}
			return existingGame;

			/*
			 * creates and starts in singleplayer mode a game with a player and
			 * a bot
			 */
		} else if (gameMode == GameMode.Singleplayer) {
			Game existingGame = new Game();
			existingGame.bot = new Bot(Bot.getNameOfBot(), player.getServerThreadForClient());
			existingGame.setPlayer1(player);
			existingGame.player1.setGame(existingGame);
			existingGame.setPlayer2(existingGame.bot);
			existingGame.bot.setGame(existingGame);
			existingGame.gameMode = GameMode.Singleplayer;
			existingGame.startGame();

			existingGame.getPlayer1().getServerThreadForClient()
					.addWaitingMessages(existingGame.getPlayer1().getServerThreadForClient().getCG_Message(existingGame));
			existingGame.logger.info(existingGame.player1.getPlayerName() + " started a singleplayer game");
			return existingGame;

			/*
			 * creates in simulation mode for testing a game between two bots,
			 * but doesn't start game directly
			 */
		} else {
			Game existingGame = new Game();
			Player dummyPlayer = new Player("hallo", ServerThreadForClient.getServerThreadForClient(new Socket()));
			existingGame.bot = new Bot(Bot.getNameOfBot(), dummyPlayer.getServerThreadForClient());
			existingGame.setPlayer1(existingGame.bot);
			existingGame.bot.setGame(existingGame);

			existingGame.bot2 = new Bot(Bot.getNameOfBot(), dummyPlayer.getServerThreadForClient());
			existingGame.setPlayer2(existingGame.bot2);
			existingGame.bot2.setGame(existingGame);

			existingGame.gameMode = GameMode.Simulation;
			return existingGame;
		}
	}

	/**
	 * Builds a Hashmap with all names of the card stacks and their size.
	 * 
	 * @author Bodo Gruetter
	 * 
	 * @return
	 * Hashmap, with the stackname and the number of cards
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
	 * Gets the opponent of the current player.
	 *         
	 * @author Bodo Gruetter
	 * 
	 * @param
	 * currentPlayer, the current Player
	 * @return
	 * Player, the opponent of the currentplayer
	 */
	public Player getOpponent(Player currentPlayer) {
		if (currentPlayer.equals(player1))
			return player2;

		return player1;
	}

	public static int getGameCounter() {
		return gameCounter;
	}

	public static void setGameCounter(int gameCounter) {
		Game.gameCounter = gameCounter;
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

	public boolean getGameEnded() {
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

	public GameMode getGameMode() {
		return gameMode;
	}

}// end Game