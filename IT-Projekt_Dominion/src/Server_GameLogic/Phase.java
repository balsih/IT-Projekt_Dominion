package Server_GameLogic;

public enum Phase {
	Action, Buy, CleanUp;

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
