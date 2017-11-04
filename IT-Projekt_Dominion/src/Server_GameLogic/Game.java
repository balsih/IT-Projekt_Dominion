package Server_GameLogic;

import java.net.Socket;
import java.util.HashMap;
import java.util.Stack;

import Cards.Bronce_Card;
import Cards.Card;
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
import Messages.UpdateGame_Message;

/**
 * @author Bodo
 * @version 1.0
 * @created 31-Okt-2017 17:08:54
 */
public class Game {

	private Stack<Bronce_Card> broncePile;
	private Stack<Cellar_Card> cellarPile;
	private Stack<Duchy_Card> duchyPile;
	private Stack<Estate_Card> estatePile;
	private static int gameCounter = 0;
	private String gameMode;
	private Stack<Gold_Card> goldPile;
	private Stack<Market_Card> marketPile;
	private Stack<Mine_Card> minePile;
	private Player player1;
	private Player player2;
	private Stack<Province_Card> provincePile;
	private Stack<Remodel_Card> remodelPile;
	private ServerThreadForClient serverThreadForClientP1;
	private ServerThreadForClient serverThreadForClientP2;
	private Stack<Silver_Card> silverPile;
	private Stack<Smithy_Card> smithyPile;
	private Stack<Village_Card> villagePile;
	private Stack<Woodcutter_Card> woodcutterPile;
	private Stack<Workshop_Card> workshopPile;
	private HashMap<Card, Integer> buyedCards;

	private final int NUM_OF_TREASURECARDS = 30;
	private final int NUM_OF_VICTORYCARDS = 30;
	private final int NUM_OF_ACTIONCARDS = 10;

	/**
	 * 
	 * @param clientSocket
	 * @param gameMode
	 * @param player
	 */
	public Game(Socket clientSocket, String gameMode, Player player) {
		//Build treasure stacks for a new game
		this.buildTreasureCardStacks();
		this.buildVictoryCardStacks();
		this.buildActionCardStacks();
	}

	// Builds stacks for the treasure cards
	private void buildTreasureCardStacks() {
		this.broncePile = new Stack<Bronce_Card>();
		this.silverPile = new Stack<Silver_Card>();
		this.goldPile = new Stack<Gold_Card>();

		for (int i = 0; i < NUM_OF_TREASURECARDS; i++) {
			this.broncePile.push(new Bronce_Card());
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

	public boolean checkGameEnding() {
		return false;
	}

	/**
	 * 
	 * @param clientSocket
	 * @param gameMode
	 * @param player
	 */
	public static Game getGame(Socket clientSocket, String gameMode, Player player) {
		return null;
	}

	public int getGameCouner() {
		return 0;
	}

	public Player getOpponent() {
		return null;
	}

	public boolean isReadyToStart() {
		return false;
	}

	public HashMap<Card, Integer> getBuyedCards() {

		return this.buyedCards;
	}

	/**
	 * 
	 * @param cardName
	 */
	public Card removeCard(String cardName) {
		return null;
	}

	public void sendToOpponent(ServerThreadForClient source, UpdateGame_Message ugmsg) {
		// An anderen Spieler als source eine waiting message schicken, welche
		// beim anderen Spieler (Thread) in die Queue gespeichert wird
	}

	public Stack<Bronce_Card> getBroncePile() {
		return broncePile;
	}

	public Stack<Silver_Card> getSilverPile() {
		return silverPile;
	}

	public Stack<Gold_Card> getGildPile() {
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
	
	public Stack<Village_Card> getVillagePile(){
		return villagePile;
	}
	
	public Stack<Woodcutter_Card> getWoodcutterPile(){
		return woodcutterPile;
	}
	
	public Stack<Workshop_Card> getWorkshopPile(){
		return workshopPile;
	}
}// end Game