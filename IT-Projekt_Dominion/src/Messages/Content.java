package Messages;


/**
 * @author Lukas
 * @version 1.0
 * @created 31-Okt-2017 17:01:19
 */
public enum Content {
	Won,
	Lost,
	Login,
	CreateNewPlayer,
	GameMode,
	SinglePlayer,
	MultiPlayer,
	PlayCard,
	AskForChanges;
	
	/**
	 * @author Bradley Richards
	 * Parses the enum-Content for the Messages
	 * 
	 * @param mContent, in String format
	 * @return Content, enum of the given mContent
	 */
    public static Content parseContent(String mContent) {
    	Content content = null;
    	for (Content value : Content.values()) {
    		if (value.toString().equals(mContent)) content = value;
    	}
    	return content;
    }

}
