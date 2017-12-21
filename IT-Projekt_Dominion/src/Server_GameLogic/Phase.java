package Server_GameLogic;

/**
 * 
 * Enum for the four possible phases of a game Action, Buy, CleanUp and Ending
 * 
 * @author Bodo Gruetter
 *
 */
public enum Phase {
	Action, Buy, CleanUp, Ending;

	/**
	 * @author Bradley Richards Parses the enum-Name for creating new cards on
	 *         client
	 * 
	 * @param cardName
	 * @return CardName, enum of the given cardName
	 */
	public static Phase parsePhase(String phaseName) {
		Phase phase = null;
		for (Phase value : Phase.values()) {
			if (value.toString().equals(phaseName))
				phase = value;
		}
		return phase;
	}
}
