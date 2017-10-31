package Server_Services;

import Server_GameLogic.Player;

/**
 * @author Renate
 * @version 1.0
 * @created 31-Okt-2017 17:08:48
 */
public class DB_Connector {

	private DB_Connector connection;
	private ServiceLocator sl = ServiceLocator.getServiceLocator();


	private DB_Connector(){

	}

	/**
	 * 
	 * @param name
	 * @param password
	 */
	public void addNewPlayer(String name, String password){

	}

	/**
	 * 
	 * @param player
	 * @param score
	 */
	public int addScore(Player player, int score){
		return 0;
	}

	/**
	 * 
	 * @param name
	 */
	public boolean checkNameHighlander(String name){
		return false;
	}

	/**
	 * 
	 * @param name
	 */
	public void deletePlayer(String name){

	}

	public static DB_Connector getConnection(){
		return null;
	}

	public String getHighScore(){
		return "";
	}
}//end DB_Connector