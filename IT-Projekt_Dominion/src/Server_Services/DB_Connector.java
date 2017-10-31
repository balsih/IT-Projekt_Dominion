

/**
 * @author Renate
 * @version 1.0
 * @created 31-Okt-2017 17:08:48
 */
public class DB_Connector {

	private DB_Connector connection;
	private ServiceLocator sl;
	public ServiceLocator m_ServiceLocator;



	public void finalize() throws Throwable {

	}
	private DB_Connector(){

	}

	/**
	 * 
	 * @param name
	 * @param password
	 */
	public addNewPlayer(String name, String password){

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
	public deletePlayer(String name){

	}

	public static DB_Connector getConnection(){
		return null;
	}

	public String getHighScore(){
		return "";
	}
}//end DB_Connector