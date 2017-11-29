package Messages;


/**
 * @author Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:01:19
 */
public enum GameSuccess {
	Won,
	Lost;
	
	/**
	 * @author Bradley Richards
	 * Parses the enum-GameSuccess for the Messages
	 * 
	 * @param mGameSuccess
	 * @return gameSuccess, enum of the given mGameSuccess
	 */
    public static GameSuccess parseGameSuccess(String mGameSuccess) {
    	GameSuccess gameSuccess = null;
    	for (GameSuccess value : GameSuccess.values()) {
    		if (value.toString().equals(mGameSuccess)) gameSuccess = value;
    	}
    	return gameSuccess;
    }

}
