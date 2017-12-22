package Client_GameApp_MVC;



/**
 * Higscore class represents one of the top 5 winners of the higscore.   
 * 
 * 
 * @author Rene Schwab
 * 
 * @param name
 * , player name
 * @param score
 * , points of the player
 * @param moves
 * , number of required moves to win 
 * 
 * @return Highscore
 * , object used to fill in to the TableView of the MainMenu higscore list 
 * 
 */
public class Highscore {
	
	private String name, score, moves;


	public Highscore(String name, String score, String moves) {
		this.setName(name);
		this.setScore(score);
		this.setMoves(moves);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMoves() {
		return moves;
	}

	public void setMoves(String moves) {
		this.moves = moves;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

}
