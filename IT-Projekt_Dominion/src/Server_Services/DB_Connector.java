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
		this.createDBConnection();
		this.createDBStructure();
	}

	/**
	 * 
	 * @param name
	 * @param password
	 * @throws SQLException
	 */
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
			System.out.println("Der Benutzername existiert schon");
		}
		return false;
	}

	/**
	 * 
	 * @param player
	 * @param score
	 */
	public boolean addScore(Player player, int score) {
		try {
			String insertIntoPlayer_Scoring = "Insert into Player_Scoring (Username, Score) values (?, ?)";

			this.prepStmt = connection.prepareStatement(insertIntoPlayer_Scoring);
			this.prepStmt.setString(1, player.getPlayerName());
			this.prepStmt.setInt(1, score);
			this.prepStmt.execute();

			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
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
	// deletes Player
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

	// returns playername with highScore
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
		this.connector = new DB_Connector();
		return this.connector;
	}

	// creates the db structure
	private void createDBStructure() {
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
//			this.fillScoring();
			this.stmt.execute(createPlayer_Scoring);

			System.out.println("created table successfully");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// creates DB Connection
	private void createDBConnection() {
		try {
			// Load Driver
			Class.forName("org.h2.Driver");

			// creates Connection with DB on Server, and creates DB_Dominion in
			// workspace of actual user if not exists
			String presentProjectPath = System.getProperty("user.dir");
			String path = "jdbc:h2:" + presentProjectPath
					+ "/IT-Projekt_Dominion/src/Server_Services/DB_Dominion.mv.db";
			String user = "sa";
			String pw = "";
			this.connection = DriverManager.getConnection(path, user, pw);
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//PROBLEM: DUPLIKATE = EXCEPTION
	private void fillScoring() {
		try {
			int numOfScorePoints = 30;
			String insertIntoScoring = "Insert into Scoring (Score) values (?)";

			this.prepStmt = connection.prepareStatement(insertIntoScoring);

			for (int i = 0; i <= numOfScorePoints; i++) {
				this.prepStmt.setInt(1, i);
				this.prepStmt.execute();
			}

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
		System.out.println(connector.getHighScore());

	}
}// end DB_Connector