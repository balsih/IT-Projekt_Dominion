package Server_GameLogic;

/**
 * Enum for the three possible GameModes Singleplayer, Multiplayer and Simulation.
 * 
 * Simulation is just for testing the bot and is not used for final game.
 *
 * @author Bodo Gruetter
 *
 */
public enum GameMode {
	Singleplayer, Multiplayer, Simulation;
	
	/**
	 * @author Bradley Richards Parses the enum-Name for creating new cards on
	 *         client
	 * 
	 * @param cardName
	 * @return CardName, enum of the given cardName
	 */
	public static GameMode parseGameMode(String mode) {
		GameMode gameMode = null;
		for (GameMode value : GameMode.values()) {
			if (value.toString().equals(mode))
				gameMode = value;
		}
		return gameMode;
	}
}
