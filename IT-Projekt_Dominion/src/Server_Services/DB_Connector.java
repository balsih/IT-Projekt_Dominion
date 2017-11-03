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
	
	private Connection connection;
	private Statement stmt;
	private ResultSet rs;


	protected DB_Connector() {
	    
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

	public void createConnection() {

	}
	
	public void createDB(){
		try {
			this.connection = DriverManager.getConnection("jdbc:h2:~/DB_Dominion", "sa", "");
			
			String createPlayer = "create table if not exists Player(Username varchar(25) primary key, Password varchar (25))";
			String createScoring = "create table if not exists Scoring(Score int primary key)";
			String createPlayer_Scoring = "create table if not exists Player_Scoring(Username varchar(25) not null, Score int not null, primary key (Username, Score), foreign key (Username) references Player (Username), foreign key (Score) references Scoring (Score))";
			this.stmt = connection.createStatement();
			
			this.stmt.execute(createPlayer);
			this.stmt.execute(createScoring);
			this.stmt.execute(createPlayer_Scoring);
			
			System.out.println("table created successfully");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void selectPlayer(){
		try {
			this.connection = DriverManager.getConnection("jdbc:h2:~/DB_Dominion", "sa", "");
			String selectPlayer = "select * from Player";
			String insertIntoPlayer = "insert into Player (Username, Password) values ('dummy', 'abc')";
			
			this.stmt = connection.createStatement();
			this.stmt.execute(insertIntoPlayer);
			
			this.rs = stmt.executeQuery(selectPlayer);

			while(this.rs.next()){
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
		connector.createDB();
		connector.selectPlayer();
	}
}// end DB_Connector