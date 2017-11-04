package Server_GameLogic;

import java.net.Socket;
import java.util.HashMap;
import java.util.Stack;

import Cards.Copper_Card;
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

	private Stack<Copper_Card> broncePile;
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
	private Stack<Workshop_Card> workShopPile;
	private HashMap<Card, Integer> buyedCards;

	/**
	 * 
	 * @param clientSocket
	 * @param gameMode
	 * @param player
	 */
	public Game(Socket clientSocket, String gameMode, Player player) {

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
}// end Game