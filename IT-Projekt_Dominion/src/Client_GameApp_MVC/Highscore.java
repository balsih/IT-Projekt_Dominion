package Client_GameApp_MVC;

public class Highscore {
	
	private String name;
	private int score, moves;

	public Highscore(String name, int score, int moves) {
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

	public int getMoves() {
		return moves;
	}

	public void setMoves(int moves) {
		this.moves = moves;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
