package Client_GameApp_MVC;

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
