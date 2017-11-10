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
	private static DB_Connector connector;

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
		try {
			String insertIntoPlayer = "insert into Player (Username, Password) values (?,?)";
			this.prepStmt = this.connection.prepareStatement(insertIntoPlayer);
			this.prepStmt.setString(1, username);
			this.prepStmt.setString(2, password);
			this.prepStmt.execute();

			return true;

		} catch (SQLException e) {
			return false;
		}
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
			return false;
		}
	}

	/**
	 * 
	 * @param name
	 */
	// deletes Player
	public boolean deletePlayer(String username) {
		String deletePlayer = "Delete from Player where Username = ?";

		try {
			this.prepStmt = connection.prepareStatement(deletePlayer);
			this.prepStmt.setString(1, username);
			this.prepStmt.execute();

			return true;
		} catch (SQLException e) {
			return false;
		}

	}

	// returns playername with highScore
	public String getHighScore() {
		String selectHighScore = "Select max(Score) from Player_Scoring where (?) in (Select * order by Score desc limit 5)";
		String username = "";
		String highScore = "";

		try {
			this.prepStmt = connection.prepareStatement(selectHighScore);
			this.prepStmt.setString(1, "Username");
			this.rs = this.prepStmt.executeQuery();

			while (this.rs.next()) {
				highScore += rs.getString("Score") +"\n";
			}

			return highScore;

		} catch (SQLException e) {
			System.out.println(e.toString());
			return "";
		}
	}

	// Singleton
	public static DB_Connector getDB_Connector() {
		if (connector == null) {
			connector = new DB_Connector();
		}
		return connector;
	}

	// creates the db structure
	private boolean createDBStructure() {
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
			this.fillScoring();
			this.stmt.execute(createPlayer_Scoring);

			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	// creates DB Connection
	private boolean createDBConnection() {
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
			
			return true;
		} catch (SQLException | ClassNotFoundException e) {
			return false;
		}
	}

	// fills table Scoring with the Scorerpoints if not exists
	private boolean fillScoring() {
		try {
			int numOfScorePoints = 30;
			String insertIntoScoring = "Insert into Scoring (Score) values (?)";

			this.prepStmt = connection.prepareStatement(insertIntoScoring);

			for (int i = 0; i <= numOfScorePoints; i++) {
				this.prepStmt.setInt(1, i);
				this.prepStmt.execute();
			}

			return true;
		} catch (SQLException e) {
			return false;
		}

	}

	// Returns true, if Login is correct/exists. Else returns false.
	public boolean checkLoginInput(String username, String password) {
		try {
			String checkLogin = "Select * from Player where username = (?)" + "and password = (?)";

			String existingUsername = "";
			String existingPassword = "";

			this.prepStmt = connection.prepareStatement(checkLogin);
			this.prepStmt.setString(1, username);
			this.prepStmt.setString(2, password);
			this.rs = prepStmt.executeQuery();

			while (rs.next()){
				existingUsername = this.rs.getString("Username");
				existingPassword = this.rs.getString("Password");
			}

			if (existingUsername.equals(username) && existingPassword.equals(password))
				return true;
			else
				return false;
			
		} catch (SQLException e) {
			return false;
		}
	}

	// HILFSMETHODE ZUM TESTEN!! selects the player relation and prints it out
	private void selectPlayer() {
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

	// TEST
	public static void main(String[] args) {
		DB_Connector connector = new DB_Connector();
		Player tester = new Player("Bodo");
		connector.addNewPlayer("Test", "tester");
		connector.selectPlayer();
		System.out.println(connector.checkLoginInput("Bodo", "abc"));
		System.out.println(connector.checkLoginInput("Test", "tester"));
		connector.addScore(tester, 5);
		System.out.println(connector.getHighScore());
	}
}// end DB_Connector