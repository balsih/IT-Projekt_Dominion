package Messages;


/**
 * The GameSuccess is to show the clients weather they won or lost.
 * 
 * @author Lukas
 */
public enum GameSuccess {
	Won,
	Lost;
	
	/**
	 * @author Lukas, source: Bradley Richards
	 * Parses the enum-GameSuccess for the Messages
	 * 
	 * @param mGameSuccess
	 * 				The GameSuccess in String format
	 * @return GameSuccess, enum of the given input <String>
	 */
    public static GameSuccess parseGameSuccess(String mGameSuccess) {
    	GameSuccess gameSuccess = null;
    	for (GameSuccess value : GameSuccess.values()) {
    		if (value.toString().equals(mGameSuccess)) gameSuccess = value;
    	}
    	return gameSuccess;
    }

}
