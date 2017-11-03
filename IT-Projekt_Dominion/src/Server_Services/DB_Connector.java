package Server_Services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Server_GameLogic.Player;

/**
 * @author Bodo Grütter
 * @version 1.0
 * @created 31-Okt-2017 17:08:48
 */
public class DB_Connector {

	private ServiceLocator sl = ServiceLocator.getServiceLocator();
	private DB_Connector connector;

	private Connection connection;
	private Statement stmt;
	private ResultSet rs;

	protected DB_Connector() {
		try {			
			//Load Driver
			Class.forName("org.h2.Driver");
			
			//creates Connection with DB on Server, and creates DB_Dominion if not exists
			String path = "jdbc:h2:~/Server_Services/DB_Dominion.mv.db";
			String user = "sa";
			String pw = "";
			this.connection = DriverManager.getConnection(path, user, pw);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	//creates the db structure
	public void createDBStructure() {
		try {
			String createPlayer = "create table if not exists Player(Username varchar(25) primary key, Password varchar (25))";
			String createScoring = "create table if not exists Scoring(Score int primary key)";
			String createPlayer_Scoring = "create table if not exists Player_Scoring(Username varchar(25) not null, Score int not null, primary key (Username, Score), foreign key (Username) references Player (Username), foreign key (Score) references Scoring (Score))";
			
			this.stmt = connection.createStatement();
			this.stmt.execute(createPlayer);
			this.stmt.execute(createScoring);
			this.stmt.execute(createPlayer_Scoring);

			System.out.println("created table successfully");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//selects the player relation and prints it out
	public void selectPlayer() {
		try {
			String selectPlayer = "select * from Player";

			this.stmt = connection.createStatement();

			this.rs = stmt.executeQuery(selectPlayer);

			while (this.rs.next()) {
				System.out.println(this.rs.getString("Username"));
				System.out.println(this.rs.getString("Password"));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Test
	public static void main(String[] args) {
		DB_Connector connector = new DB_Connector();
		connector.createDBStructure();
		connector.selectPlayer();
	}
}// end DB_Connector