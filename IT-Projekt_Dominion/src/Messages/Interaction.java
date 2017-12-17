package Messages;

/**
 * All possible Interactions between Client and Server.
 * If there is a number behind an interaction, it tells which step of interaction it represents (Remodel1, Remodel2)
 * <li>EndOfTurn:	The client has to chose a card from his hand to show on his/her DiscardPile-top
 * <li>Cellar:		The client has to chose an undefined number of cards to discard from his/her hand
 * <li>Workshop:	The client has to chose a card from the buyCards that costs up to 4 coins
 * <li>Remodel1:	The client has to chose a card in his/her hand to dispose
 * <li>Remodel2:	The client has to chose a card from the buyCards that costs up to 2 more than the disposed card in Remodel1
 * <li>Mine:		The client has to chose a copper- or silver-card from his hand to dispose
 * <li>Skip:		The client (wants to) skips his current Phase
 *
 * @author Lukas
 */
public enum Interaction {
	
	EndOfTurn,
	Cellar,
	Workshop,
	Remodel1,
	Remodel2,
	Mine,
	Skip;
	
	/**
	 * Parses the enum-Interaction for the Messages
	 * 
	 * @author Lukas, source: Bradley Richards
	 * @param InInteraction
	 * 					in String format
	 * @return Interaction, enum of the given input (String)
	 */
    public static Interaction parseInteraction(String InInteraction) {
    	Interaction interaction = null;
    	for (Interaction value : Interaction.values()) {
    		if (value.toString().equals(InInteraction)) interaction = value;
    	}
    	return interaction;
    }

}
