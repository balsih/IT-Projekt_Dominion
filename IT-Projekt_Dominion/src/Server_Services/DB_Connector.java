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
	private PreparedStatement prepStmt;
	private ResultSet rs;

	protected DB_Connector() {
		try {
			// Load Driver
			Class.forName("org.h2.Driver");

			// creates Connection with DB on Server, and creates DB_Dominion if
			// not exists
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
	 * @throws SQLException
	 */
	//NOCH NICHT FERTIG!
	public boolean addNewPlayer(String username, String password) {
		String existingUser = "";

		try {
			String selectUsername = "select * from Player";
			this.stmt = connection.createStatement();
			this.rs = stmt.executeQuery(selectUsername);

			while (this.rs.next()) {
				existingUser = rs.getString("Username");
			}

			if (username != existingUser) {
				String insertIntoPlayer = "insert into Player (Username, Password) values (?,?)";
				this.prepStmt = this.connection.prepareStatement(insertIntoPlayer);
				this.prepStmt.setString(1, username);
				this.prepStmt.setString(2, password);
				this.prepStmt.execute();
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
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
	//deletes Player
	public void deletePlayer(String username) {
		String deletePlayer = "Delete from Player where Username = ?";

		try {
			this.prepStmt = connection.prepareStatement(deletePlayer);
			this.prepStmt.setString(1, username);
			this.prepStmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	//returns playername with highScore
	public String getHighScore() {
		String selectHighScore = "Select Username, max(Score) from Player_Scoring group by ?";
		String username = "username";
		String highScore = "";

		try {
			this.prepStmt = connection.prepareStatement(selectHighScore);
			this.prepStmt.setString(1, username);
			this.rs = this.prepStmt.executeQuery();

			while (this.rs.next()) {
				username = rs.getString("Username");
				highScore = rs.getString("Score");
			}

			return username + ": " + highScore;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public DB_Connector getDB_Connector() {
		return this.connector;
	}

	// creates the db structure
	public void createDBStructure() {
		try {
			String createPlayer = "create table if not exists Player(" + "Username varchar(25) primary key,"
					+ "Password varchar (25))";
			String createScoring = "create table if not exists Scoring(" + "Score int primary key)";
			String createPlayer_Scoring = "create table if not exists Player_Scoring("
					+ "Username varchar(25) not null," + "Score int not null," + "primary key (Username, Score),"
					+ "foreign key (Username) references Player (Username),"
					+ "foreign key (Score) references Scoring (Score))";

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

	// selects the player relation and prints it out
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

		// Problem wenn Duplikate = Exception
		// connector.addNewPlayer("Hausi", "test");
		connector.deletePlayer("Hausi");

		connector.selectPlayer();

	}
}// end DB_Connector