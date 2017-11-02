package Server_Services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import Server_GameLogic.Player;

/**
 * @author Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:08:48
 */
public class DB_Connector {

	private DB_Connector connection;
	private ServiceLocator sl = ServiceLocator.getServiceLocator();
	
	private Connection connect;
	private Statement statement;
	private PreparedStatement prepStatement;
	private ResultSet resultSet;


	private DB_Connector(){
		try{
			// This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager
                    .getConnection("jdbc:mysql://localhost/feedback?"
                            + "user=sqluser&password=sqluserpw");
		} catch (Exception e){
			
		}
	}

	/**
	 * 
	 * @param name
	 * @param password
	 */
	public boolean addNewPlayer(String name, String password){
		return false;
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