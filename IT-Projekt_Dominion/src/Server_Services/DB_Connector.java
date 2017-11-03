package Server_Services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Server_GameLogic.Player;

/**
 * @author Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:08:48
 */
public class DB_Connector {

	private ServiceLocator sl = ServiceLocator.getServiceLocator();
	private DB_Connector connector;

	private String driver;
	private String url;
	private String user;
	private String password;

	private Connection connection;

	protected DB_Connector() {

		this.connection = null;
		this.driver = "com.mysql.jdbc.Driver";
		this.url = "jdbc:h2:~/test";
		this.user = "sa";
		this.password = "";
	}

	/**
	 * 
	 * @param name
	 * @param password
	 */
	public boolean addNewPlayer(String name, String password) {
		return false;
	}

	/**
	 * 
	 * @param player
	 * @param score
	 */
	public int addScore(Player player, int score) {
		return 0;
	}

	/**
	 * 
	 * @param name
	 */
	public boolean checkNameHighlander(String name) {
		return false;
	}

	/**
	 * 
	 * @param name
	 */
	public void deletePlayer(String name) {

	}

	public String getHighScore() {
		return "";
	}

	public DB_Connector getDB_Connector() {
		return this.connector;
	}

	private void createConnection() {

		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName(driver);
			// Setup the connection with the DB
			connection = DriverManager.getConnection(url, user, password);

			if (connection != null)
				System.out.println("connected");
		} catch (Exception e) {
			System.out.println("not connected");
		}

	}

	public void createDB() {
		
	}
	
	//Test
	public static void main(String[] args){
		DB_Connector connector = new DB_Connector();
		connector.createDB();
	}
}// end DB_Connector