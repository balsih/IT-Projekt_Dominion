import Klassendiagramm.Cards.Card;

/**
 * @author Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:08:54
 */
public class Game {

	private Stack broncePile;
	private Stack cellarPile;
	private Stack duchyPile;
	private Stack estatePile;
	private static int gameCounter;
	private String gameMode;
	private Stack goldPile;
	private Stack marketPile;
	private Stack minePile;
	private Player player1;
	private Player player2;
	private Stack provincePile;
	private Stack remodelPile;
	private ServerThreadForClient serverThreadForClientP1;
	private ServerThreadForClient serverThreadForClientP2;
	private Stack silverPile;
	private Stack smithyPile;
	private Stack villagePile;
	private Stack woodcutterPile;
	private Stack workShopPile;
	public Card m_Card;
	public Bot m_Bot;

	public Game(){

	}

	public void finalize() throws Throwable {

	}
	/**
	 * 
	 * @param clientSocket
	 * @param gameMode
	 * @param player
	 */
	private Game(Socket clientSocket, String gameMode, Player player){

	}

	public boolean checkGameEnding(){
		return false;
	}

	/**
	 * 
	 * @param clientSocket
	 * @param gameMode
	 * @param player
	 */
	public static Game getGame(Socket clientSocket, String gameMode, Player player){
		return null;
	}

	public int getGameCouner(){
		return 0;
	}

	public Player getOpponent(){
		return null;
	}

	public boolean isReadyToStart(){
		return false;
	}

	/**
	 * 
	 * @param cardName
	 */
	public Card removeCard(String cardName){
		return null;
	}
}//end Game