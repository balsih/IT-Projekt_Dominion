package Messages;

/**
 * @author Lukas
 * All possible Interactions between Client and Server.
 * If there is a number behind an interaction, it tells which step of interaction it represents
 *
 */
public enum Interaction {
	
	EndOfTurn,
	Cellar,
	Workshop,
	Remodel1,
	Remodel2;
	
	/**
	 * @author Bradley Richards
	 * Parses the enum-Interaction for the Messages
	 * 
	 * @param InInteraction, in String format
	 * @return Interaction, enum of the given InInteraction
	 */
    public static Interaction parseInteraction(String InInteraction) {
    	Interaction interaction = null;
    	for (Interaction value : Interaction.values()) {
    		if (value.toString().equals(InInteraction)) interaction = value;
    	}
    	return interaction;
    }

}
