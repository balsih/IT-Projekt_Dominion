package Server_Services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import Server_GameLogic.Player;

/**
 * 
 * 
 * A database connector which builds a connection to the embedded h2 database.
 * This class allows Select statements of relevant data and data manipulation
 * with DML in SQL.
 * 
 * @author Bodo Gruetter
 * Adapted from:
 * http://openbook.rheinwerk-verlag.de/javainsel9/javainsel_24_001.htm#mja621f3b4a74c7fa576b4a58b1614041e
 */
public class DB_Connector {

	private static DB_Connector connector;
	private final Logger logger = Logger.getLogger("");

	// Necessary objects that we use with jdbc
	private Connection connection;
	private Statement stmt;
	private PreparedStatement prepStmt;
	private ResultSet rs;

	/**
	 * The constructor creates a connection to the database and if creates if
	 * not exists the database structure.
	 * 
	 * @author Bodo Gruetter
	 * 
	 */
	protected DB_Connector() {
		this.createDBConnection();
		this.createDBStructure();
	}

	/**
	 * Creates a new user in database with a username as primary key and a
	 * password if not already exists.
	 * 
	 * @author Bodo Gruetter
	 * 
	 * @param username
	 *            - the username of a new player
	 * @param password
	 *            - the password of a new player
	 * @return Boolean - depending on if the username already exists.
	 */
	public boolean addNewPlayer(String username, String password) {
		try {
			/*
			 * prepare the preparedStatement with the insert into statement.
			 * sets the parameters as value and executes the statement.
			 */
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
	 * Inserts an existing player with his score of a game into the database.
	 * 
	 * @autor Bodo Gruetter
	 * 
	 * @param player
	 *            - the existing player
	 * @param score
	 *            - the achieved score in a game.
	 * @return Boolean - depending on if the insert statement works.
	 */
	public boolean addScore(Player player, int score, int moves) {
		try {
			/*
			 * prepares the preparedStatement with the insert into statement.
			 * sets the parameters as value and executes the statement.
			 */
			String insertIntoPlayer_Scoring = "Insert into Player_Scoring (Username, Score, Moves) values (?, ?, ?)";
			this.prepStmt = connection.prepareStatement(insertIntoPlayer_Scoring);
			this.prepStmt.setString(1, player.getPlayerName());
			this.prepStmt.setInt(2, score);
			this.prepStmt.setInt(3, moves);
			this.prepStmt.execute();

			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	/**
	 * Deletes an existing player from the database.
	 * 
	 * @author Bodo Gruetter
	 * 
	 * @param username
	 *            - the username of the player which should been deleted.
	 * @return Boolean - depending on the delete statement works.
	 */
	public boolean deletePlayer(String username) {
		try {
			/*
			 * prepares the preparedStatement with the insert into statement.
			 * sets the parameters as value and executes the statement.
			 */
			String deletePlayer = "Delete from Player where Username = ?";
			this.prepStmt = connection.prepareStatement(deletePlayer);
			this.prepStmt.setString(1, username);
			this.prepStmt.execute();

			return true;
		} catch (SQLException e) {
			return false;
		}

	}

	/**
	 * Deletes an existing player_Scoring from the database.
	 * 
	 * @author Bodo Gruetter
	 * 
	 * @param username
	 *            - the username of the player which should been deleted.
	 * @return Boolean - depending on the delete statement works.
	 */
	public boolean deletePlayer_Scoring(String username) {
		try {
			String deletePlayer_Scoring = "Delete from Player_Scoring where Username = ?";
			this.prepStmt = connection.prepareStatement(deletePlayer_Scoring);
			this.prepStmt.setString(1, username);
			this.prepStmt.execute();

			return true;
		} catch (SQLException e) {
			return false;
		}

	}

	/**
	 * Selects the 5 highscores in the database
	 * 
	 * @author Bodo Gruetter
	 * 
	 * @return highScore - the 5 best players with their score.
	 */
	public String getHighScore() {
		String selectHighScore = "Select distinct Username, Score, Moves from Player_Scoring order by Moves asc, Score desc limit 0,5";
		String highScore = "";

		try {
			this.prepStmt = connection.prepareStatement(selectHighScore);
			this.rs = this.prepStmt.executeQuery();

			while (this.rs.next()) {
				highScore += rs.getString("Username") + ",";
				highScore += rs.getString("Score") + ",";
				highScore += rs.getString("Moves") + ",";
			}

			return highScore;

		} catch (SQLException e) {
			System.out.println(e.toString());
			return "";
		}
	}

	/**
	 * Creates a new instance of database if not exists
	 * 
	 * @author Bodo Gruetter
	 * 
	 * @return connector - an existing connector
	 */
	public static DB_Connector getDB_Connector() {
		if (connector == null) {
			connector = new DB_Connector();
		}
		return connector;
	}

	/**
	 * Creates the database schema with two tables if no database exists.
	 * 
	 * @author Bodo Gruetter
	 * 
	 * @return Boolean - depending on the create table statement works.
	 */
	private boolean createDBStructure() {
		try {
			String createPlayer = "create table if not exists Player(" + "Username varchar(25) primary key,"
					+ "Password varchar (130))";
			String createPlayer_Scoring = "create table if not exists Player_Scoring("
					+ "ID int not null auto_increment primary key," + "Username varchar(25) not null,"
					+ "Score int not null," + "Moves int not null,"
					+ "foreign key (Username) references Player (Username))";

			this.stmt = connection.createStatement();
			this.stmt.execute(createPlayer);
			this.stmt.execute(createPlayer_Scoring);

			return true;
		} catch (SQLException | NullPointerException e) {
			this.logger.severe("Exception in createDBStructure: " + e.toString());
			return false;
		}
	}

	/**
	 * Creates Connection with DB on Server, and creates DB_Dominion in
	 * workspace of actual user if not exists.
	 * 
	 * @author Bodo Gruetter
	 * 
	 * @return true or false depending on the connection could been created.
	 */
	private boolean createDBConnection() {
		try {
			// Load Driver
			Class.forName("org.h2.Driver");

			/*
			 * creates Connection with DB on Server, and creates DB_Dominion in
			 * workspace of actual user if not exists
			 */
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

	/**
	 * checks if the player inputs the correct user data to login.
	 * 
	 * @author Bodo Gruetter
	 * 
	 * @pram username and password of a player
	 * @return true or false depending on the user input is correct and the
	 *         select statement works.
	 */
	public boolean checkLoginInput(String username, String password) {
		try {
			String checkLogin = "Select * from Player where username = (?)" + "and password = (?)";

			String existingUsername = "";
			String existingPassword = "";

			this.prepStmt = connection.prepareStatement(checkLogin);
			this.prepStmt.setString(1, username);
			this.prepStmt.setString(2, password);
			this.rs = prepStmt.executeQuery();

			while (rs.next()) {
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

	/**
	 * service method which allows to select the existing player in database and
	 * print them out in console.
	 * 
	 * @author Bodo Gruetter
	 */
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

	/**
	 * service method which allows to select the existing player_scores in
	 * database and print them out in console.
	 * 
	 * @author Bodo Gruetter
	 */
	public void selectPlayer_Scoring() {
		String selectPlayer_Scoring = "select * from Player_Scoring";

		try {
			this.stmt = connection.createStatement();
			this.rs = stmt.executeQuery(selectPlayer_Scoring);

			while (rs.next()) {
				System.out.println(this.rs.getString(2) + ": " + this.rs.getInt(3) + ", " + this.rs.getInt(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}